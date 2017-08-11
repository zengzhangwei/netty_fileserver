package com.xinghai.fileServer.service.httpService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpOptionsProcessor {
    // 处理options请求
    public static void doOptions(ChannelHandlerContext ctx, HttpRequest request) {

        boolean close = HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.headers().get(CONNECTION))
                || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request.headers().get(CONNECTION));

        String SUCCESS = "{\"errno\": 0,\"errmsg\": \"success\"}\n";
        byte[] fileByte = SUCCESS.getBytes();

        ByteBuf fileByteBuf = Unpooled.wrappedBuffer(fileByte);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, fileByteBuf);
        int contentLen = response.content().readableBytes();

        response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, contentLen);
        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(ACCESS_CONTROL_ALLOW_METHODS, "POST,GET,OPTIONS");
        response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "token,isAdmin,Accept-Encoding,Authorization," +
                "access-control-allow-origin,Content-Type,Accept,Origin,User-Agent,DNT," +
                "Cache-Control,X-Mx-ReqToken,Keep-Alive,X-Requested-With,If-Modified-Since");
        response.headers().set(ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
        response.headers().set(ACCESS_CONTROL_MAX_AGE, 86400);

        if (!close) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response, ctx.newProgressivePromise());
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (close) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
