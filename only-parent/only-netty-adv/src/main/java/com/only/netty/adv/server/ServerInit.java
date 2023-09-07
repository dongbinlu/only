package com.only.netty.adv.server;

import com.only.netty.adv.kryocodec.KryoDecoder;
import com.only.netty.adv.kryocodec.KryoEncoder;
import com.only.netty.adv.server.asyncpro.DefaultTaskProcessor;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 类说明：
 */
public class ServerInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

//        ch.pipeline().addLast(new MetricsHandler());
        /*粘包半包问题*/
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,
                0,2,0,
                2));
        ch.pipeline().addLast(new LengthFieldPrepender(2));

        /*序列化相关*/
        ch.pipeline().addLast(new KryoDecoder());
        ch.pipeline().addLast(new KryoEncoder());

        /*处理心跳超时*/
        ch.pipeline().addLast(new ReadTimeoutHandler(15));

        ch.pipeline().addLast(new LoginAuthRespHandler());
        ch.pipeline().addLast(new HeartBeatRespHandler());
        ch.pipeline().addLast(new ServerBusiHandler(new DefaultTaskProcessor()));

    }
}
