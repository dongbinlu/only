package com.only.netty.server;

import com.only.base.constant.OnlyConstants;
import com.only.netty.MessageDecoder;
import com.only.netty.MessageEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;

public class SocketChooseHandle extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {

        String protocol = getBufStart(byteBuf);

        // ws协议不是独立存在的，必须借助http协议升级得到的
        if (protocol.startsWith(OnlyConstants.Netty.WEB_SOCKET_PREFIX)) {
            // http协议解码
            ctx.pipeline().addBefore("nettyServerHandler", "http-decode", new HttpRequestDecoder());
            // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息,将请求头和请求体合并在一起，最大不能超过64KB
            ctx.pipeline().addBefore("nettyServerHandler", "http-aggregator", new HttpObjectAggregator(65535));
            // http协议编码
            ctx.pipeline().addBefore("nettyServerHandler", "http-encode", new HttpResponseEncoder());

            // ChunkedWriteHandler：向客户端发送HTML5文件,文件过大会将内存撑爆
            ctx.pipeline().addBefore("nettyServerHandler", "http-chunked", new ChunkedWriteHandler());

            ctx.pipeline().addBefore("nettyServerHandler", "WebSocketAggregator", new WebSocketFrameAggregator(65535));
            //用于处理websocket, /ws为访问websocket时的uri
            ctx.pipeline().addBefore("nettyServerHandler", "ProtocolHandler", new WebSocketServerProtocolHandler(OnlyConstants.Netty.WEB_SOCKET_PATH));

            // 此次要移除socket 相关的编码
            ctx.pipeline().remove(MessageDecoder.class);
            ctx.pipeline().remove(MessageEncoder.class);

            // 此次要移除心跳检测
            ctx.pipeline().remove(IdleStateHandler.class);
        }

        byteBuf.resetReaderIndex();
        ctx.pipeline().remove(this.getClass());
    }

    private String getBufStart(ByteBuf byteBuf) {
        int length = byteBuf.readableBytes();
        if (length > OnlyConstants.Netty.MAX_LENGTH) {
            length = OnlyConstants.Netty.MAX_LENGTH;
        }
        // 标记读位置
        byteBuf.markReaderIndex();
        byte[] content = new byte[length];
        byteBuf.readBytes(content);
        return new String(content);
    }
}