package com.only.rabbitMQ.producer.compent;

import com.only.rabbitMQ.producer.constants.MsgStatusEnum;
import com.only.rabbitMQ.producer.entity.MessageContent;
import com.only.rabbitMQ.producer.mapper.MessageContentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MsgConfirm implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private MessageContentMapper messageContentMapper;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        String msgId = correlationData.getId();

        if (ack) {
            log.info("消息ID:{}对应的消息被broker签收成功", msgId);
            updateMsgStatusWithAck(msgId);
        } else {
            log.warn("消息ID:{}对应的消息被broker签收失败,失败原因:{}", msgId, cause);
            updateMsgStatusWithNAck(msgId, cause);
        }

    }

    private void updateMsgStatusWithAck(String msgId) {
        MessageContent messageContent = MessageContent.builder()
                .msgId(msgId)
                .updateTime(new Date())
                .msgStatus(MsgStatusEnum.SENDING_SUCCESS.getCode())
                .build();
        messageContentMapper.updateByPrimaryKeySelective(messageContent);
    }

    private void updateMsgStatusWithNAck(String msgId, String cause) {
        MessageContent messageContent = MessageContent.builder()
                .msgId(msgId)
                .updateTime(new Date())
                .errCause(cause)
                .msgStatus(MsgStatusEnum.SENDING_FAIL.getCode())
                .build();
        messageContentMapper.updateByPrimaryKeySelective(messageContent);
    }

}
