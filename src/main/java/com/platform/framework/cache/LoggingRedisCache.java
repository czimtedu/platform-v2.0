/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.framework.cache;

import org.apache.ibatis.cache.decorators.LoggingCache;

/**
 * 日志缓存
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class LoggingRedisCache extends LoggingCache {

    public LoggingRedisCache(String id) {
        super(new MybatisRedisCache(id));
    }
}
