package com.only.netty.client.simple.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Send {


    public static void main(String[] args) throws Exception {

        // 1、创建码头
        DatagramSocket socket = new DatagramSocket();

        // 2、创建集装箱，封装发送的数据，并指定IP和端口号
        String msg = "hello";
        DatagramPacket packet = new DatagramPacket(msg.getBytes(),
                msg.length(),
                InetAddress.getLocalHost(),
                18090);
        // 3、发送数据
        socket.send(packet);

        System.out.println("数据发送成功");
        // 4、关闭资源
        socket.close();

    }

}
