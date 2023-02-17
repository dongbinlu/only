package com.only.seata.order.controller;


import com.only.seata.order.entity.Order;
import com.only.seata.order.service.IOrderService;
import com.only.seata.order.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 创建订单
     */
    @GetMapping("/create")
    public ResultVo create(Order order) {
        orderService.createOrder(order);

        ResultVo resultVo = new ResultVo();
        resultVo.setMsg("创建订单成功");
        resultVo.setSuccess(true);
        resultVo.setData("订单ID:"+order.getId());
        return resultVo;
    }
}

