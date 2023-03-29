package com.safecode.security.order.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.safecode.security.order.entity.OrderInfo;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private RestTemplate restTemplate = new RestTemplate();

    /*
     * 用户名从哪来，在网关路由时传递过来
     */
    @PostMapping
    public OrderInfo create(OrderInfo info, @RequestHeader String username) {

        log.info("username is {}", username);

        // log.info("product id is {}", info.getProductId());
        // PriceInfo price =
        // restTemplate.getForObject("http://127.0.0.1:8020/prices/" +
        // info.getProductId(),
        // PriceInfo.class);
        // log.info("price is {}", price.getPrice());
        return info;
    }

    @GetMapping("/{id}")
    public OrderInfo get(@PathVariable("id") Long id, @RequestHeader String username, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        log.info("order id is {} , username is :{}", id, username);
        OrderInfo info = new OrderInfo();
        info.setId(id);
        info.setProductId(110l);
        return info;
    }

}
