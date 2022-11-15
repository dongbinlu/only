package com.only.rabbitMQ.producer.service;

import com.only.rabbitMQ.producer.entity.MessageContent;
import com.only.rabbitMQ.producer.entity.OrderInfo;

public interface IOrderInfoService {

    void saveOrderInfo(OrderInfo orderInfo, MessageContent messageContent);

    void saveOrderInfoWithMessage(OrderInfo orderInfo);

}
