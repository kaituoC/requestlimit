package com.qknode.bigdata.requestlimit.entity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author kaituo
 * @date 2018-10-18
 */
public class MemCache {
    private static volatile MemCache instance;
    private static Cache<String, Long> limitCache;

    private void init() {
        limitCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .concurrencyLevel(1)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .initialCapacity(10000)
                .build();
    }

    private MemCache() {
        init();
    }

    public Cache<String, Long> getLimitCache() {
        return limitCache;
    }

    public static MemCache getInstance() {
        if (instance==null) {
            synchronized (MemCache.class) {
                if (instance==null) {
                    instance = new MemCache();
                }
            }
        }
        return instance;
    }
}
