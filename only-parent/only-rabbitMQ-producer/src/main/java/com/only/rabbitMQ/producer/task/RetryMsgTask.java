package com.only.rabbitMQ.producer.task;

import com.only.rabbitMQ.producer.compent.MsgSender;
import com.only.rabbitMQ.producer.constants.MqConst;
import com.only.rabbitMQ.producer.constants.MsgStatusEnum;
import com.only.rabbitMQ.producer.entity.MessageContent;
import com.only.rabbitMQ.producer.entity.MsgTxtBo;
import com.only.rabbitMQ.producer.mapper.MessageContentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RetryMsgTask {

    @Autowired
    private MsgSender msgSender;

    @Autowired
    private MessageContentMapper messageContentMapper;

    /*
     * 延时5s启动 周期10s一次
     *
     * initialDelay 在第一次执行之前要延迟的毫秒数, fixedDelay 在上一次调用结束，下一次调用开始
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void retrySend() {
        // 查询30s消息状态还没有完结的消息
        List<MessageContent> messageContents = messageContentMapper
                .qryNeedRetryMsg(MsgStatusEnum.CONSUMER_SUCCESS.getCode(), MqConst.TIME_DIFF);
        for (MessageContent messageContent : messageContents) {
            if (messageContent.getMaxRetry() > messageContent.getCurrentRetry()) {
                MsgTxtBo msgTxtBo = MsgTxtBo.builder()
                        .msgId(messageContent.getMsgId())
                        .orderNo(messageContent.getOrderNo())
                        .productNo(messageContent.getProductNo())
                        .build();
                // 更新重试次数
                messageContentMapper.updateMsgRetryCount(messageContent.getMsgId());
                // 重试
                msgSender.senderMsg(msgTxtBo);
            } else {
                log.warn("消息:{}以及达到最大重试次数。。。", messageContent);
            }
        }
    }

}
