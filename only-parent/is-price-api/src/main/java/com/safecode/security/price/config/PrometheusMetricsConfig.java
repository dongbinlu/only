package com.safecode.security.price.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Summary;

//自定义Metrics监控指标
@Configuration
public class PrometheusMetricsConfig {

    @Autowired
    private PrometheusMeterRegistry prometheusMeterRegistry;

    @Bean
    public Counter requestCounter() {
        return Counter.build("is_request_count", "count request by service")
                .labelNames("service", "method", "code")
                .register(prometheusMeterRegistry.getPrometheusRegistry());

    }

    // 客户端定义的数据分布统计图
    @Bean
    public Summary requestLatency() {
        return Summary.build("is_request_latency", "monite_request_latency_by_service")
                .quantile(0.5, 0.05)
                .quantile(0.9, 0.01)
                .labelNames("service", "method", "code")
                .register(prometheusMeterRegistry.getPrometheusRegistry());
    }

}
