package com.only.rabbitMQ.spring.workqueue;

import com.only.rabbitMQ.spring.utils.RabbitMQUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class Consumer2 {


    @Test
    public void receiveMessage() throws IOException {


        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("world",
                true,
                false,
                false,
                null);

        // 设置一次只接受2条未确认的消息
        channel.basicQos(2);
        // 关闭自动签收机制，手动签收
        channel.basicConsume("world", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者【2】收到消息：" + new String(body));
                // 手动签收
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

        System.in.read();

    }

}
