/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.modules.sys.bean.SysDict;
import com.platform.modules.sys.service.DictService;
import com.platform.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字典表service实现类
 *
 * @author lufengc
 * @date 2016/1/16 14:05
 */
@Service
public class dictServiceImpl extends BaseServiceImpl<SysDict> implements DictService {
    @Autowired
    private MybatisDao mybatisDao;

    /**
     * 保存
     *
     * @param object SysDict
     * @return ID
     * @throws Exception
     */
    @Override
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

    /**
     * 删除
     *
     * @param ids 删除的ID
     * @return int
     * @throws Exception
     */
    @Override
    public int delete(String ids) throws Exception {
        int delete = super.delete(ids);
        JedisUtils.delObject(DictUtils.CACHE_DICT_MAP);
        return delete;
    }

}
