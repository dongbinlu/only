package com.only.netty.server;

import com.only.base.constant.OnlyConstants;
import com.only.netty.MessageDecoder;
import com.only.netty.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class NettyServer implements ApplicationRunner, ApplicationListener<ContextClosedEvent> {

    private Channel channel;

    @Autowired
    private NettyServerHandler nettyServerHandler;

    /**
     * 监听springboot启动
     *
     * @param args
     * @throws Exception
     */
    @Async
    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(OnlyConstants.Netty.BOSS_GROUP_THREAD);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast("socketChoose", new SocketChooseHandle());

                            pipeline.addLast(new MessageEncoder());

                            pipeline.addLast(new MessageDecoder());

                            /**
                             * IdleStateHandler的readerIdleTime参数指定超过10秒还没收到客户端的连接，
                             * 会触发IdleStateEvent事件并且交给下一个handler处理，下一个handler必须
                             * 实现userEventTriggered方法处理对应事件
                             */
                            ch.pipeline().addLast(new IdleStateHandler(OnlyConstants.Netty.READER_IDLE_TIME, OnlyConstants.Netty.WRITER_IDLE_TIME, OnlyConstants.Netty.ALL_IDLE_TIME, TimeUnit.SECONDS));

                            pipeline.addLast("nettyServerHandler", nettyServerHandler);

                        }
                    });
            log.info("netty server start。。。");
            ChannelFuture cf = bootstrap.bind(OnlyConstants.Netty.TCP_PORT).sync();
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()) {
                        log.info("监听端口" + OnlyConstants.Netty.TCP_PORT + "成功");
                    } else {
                        log.error("监听端口" + OnlyConstants.Netty.TCP_PORT + "失败");
                    }
                }
            });
            this.channel = cf.channel();
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    /**
     * 监听springboot关闭
     *
     * @param contextClosedEvent
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        if (this.channel != null) {
            this.channel.close();
        }
        log.info("netty server stop。。。");
    }
}
