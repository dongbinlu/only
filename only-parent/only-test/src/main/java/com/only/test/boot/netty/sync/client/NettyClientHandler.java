package com.only.test.boot.netty.sync.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 在该示例代码中，我们继承了SimpleChannelInboundHandler类，并实现了channelActive()、channelRead0()、exceptionCaught()和send()方法。
 * 在channelActive()方法中，我们获取了ChannelHandlerContext对象，用于后续的操作。
 * 在channelRead0()方法中，我们使用Promise接口的trySuccess()方法来设置异步操作的结果，并将结果传递给客户端。
 * 在exceptionCaught()方法中，我们使用Promise接口的tryFailure()方法来设置异步操作的异常信息，并关闭客户端的连接。
 * 在send()方法中，我们使用Promise接口的newPromise()方法创建了一个新的Promise对象，并使用Executor线程池来执行异步操作。在异步操作完成后，我们通过Promise接口的get()方法获取异步操作的结果。
 * 上述代码示例中，我们使用Promise来处理异步操作的结果，实现了类似同步通信的效果。通过这种方式，我们可以在不阻塞线程的情况下，实现请求响应同步通信的效果。
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    private static final Object ATTR_KEY_RESPONSE = new Object();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("Received message from server: " + msg);
        //NettyClient.receiveResponse(ctx.channel(), msg);
        ctx.writeAndFlush(msg);
    }

}