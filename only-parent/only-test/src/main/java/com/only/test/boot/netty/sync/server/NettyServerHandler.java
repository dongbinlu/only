package com.only.test.boot.netty.sync.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 在上述代码中，我们实现了handleRequest()方法，用于根据请求消息进行业务处理，并返回处理结果。在channelRead0()方法中，我们获取了请求消息，并调用handleRequest()方法进行业务处理。最后，我们将处理结果返回给客户端。
 * 在exceptionCaught()方法中，我们关闭了服务端的连接。
 * 通过以上示例代码，我们可以实现一个简单的Netty服务端，用于处理请求响应同步通信。
 */
@Data
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    private final Map<String, Channel> channelMap;
    private final ExecutorService executorService;
    private static final AttributeKey<CompletableFuture<String>> ATTR_KEY_RESPONSE = AttributeKey.valueOf("ATTR_KEY_RESPONSE");

    public NettyServerHandler() {
        this.channelMap = new ConcurrentHashMap<>();
        this.executorService = new ThreadPoolExecutor(
                10,
                16,
                3L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
    /**
     * 表示channel处于就绪状态，提示上线
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 将新连接加入到ChannelMap中
        System.out.println(ctx.channel().id().asShortText());
        channelMap.put(ctx.channel().id().asShortText(), ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //ByteBuf buf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
        //
        //byte[] bytes = new byte[buf.readableBytes()];
        //buf.readBytes(bytes);
        //String request = new String(bytes);
        //System.out.println("Received request from client: " + request);
        //
        //// 向客户端发送请求并等待响应
        //CompletableFuture<String> response = sendAndReceive(ctx.channel(), "Hello, client!");
        //System.out.println("Received response from client: " + response.get());

        // 处理客户端发送的消息
        String responseMsg = "Server has received the message: " + msg;

        // 获取当前连接的 Channel，并将响应结果写入 Channel 的属性中
        Channel channel = ctx.channel();
        AttributeKey<String> key = AttributeKey.valueOf("response");
        channel.attr(key).set(responseMsg);
    }

    /**
     * 数据读取完毕处理方法
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public CompletableFuture<String> sendAndReceive(Channel channel, String message) {
        ChannelPromise promise = channel.newPromise();

        channel.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()))
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        promise.setSuccess();
                    } else {
                        promise.setFailure(future.cause());
                    }
                });

        CompletableFuture<String> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                CompletableFuture<String> f = promise.await().channel().attr(ATTR_KEY_RESPONSE).getAndRemove();
                String response = f.get();
                future.complete(response);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public CompletableFuture<String> sendAsync(Channel channel, String request) {
        CompletableFuture<String> future = new CompletableFuture<>();
        ChannelFuture channelFuture = channel.writeAndFlush(request);
        channelFuture.addListener((ChannelFutureListener) futureChannel -> {
            if (futureChannel.isSuccess()) {
                // 发送成功，添加到 future 中等待响应结果
                channel.attr(ATTR_KEY_RESPONSE).set(future);
            } else {
                // 发送失败，将异常信息添加到 future 中
                future.completeExceptionally(futureChannel.cause());
            }
        });
        return future;
    }


}