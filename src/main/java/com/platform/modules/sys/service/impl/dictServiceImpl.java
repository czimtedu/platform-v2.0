/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.modules.sys.bean.SysDict;
import com.platform.modules.sys.service.DictService;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
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
    private MybatisDao mybatisDao;

    @Override
    @Transactional()
    public String save(SysDict object) throws Exception {
        String id;
        if (object.getId() != null) {
            id = object.getId().toString();
            mybatisDao.update(object);
        } else {
            id = mybatisDao.insert(object);
        }
        JedisUtils.delObject(DictUtils.CACHE_DICT_MAP);
        return id;
    }

    @Override
    @Transactional()
    public String delete(String ids) throws Exception {
        mybatisDao.deleteByIds(SysDict.class, ids);
        JedisUtils.delObject(DictUtils.CACHE_DICT_MAP);
        return "";
    }

}