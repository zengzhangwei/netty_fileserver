package com.xinghai.fileServer.service.response;

import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.domain.BO.FileMetaBO;
import com.xinghai.fileServer.FileHandle.HandleFile;
import com.xinghai.fileServer.FileHandle.impl.CassandraHandleFileImpl;
import com.xinghai.fileServer.service.nettyInit.FileServerConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import sun.util.resources.cldr.de.CalendarData_de_AT;

import java.io.UnsupportedEncodingException;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class ResponseWriterDownload {
    public static void writeDownLoadResponse(ChannelHandlerContext ctx, FileMetaBO file,
                                             HttpRequest request) throws FileServerException {
        //判断是否关闭请求响应连接
        boolean close =
                HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.headers().get(CONNECTION))
                        || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                        && !HttpHeaders.Values.KEEP_ALIVE
                        .equalsIgnoreCase(request.headers().get(CONNECTION));
        try {
               //小文件下载
                HandleFile handleFile = new CassandraHandleFileImpl();
                byte[] fileByte = handleFile.getFile(file);
                ByteBuf fileByteBuf = Unpooled.wrappedBuffer(fileByte);
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, fileByteBuf);
                String fileType = file.getContentType();
                if (fileType != null) {
                  response.headers().set(CONTENT_TYPE, fileType);
                }
                String filenameDownload = new String(file.getName().getBytes("GBK"), "ISO_8859_1");
                response.headers()
                        .set("Content-Disposition", "attachment;filename=" + filenameDownload);
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
                if (!close) {
                    response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                }
                //setContentHeader(response, file);
                ChannelFuture writeFuture = ctx.write(response, ctx.newProgressivePromise());
                ctx.flush();
                writeFuture.addListener(new ChannelProgressiveFutureListener() {
                    @Override
                    public void operationProgressed(ChannelProgressiveFuture future, long progress,
                                                    long total) {
                        if (total < 0) {
                            FileServerConfig.logger
                                    .info(future.channel() + " Transfer progress: " + progress);
                        } else {
                            FileServerConfig.logger.info(
                                    future.channel() + " Transfer progress: " + progress + " / "
                                            + total);
                        }
                    }

                    @Override
                    public void operationComplete(ChannelProgressiveFuture future) {
                        System.err.println(future.channel() + "send process finished!");
                    }
                });

                ChannelFuture lastContentFuture =
                        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                System.out.println("last content send completed");
                if (close) {
                    lastContentFuture.addListener(ChannelFutureListener.CLOSE);
                }
        } catch (UnsupportedEncodingException e) {
            String exceptionMsg = "Unsupported encoding";
            FileServerConfig.logger.error(exceptionMsg + ": " + e.getMessage());
            HttpResponseUtils.sendError(ctx,HttpResponseStatus.UNPROCESSABLE_ENTITY);
        }catch (Exception e){
            e.printStackTrace();
            FileServerConfig.logger.error( "下载文件未知错误: " + e.getMessage());
            HttpResponseUtils.sendError(ctx,HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }   // END  writeDownLoadResponse

    // 设置跨域响应头
    private static void setResponseHeader(HttpResponse response) {
        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
        response.headers().set(ACCESS_CONTROL_MAX_AGE, 86400);
    }
}
