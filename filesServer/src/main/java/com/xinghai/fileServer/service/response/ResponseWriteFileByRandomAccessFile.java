package com.xinghai.fileServer.service.response;

import com.xinghai.fileServer.FileHandle.HandleFile;
import com.xinghai.fileServer.FileHandle.impl.CassandraHandleFileImpl;
import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.domain.BO.FileMetaBO;
import com.xinghai.fileServer.service.nettyInit.FileServerConfig;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.xinghai.fileServer.service.nettyInit.FileServerConfig.HTTP_CACHE_SECONDS;
import static com.xinghai.fileServer.service.nettyInit.FileServerConfig.HTTP_DATE_FORMAT;
import static com.xinghai.fileServer.service.nettyInit.FileServerConfig.HTTP_DATE_GMT_TIMEZONE;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by scream on 2017/8/1.
 */
public class ResponseWriteFileByRandomAccessFile {
    public static void writeFile(ChannelHandlerContext ctx, HttpRequest request, FileMetaBO fileMetaBO) {
        //判断是否关闭请求响应连接
        boolean close =
                HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.headers().get(CONNECTION))
                        || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                        && !HttpHeaders.Values.KEEP_ALIVE
                        .equalsIgnoreCase(request.headers().get(CONNECTION));
        try {
            //随机生成一个文件名，用于保存文件
            final File file = new File(FileServerConfig.TEMP_FILE_DIR + "/" + Math.random() + fileMetaBO.getName());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FileServerException(FileServerErrorEnum.CREATE_RANDOM_FILE_ERROR);
                }
            }
            final RandomAccessFile raf = new RandomAccessFile(file, "rw");
            HandleFile handleFile = new CassandraHandleFileImpl();
            handleFile.getFile(fileMetaBO, raf);
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
            if (fileMetaBO.getSize() != null) {
                response.headers().set(CONTENT_LENGTH, fileMetaBO.getSize());
            }
            if (fileMetaBO.getContentType() != null) {
                response.headers().set(CONTENT_TYPE, fileMetaBO.getContentType());
            }
            setDateAndCacheHeaders(response, file);
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
                        ctx.write(new DefaultFileRegion(raf.getChannel(), 0, raf.length()), ctx.newProgressivePromise());
                // Write the end marker.
                lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            } else {
                sendFileFuture =
                        ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, raf.length(), 8192)),
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
                    if(file != null && file.exists()){
                        file.delete();
                    }
                    try {
                        if (raf != null) {
                            raf.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.err.println(future.channel() + " Transfer complete.");
                }
            });
            // Decide whether to close the connection or not.
            if (close) {
                // Close the connection when the whole content is written out.
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            FileServerConfig.logger.info(e.getMessage());
            HttpResponseUtils.sendError(ctx,HttpResponseStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            FileServerConfig.logger.info(e.getMessage());
            HttpResponseUtils.sendError(ctx,HttpResponseStatus.INTERNAL_SERVER_ERROR);
            //throw new FileServerException(FileServerErrorEnum.READ_FILE_CHUNK_ERROR);
        }
    }

    /**
     * Sets the Date and Cache headers for the HTTP Response
     *
     * @param response    HTTP response
     * @param fileToCache file to extract content type
     */
    private static void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));
        // Date header
        Calendar time = new GregorianCalendar();
        response.headers().set(DATE, dateFormatter.format(time.getTime()));

        // Add cache headers
        time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
        response.headers().set(EXPIRES, dateFormatter.format(time.getTime()));
        response.headers().set(CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
        response.headers().set(LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
    }
}
