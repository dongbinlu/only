package com.only.netty.client;

import com.only.netty.MessageDecoder;
import com.only.netty.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NetttyClient {

    public static void main(String[] args) throws Exception {

        // 客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 创建客户端引导类
            Bootstrap bootstrap = new Bootstrap();
            // 采用链式编程,设置相关参数
            bootstrap.group(group)
                    // 使用NioSocketChannel作为客户都的通道实现
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 创建通道初始化对象，设置初始化参数
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new MessageDecoder());

                            pipeline.addLast(new MessageEncoder());

                            pipeline.addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("netty client start。。。");
            // 启动客户都去连接服务器端
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 18090).sync();

            // 得到channel
            Channel channel = cf.channel();
            System.out.println("====" + channel.localAddress() + "====");
            /*
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String msg = sc.nextLine();
                // 通过channel发送至服务器端
                channel.writeAndFlush(msg);
            }
            */
            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }

}
