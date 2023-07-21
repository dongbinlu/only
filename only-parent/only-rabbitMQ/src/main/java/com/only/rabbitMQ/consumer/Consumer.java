package com.only.rabbitMQ.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Consumer {

    @RabbitListener(queuesToDeclare = {@Queue("spring-boot-direct")})
    public void receiveMessageToDirect(String message) {
        System.out.println("消费者收到消息：" + message);

    }

    @RabbitListener(queuesToDeclare = {@Queue("spring-boot-work-queue")})
    public void receiveMessageToWorkQueue1(String message) throws IOException {
        System.out.println("消费者【1】收到消息： " + message);
    }

    @RabbitListener(queuesToDeclare = {@Queue("spring-boot-work-queue")})
    public void receiveMessageToWorkQueue2(String context,
                                           Message message,
                                           Channel channel) throws IOException {
        // 一次只签收1条未确认的消息
        channel.basicQos(1);

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        // 手动签收
        channel.basicAck(deliveryTag, false);
        System.out.println("消费者【2】收到消息： " + context);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("fanoutQueue1"),
            exchange = @Exchange(name = "spring-boot-fanout-exchange",
                    type = ExchangeTypes.FANOUT)
    ))
    public void receiveMessageToFanout1(String message) {
        System.out.println("消费者【1】收到消息: " + message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("fanoutQueue2"),
            exchange = @Exchange(name = "spring-boot-fanout-exchange",
                    type = ExchangeTypes.FANOUT)
    ))
    public void receiveMessageToFanout2(String message) {
        System.out.println("消费者【2】收到消息： " + message);
    }

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue("directQueue1"),
            exchange = @Exchange(name = "spring-boot-rounting-direct",
                    type = ExchangeTypes.DIRECT),
            key = "info"
    )})
    public void receiveMessageToRountDirect1(String content,Message message , Channel channel) throws IOException {
        System.out.println("消费者【1】收到消息： " + content);
        System.out.println("消息对象为：" + message);
        System.out.println("信道为：" + channel);
        //消息投递ID
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("投递ID："+ deliveryTag);
        //消息自定义ID
        String messageId = message.getMessageProperties().getMessageId();
        System.out.println("自定义ID："+ messageId);

        /**
         * deliveryTag 消息投递ID，要签收的消息ID是多少
         * multiple 是否批量签收
         */
        channel.basicAck(deliveryTag, false);
        System.out.println("消息签收成功");
        System.out.println("--------------------------------------------------");

    }

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue("directQueue2"),
            exchange = @Exchange(name = "spring-boot-rounting-direct",
                    type = ExchangeTypes.DIRECT),
            key = {"debug", "error", "warn"}
    )})
    public void receiveMessageToRountDirect2(String content,Message message , Channel channel) throws IOException {
        //消息投递ID
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        /**
         * deliveryTag 消息投递ID，要签收的消息ID是多少
         * multiple 是否批量签收
         */
        channel.basicAck(deliveryTag, false);
        System.out.println("消费者【2】收到消息： " + content);
    }

    @RabbitListener(bindings={@QueueBinding(
            value = @Queue("direct-exchange-queue"),
            exchange = @Exchange(name = "direct-exchange",type = ExchangeTypes.DIRECT),
            key = {"info"}
    )})
    public void receiveMessage(String content,Message message , Channel channel) throws Exception{
        //消息投递ID
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("投递ID："+ deliveryTag);
        //消息自定义ID
        String messageId = message.getMessageProperties().getMessageId();
        System.out.println("自定义ID："+ messageId);
        if(content.equals("hello world3")){

            /**
             * deliveryTag 消息投递ID，要签收的消息ID是多少
             * multiple 是否批量签收
             */
            channel.basicAck(deliveryTag, true);
            System.out.println("消息签收成功-内容为："+ content);
        }
        System.out.println("--------------------------------------------------");

    }



    @RabbitListener(queues = {"new.queue"})
    public void receiveDelayedMessage(String content,Message message , Channel channel) throws Exception{
        System.out.println("正在消费中。。。。");
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        channel.basicAck(deliveryTag, false);
        System.out.println("消息签收成功： " + content);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("签收成功时间：" + sdf.format(new Date()));
    }


}
