package com.only.rabbitMQ.config;

import com.google.common.collect.Maps;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class Config {

    @Bean
    public Queue sbDirect() {
        Queue queue = new Queue("spring-boot-direct", true);
        return queue;
    }

    @Bean
    public Queue sbWorkQueue() {
        Queue queue = new Queue("spring-boot-work-queue", true);
        return queue;
    }

    @Bean
    public FanoutExchange sbFanoutExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange("spring-boot-fanout-exchange");
        return fanoutExchange;
    }

    @Bean
    public Queue sbFanoutQueue1() {
        Queue queue = new Queue("fanoutQueue1");
        return queue;
    }

    @Bean
    public Queue sbFanoutQueue2() {
        Queue queue = new Queue("fanoutQueue2");
        return queue;
    }

    @Bean
    public Binding bindQueue1ToFanoutExchange() {
        Binding binding = BindingBuilder.bind(sbFanoutQueue1()).to(sbFanoutExchange());
        return binding;
    }

    @Bean
    public Binding bindQueue2ToFanoutExchange() {
        Binding binding = BindingBuilder.bind(sbFanoutQueue2()).to(sbFanoutExchange());
        return binding;
    }

    @Bean
    public DirectExchange sbDirectExchange() {
        DirectExchange directExchange = new DirectExchange("spring-boot-rounting-direct");
        return directExchange;
    }

    @Bean
    public Queue sbDirectQueue1() {
        Queue queue = new Queue("directQueue1");
        return queue;
    }

    @Bean
    public Queue sbDirectQueue2() {
        Queue queue = new Queue("directQueue2");
        return queue;
    }


    @Bean
    public Binding bindingQueueTODirectExchangeInfo() {
        Binding binding = BindingBuilder.bind(sbDirectQueue1()).to(sbDirectExchange()).with("info");
        return binding;
    }

    @Bean
    public Binding bindingQueueTODirectExchangeDebug() {
        Binding binding = BindingBuilder.bind(sbDirectQueue2()).to(sbDirectExchange()).with("debug");
        return binding;
    }

    @Bean
    public Binding bindingQueueTODirectExchangeError() {
        Binding binding = BindingBuilder.bind(sbDirectQueue2()).to(sbDirectExchange()).with("error");
        return binding;
    }

    @Bean
    public Binding bindingQueueTODirectExchangeWarn() {
        Binding binding = BindingBuilder.bind(sbDirectQueue2()).to(sbDirectExchange()).with("warn");
        return binding;
    }

    // 死信交换机
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("DeadLetter.exc");
    }

    //延迟队列
    @Bean
    public Queue dealyQueue() {
        // 把一个队列修改为延时队列
        HashMap<String, Object> map = Maps.newHashMap();
        // 消息的最大存活时间
        map.put("x-message-ttl", 10 * 1000);

        // 该队列里面的消息死了，去哪个交换机，也就是该队列里面的消息没人消费，直到超时
        map.put("x-dead-letter-exchange", "DeadLetter.exc");

        // 该队列里面的消息死了，去哪个交换机，由哪个路由key路由他
        map.put("x-dead-letter-routing-key", "DeadLetter.key");

        Queue dealy = new Queue("dealy", true, false, false, map);
        return dealy;
    }


    @Bean
    public Queue newQueue() {
        return new Queue("new.queue");
    }

    @Bean
    public Binding newAndDeadLetterExchange() {
        Binding binding = BindingBuilder.bind(newQueue()).to(deadLetterExchange()).with("DeadLetter.key");
        return binding;
    }



}
