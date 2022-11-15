package com.only.rabbitMQ.consumer.entity;

import lombok.*;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageContent {
    private String msgId;

    private Integer productNo;

    private Long orderNo;

    private String exchange;

    private String routingKey;

    private String errCause;

    private Integer maxRetry;

    private Integer currentRetry;

    private Integer msgStatus;

    private Date createTime;

    private Date updateTime;


}
