/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.modules.sys.bean.SysOffice;
import com.platform.modules.sys.service.OfficeService;
import com.platform.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 机构Service
 *
 * @author jeeplus
 * @version 2014-05-16
 */
@Service
public class OfficeServiceImpl extends BaseServiceImpl<SysOffice> implements OfficeService {

    @Autowired
    private MybatisDao mybatisDao;

    @Override
    public List<SysOffice> getList(Boolean isAll) {
        if (isAll != null && isAll){
            return UserUtils.getOfficeAllList();
        }else{
            return UserUtils.getOfficeList();
        }
    }

    @Override
    public List<SysOffice> getByUserId(int userId) {
        return null;
    }

    @Override
    public List<SysOffice> getByParentIdsLike(String parentIds) {
        return mybatisDao.selectListByConditions(SysOffice.class, "parent_ids LIKE '" + parentIds + "%'");
    }

    @Override
    public List<SysOffice> getByParentId(String parentId) {
        return mybatisDao.selectListByConditions(SysOffice.class, "parent_id = '" + parentId + "'");
    }

    @Override
    public String save(SysOffice object) throws Exception {
        return null;
    }

    @Override
    public String delete(String ids) throws Exception {
        return null;
    }

}
