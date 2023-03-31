package com.only.test.boot.netty.sync.client;

import com.only.test.boot.netty.sync.server.NettyServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9999;
    private static final AttributeKey<CompletableFuture<String>> ATTR_KEY_RESPONSE = AttributeKey.valueOf("response");

    private static final Map<String, CompletableFuture<String>> responseFutures = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new NettyServerHandler());
                }
            });

            ChannelFuture f = b.connect(HOST, PORT).sync();
            ChannelHandlerContext ctx = f.channel().pipeline().lastContext();
/*
            for (int i = 0; i < 5; i++) {
                String request = "request" + i;
                CompletableFuture<String> responseFuture = new CompletableFuture<>();
                responseFutures.put(request, responseFuture);
                sendRequest(ctx, request, responseFuture);
            }

            responseFutures.forEach((request, responseFuture) -> {
                String response = responseFuture.join();
                System.out.println("Received response for request " + request + ": " + response);
            });*/

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static void sendRequest(ChannelHandlerContext ctx, String request, CompletableFuture<String> responseFuture) {
        ctx.channel().attr(ATTR_KEY_RESPONSE).set(responseFuture);
        ctx.writeAndFlush(request);
    }

    public static void receiveResponse(ChannelHandlerContext ctx, String response) {
        CompletableFuture<String> responseFuture = ctx.channel().attr(ATTR_KEY_RESPONSE).get();
        responseFuture.complete(response);
    }
}

