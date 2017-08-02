package com.xinghai.fileServer.service.nettyInit;

import com.xinghai.fileServer.dao.cassandraDao.CassandraManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Created by scream on 2017/7/13.
 * 文件服务启动类
 */
public class HttpFileServer {
    private static final boolean SSL = System.getProperty("ssl") != null;
    private static final int PORT =
            Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));
    public static void  main(String[] args)throws Exception {
        // Configure SSL.
        FileServerConfig.logger.info("fileServer start !");
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }
        //初始化cassandra
        CassandraManager.init();
        EventLoopGroup bossGroup = new NioEventLoopGroup(FileServerConfig.BIZGROUPSIZE);
        EventLoopGroup workerGroup = new NioEventLoopGroup(FileServerConfig.BIZTHREADSIZE);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.handler(new LoggingHandler(LogLevel.INFO));
            //初始化
            b.childHandler(new HttpFileServerInitializer(sslCtx));
            Channel ch = b.bind(PORT).sync().channel();
            System.out.println("Start fileserver " +
                    (SSL ? "https" : "http") + "://" + FileServerConfig.HOST_IP + ":" + PORT + '/');
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            //关闭Cassandra
            CassandraManager.close();
        }
    }
}
