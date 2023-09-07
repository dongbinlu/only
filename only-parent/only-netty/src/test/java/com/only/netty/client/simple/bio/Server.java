package com.only.netty.client.simple.bio;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
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

        InetAddress inetAddress = socket.getInetAddress();

        /**
         * 注意：以下代码的写法，如果当没有数据可读时，break掉，意味着客户端
         * 如果再一次发送数据，服务端是接受不到的，如果想要服务端一直能接受数据，read方法必须一直
         * 阻塞，不能让他退出。
         */
        // 3,获取客户端输入流
        InputStream inputStream = socket.getInputStream();

        byte[] bytes = new byte[1024];
        int read;
        while ((read =inputStream.read(bytes)) != -1) {
            System.out.println("IP: " + inetAddress.getHostAddress() + ",port: " + socket.getPort() + ",msg:"
                    + new String(bytes, 0, read));
            if (inputStream.available() == 0) {
                break;
            }

        }

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello client".getBytes());
        outputStream.flush();


    }
}
