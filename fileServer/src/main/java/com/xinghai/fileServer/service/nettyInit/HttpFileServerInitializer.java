package com.xinghai.fileServer.service.nettyInit;

import com.xinghai.fileServer.config.FileServerConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public HttpFileServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        FileServerConfig.logger.info("--------------- initChannel ----------------");
        ChannelPipeline pipeline = ch.pipeline();

        ///事件在ChannelPipeline传递过程中只会调用匹配流的ChannelHandler
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }

        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());

        // Remove the following line if you don't want automatic content compression.
        //pipeline.addLast(new HttpContentCompressor());
        //
        // 分块写数据
        pipeline.addLast(new ChunkedWriteHandler());

        //组合数据块,一旦使用，将不启用分段接收数据
        //pipeline.addLast(new HttpObjectAggregator(1024));
        pipeline.addLast(new HttpFileServerHandler());

    }
}
