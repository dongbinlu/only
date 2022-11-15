package com.only.rabbitMQ.spring.fanout;

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

        // 申明交换机
        channel.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);

        // 向交换机发送消息
        channel.basicPublish("logs",
                "",
                null,
                "this is first fanout".getBytes());

        RabbitMQUtil.closeChannelAndConnection(channel, connection);
        System.out.println("消息发送成功");

    }

}
