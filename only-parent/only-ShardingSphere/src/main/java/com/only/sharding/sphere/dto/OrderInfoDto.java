package com.only.sharding.sphere.dto;

import com.only.sharding.sphere.entity.Order;
import com.only.sharding.sphere.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

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
 * @author ：杨过
 * @date ：Created in 2019/11/17 17:25
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description:
 **/
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class OrderInfoDto {

    private List<Order> order;

    private List<OrderItem> item;

}
