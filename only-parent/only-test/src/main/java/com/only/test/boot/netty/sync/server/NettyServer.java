package com.only.test.boot.netty.sync.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 以下是一个简单的Netty服务端实现请求响应同步通信的示例代码：
 * <p>
 * 在该示例代码中，我们创建了一个Netty服务端，用于监听客户端请求，并处理请求，最后将处理结果返回给客户端。
 * <p>
 * 在start()方法中，我们使用NioEventLoopGroup和ServerBootstrap类创建一个服务端，并设置服务端参数，如监听端口号等。
 * 在ChannelInitializer中，我们添加了一个用于编解码的StringEncoder和StringDecoder，以及一个用于处理请求响应逻辑的NettyServerHandler。
 * 在NettyServerHandler中，我们继承了SimpleChannelInboundHandler类，并实现了channelRead0()方法。
 * 在该方法中，我们获取了请求消息，并根据请求消息进行业务处理，最后将处理结果返回给客户端。
 */
@Component
public class NettyServer implements ApplicationRunner, ApplicationListener<ContextClosedEvent> {

    private static final AttributeKey<CompletableFuture<String>> ATTR_KEY_RESPONSE = AttributeKey.valueOf("ATTR_KEY_RESPONSE");

    private static final ExecutorService executorService = new ThreadPoolExecutor(
            10,
            16,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(nettyServerHandler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(9999).sync();
            System.out.println("Server started on port 8080");

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // do nothing
    }

    public String sendAndReceive(ChannelHandlerContext ctx, String message) {
        if (ctx == null) {
            throw new RuntimeException("ChannelHandlerContext not found ");
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        ctx.channel().attr(ATTR_KEY_RESPONSE).set(future);

        ctx.channel().writeAndFlush(message);

        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to receive response for message " + message, e);
        }
    }

    public Future<String> sendAndReceiveAsync(ChannelHandlerContext ctx, String message) {
        if (ctx == null) {
            throw new RuntimeException("ChannelHandlerContext not found ");
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        ctx.channel().attr(ATTR_KEY_RESPONSE).set(future);

        return (Future<String>) executorService.submit(() -> {
            ctx.channel().writeAndFlush(message);
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to receive response for message " + message, e);
            }
        });
    }
}