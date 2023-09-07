package com.only.netty.client.plus.nio.nio;

/**
 * 类说明：nio通信服务端
 */
public class NioServer {
    private static NioServerHandle nioServerHandle;

    public static void main(String[] args){
        nioServerHandle = new NioServerHandle(12345);
        new Thread(nioServerHandle,"Server").start();
    }

}
