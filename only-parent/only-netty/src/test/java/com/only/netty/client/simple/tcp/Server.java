package com.only.netty.client.simple.tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {
        // 1、创建ServerSocket服务---服务端，并指定端口
        ServerSocket serverSocket = new ServerSocket(18090);

        while (true) {
            System.out.println("等待客户端连接。。。。。。");
            // 2、与客户端通信的socket，没有客户端连接时，一直阻塞在这里
            Socket socket = serverSocket.accept();
            System.out.printf("IP: %s , Port: %s ,连接了 \n",
                    socket.getInetAddress().getHostAddress(),
                    socket.getPort());
            handler(socket);
        }
    }

    private static void handler(Socket socket) throws Exception {

        // 3、获取客户端输入流
        InputStream input = socket.getInputStream();
        byte[] buf = new byte[1024];
        System.out.println("准备读取。。。");
        // 客户端没有数据发送时一直阻塞在这里
        int length = input.read(buf);
        if (length != -1) {
            System.out.printf("IP: %s, Port: %s ,msg: %s \n",
                    socket.getInetAddress().getHostAddress(),
                    socket.getPort(),
                    new String(buf, 0, length));
        }

        OutputStream output = socket.getOutputStream();
        output.write("hello client".getBytes());
        output.flush();

    }
}
