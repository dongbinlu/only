package com.only.netty.client.plus.nio.nio.writeable;

/**
 * 类说明：nio通信服务端
 */
public class NioServerWritable {
    private static NioServerHandleWriteable nioServerHandle;

    public static void start(){

    }
    public static void main(String[] args){
        nioServerHandle = new NioServerHandleWriteable(12345);
        new Thread(nioServerHandle,"NIO_Server").start();
    }

}
