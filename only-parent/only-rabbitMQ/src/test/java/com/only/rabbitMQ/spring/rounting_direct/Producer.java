package com.only.rabbitMQ.spring.rounting_direct;

import com.only.rabbitMQ.spring.utils.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

public class Producer {

    @Test
    public void sendMessage() throws Exception {

        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("logs_direct", BuiltinExchangeType.DIRECT);

        // 向交换机发送消息
        channel.basicPublish("logs_direct",
                "info",
                null,
                "this is first routing direct info".getBytes());
        channel.basicPublish("logs_direct",
                "debug",
                null,
                "this is first routing direct debug".getBytes());
        channel.basicPublish("logs_direct",
                "error",
                null,
                "this is first routing direct error".getBytes());
        channel.basicPublish("logs_direct",
                "warn",
                null,
                "this is first routing direct warn".getBytes());

        RabbitMQUtil.closeChannelAndConnection(channel, connection);
        System.out.println("消息发送成功");


    }

}
