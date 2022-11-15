package com.only.rabbitMQ.consumer.service.impl;

import com.only.rabbitMQ.consumer.constants.MsgStatusEnum;
import com.only.rabbitMQ.consumer.entity.MessageContent;
import com.only.rabbitMQ.consumer.entity.MsgTxtBo;
import com.only.rabbitMQ.consumer.exception.BizExp;
import com.only.rabbitMQ.consumer.mapper.MessageContentMapper;
import com.only.rabbitMQ.consumer.mapper.ProductInfoMapper;
import com.only.rabbitMQ.consumer.service.IProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class ProductInfoServiceImpl implements IProductInfoService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private MessageContentMapper messageContentMapper;

    @Override
    @Transactional
    public void updateProductStore(MsgTxtBo msgTxtBo) {

        try {
            // 更新库存
            productInfoMapper.updateProductStoreById(msgTxtBo.getProductNo());
            // 更新消息状态
            updateMsgStatus(msgTxtBo);
            // System.out.println(1/0);
        } catch (Exception e) {
            log.error("消费-更新数据库失败:{}", e);
            throw new BizExp(0, "消费-更新数据库失败");
        }

    }

    public void updateMsgStatus(MsgTxtBo msgTxtBo) {
        MessageContent messageContent = MessageContent.builder()
                .msgId(msgTxtBo.getMsgId())
                .updateTime(new Date())
                .msgStatus(MsgStatusEnum.CONSUMER_SUCCESS.getCode())
                .build();
        messageContentMapper.updateByPrimaryKeySelective(messageContent);
    }

}
