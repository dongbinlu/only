package com.only.netty.client.simple.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {

    public static void main(String[] args) throws Exception {

        // 1、创建一个在本地端口进行监听的服务socket通道，并设置为非阻塞方式
        ServerSocketChannel ssc = ServerSocketChannel.open();

        // 必须配置为非阻塞才能往selector上注册，否则会报错，selector本身就是非阻塞模式
        ssc.configureBlocking(false);

        ssc.socket().bind(new InetSocketAddress(18090));

        // 2、创建一个选择器selector
        Selector selector = Selector.open();

        // 3、把ServerSocketChannel注册到Selector上（事件通知机制，每当有IO事件就绪，系统注册的回调函数就会被调用），
        // 并且selector对客户端accept连接操作感兴趣
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            System.out.println("等待客户端事件发生。。。");
            // 4、轮询监听selector里的key,select是阻塞的，accept()也是阻塞的
            // 轮询监听channel里的key，没有客户端事件时一直阻塞在这里，这是正常情况
            int select = selector.select();

            System.out.println("有事件发生了。。。");
            // 有客户端请求，被轮询监听到
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                // 删除本次已处理的key，防止下次select重复处理
                it.remove();
                handler(key);
            }
        }

    }

    private static void handler(SelectionKey key) throws Exception {

        if (key.isAcceptable()) {
            System.out.println("有客户端连接事件发生了。。。");

            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();

            // NIO非阻塞体现：此处accpet方法是阻塞的，但是这里因为是发生了连接事件
            // 所以这个方法马上会被执行，不会阻塞
            // 处理完连接请求不会继续等待客户端的数据发送
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            // 将SocketChannel注册到Selector上，并且Selector对读事件感兴趣
            sc.register(key.selector(), SelectionKey.OP_READ);
        } else if (key.isReadable()) {

            System.out.println("有客户端数据可读事件发生了。。。");
            SocketChannel sc = (SocketChannel) key.channel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // NIO 非阻塞体现：首先read方法不会阻塞，其次这种事件响应模型，当
            //调用到read方法时肯定发生了客户端发送数据的事件
            int length = sc.read(buffer);
            if (length != -1) {
                System.out.println("读取到客户端发送的数据：" + new String(buffer.array(), 0, length));
            }

            ByteBuffer bufferToWrite = ByteBuffer.wrap("hello client".getBytes());
            sc.write(bufferToWrite);
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        } else if (key.isWritable()) {
            System.out.println("write事件。。。");
            SocketChannel sc = (SocketChannel) key.channel();
            // NIO事件触发是水平触发
            // 使用Java的NIO编程的时候，在没有数据可以往外写的时候要取消写事件，
            // 在有数据往外写的时候再注册写事件
            key.interestOps(SelectionKey.OP_READ);
            //sc.close();

        }


    }


}
