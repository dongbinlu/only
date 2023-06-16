package com.only.product.center.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/service/instance/list")
    public List<ServiceInstance> getServiceInstanceList() {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("only-order-center");
        return serviceInstanceList;
    }

    @GetMapping("/get")
    public String get() {
        System.out.println("product...get");
        return "product";
    }

}
