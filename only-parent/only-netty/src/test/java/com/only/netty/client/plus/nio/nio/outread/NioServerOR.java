package com.only.netty.client.plus.nio.nio.outread;

/**
 * 类说明：nio通信服务端
 */
public class NioServerOR {
    private static NioServerORHandle nioServerHandle;

    public static void main(String[] args){
        nioServerHandle = new NioServerORHandle(12345);
        new Thread(nioServerHandle,"Server").start();
    }

}
