package com.only.netty.client.simple.tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {
        // 1、创建socket客户端，连接localhost:18090服务
        Socket socket = new Socket("localhost", 18090);

        // 2、获取输出流
        OutputStream output = socket.getOutputStream();
        output.write("hello server".getBytes());
        output.flush();
        System.out.println("向服务端发送数据结束。。。");

        InputStream input = socket.getInputStream();
        byte[] buf = new byte[1024];
        int length = 0;
        while ((length = input.read(buf)) != -1) {
            System.out.println("接收到服务端的数据：" + new String(buf, 0, length));
        }
        socket.close();

    }

}
