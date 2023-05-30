package com.only.order.center.feign;

import com.only.order.center.config.FeignConfig;
import com.only.order.center.handler.FeignSentinelFallbackHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-center", path = "/product", configuration = FeignConfig.class, fallbackFactory = FeignSentinelFallbackHandler.class)
public interface ProductCenterFeignApi {

    //声明式接口,远程调用http://product‐center/product/{id}

    @GetMapping("/{id}")
    String product(@PathVariable("id") Integer id);

}
