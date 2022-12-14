package com.only.rabbitMQ.producer.constants;

public class MqConst {

    /**交换机名称*/
    public static final String ORDER_TO_PRODUCT_EXCHANGE_NAME = "order-to-product.exchange";

    /**队列名称*/
    public static final String ORDER_TO_PRODUCT_QUEUE_NAME = "order-to-product.queue";

    /**路由key*/
    public static final String ORDER_TO_PRODUCT_ROUTING_KEY = "order-to-product.key";

    /**消息重发的最大次数*/
    public static final Integer MSG_RETRY_COUNT = 5;

    public static final Integer TIME_DIFF = 30;
}
