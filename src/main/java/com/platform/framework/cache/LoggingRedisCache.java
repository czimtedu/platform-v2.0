/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.cache;

import org.apache.ibatis.cache.decorators.LoggingCache;

/**
 * Mybatis缓存，在mapper.xml中添加cache标签，<cache type="com.platform.framework.cache.LoggingRedisCache" />
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class LoggingRedisCache extends LoggingCache {

    public LoggingRedisCache(String id) {
        super(new MybatisRedisCache(id));
    }
}
