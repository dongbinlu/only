package com.only.rabbitMQ.producer.compent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.only.rabbitMQ.producer.constants.MsgStatusEnum;
import com.only.rabbitMQ.producer.entity.MessageContent;
import com.only.rabbitMQ.producer.entity.MsgTxtBo;
import com.only.rabbitMQ.producer.mapper.MessageContentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class MsgReturn implements RabbitTemplate.ReturnCallback {

    @Autowired
    private MessageContentMapper messageContentMapper;

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            MsgTxtBo msgTxtBo = objectMapper.readValue(message.getBody(), MsgTxtBo.class);
            log.info("无法路由消息内容:{},原因为:{}", msgTxtBo, replyText);
            updateMsgStatusWithReturn(msgTxtBo, replyText);
        } catch (IOException e) {
            log.error("更新消息表异常:{}", e);

        }

    }

    private void updateMsgStatusWithReturn(MsgTxtBo msgTxtBo, String replyText) {
        MessageContent messageContent = MessageContent.builder()
                .msgId(msgTxtBo.getMsgId())
                .errCause(replyText)
                .updateTime(new Date())
                .msgStatus(MsgStatusEnum.SENDING_FAIL.getCode())
                .build();
        messageContentMapper.updateByPrimaryKeySelective(messageContent);
    }

}
