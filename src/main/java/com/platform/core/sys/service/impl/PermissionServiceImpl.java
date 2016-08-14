/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.core.sys.service.impl;

import com.platform.core.sys.bean.SysPermission;
import com.platform.core.sys.bean.SysRolePermission;
import com.platform.core.sys.service.PermissionService;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisBaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private MybatisBaseDaoImpl mybatisBaseDaoImpl;

    /**
     * 根据角色ID获取权限
     *
     * @param roleId 角色ID
     * @return List
     */
    @Override
    public List<SysPermission> getByRoleId(Integer roleId) {
        String sql = "select * from sys_role_permission where role_id = " + roleId;
        List<SysRolePermission> sysRolePermissionList = mybatisBaseDaoImpl.selectListBySql(SysRolePermission.class, sql);
        StringBuilder ids = new StringBuilder();
        for (SysRolePermission sysRolePermission : sysRolePermissionList) {
            if(ids.length() == 0){
                ids.append(sysRolePermission.getPermissionId());
            } else {
                ids.append(",").append(sysRolePermission.getPermissionId());
            }
        }
        return mybatisBaseDaoImpl.selectListByIds(SysPermission.class, ids.toString());
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
        List<SysRolePermission> sysRolePermissionList = mybatisBaseDaoImpl.selectListBySql(SysRolePermission.class, sql);
        StringBuilder ids = new StringBuilder();
        for (SysRolePermission sysRolePermission : sysRolePermissionList) {
            if(ids.length() == 0){
                ids.append(sysRolePermission.getPermissionId());
            } else {
                ids.append(",").append(sysRolePermission.getPermissionId());
            }
        }
        return mybatisBaseDaoImpl.selectListByIds(SysPermission.class, ids.toString());
    }

    /**
     * 根据parentId获取权限
     *
     * @param parentId 父ID
     * @return List
     */
    @Override
    public List<SysPermission> getByParentId(int parentId) {
        return mybatisBaseDaoImpl.selectListByConditions(
                SysPermission.class, "parent_id = " + parentId + "");
    }

    /**
     * 保存权限信息
     *
     * @param object object
     * @return 保存的ID
     * @throws Exception
     */
    @Override
    @Transactional()
    public String save(SysPermission object) throws Exception {
        String id;
        String oldParentIds = object.getParentIds();
        SysPermission parent = get(SysPermission.class, object.getParentId().toString());
        if(parent != null) {
            object.setParentIds(parent.getParentIds() + parent.getId() + ",");
        } else {
            object.setParentIds(SysPermission.getRootId().toString());
        }
        if (object.getId() != null) {
            mybatisBaseDaoImpl.update(object);
            id = object.getId().toString();
            // 更新子节点parentIds
            List<SysPermission> list = mybatisBaseDaoImpl.selectListByConditions(SysPermission.class,
                    "parent_id like '%," + object.getId() + ",%'");
            if(list != null && list.size() > 0){
                for (SysPermission p : list) {
                    p.setParentIds(p.getParentIds().replace(oldParentIds, object.getParentIds()));
                    mybatisBaseDaoImpl.update(p);
                }
            }
        } else {
            id = mybatisBaseDaoImpl.insert(object);
        }
        // 清除日志相关缓存
        JedisUtils.delObject(LogServiceImpl.CACHE_PERMISSION_NAME_PATH_MAP);
        return id;
    }

    /**
     * 删除权限信息
     *
     * @param ids 删除的ID
     * @throws Exception
     */
    @Override
    @Transactional()
    public String delete(String ids) throws Exception {
        mybatisBaseDaoImpl.deleteByIds(SysPermission.class, ids);
        String sql = "delete from sys_role_permission where permission_id in (" + ids + ")";
        mybatisBaseDaoImpl.deleteBySql(sql, null);
        // 清除日志相关缓存
        JedisUtils.delObject(LogServiceImpl.CACHE_PERMISSION_NAME_PATH_MAP);
        return "";
    }


}
