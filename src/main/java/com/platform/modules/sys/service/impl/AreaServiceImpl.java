/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.Encodes;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.SysArea;
import com.platform.modules.sys.service.AreaService;
import com.platform.modules.sys.utils.DictUtils;
import com.platform.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 区域Service
 *
 * @author jeeplus
 * @version 2014-05-16
 */
@Service
public class AreaServiceImpl extends BaseServiceImpl<SysArea> implements AreaService {

    @Autowired
    private MybatisDao mybatisDao;

    @Override
    public String save(SysArea object) throws Exception {
        if (StringUtils.isNotEmpty(object.getId())) {
            mybatisDao.update(object);
        } else {
            object.setId(Encodes.uuid());
            mybatisDao.insert(object);
        }
        UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
        return object.getId();
    }

    @Override
    public String delete(String ids) throws Exception {
        mybatisDao.deleteByIds(SysArea.class, ids);
        UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
        return "";
    }
}
