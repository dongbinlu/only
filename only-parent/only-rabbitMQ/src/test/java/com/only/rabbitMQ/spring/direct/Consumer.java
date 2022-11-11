package com.only.rabbitMQ.spring.direct;

import com.only.rabbitMQ.spring.utils.RabbitMQUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class Consumer {
    @Test
    public void receiveMessage() throws Exception {

        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("hello",
                true,
                false,
                false,
                null);

        channel.basicConsume("hello", true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                System.out.println("消费者收到消息： " + new String(body));
            }

        });

        System.in.read();
    }

}
