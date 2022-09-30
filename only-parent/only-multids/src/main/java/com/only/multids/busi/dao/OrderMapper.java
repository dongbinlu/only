package com.only.multids.busi.dao;

import com.only.multids.busi.bean.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper {

    void insertOrder(Order order);


    @Insert("insert into student values(#{params.id},#{params.name})")
    void insertS(@Param("params") Map<String, Object> params);


    List<Order> getByOrderId(@Param("orderId") Long orderId, @Param("tableSuffix") String tableSuffix);
}
