package com.only.rabbitMQ.producer.mapper;

import com.only.rabbitMQ.producer.entity.OrderInfo;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(Long orderNo);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long orderNo);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

}
