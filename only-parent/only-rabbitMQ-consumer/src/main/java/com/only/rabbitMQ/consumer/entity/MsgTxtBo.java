package com.only.rabbitMQ.consumer.entity;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MsgTxtBo {

    private long orderNo;

    private int productNo;

    private String msgId;

}
