package com.safecode.security.permission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.safecode.security.permission.common.CacheKeyConstants;
import com.safecode.security.permission.utils.JsonMapper;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service("sysCacheService")
@Slf4j
public class SysCacheService {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public void saveCache(String toSaveValue, int timeoutSeconds, CacheKeyConstants prefix) {
        saveCache(toSaveValue, timeoutSeconds, prefix, null);
    }

    public void saveCache(String toSaveValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys) {
        if (toSaveValue == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.setex(cacheKey, timeoutSeconds, toSaveValue);
        } catch (Exception e) {
            log.error("save cache exception,prefix:{} , keys:{}", prefix.name(), JsonMapper.obj2String(keys), e);
        } finally {
            shardedJedis.close();
        }
    }

    public String getCache(CacheKeyConstants prefix, String... keys) {
        String cacheKey = generateCacheKey(prefix, keys);
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            String value = shardedJedis.get(cacheKey);
            return value;
        } catch (Exception e) {
            log.error("get form cache exception,prefix:{},keys:{}", prefix.name(), JsonMapper.obj2String(keys), e);
            return null;
        } finally {
            shardedJedis.close();
        }

    }

    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }

}
