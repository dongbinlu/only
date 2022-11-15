CREATE TABLE `message_content` (
                                   `msg_id` varchar(50) NOT NULL,
                                   `create_time` datetime DEFAULT NULL,
                                   `update_time` datetime DEFAULT NULL,
                                   `msg_status` int DEFAULT NULL COMMENT '(0,"发送中"),(1,"mq的broker确认接受到消息"),(2,"没有对应交换机"),(3,"没有对应的路由"),(4,"消费端成功消费消息")',
                                   `exchange` varchar(50) DEFAULT NULL,
                                   `routing_key` varchar(50) DEFAULT NULL,
                                   `err_cause` varchar(1000) DEFAULT NULL,
                                   `order_no` bigint DEFAULT NULL,
                                   `max_retry` int DEFAULT NULL,
                                   `current_retry` int DEFAULT NULL,
                                   `product_no` int DEFAULT NULL,
                                   PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `order_info` (
                              `order_no` bigint NOT NULL AUTO_INCREMENT,
                              `create_time` datetime DEFAULT NULL,
                              `update_time` datetime DEFAULT NULL,
                              `user_name` varchar(50) DEFAULT NULL,
                              `money` double(10,2) DEFAULT NULL,
  `product_no` int DEFAULT NULL,
  `order_status` int DEFAULT NULL,
  PRIMARY KEY (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1608273869014 DEFAULT CHARSET=utf8;

