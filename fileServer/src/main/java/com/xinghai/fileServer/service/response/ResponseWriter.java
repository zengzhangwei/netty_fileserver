package com.xinghai.fileServer.service.response;

import com.xinghai.fileServer.service.nettyInit.HttpFileServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;

/**
 * 响应类，返回数据
 */
public class ResponseWriter {

    public static final Logger logger = LogManager.getLogger(HttpFileServerHandler.class);
    public static void writeResponse(HttpRequest request, Channel channel, String returnMsg) {
        logger.info("writeResponse ...");
        // Convert the response content to a ChannelBuffer.
        ByteBuf buf = copiedBuffer(returnMsg, CharsetUtil.UTF_8);

        // Decide whether to close the connection or not.
        boolean close = HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.headers().get(CONNECTION))
                || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request.headers().get(CONNECTION));

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        setResponseHeader(response);

        if (!close) {  //若该请求响应是最后的响应，则在响应头中没有必要添加'Content-Length'
            response.headers().set(CONTENT_LENGTH, buf.readableBytes());
        }
        ChannelFuture future = channel.writeAndFlush(response);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    } // END writeResponse

    public static void writeResponseJSON(HttpRequest request, Channel channel, String returnMsg) {
        logger.info("writeResponse ...");
        ByteBuf buf = copiedBuffer(returnMsg, CharsetUtil.UTF_8);
        boolean close = HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.headers().get(CONNECTION))
                || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request.headers().get(CONNECTION));

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
        setResponseHeader(response);

        if (!close) {
            //若该请求响应是最后的响应，则在响应头中没有必要添加'Content-Length'
            response.headers().set(CONTENT_LENGTH, buf.readableBytes());
        }
        ChannelFuture future = channel.writeAndFlush(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    public static void writeResponseErrJSON(HttpRequest request, Channel channel, String returnMsg) {
        logger.info("writeResponse ...");
        ByteBuf buf = copiedBuffer(returnMsg, CharsetUtil.UTF_8);
        boolean close = HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.headers().get(CONNECTION))
                || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request.headers().get(CONNECTION));

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SERVICE_UNAVAILABLE, buf);
        response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
        setResponseHeader(response);

        if (!close) {
            //若该请求响应是最后的响应，则在响应头中没有必要添加'Content-Length'
            response.headers().set(CONTENT_LENGTH, buf.readableBytes());
        }
        ChannelFuture future = channel.writeAndFlush(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    // 设置跨域响应头
    private static void setResponseHeader(HttpResponse response) {
        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
        response.headers().set(ACCESS_CONTROL_MAX_AGE, 86400);
        response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "token,isAdmin,Accept-Encoding,Authorization," +
                "access-control-allow-origin,Content-Type,Accept,Origin,User-Agent,DNT," +
                "Cache-Control,X-Mx-ReqToken,Keep-Alive,X-Requested-With,If-Modified-Since");
        response.headers().set(ACCESS_CONTROL_ALLOW_METHODS, "POST,GET,OPTIONS");
        response.headers().set(ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
    }
}
