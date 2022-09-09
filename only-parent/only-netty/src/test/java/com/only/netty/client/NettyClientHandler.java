package com.only.netty.client;

import com.only.netty.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<Message> {

    /**
     * 当客户端连接服务器完成就会触发该方法
     * 客户端发送数据
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                if (ctx.channel().isActive()) {
                    System.out.println("请输入业务标识:");
                    String tag = sc.nextLine();
                    System.out.println("请输入消息内容:");
                    String value = sc.nextLine();

                    Message message = ipnetFull(Byte.parseByte(tag), value);

                    // 通过channel发送至服务器端
                    ctx.writeAndFlush(message);
                } else {
                    break;
                }
            }
        }).start();

    }

    /**
     * 读取数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {

        byte flag = message.getFlag();
        byte type = message.getType();
        byte tag = message.getTag();
        int length = message.getLength();
        byte[] value = message.getValue();
        log.info("【客户端】收到消息, flag:{},type:{},type:{},length:{},value:{}",
                flag, type, tag, length, new String(value, CharsetUtil.UTF_8));

    }

    /**
     * 数据读取完毕处理方法
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("【客户端】读取数据完毕");
    }

    /**
     * 处理异常, 一般是需要关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("cause:{}", cause);
        cause.printStackTrace();
        ctx.close();
    }

    public static Message ipnetFull(byte tag, String msg) {

        Message message = new Message();

        message.setFlag((byte) 0xD0);
        message.setType((byte) 0x00);
        message.setTag(tag);

        // length 为value的长度，发送的包体内容
        message.setLength(msg.length());

        message.setValue(msg.getBytes(CharsetUtil.UTF_8));

        log.info("【客户端】发送的消息长度 length:{} ", msg.length());

        return message;

    }
}

