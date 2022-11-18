package com.only.rocketMQ.producer.controller;

import com.only.rocketMQ.producer.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @GetMapping
    public String pay() {
        payService.pay(100L);
        return "ok";
    }

}
