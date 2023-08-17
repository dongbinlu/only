package com.only.netty.client.simple.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receive {

    public static void main(String[] args) throws Exception {

        // 1、创建码头，并指定端口
        DatagramSocket socket = new DatagramSocket(18090);
        // 2、创建集装箱，接受数据
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {
            // 3、接受数据存储到DatagramPacket对象中，没有数据接受时，一直阻塞在这里。
            socket.receive(packet);
            // 4、获取DataGramPacket对象内容
            byte[] data = packet.getData();
            int length = packet.getLength();
            int port = packet.getPort();
            InetAddress address = packet.getAddress();
            String hostAddress = address.getHostAddress();

            String msg = new String(data, 0, length);

            System.out.printf("IP地址: %s ,端口: %s ,数据大小: %s ,数据是: %s", hostAddress, port, length, msg);
        }

    }


}
