package com.only.rabbitMQ.producer.mapper;

import com.only.rabbitMQ.producer.entity.MessageContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageContentMapper {
    int deleteByPrimaryKey(String msgId);

    int insert(MessageContent record);

    int insertSelective(MessageContent record);

    MessageContent selectByPrimaryKey(String msgId);

    int updateByPrimaryKeySelective(MessageContent record);

    int updateByPrimaryKey(MessageContent record);

    List<MessageContent> qryNeedRetryMsg(@Param("msgStatus") Integer status, @Param("timeDiff") Integer timeDiff);

    void updateMsgRetryCount(@Param("msgId") String msgId);

}
