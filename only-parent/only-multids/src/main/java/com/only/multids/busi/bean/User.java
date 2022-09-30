package com.only.multids.busi.bean;

import lombok.Data;

@Data
public class User extends BaseDomin {
    private Long userId;

    private Long orderId;


    private double money;

}
