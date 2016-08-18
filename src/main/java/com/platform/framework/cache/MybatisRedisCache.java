/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.cache;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * mybatis缓存，真正使用cache的方式是通过集成org.apache.ibatis.cache.decorators.LoggingCache 这个类实现的
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class MybatisRedisCache implements Cache {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private String id;

    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
        logger.debug("mybatisRedisCache:id=" + id);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        JedisUtils.setObject(key.toString(), value, 0);
        logger.debug("mybatisRedisCache:putObject " + key + "=" + value);
    }

    @Override
    public Object getObject(Object key) {
        Object value = JedisUtils.getObject(key.toString());
        logger.debug("mybatisRedisCache:getObject " + key + "=" + value);
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        long result = JedisUtils.delObject(key.toString());
        logger.debug("mybatisRedisCache:removeObject key=" + key + ",result=" + result);
        return result;
    }

    @Override
    public void clear() {
        JedisUtils.getResource().flushDB();
    }

    @Override
    public int getSize() {
        return Integer.valueOf(JedisUtils.getResource().dbSize().toString());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

}
