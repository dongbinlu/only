package com.only.seata.order.mapper;

import com.only.seata.order.entity.Order;
import org.apache.ibatis.annotations.Param;

/**
 * Created by smlz on 2019/12/9.
 */
public interface OrderMapper {

    int saveOrder(Order order);

    int updateOrderStatusById(@Param("orderId") Integer orderId,@Param("status") Integer orderStatus);
}
