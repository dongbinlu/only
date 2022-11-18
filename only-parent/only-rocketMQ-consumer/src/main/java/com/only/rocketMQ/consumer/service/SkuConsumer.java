package com.only.rocketMQ.consumer.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "SpringTransTopic", consumerGroup = "TxPayGroup")
public class SkuConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String msg) {
        System.out.printf("----StringTransactionConsumer received: %s \n", msg);
    }


}
