package com.only.order.center.controller;

import com.only.order.center.feign.ProductCenterFeignApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RefreshScope
@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${isNewBusi}")
    private Integer isNewBusi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ProductCenterFeignApi productCenterFeignApi;

    @GetMapping("/service/instance/list")
    public List<ServiceInstance> getServiceInstanceList() {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("only-order-center");
        return serviceInstanceList;
    }

    @GetMapping
    public String get() {
        System.out.println("order...get");

        restTemplate.getForEntity("http://product-center/test", String.class);

        return "order";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable("id") Integer id) {
        String product = productCenterFeignApi.product(id);
        return product;
    }

    @GetMapping("/isNewBusi")
    public Integer getIsNewBusi() {
        return isNewBusi;
    }
}
