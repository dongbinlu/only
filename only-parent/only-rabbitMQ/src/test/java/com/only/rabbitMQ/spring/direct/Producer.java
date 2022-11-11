package com.only.rabbitMQ.spring.direct;

import com.only.rabbitMQ.spring.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.io.IOException;

public class Producer {

    @Test
    public void sendMessage() throws Exception {

        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("hello",
                true,
                false,
                false,
                null);

        channel.basicPublish("",
                "hello",
                null,
                "this is first MQ".getBytes());

        RabbitMQUtil.closeChannelAndConnection(channel, connection);

        System.out.println("消息发送成功");


    }


}
