package com.xinghai.fileServer.service.response;

import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.config.FileServerConfig;
import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.GetFileData;
import com.xinghai.fileServer.domain.PO.FileMeta;
import com.xinghai.fileServer.fileHandle.fileContentService.HandleFile;
import com.xinghai.fileServer.fileHandle.fileContentService.impl.LocalFileServerHandleFile;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import org.apache.commons.beanutils.BeanUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by scream on 2017/8/18.
 * 文件存在于服务器的某一文件夹下，通过该类可提供下载功能
 */
public class ResponseWriteFileByLocalFile {
    public static void writeFile(ChannelHandlerContext ctx, HttpRequest request, FileMeta fileMeta) {
        //判断是否关闭请求响应连接
        boolean close =
                HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.headers().get(CONNECTION))
                        || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                        && !HttpHeaders.Values.KEEP_ALIVE
                        .equalsIgnoreCase(request.headers().get(CONNECTION));

        GetFileData getFileData = new GetFileData();
        try {
            BeanUtils.copyProperties(getFileData, fileMeta);
        } catch (IllegalAccessException | InvocationTargetException e1) {
            FileServerConfig.logger.error("对象属性赋值错误！");
            e1.printStackTrace();
        }
        HandleFile handleFile = new LocalFileServerHandleFile();
        FileInputStream is = (FileInputStream) handleFile.getFileDataByStream(getFileData);
        if(is == null){
            FileServerConfig.logger.info("file id :" + fileMeta.getId() +" and file name :" + fileMeta.getName() + "is not exist !!");
            throw new FileServerException(FileServerErrorEnum.FILE_NOT_EXIST_ON_DEFINE_DIRECTORY);
        }
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        if (fileMeta.getSize() != null) {
            response.headers().set(CONTENT_LENGTH, fileMeta.getSize());
        }
        if (fileMeta.getContentType() != null) {
            response.headers().set(CONTENT_TYPE, fileMeta.getContentType());
        }
        if(fileMeta.getName() != null){
            response.headers().set("fileName",fileMeta.getName());
        }
        if (!close) {
            response.headers().set(CONNECTION, KEEP_ALIVE);
        }
        // Write the initial line and the header.
        ctx.write(response);
        // Write the content.
        ChannelFuture sendFileFuture = null;
        ChannelFuture lastContentFuture;
        if (ctx.pipeline().get(SslHandler.class) == null) {
            FileServerConfig.logger.info("write once !");
            sendFileFuture =
                    ctx.write(new DefaultFileRegion(is.getChannel(), 0, fileMeta.getSize()), ctx.newProgressivePromise());
            // Write the end marker.
            lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        } else {
            sendFileFuture =
                    ctx.writeAndFlush(new HttpChunkedInput(new ChunkedStream(is)),
                            ctx.newProgressivePromise());
            // HttpChunkedInput will write the end marker (LastHttpContent) for us.
            lastContentFuture = sendFileFuture;
        }
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                if (total < 0) { // total unknown
                    System.err.println(future.channel() + " Transfer progress: " + progress);
                } else {
                    System.err.println(future.channel() + " Transfer progress: " + progress + " / " + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) {
                try {
                    if(is != null){
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // Decide whether to close the connection or not.
        if (close) {
            // Close the connection when the whole content is written out.
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}

