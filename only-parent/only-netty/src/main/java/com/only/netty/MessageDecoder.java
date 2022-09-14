package com.only.netty;

import com.only.base.constant.OnlyConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        // 需要将二进制字节码转换为Message数据包对象
        if (byteBuf.readableBytes() >= OnlyConstants.Netty.READABLE_BYTES_SIZE) {

            Message message = new Message();

            // flag
            byte flag = byteBuf.readByte();
            message.setFlag(flag);

            // type
            byte type = byteBuf.readByte();
            message.setType(type);

            // tag
            byte tag = byteBuf.readByte();
            message.setTag(tag);

            //如果byteBuf剩下的长度还有大于4个字节，说明body不为空
            if (byteBuf.readableBytes() > OnlyConstants.Netty.READABLE_BYTES_SIZE_MESSAGE) {

                // length
                int length = byteBuf.readInt();
                message.setLength(length);

                //value
                byte[] value = new byte[length];
                byteBuf.readBytes(value);
                message.setValue(value);

                out.add(message);
            } else {
                log.warn("消息内容为空");
                out.add(message);
            }
        } else {
            log.error("byteBuf readable bytes 不足" + OnlyConstants.Netty.READABLE_BYTES_SIZE + "位");
        }

    }

}
