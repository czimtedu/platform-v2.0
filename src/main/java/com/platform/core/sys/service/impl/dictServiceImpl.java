/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.sys.service.impl;

import com.platform.core.sys.bean.SysDict;
import com.platform.core.sys.service.DictService;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisBaseDaoImpl;
import com.platform.framework.util.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典表service实现类
 *
 * @author lufengc
 * @date 2016/1/16 14:05
 */
@Service
@Transactional(readOnly = true)
public class dictServiceImpl extends BaseServiceImpl<SysDict> implements DictService {
    @Autowired
    private MybatisBaseDaoImpl mybatisBaseDaoImpl;

    @Override
    @Transactional(readOnly = false)
    public Long save(SysDict object) throws Exception {
        Integer id = object.getId();
        if (id != null) {
            mybatisBaseDaoImpl.updateDbAndCache(object);
        } else {
            id = mybatisBaseDaoImpl.saveDb(object).intValue();
        }
        JedisUtils.delObject(DictUtils.CACHE_DICT_MAP);
        return id.longValue();
    }

    @Override
    @Transactional(readOnly = false)
    public Long delete(String ids) throws Exception {
        mybatisBaseDaoImpl.deleteDbAndCacheByIds(SysDict.class, ids);
        JedisUtils.delObject(DictUtils.CACHE_DICT_MAP);
        return 1L;
    }

}
