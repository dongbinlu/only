package com.only.order.center.handler;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.only.order.center.feign.ProductCenterFeignApi;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignSentinelFallbackHandler implements FallbackFactory<ProductCenterFeignApi> {
    @Override
    public ProductCenterFeignApi create(Throwable throwable) {


        return new ProductCenterFeignApi() {
            @Override
            public String product(Integer id) {

                if (throwable instanceof FlowException) {

                    return "被流控了-默认商品";
                } else if (throwable instanceof DegradeException) {

                    return "被降级了-默认商品";

                } else {
                    return "其他-默认商品";
                }

            }
        };
    }
}
