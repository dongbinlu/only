package com.only.netty.client.simple.tcp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {
        // 1、创建socket客户端，连接localhost:18090服务
        Socket socket = new Socket("localhost", 18090);
        // 3s后，input.read还没有数据可读，则退出read阻塞。
        socket.setSoTimeout(3*1000);

        // 2、获取输出流
        OutputStream output = socket.getOutputStream();
        output.write("hello server".getBytes());
        output.flush();
        System.out.println("向服务端发送数据结束。。。");

        InputStream input = socket.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


        int length = 0;
        System.out.println("正在等待接受报文。。。");
        // 这行代码保障服务端确实响应发送数据，客户端才读取
        // 防止客户端发送数据后立马读取数据时为空
        while (length == 0){
            length = input.available();
        }

        // 每次读取1024个字节
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = input.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
            if (input.available() == 0){
                break;
            }
        }
        byte[] bytes = outputStream.toByteArray();
        System.out.println(new String(bytes));
        socket.close();

    }

}
