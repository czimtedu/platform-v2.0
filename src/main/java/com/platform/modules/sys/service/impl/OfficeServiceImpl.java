/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.SysOffice;
import com.platform.modules.sys.bean.SysPermission;
import com.platform.modules.sys.service.OfficeService;
import com.platform.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public SysOffice get(String id) throws Exception {
        return super.get(id);
    }

    @Override
    public List<SysOffice> getList(Boolean isAll) {
        if (isAll != null && isAll) {
            return UserUtils.getOfficeAllList();
        } else {
            return UserUtils.getOfficeList();
        }
    }

    @Override
    public List<SysOffice> getByUserId(int userId) {
        return null;
    }

    @Override
    public List<SysOffice> getByParentIdsLike(String parentIds) {
        return mybatisDao.selectListByConditions(SysOffice.class, "parent_ids LIKE '%" + parentIds + "%'");
    }

    @Override
    public List<SysOffice> getByParentId(String parentId) throws Exception {
        List<SysOffice> childList = new ArrayList<>();
        List<SysOffice> officeList = UserUtils.getOfficeList();
        getChildList(childList, UserUtils.getOfficeList(), parentId);
        for (SysOffice office : officeList) {
            if (parentId.equals(office.getId())) {
                childList.add(office);
            }
        }
        return childList;
    }

    /**
     * 获取某个父节点下面的所有子节点
     *
     * @param childList 用户保存子节点的集合
     * @param allList   总数据结合
     * @param parentId  父ID
     */
    private void getChildList(List<SysOffice> childList, List<SysOffice> allList, String parentId) {
        for (SysOffice object : allList) {
            if (parentId.equals(object.getParentId())) {
                getChildList(childList, allList, object.getId());
                childList.add(object);
            }
        }
    }

    @Override
    public String save(SysOffice object) throws Exception {
        String id;
        String oldParentIds = object.getParentIds();
        SysOffice parent = get(object.getParentId());
        if(parent != null) {
            object.setParentIds(parent.getParentIds() + parent.getId() + ",");
        } else {
            object.setParentIds(SysPermission.getRootId().toString());
        }
        if (object.getId() != null) {
            mybatisDao.update(object);
            id = object.getId();
            // 更新子节点parentIds
            List<SysOffice> list = mybatisDao.selectListByConditions(SysOffice.class,
                    "parent_id like '%," + object.getId() + ",%'");
            if(list != null && list.size() > 0){
                for (SysOffice p : list) {
                    p.setParentIds(p.getParentIds().replace(oldParentIds, object.getParentIds()));
                    mybatisDao.update(p);
                }
            }
        } else {
            id = mybatisDao.insert(object);
        }
        return id;
    }

    @Override
    public String delete(String id) throws Exception {
        String ids = id;
        //获取子节点集合
        List<SysOffice> childList = new ArrayList<>();
        getChildList(childList, UserUtils.getOfficeList(), id);
        for (SysOffice office : childList) {
            ids += "," + office.getId();
        }
        //删除该机构及所有子机构项
        mybatisDao.deleteByIds(SysPermission.class, ids);
        //删除角色机构关联表
        String sql = "delete from sys_role_office where office_id in (" + StringUtils.idsToString(ids) + ")";
        mybatisDao.deleteBySql(sql, null);
        return "";
    }

}
