package com.only.sharding.sphere.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ,;,,;
 * ,;;'(    社
 * __      ,;;' ' \   会
 * /'  '\'~~'~' \ /'\.)  主
 * ,;(      )    /  |.     义
 * ,;' \    /-.,,(   ) \    码
 * ) /       ) / )|    农
 * ||        ||  \)
 * (_\       (_\
 *
 * @date ：Created in 2019/11/17 17:25
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description: 订单从表
 **/
@Data
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 263434701950670170L;

    private long orderItemId;

    private long orderId;

    private int userId;

    private String status;

    @Override
    public String toString() {
        return String.format("order_item_id:%s, order_id: %s, user_id: %s, status: %s", orderItemId, orderId, userId, status);
    }
}
