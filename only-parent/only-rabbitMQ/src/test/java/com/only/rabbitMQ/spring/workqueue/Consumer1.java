package com.only.rabbitMQ.spring.workqueue;

import com.only.rabbitMQ.spring.utils.RabbitMQUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class Consumer1 {


    @Test
    public void receiveMessage() throws IOException {


        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("world",
                true,
                false,
                false,
                null);

        channel.basicConsume("world", true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者【1】收到消息：" + new String(body));
            }
        });

        System.in.read();
    }

}
