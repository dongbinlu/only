package com.only.rabbitMQ.spring.workqueue;

import com.only.rabbitMQ.spring.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.io.IOException;

public class Producer {


    @Test
    public void sendMessage() throws IOException {

        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("world",
                true,
                false,
                false,
                null);

        for (int i = 1; i <= 100; i++) {

            channel.basicPublish("",
                    "world",
                    null,
                    "this is work queue".getBytes());
        }
        RabbitMQUtil.closeChannelAndConnection(channel, connection);
        System.out.println("消息发送成功");


    }

}
