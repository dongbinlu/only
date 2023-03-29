package com.safecode.security.price.controller;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.security.price.entity.PriceInfo;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/prices")
@Slf4j
public class PriceController {

    @GetMapping("/{id}")
    public PriceInfo get(@PathVariable("id") Long id, @AuthenticationPrincipal String username) throws Exception {
        log.info("product id is {} , username:{}", id, username);
        PriceInfo info = new PriceInfo();
        info.setId(id);
        info.setPrice(new BigDecimal(100));
        Thread.sleep(RandomUtils.nextInt(100, 1000));

        return info;
    }

    @GetMapping("/test")
    public Object test() {
        throw new RuntimeException("haha test");
    }

}
