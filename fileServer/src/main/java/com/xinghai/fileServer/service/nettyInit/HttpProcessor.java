package com.xinghai.fileServer.service.nettyInit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Map;

/**
 * 请求处理的基类
 */
public abstract class HttpProcessor {

    protected HttpRequest request;

    //url 参数（只有当get请求才有值被放进来）
    protected Map<String, String> requestParams;

    protected void processHttpRequest(ChannelHandlerContext ctx, HttpRequest httpRequest){
        this.request = httpRequest;
    }

    abstract protected void processHttpContent(ChannelHandlerContext ctx, HttpContent httpContent);

    public void channelUnregistered(ChannelHandlerContext ctx){
    }

    public void setRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
    }
}
