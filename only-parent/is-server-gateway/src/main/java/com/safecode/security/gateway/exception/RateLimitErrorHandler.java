package com.safecode.security.gateway.exception;

import org.springframework.stereotype.Component;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RateLimitErrorHandler extends DefaultRateLimiterErrorHandler {

    /*
     * 往存储里存限流的信息的时候出错，该怎么处理
     */
    @Override
    public void handleSaveError(String key, Exception e) {
        super.handleSaveError(key, e);
    }

    /*
     * 从存储里读出来的时候出错，该怎么来处理
     */
    @Override
    public void handleFetchError(String key, Exception e) {
        super.handleFetchError(key, e);
    }

    /*
     * 真的的限流了，在限流的过程中发生的错误，在这里该如何处理
     */
    @Override
    public void handleError(String msg, Exception e) {
        super.handleError(msg, e);
    }
}
