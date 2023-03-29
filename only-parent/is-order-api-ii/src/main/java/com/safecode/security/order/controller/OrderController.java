package com.safecode.security.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safecode.security.order.entity.OrderInfo;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private static final String OAuth2Authentication = null;

    // private Oauth2RestTemplate restTemplate = new Oauth2RestTemplate();

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /*
     * 用户名从哪来，在网关路由时传递过来
     */
    @PostMapping
    // @PreAuthorize("#oauth2.hasScope('write')")
    // @PreAuthorize("hasRole('ADMIN')")
    // blockHandler处理降级
    @SentinelResource(value = "createOrder", blockHandler = "doOnBlock")
    public OrderInfo create(OrderInfo info, @AuthenticationPrincipal String username) throws Exception {

        // 1，申明资源
        // try(Entry entry = SphU.entry("createOrder")){
        // log.info("username is {}", username);
        // }catch (BlockException e) {
        // log.info("Blocked!");
        // log.error("Blocked ! " , e);
        // }
        log.info("username is {}", username);
        Thread.sleep(50);
        // log.info("product id is {}", info.getProductId());
        // PriceInfo price =
        // restTemplate.getForObject("http://127.0.0.1:8020/prices/" +
        // info.getProductId(),
        // PriceInfo.class);
        // log.info("price is {}", price.getPrice());
        return info;
    }

    // doOnBlock需要资源方法一致 ，只不过多了一个参数:BlockException
    public OrderInfo doOnBlock(OrderInfo info, @AuthenticationPrincipal String username, BlockException exception)
            throws Exception {

        log.info("blocked by " + exception.getClass().getSimpleName());

        return info;

    }

    @GetMapping("/{id}")
    @SentinelResource("getOrder")
    public OrderInfo get(@PathVariable("id") Long id, @AuthenticationPrincipal String username) {
        log.info("order id is {} , username is :{}", id, username);
        OrderInfo info = new OrderInfo();
        info.setId(id);
        info.setProductId(110l);
        return info;
    }

}
