package com.only.rabbitMQ.consumer.compent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.only.rabbitMQ.consumer.constants.MqConst;
import com.only.rabbitMQ.consumer.constants.MsgStatusEnum;
import com.only.rabbitMQ.consumer.entity.MessageContent;
import com.only.rabbitMQ.consumer.entity.MsgTxtBo;
import com.only.rabbitMQ.consumer.exception.BizExp;
import com.only.rabbitMQ.consumer.mapper.MessageContentMapper;
import com.only.rabbitMQ.consumer.service.IProductInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MqConsumer {

    @Autowired
    private IProductInfoService productInfoService;

    @Autowired
    private MessageContentMapper messageContentMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // 不加分布式锁 , 可能存在重复消费问题
    //@RabbitListener(bindings = {
    //        @QueueBinding(value = @Queue(MqConst.ORDER_TO_PRODUCT_QUEUE_NAME),
    //                exchange = @Exchange(name = MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME, type
    //                        = ExchangeTypes.DIRECT), key = {
    //                MqConst.ORDER_TO_PRODUCT_ROUTING_KEY})})
    public void consumerMsg(String context, Message message, Channel channel) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MsgTxtBo msgTxtBo = objectMapper.readValue(message.getBody(), MsgTxtBo.class);

        log.info("消费消息:{}", msgTxtBo);

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        log.info("投递ID:{}", deliveryTag);

        try {
            productInfoService.updateProductStore(msgTxtBo);
            // 在网络瞬断的情况下，出现重复消费，库存在减少
            //System.out.println(1 / 0);
            // 消息签收
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            updateMsgStatus(msgTxtBo);
            channel.basicReject(deliveryTag, false);
        }

    }

    // 分布式锁
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(MqConst.ORDER_TO_PRODUCT_QUEUE_NAME), exchange = @Exchange(name = MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME, type = ExchangeTypes.DIRECT), key = {
                    MqConst.ORDER_TO_PRODUCT_ROUTING_KEY})})
    public void consumerMsgWithLock(String context, Message message, Channel channel) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MsgTxtBo msgTxtBo = objectMapper.readValue(message.getBody(), MsgTxtBo.class);

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        if (stringRedisTemplate.opsForValue().setIfAbsent("LOCK_KEY:" + msgTxtBo.getMsgId(), msgTxtBo.getMsgId())) {
            log.info("消费消息:{}", msgTxtBo);
            log.info("投递ID:{}", deliveryTag);

            try {
                productInfoService.updateProductStore(msgTxtBo);
                // 在网络瞬断的情况下，出现重复消费，库存不会减少
                System.out.println(1 / 0);
                // 消息签收
                channel.basicAck(deliveryTag, false);
            } catch (Exception e) {
                if (e instanceof BizExp) {
                    BizExp bizExp = (BizExp) e;
                    log.info("数据业务异常:{},即将删除分布式锁", bizExp.getErrMsg());
                    stringRedisTemplate.delete("LOCK_KEY:" + msgTxtBo.getMsgId());
                }
                updateMsgStatus(msgTxtBo);
                channel.basicReject(deliveryTag, false);
            }
        } else {
            log.info("请不要重复消费:{}", msgTxtBo);
            channel.basicReject(deliveryTag, false);
        }
    }

    public void updateMsgStatus(MsgTxtBo msgTxtBo) {
        MessageContent messageContent = MessageContent.builder()
                .msgId(msgTxtBo.getMsgId())
                .updateTime(new Date())
                .msgStatus(MsgStatusEnum.CONSUMER_FAIL.getCode())
                .build();
        messageContentMapper.updateByPrimaryKeySelective(messageContent);
    }

}

