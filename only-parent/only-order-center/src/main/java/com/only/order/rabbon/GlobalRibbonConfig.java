package com.only.order.rabbon;

import com.netflix.loadbalancer.IRule;
import com.only.order.center.config.CustomWeightedRule;
import com.only.order.center.config.TheSameClusterPriorityRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalRibbonConfig {

/*
    @Bean
    public IRule theCustomWeightedRule() {
        return new CustomWeightedRule();
    }
*/

    @Bean
    public IRule theSameClusterPriorityRule() {
        return new TheSameClusterPriorityRule();
    }

}
