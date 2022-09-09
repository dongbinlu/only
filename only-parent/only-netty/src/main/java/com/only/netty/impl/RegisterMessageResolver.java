package com.only.netty.impl;

import com.only.netty.Message;
import com.only.netty.Resolver;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RegisterMessageResolver implements Resolver {

    @Override
    public boolean support(Message message) {
        return message.getFlag() == (byte) 0xD0 && message.getType() == (byte) 0x00 && message.getTag() == (byte) 0x01;
    }

    @Override
    public Message resolve(ChannelGroup channelGroup, ChannelHandlerContext ctx, Message message) {

        log.info("注册处理器");

        ByteBuf byteBuf = Unpooled.copiedBuffer(message.getValue());

        Message response = new Message();
        String value = "";
        try {
            response.setFlag(message.getFlag());
            response.setTag((byte) 0x01);
            response.setTag(message.getTag());
            value = "注册成功";
        } catch (Exception e) {
            value = "注册失败";
        }
        response.setLength(value.length());
        response.setValue(value.getBytes(CharsetUtil.UTF_8));

        ctx.channel().writeAndFlush(response);

        return response;
    }

    /**
     * TLV 解析
     *
     * @param byteBuf
     * @return
     */
    private byte[] getValue(ByteBuf byteBuf) {

        int length = byteBuf.readInt();
        byte[] value = new byte[length];
        byteBuf.readBytes(value);

        return value;
    }

}