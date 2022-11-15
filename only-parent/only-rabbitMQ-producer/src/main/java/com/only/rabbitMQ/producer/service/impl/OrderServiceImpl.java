package com.only.rabbitMQ.producer.service.impl;

import com.only.rabbitMQ.producer.compent.MsgSender;
import com.only.rabbitMQ.producer.constants.MqConst;
import com.only.rabbitMQ.producer.constants.MsgStatusEnum;
import com.only.rabbitMQ.producer.entity.MessageContent;
import com.only.rabbitMQ.producer.entity.MsgTxtBo;
import com.only.rabbitMQ.producer.entity.OrderInfo;
import com.only.rabbitMQ.producer.mapper.MessageContentMapper;
import com.only.rabbitMQ.producer.mapper.OrderInfoMapper;
import com.only.rabbitMQ.producer.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private MessageContentMapper messageContentMapper;

    @Autowired
    private MsgSender msgSender;


    @Override
    @Transactional
    public void saveOrderInfo(OrderInfo orderInfo, MessageContent messageContent) {
        try {
            orderInfoMapper.insertSelective(orderInfo);
            messageContentMapper.insertSelective(messageContent);
        } catch (Exception e) {
            log.error("第一步消息入库失败:{}", e);
            throw new RuntimeException("第一步消息入库失败");
        }

    }

    @Override
    public void saveOrderInfoWithMessage(OrderInfo orderInfo) {
        MessageContent messageContent = builderMessageContent(orderInfo);
        saveOrderInfo(orderInfo, messageContent);

        // 构造消息发送对象
        MsgTxtBo msgTxtBo = MsgTxtBo.builder()
                .productNo(orderInfo.getProductNo())
                .orderNo(orderInfo.getOrderNo())
                .msgId(messageContent.getMsgId())
                .build();

        msgSender.senderMsg(msgTxtBo);
    }

    private MessageContent builderMessageContent(OrderInfo orderInfo) {

        MessageContent messageContent = MessageContent.builder()
                .msgId(UUID.randomUUID().toString())
                .orderNo(orderInfo.getOrderNo())
                .productNo(orderInfo.getProductNo())
                .exchange(MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME)
                .routingKey(MqConst.ORDER_TO_PRODUCT_ROUTING_KEY)
                .maxRetry(MqConst.MSG_RETRY_COUNT)
                .currentRetry(0)
                .msgStatus(MsgStatusEnum.SENDING.getCode())
                .createTime(new Date())
                .build();

        return messageContent;

    }
}
