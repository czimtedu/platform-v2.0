/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据缓存定义
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataCached {

    /**
     * 缓存类型（NO_CACHED：不缓存；REDIS_CACHED：redis缓存；）
     */
    CachedType type() default CachedType.NO_CACHED;

    enum CachedType {
        NO_CACHED,
        CachedType, REDIS_CACHED
    }
}
