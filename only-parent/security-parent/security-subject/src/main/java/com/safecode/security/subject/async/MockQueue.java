package com.safecode.security.subject.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 模拟消息队列的对象
 *
 * @author v_boy
 */

@Component
public class MockQueue {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // 表示下单消息，如果placeOrder有值就表示有人下单
    private String placeOrder;

    // 表示下单成功消息，如果completeOrder有值就表示下单成功
    private String completeOrder;

    public String getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(String placeOrder) throws Exception {
        //相当应用2
        new Thread(() -> {
            logger.info("接受到下单请求" + placeOrder);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.completeOrder = placeOrder;
            logger.info("下单请求处理完毕" + placeOrder);
        }).start();
    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }

}
