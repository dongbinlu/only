package com.only.rabbitMQ.producer.entity;

import lombok.*;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInfo {
    private Long orderNo;

    private Integer productNo;

    private String userName;

    private Double money;

    private Integer orderStatus;

    private Date createTime;

    private Date updateTime;
}
