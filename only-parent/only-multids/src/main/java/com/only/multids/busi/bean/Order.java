package com.only.multids.busi.bean;

import lombok.Data;

@Data
public class Order extends BaseDomin {

    private Long orderId;

    private String userId;

    private double money;


}
