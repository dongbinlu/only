package com.only.rabbitMQ.producer.controller;

import com.only.rabbitMQ.producer.entity.OrderInfo;
import com.only.rabbitMQ.producer.service.IOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderInfoService iOrderInfoService;

    @GetMapping
    public String saveOrder() {

        OrderInfo orderInfo = OrderInfo.builder()
                .orderNo(System.currentTimeMillis())
                .productNo(1)
                .userName("boy")
                .money(1000D)
                .createTime(new Date())
                .build();

        iOrderInfoService.saveOrderInfoWithMessage(orderInfo);

        return "ok";

    }


}
