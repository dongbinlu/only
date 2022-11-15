package com.only.rabbitMQ.spring.rounting_direct;

import com.only.rabbitMQ.spring.utils.RabbitMQUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class Consumer1 {

    @Test
    public void receiveMessage() throws Exception {

        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        // 申明交换机
        channel.exchangeDeclare("logs_direct", BuiltinExchangeType.DIRECT);

        // 从信道里获取一个临时队列
        String queue = channel.queueDeclare().getQueue();
        //交换机和队列绑定
        channel.queueBind(queue,
                "logs_direct",
                "info");

        channel.basicConsume(queue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者【1】收到消息：" + new String(body));
            }
        });
        System.in.read();


    }


}
