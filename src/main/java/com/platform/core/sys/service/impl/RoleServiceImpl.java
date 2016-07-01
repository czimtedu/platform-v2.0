/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.core.sys.service.impl;

import com.platform.core.sys.bean.SysRole;
import com.platform.core.sys.bean.SysUser;
import com.platform.core.sys.bean.SysUserRole;
import com.platform.core.sys.service.RoleService;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisBaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统角色service实现类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl extends BaseServiceImpl<SysRole> implements RoleService {

    @Autowired
    private MybatisBaseDaoImpl mybatisBaseDaoImpl;

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID
     * @return List
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<SysRole> getByUserId(Integer userId) {
        String sql = "select * from sys_user_role where user_id = " + userId;
        List<SysUserRole> sysUserRoleList = mybatisBaseDaoImpl.findByJPQL(SysUserRole.class, sql);
        StringBuilder ids = new StringBuilder();
        for (SysUserRole sysUserRole : sysUserRoleList) {
            if (ids.length() == 0) {
                ids.append(sysUserRole.getRoleId());
            } else {
                ids.append(",").append(sysUserRole.getRoleId());
            }
        }
        return mybatisBaseDaoImpl.findListDbAndCacheByIds(SysRole.class, ids.toString());
    }

    @Override
    @Transactional(readOnly = false)
    public void assignUserToRole(SysRole role, String ids) {
        Integer roleId = role.getId();
        StringBuilder values = new StringBuilder();
        for (String id : ids.split(",")) {
            if(values.length() == 0){
                values.append("(").append(id).append(",").append(roleId).append(")");
            }else{
                values.append(",").append("(").append(id).append(",").append(roleId).append(")");
            }
        }
        String saveSql = "insert into sys_user_role (user_id, role_id) values " + values.toString();
        mybatisBaseDaoImpl.insertBySQL(saveSql);
    }

    @Override
    public boolean outUserInRole(SysRole role, SysUser user) {
        //删除用户角色关联
        String deleteSql = "delete from sys_user_role where user_id = " + user.getId() + " and role_id = " + role.getId();
        mybatisBaseDaoImpl.deleteBySQL(deleteSql);
        return true;
    }

    /**
     * 保存角色信息
     *
     * @param object object
     * @return 保存的ID
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false)
    public Long save(SysRole object) throws Exception {
        Integer id = object.getId();
        if (id != null) {
            mybatisBaseDaoImpl.updateDbAndCache(object);
            //删除角色权限关联表
            String deleteSql = "delete from sys_role_permission where role_id =" + id;
            mybatisBaseDaoImpl.deleteBySQL(deleteSql);
        } else {
            id = mybatisBaseDaoImpl.saveDb(object).intValue();
        }
        //保存角色权限关联表
        StringBuilder values = new StringBuilder();
        String[] split = object.getPermissionIds().split(",");
        for (String s : split) {
            if (values.length() == 0) {
                values.append("(").append(id).append(",").append(s).append(")");
            } else {
                values.append(",").append("(").append(id).append(",").append(s).append(")");
            }
        }
        String saveSql = "insert into sys_role_permission (role_id, permission_id) values " + values.toString();
        mybatisBaseDaoImpl.insertBySQL(saveSql);
        return id.longValue();
    }

    /**
     * 删除角色
     *
     * @param ids 删除的ID
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false)
    public Long delete(String ids) throws Exception {
        //删除角色表
        mybatisBaseDaoImpl.deleteDbAndCacheByIds(SysRole.class, ids);
        //删除用户角色关联表
        String sql = "delete from sys_user_role where role_id in (" + ids + ")";
        mybatisBaseDaoImpl.deleteBySQL(sql);
        //删除角色权限关联表
        String deleteRelSql = "delete from sys_role_permission where role_id in (" + ids + ")";
        mybatisBaseDaoImpl.deleteBySQL(deleteRelSql);
        return 1L;
    }


}
