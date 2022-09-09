package com.only.netty.impl;

import com.only.netty.Message;
import com.only.netty.Resolver;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HistoryMessageResolver implements Resolver {


    @Override
    public boolean support(Message message) {
        return message.getFlag() == (byte) 0x00 &&
                message.getType() == (byte) 0x00 &&
                message.getTag() == (byte) 0x00 &&
                StringUtils.equalsIgnoreCase("0x00", new String(message.getValue(), CharsetUtil.UTF_8));
    }

    @Override
    public Message resolve(ChannelGroup channelGroup, ChannelHandlerContext ctx, Message message) {

        log.info("历史处理器");

        ByteBuf byteBuf = Unpooled.copiedBuffer(message.getValue());

        Message response = new Message();
        String value = "";
        try {
            response.setFlag(message.getFlag());
            response.setTag((byte) 0x01);
            response.setTag(message.getTag());
            value = "历史处理成功";
        } catch (Exception e) {
            value = "历史处理失败";
        }
        response.setLength(value.length());
        response.setValue(value.getBytes(CharsetUtil.UTF_8));

        new Thread(() -> {
            while (true) {
                if (ctx.channel().isActive()) {
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(new String(response.getValue(), CharsetUtil.UTF_8)));
                    try {
                        Thread.sleep(2 * 1000L);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    break;
                }
            }
        }).start();

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