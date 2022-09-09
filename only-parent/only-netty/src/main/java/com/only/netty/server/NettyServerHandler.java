package com.only.netty.server;

import com.only.netty.Message;
import com.only.netty.MessageResolverFactory;
import com.only.netty.Resolver;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义Handler需要继承netty规定好的某个HandlerAdapter(规范)
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 收集系统中所有{@link Resolver} 接口的实现。 key为在spring容器中Bean的名字
     */

    @Autowired
    private Map<String, Resolver> resolvers;

    /**
     * 获取一个消息处理器工厂类实例
     */
    @Autowired
    private MessageResolverFactory messageResolverFactory;


    /**
     * 客户连接池
     * GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 表示channel处于就绪状态，提示上线
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channel.writeAndFlush(new TextWebSocketFrame("【客户端】 " + channel.remoteAddress() + " 上线了！！！"));
        channelGroup.add(channel);
        log.info("【客户端】 " + channel.remoteAddress() + " 上线了！！！");

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("12222222222222222");
    }

    /**
     * 读取数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {

        if (obj instanceof TextWebSocketFrame) {

            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) obj;

            String value = textWebSocketFrame.text();
            log.info("【服务端(TextWebSocketFrame)】收到消息, value:{}", value);

            Message request = Message.builder()
                    .flag((byte) 0x00)
                    .type((byte) 0x00)
                    .tag((byte) 0x00)
                    .length(value.length())
                    .value(value.getBytes(CharsetUtil.UTF_8))
                    .build();


            Channel channel = ctx.channel();
            Resolver resolver = messageResolverFactory.getMessageResolver(request);

            /**
             * 弹幕、聊天室
             */
            if (null == resolver) {
                channelGroup.forEach(ch -> {
                    // 不是当前的channel,转发消息
                    if (channel != ch) {
                        ch.writeAndFlush(new TextWebSocketFrame(value));
                    } else {
                        // 回显自己发送的消息给自己
                        ch.writeAndFlush(new TextWebSocketFrame("【我发送的: 】" + value));
                    }
                });
            } else {// webSocket
                resolver.resolve(channelGroup, ctx, request);
            }
            // socket
        } else if (obj instanceof Message) {

            Message request = (Message) obj;

            byte flag = request.getFlag();
            byte type = request.getType();
            byte tag = request.getTag();
            int length = request.getLength();
            byte[] value = request.getValue();
            log.info("【服务端(Message)】收到消息, flag:{},type:{},type:{},length:{},value:{}",
                    flag, type, tag, length, new String(value, CharsetUtil.UTF_8));

            Resolver resolver = messageResolverFactory.getMessageResolver(request);
            resolver.resolve(channelGroup, ctx, request);

        } else {
            log.warn("Message type not found ???................");
        }

    }

    /**
     * 表示channel处于不活动状态，提示离线了
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channel.writeAndFlush(new TextWebSocketFrame("【客户端】 " + channel.remoteAddress() + " 下线了！！！"));
        log.info("【客户端】 " + channel.remoteAddress() + " 下线了！！！");
    }

    /**
     * 处理异常, 一般是需要关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("cause:{}", cause);
        ctx.close();
    }

}
