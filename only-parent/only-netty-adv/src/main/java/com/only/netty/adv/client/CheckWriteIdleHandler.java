package com.only.netty.adv.client;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @description ：客户端检测自己的写空闲
 */
public class CheckWriteIdleHandler extends IdleStateHandler {

    public CheckWriteIdleHandler() {
        super(0, 8, 0);
    }
}
