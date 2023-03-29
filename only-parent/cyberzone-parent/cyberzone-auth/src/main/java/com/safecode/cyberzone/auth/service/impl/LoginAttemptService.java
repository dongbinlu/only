package com.safecode.cyberzone.auth.service.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class LoginAttemptService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Integer MAX_ATTEMPT = 5;

    private LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.HOURS)
                .build(new CacheLoader<String, Integer>() {

                    @Override
                    public Integer load(String key) throws Exception {
                        return 0;
                    }

                });
    }

    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    public void loginFailed(String key) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (Exception e) {
            attempts = 0;
            logger.error("loginFailed", e);
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (Exception e) {
            logger.error("isBlocked", e);
            return false;
        }
    }

}
