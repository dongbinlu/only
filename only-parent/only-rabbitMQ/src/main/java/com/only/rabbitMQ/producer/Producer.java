package com.only.rabbitMQ.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessageToDirect() {
        rabbitTemplate.convertAndSend("spring-boot-direct", "this is first springboot MQ direct");
        System.out.println("消息发送成功");
    }


    @Test
    public void sendMessageToWorkQueue() throws InterruptedException {
        for (int i = 0; i < 100; i++) {

            rabbitTemplate.convertAndSend("spring-boot-work-queue", "this is first springboot MQ work queue " + i);

        }
        System.out.println("消息发送成功");

    }

    @Test
    public void sendMessageToFanout()throws Exception {

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * 当消息到达交换机后该消息会被回调
             * @param correlationData 相关的数据
             * @param ack 交换机接受消息是否成功
             * @param cause 如果没有成功，返回原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

                if (ack) {
                    System.out.println("交换机接受消息成功");
                } else {
                    System.out.println("交换机接受消息失败，原因：" + cause);
                }

            }
        });


        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 消息如未正常到达队列里面会回调
             *
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

                System.out.println("消息未正常到达队列-message:" + message);
                System.out.println("消息未正常到达队列-replyCode:" + replyCode);
                System.out.println("消息未正常到达队列-replyText:" + replyText);
                System.out.println("消息未正常到达队列-exchange:" + exchange);
                System.out.println("消息未正常到达队列-rountingKey:" + routingKey);

            }

        });


        rabbitTemplate.convertAndSend("spring-boot-fanout-exchange", "", "this is first springboot MQ fanout");

        System.out.println("发送成功");
    }


    @Test
    public void sendMessageTORoutingDirect() {
        rabbitTemplate.convertAndSend("spring-boot-rounting-direct", "info", "this is first springboot MQ rounting-direct-info", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                String messageId = UUID.randomUUID().toString().replace("-", "");
                message.getMessageProperties().setMessageId(messageId);
                return message;

            }
        });
        rabbitTemplate.convertAndSend("spring-boot-rounting-direct", "debug", "this is first springboot MQ rounting-direct-debug");
        rabbitTemplate.convertAndSend("spring-boot-rounting-direct", "error", "this is first springboot MQ rounting-direct-error");
        rabbitTemplate.convertAndSend("spring-boot-rounting-direct", "warn", "this is first springboot MQ rounting-direct-warn");
        System.out.println("消息发送成功");
    }

    static int a =1;
    @Test
    public void sendMessage() throws Exception {
        for (int i = 1; i <= 5; i++) {
            rabbitTemplate.convertAndSend("direct-exchange", "info", "hello world" + i, new MessagePostProcessor() {

                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setMessageId((a++)+"");
                    return message;
                }
            });
        }
        System.out.println("消息发送成功");
        System.in.read();
    }


    @Test
    public void sendDelayedMessage() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("消息发送时间：" + sdf.format(new Date()));
        rabbitTemplate.convertAndSend("dealy", "我是一个延时消息");
        System.out.println("消息发送成功");
        Thread.sleep(20 * 1000);
    }


}
