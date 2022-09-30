package com.only.multids.config;

import com.only.multids.core.IRouting;
import com.only.multids.core.RoutingDsAndTbStrategy;
import com.only.multids.core.RoutingDsStrategy;
import com.only.multids.core.RoutingTbStategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Title: 策略配置类与配置属性关联类
 */
@Configuration
public class RoutingStategyConfig {

    /**
     *  多库多表
     * @return
     *  @ConditionalOnProperty  读取propeties文件中内容
     *
     *  属性name以及havingValue来实现的，其中name用来从application.properties中读取某个属性值。
        如果该值为空，则返回false;
        如果值不为空，则将该值与havingValue指定的值进行比较，如果一样则返回true;否则返回false。
        如果返回值为false，则该configuration不生效；为true则生效。
     *
     *
     *
     */
    @Bean
    @ConditionalOnProperty(prefix = "only.dsroutingset",name = "routingStategy",havingValue ="ROUTING_DS_TABLE_STATEGY")
    public IRouting routingDsAndTbStrategy() {
        return new RoutingDsAndTbStrategy();
    }

    /**
     *  多库一表
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "only.dsroutingset",name = "routingStategy",havingValue ="ROUTGING_DS_STATEGY")
    public IRouting routingDsStrategy() {
        return new RoutingDsStrategy();
    }

    /**
     *  一库夺多表
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "only.dsroutingset",name = "routingStategy",havingValue ="ROUTGIN_TABLE_STATEGY")
    public IRouting routingTbStategy() {
        return new RoutingTbStategy();
    }
}
