package com.only.order.center.config;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

import java.util.List;

public class WeightBalancer extends Balancer {

    public static Instance chooseInstanceByRandomWeight(List<Instance> hosts){
        return getHostByRandomWeight(hosts);
    }

}
