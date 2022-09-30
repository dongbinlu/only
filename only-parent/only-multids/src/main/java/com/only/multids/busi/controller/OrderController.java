package com.only.multids.busi.controller;

import com.only.multids.annotation.Router;
import com.only.multids.busi.bean.Order;
import com.only.multids.busi.bean.User;
import com.only.multids.busi.service.OrderServiceImpl;
import com.only.multids.busi.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private UserServiceImpl userService;

    /**
     * 测试事务
     *
     * @param order
     * @return
     */
    @RequestMapping("/save3")
    //@Transactional        //加上这个注解 会使用默认数据源 有问题
    public Order insertOrder3(Order order) throws Exception {
        orderService.insertOrder3(order);

        User user = new User();
        user.setUserId(Long.valueOf(order.getUserId()));
        user.setOrderId(order.getOrderId());
        user.setMoney(order.getMoney());
        userService.insert(user);
        return order;
    }


    /**
     * 新增
     *
     * @param order
     * @return
     */
    @RequestMapping("/save")
    @Router(routingFiled = "orderId")
    public Order insertOrder(Order order) {
        orderService.insertOrder(order);
        User user = new User();
        user.setUserId(Long.valueOf(order.getUserId()));
        user.setOrderId(order.getOrderId());
        user.setMoney(order.getMoney());
        userService.insert(user);
        return order;
    }

    /**
     * 默认数据源  测试
     *
     * @param params
     */
    @RequestMapping("/save2")
    public void insertS(@RequestBody Map<String, Object> params) {
        orderService.insertS(params);
        System.out.println(123);
    }


    /**
     * 查询  根据订单id
     *
     * @param order
     * @return
     */
    @RequestMapping("/getByOrderId")
    @Router(routingFiled = "orderId")
    public List<Order> getByOrderId(Order order) {
        Long orderId = order.getOrderId();
        String tableSuffix = order.getTableSuffix();
        List<Order> list = orderService.getByOrderId(orderId, tableSuffix);
        return list;
    }


}
