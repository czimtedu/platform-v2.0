/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.modules.sys.bean.SysPermission;
import com.platform.modules.sys.bean.SysRolePermission;
import com.platform.modules.sys.service.PermissionService;
import com.platform.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统权限sevice实现类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<SysPermission> implements PermissionService {

    @Autowired
    private MybatisDao mybatisDao;

    /**
     * 根据角色ID获取权限
     *
     * @param roleId 角色ID
     * @return List
     */
    @Override
    public List<SysPermission> getByRoleId(Integer roleId) {
        String sql = "select * from sys_role_permission where role_id = " + roleId;
        List<SysRolePermission> sysRolePermissionList = mybatisDao.selectListBySql(SysRolePermission.class, sql);
        StringBuilder ids = new StringBuilder();
        for (SysRolePermission sysRolePermission : sysRolePermissionList) {
            if (ids.length() == 0) {
                ids.append(sysRolePermission.getPermissionId());
            } else {
                ids.append(",").append(sysRolePermission.getPermissionId());
            }
        }
        return mybatisDao.selectListByIds(SysPermission.class, ids.toString());
    }

    /**
     * 根据用户ID获取权限
     *
     * @param userId 用户ID
     * @return List
     */
    @Override
    public List<SysPermission> getByUserId(Integer userId) {
        String sql = "SELECT rp.permission_id, rp.role_id" +
                " FROM sys_role_permission rp" +
                " JOIN sys_user_role ur ON ur.role_id = rp.role_id" +
                " AND ur.user_id = " + userId;
        List<SysRolePermission> sysRolePermissionList = mybatisDao.selectListBySql(SysRolePermission.class, sql);
        StringBuilder ids = new StringBuilder();
        for (SysRolePermission sysRolePermission : sysRolePermissionList) {
            if (ids.length() == 0) {
                ids.append(sysRolePermission.getPermissionId());
            } else {
                ids.append(",").append(sysRolePermission.getPermissionId());
            }
        }
        return mybatisDao.selectListByIds(SysPermission.class, ids.toString());
    }

    /**
     * 根据parentId获取权限
     *
     * @param parentId 父ID
     * @return List
     */
    @Override
    public List<SysPermission> getByParentId(int parentId) {
        return mybatisDao.selectListByConditions(
                SysPermission.class, "parent_id = " + parentId + "");
    }

    @Override
    public void updatePermissionSort(Integer[] ids, Integer[] sorts) {
        String sql;
        for (int i = 0; i < ids.length; i++) {
            sql = "UPDATE sys_permission SET sort_id = " + sorts[i] + " WHERE id = " + ids[i];
            mybatisDao.updateBySql(sql, SysPermission.class);
        }
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_PERMISSION_LIST);
        // 清除日志相关缓存
        JedisUtils.delObject(LogServiceImpl.CACHE_PERMISSION_NAME_PATH_MAP);
    }

    /**
     * 保存权限信息
     *
     * @param object object
     * @return 保存的ID
     * @throws Exception
     */
    @Override
    public String save(SysPermission object) throws Exception {
        String id;
        String oldParentIds = object.getParentIds();
        SysPermission parent = get(object.getParentId().toString());
        if (parent != null) {
            object.setParentIds(parent.getParentIds() + parent.getId() + ",");
        } else {
            object.setParentIds(SysPermission.getRootId().toString());
        }
        if (object.getId() != null) {
            mybatisDao.update(object);
            id = object.getId().toString();
            // 更新子节点parentIds
            List<SysPermission> list = mybatisDao.selectListByConditions(SysPermission.class,
                    "parent_id like '%," + object.getId() + ",%'");
            if (list != null && list.size() > 0) {
                for (SysPermission p : list) {
                    p.setParentIds(p.getParentIds().replace(oldParentIds, object.getParentIds()));
                    mybatisDao.update(p);
                }
            }
        } else {
            id = mybatisDao.insert(object);
        }
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_PERMISSION_LIST);
        // 清除日志相关缓存
        JedisUtils.delObject(LogServiceImpl.CACHE_PERMISSION_NAME_PATH_MAP);
        return id;
    }

    /**
     * 删除权限信息
     *
     * @param id 删除的ID
     * @throws Exception
     */
    @Override
    public int delete(String id) throws Exception {
        String ids = id;
        //获取子节点集合
        List<SysPermission> childList = new ArrayList<>();
        getChildList(childList, UserUtils.getMenuList(), Integer.valueOf(id));
        for (SysPermission sysPermission : childList) {
            ids += "," + sysPermission.getId();
        }
        //删除该菜单及所有子菜单
        mybatisDao.deleteByIds(SysPermission.class, ids);
        //删除角色权限关联表
        String sql = "delete from sys_role_permission where permission_id in (" + ids + ")";
        mybatisDao.deleteBySql(sql, null);
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_PERMISSION_LIST);
        // 清除日志相关缓存
        JedisUtils.delObject(LogServiceImpl.CACHE_PERMISSION_NAME_PATH_MAP);
        return 1;
    }

    /**
     * 获取某个父节点下面的所有子节点
     *
     * @param childList 用户保存子节点的集合
     * @param allList   总数据结合
     * @param parentId  父ID
     */
    private void getChildList(List<SysPermission> childList, List<SysPermission> allList, Integer parentId) {
        for (SysPermission object : allList) {
            if (parentId.equals(object.getParentId())) {
                getChildList(childList, allList, object.getId());
                childList.add(object);
            }
        }
    }

}
