package com.only.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;

/**
 * 消息分解器
 */
public interface Resolver {

    boolean support(Message message);

    Message resolve(ChannelGroup channelGroup, ChannelHandlerContext ctx, Message message);

}
