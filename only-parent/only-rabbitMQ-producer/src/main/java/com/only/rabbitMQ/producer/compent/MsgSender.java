package com.only.rabbitMQ.producer.compent;

import com.only.rabbitMQ.producer.constants.MqConst;
import com.only.rabbitMQ.producer.entity.MsgTxtBo;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MsgSender implements InitializingBean {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MsgConfirm msgConfirm;

    @Autowired
    private MsgReturn msgReturn;

    public void senderMsg(MsgTxtBo msgTxtBo) {
        CorrelationData correlationData = new CorrelationData(msgTxtBo.getMsgId());
        rabbitTemplate.convertAndSend(MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME,
                MqConst.ORDER_TO_PRODUCT_ROUTING_KEY,
                msgTxtBo, correlationData);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        rabbitTemplate.setConfirmCallback(msgConfirm);
        rabbitTemplate.setReturnCallback(msgReturn);
        // 设置消息转换器
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);

    }
}
