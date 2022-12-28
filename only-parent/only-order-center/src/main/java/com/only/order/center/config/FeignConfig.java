package com.only.order.center.config;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    /**
     * feign的默认契约，采用springMVC的注解
     * @return
     */
    //@Bean
    //public Contract feignConstract(){
    //    return new Contract.Default();
    //}

}
