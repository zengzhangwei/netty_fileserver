package com.xinghai.fileServer.service.nettyInit;

import com.xinghai.fileServer.config.FileServerConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.*;

/**
 * 文件处理的父类
 */
public abstract class HttpFileProcessor extends HttpProcessor {
    protected static final HttpDataFactory factory =
            new DefaultHttpDataFactory(FileServerConfig.SIZE_IN_MEMORY); // Disk if size exceed
    protected HttpPostRequestDecoder httpDecoder;
    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true;    // should delete file on exit (in normal exit)
        DiskFileUpload.baseDirectory = FileServerConfig.BASEDIRECTORY_FILEUPLOAD;   // system temp directory
        DiskAttribute.deleteOnExitTemporaryFile = true;     // should delete file on exit (in normal exit)
        DiskAttribute.baseDirectory = FileServerConfig.BASEDIRECTORY_ATTRIBUTE;     // system temp directory
    }

    @Override
    protected void processHttpRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        super.processHttpRequest(ctx, httpRequest);
        httpDecoder = new HttpPostRequestDecoder(factory, request);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        super.channelUnregistered(ctx);
        if (httpDecoder != null) {
            httpDecoder.cleanFiles();
        }
    }
}
