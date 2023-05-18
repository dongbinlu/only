package com.only.feign.core;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public abstract class AbstractDiscoveryClientSupport implements InitializingBean {

    private DiscoveryClient discoveryClient;

    public DiscoveryClient getDiscoveryClient() {
        return discoveryClient;
    }

    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
