package com.only.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageEncoder extends MessageToByteEncoder<Message> {

    /**
     * // 获取UTF-8字符的最大字节序列长度
     * public static int utf8MaxBytes(CharSequence seq){}
     * <p>
     * // 写入UTF-8字符序列，返回写入的字节长度 - 建议使用此方法
     * public static int writeUtf8(ByteBuf buf, CharSequence seq){}
     *
     * @param ctx
     * @param message
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf byteBuf) throws Exception {

        byteBuf.writeByte(message.getFlag());
        byteBuf.writeByte(message.getType());
        byteBuf.writeByte(message.getTag());
        // 记录写入游标
        int writerIndex = byteBuf.writerIndex();
        // 预写一个length 此消息长度并不一定是真实长度（UTF-8编码的中文占3个字节）
        byteBuf.writeInt(message.getLength());
        //  写入UTF-8字符序列并返回写入的字节长度
        int length = ByteBufUtil.writeUtf8(byteBuf, new String(message.getValue(), CharsetUtil.UTF_8));
        // 覆盖length，替换为消息真实长度
        byteBuf.setInt(writerIndex, length);
    }

}