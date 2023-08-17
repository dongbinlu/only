package com.only.rocketMQ.producer.service.impl;

import com.only.rocketMQ.producer.service.PayService;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {

    private static final String TX_GROUP_NAME = "TxPayGroup";

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void pay(double money) {
        sendTransMsg();
    }

    /**
     * 发送事务消息
     */
    private void sendTransMsg() {

        String[] tags = {"TagA", "TagB", "TagC", "TagD", "TagE"};

        for (int i = 0; i < 3; i++) {

            try {
                Message<String> msg = MessageBuilder.withPayload("Hello RocketMQ" + i)
                        .setHeader(RocketMQHeaders.KEYS, "KEY_" + i)
                        .build();

                // TX_GROUP_NAME 必须同TransactionListener类注解@RocketMQTransactionListener(txProducerGroup = "TxPayGroup")
                TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(TX_GROUP_NAME,
                        "SpringTransTopic" + ":" + tags[i % tags.length],
                        msg,
                        null);

                System.out.printf("=send transactional msg body = %s , sendResult=%s %n",
                        msg.getPayload(),
                        sendResult.getSendStatus());

                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }
}
