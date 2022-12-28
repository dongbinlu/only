package com.only.netty.server;

import com.only.base.constant.OnlyConstants;
import com.only.netty.Message;
import com.only.netty.MessageResolverFactory;
import com.only.netty.Resolver;
import com.only.netty.entity.ClientInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 自定义Handler需要继承netty规定好的某个HandlerAdapter(规范)
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {


    private static ConcurrentMap<Channel, ClientInfo> clientInfos = new ConcurrentHashMap<>();

    int readIdleTimes = 0;

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
     * 握手建立
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    /**
     * 握手取消
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

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

    /**
     * 读取数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {

        // 文本消息
        if (obj instanceof TextWebSocketFrame) {

            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) obj;

            String value = textWebSocketFrame.text();
            log.info("【服务端(TextWebSocketFrame)】收到消息, value:{}", value);

            Message request = Message.builder()
                    .flag(OnlyConstants.Netty.WebSocketProtocol.FLAG)
                    .type(OnlyConstants.Netty.WebSocketProtocol.TYPE[0])
                    .tag(OnlyConstants.Netty.WebSocketProtocol.TAG[0])
                    .length(value.length())
                    .value(value.getBytes(CharsetUtil.UTF_8))
                    .build();


            Channel channel = ctx.channel();
            Resolver resolver = messageResolverFactory.getMessageResolver(request);

            /**
             * 弹幕、群聊聊天室
             */
            if (null == resolver) {
                channelGroup.forEach(ch -> {

                    // 群聊聊天室
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
     * 超时处理
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof WebSocketServerProtocolHandler.ServerHandshakeStateEvent) {
            ctx.fireUserEventTriggered(evt);
        }

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;

            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    readIdleTimes++;// 读空闲的计数加1
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    // 不处理
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    // 不处理
                    break;
                default:
                    break;
            }
            log.warn(ctx.channel().remoteAddress() + "超时事件: " + eventType);
            if (readIdleTimes > OnlyConstants.Netty.READ_IDLE_TIMES) {

                log.warn("【服务端】读空闲超过" + OnlyConstants.Netty.READ_IDLE_TIMES + "次,关闭连接，释放更多资源");

                Message response = new Message();
                response.setFlag(OnlyConstants.Netty.OnlyProtocol.FLAG);
                response.setType(OnlyConstants.Netty.OnlyProtocol.TYPE[1]);
                response.setTag(OnlyConstants.Netty.OnlyProtocol.TAG[0]);
                String value = "idle close";
                response.setLength(value.length());
                response.setValue(value.getBytes(CharsetUtil.UTF_8));

                ctx.channel().writeAndFlush(response);
                ctx.channel().close();
            }
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
        log.error("cause:", cause);
        ctx.close();
    }
}
