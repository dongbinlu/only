package com.only.order.center.config;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced

    /**
     *流控后失败的处理
     */
    @SentinelRestTemplate(
//            blockHandler = "handleBlock",
//            blockHandlerClass = RibbonSentinelFallbackHander.class,
//            fallback = "handleFallback",
//            fallbackClass = RibbonSentinelFallbackHander.class
    )
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

/*    @Bean
    public RetryTemplate retryTemplate(){
        return new RetryTemplate();
    }*/


/*    @Bean
    public RestTemplate restTemplate(DiscoveryClient discoveryClient) {
        return new CustomRestTemplate(discoveryClient);
    }*/

}
