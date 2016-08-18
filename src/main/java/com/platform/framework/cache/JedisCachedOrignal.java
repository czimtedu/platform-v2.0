/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.cache;

import com.platform.framework.util.ObjectUtils;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * jedis对数据操作
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class JedisCachedOrignal {

    /**
     * 检查对象是否需要缓存
     * @return boolean
     */
    public static boolean isCached(Class entityClass) {
        // 检查是否缓存
        DataCached dataCached = (DataCached) entityClass.getAnnotation(DataCached.class);
        DataCached.CachedType type = DataCached.CachedType.NO_CACHED;
        if (dataCached != null) {
            type = dataCached.type();
        }
        return DataCached.CachedType.REDIS_CACHED.equals(type);
    }

    /**
     * 保存对象数据
     *
     * @param constants key 包名+类名
     * @param obj 保存的对象
     */
    @SuppressWarnings("unchecked")
    public static void saveObject(String constants, Object obj) {
        try {
            Map<String, Object> map = ObjectUtils.objectToMap(obj);
            if (map != null && map.get("id") != null) {
                String key = constants + ":" + map.get("id");
                JedisUtils.setObject(key, obj, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除对象数据
     *
     * @param constants key 包名+类名
     * @param ids 对象ID
     */
    public static void deleteCache(String constants, String ids) {
        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                JedisUtils.del(constants + ":" + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除对象数据
     *
     * @param constants key 包名+类名
     * @param obj 保存的对象
     */
    @SuppressWarnings("unchecked")
    public static void deleteObject(String constants, Object obj) {
        try {
            Map<String, Object> map = ObjectUtils.objectToMap(obj);
            if (map != null && map.get("id") != null) {
                String key = constants + ":" + map.get("id");
                JedisUtils.del(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除某表缓存数据
     *
     * @param likekey 包名
     */
    @SuppressWarnings("unchecked")
    public static void deleteObjectLike(String likekey) {
        try {
            JedisUtils.delKeysLike(likekey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找指定ids列表的Object对象
     *
     * @param constants key 包名+类名
     * @param ids 对象ID
     * @return Object[]
     */
    public static Object[] findListObject(String constants, String ids) {
        Object[] objArray = new Object[2];
        List<Object> list = new ArrayList<>();
        String noneCacheIds = "";
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            Object object = JedisUtils.getObject(constants + ":" + id.trim());
            if (object != null) {
                list.add(object);
            } else {
                if ("".equals(noneCacheIds)) {
                    noneCacheIds = id;
                } else {
                    noneCacheIds += "," + id;
                }
            }
        }
        objArray[0] = list;
        objArray[1] = noneCacheIds;
        return objArray;
    }

}
