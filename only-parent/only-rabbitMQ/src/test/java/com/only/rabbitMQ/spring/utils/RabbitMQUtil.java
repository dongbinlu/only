package com.only.rabbitMQ.spring.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtil {
    private static ConnectionFactory connectionFactory = null;

    static {
        // 创建连接工厂
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("boy");
        connectionFactory.setPassword("boy");
        connectionFactory.setVirtualHost("/mall");
    }

    public static Connection getConnection() {
        try {
            // 从连接工厂里面获取连接
            return connectionFactory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeChannelAndConnection(Channel channel, Connection connection) {

        try {
            if (null != channel)
                channel.close();
            if (null != connection)
                connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
