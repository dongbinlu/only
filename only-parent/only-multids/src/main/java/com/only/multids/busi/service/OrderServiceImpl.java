package com.only.multids.busi.service;

import com.only.multids.annotation.Router;
import com.only.multids.busi.bean.Order;
import com.only.multids.busi.dao.OrderMapper;
import com.only.multids.busi.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;


    public void insertOrder(Order order) {
        orderMapper.insertOrder(order);
    }

    @Router(routingFiled = "orderId")
    public void insertOrder3(Order order) {

        orderMapper.insertOrder(order);
    }


    public void insertS(Map<String, Object> params) {
        orderMapper.insertS(params);
    }


    public List<Order> getByOrderId(Long orderId, String tableSuffix) {

        return orderMapper.getByOrderId(orderId, tableSuffix);
    }
}
