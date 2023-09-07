package com.only.netty.client.simple.udp;

import org.apache.commons.lang3.StringUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Send {


    public static void main(String[] args) throws Exception {

        // 1、创建码头
        DatagramSocket socket = new DatagramSocket();


        Scanner scanner = new Scanner(System.in);
        System.out.println("请您输入要发送的数据！");
        String msg = scanner.nextLine();
        while (!StringUtils.equals(msg, "0")) {
            // 2、创建集装箱，封装发送的数据，并指定IP和端口号
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getLocalHost(), 18090);
            // 3、发送数据
            socket.send(packet);
            System.out.println("数据发送成功,请继续，按0退出");
            msg = scanner.nextLine();
        }
        // 4、关闭资源
        socket.close();

    }

}
