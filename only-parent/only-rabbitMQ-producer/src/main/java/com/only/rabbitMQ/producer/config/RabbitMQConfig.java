package com.only.rabbitMQ.producer.config;

import com.only.rabbitMQ.producer.constants.MqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue queue() {
        return new Queue(MqConst.ORDER_TO_PRODUCT_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding binding() {
        Binding binding = BindingBuilder.bind(queue()).to(directExchange()).with(MqConst.ORDER_TO_PRODUCT_ROUTING_KEY);
        return binding;
    }

}
