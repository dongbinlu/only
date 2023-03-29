package com.safecode.security.subject.async;

import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 异步处理REST 接口
 * <p>
 * 增加服务器的吞吐量
 *
 * @author v_boy
 */
@RestController
@RequestMapping("/async")
public class AsyncController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockQueue mockQueue;

    @Autowired
    private DeferredResultHolder deferredResultHolder;

    /**
     * 使用Runnable异步处理下单服务
     *
     * @return
     * @throws Exception
     */

    @GetMapping("/runnable")
    public Callable<String> runnable() throws Exception {
        logger.info("主线程开始");

        Callable<String> result = new Callable<String>() {

            @Override
            public String call() throws Exception {
                logger.info("副线程开始");
                Thread.sleep(1000);
                logger.info("副线程返回");
                return "下单成功";
            }

        };
        logger.info("主线程返回");

        return result;
    }

    /**
     * 使用DeferredResult异步处理下单服务
     *
     * @return
     * @throws Exception
     */

    @GetMapping("/deferredResult")
    public DeferredResult<String> deferredResult() throws Exception {
        logger.info("主线程开始");
        // 8位的订单编号
        String orderNumber = RandomStringUtils.randomNumeric(8);
        mockQueue.setPlaceOrder(orderNumber);

        DeferredResult<String> result = new DeferredResult<String>();

        Map<String, DeferredResult<String>> map = deferredResultHolder.getMap();
        map.put(orderNumber, result);

        logger.info("主线程返回");

        return result;
    }

}
