/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.SysRole;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.bean.SysUserRole;
import com.platform.modules.sys.service.RoleService;
import com.platform.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统角色service实现类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<SysRole> implements RoleService {

    @Autowired
    private MybatisDao mybatisDao;

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID
     * @return List
     */
    @Override
    public List<SysRole> getByUserId(Integer userId) {
        String sql = "select * from sys_user_role where user_id = " + userId;
        List<SysUserRole> sysUserRoleList = mybatisDao.selectListBySql(SysUserRole.class, sql);
        StringBuilder ids = new StringBuilder();
        for (SysUserRole sysUserRole : sysUserRoleList) {
            if (ids.length() == 0) {
                ids.append(sysUserRole.getRoleId());
            } else {
                ids.append(",").append(sysUserRole.getRoleId());
            }
        }
        return mybatisDao.selectListByIds(SysRole.class, ids.toString());
    }

    /**
     * 给角色分配用户
     * @param role SysRole
     * @param ids 要分配的用户ids
     */
    @Override
    public void assignUserToRole(SysRole role, String ids) {
        Integer roleId = role.getId();
        StringBuilder values = new StringBuilder();
        for (String id : ids.split(",")) {
            if (values.length() == 0) {
                values.append("(").append(id).append(",").append(roleId).append(")");
            } else {
                values.append(",").append("(").append(id).append(",").append(roleId).append(")");
            }
        }
        String saveSql = "insert into sys_user_role (user_id, role_id) values " + values.toString();
        mybatisDao.insertBySql(saveSql);
    }

    /**
     * 移除角色中的用户
     * @param role SysRole
     * @param user SysUser
     * @return boolean
     */
    @Override
    public boolean outUserInRole(SysRole role, SysUser user) {
        //删除用户角色关联
        String deleteSql = "delete from sys_user_role where user_id = " + user.getId() + " and role_id = " + role.getId();
        mybatisDao.deleteBySql(deleteSql, null);
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
    public String save(SysRole object) throws Exception {
        String id;
        if (object.getId() != null) {
            id = object.getId().toString();
            mybatisDao.update(object);
            //删除角色权限关联表
            String deleteSql = "delete from sys_role_permission where role_id =" + id;
            mybatisDao.deleteBySql(deleteSql, null);
        } else {
            id = mybatisDao.insert(object);
        }
        if (StringUtils.isNotEmpty(object.getPermissionIds())) {
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
            mybatisDao.insertBySql(saveSql);
        }
        // 清除用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
        return id;
    }

    /**
     * 删除角色
     *
     * @param ids 删除的ID
     * @throws Exception
     */
    @Override
    public int delete(String ids) throws Exception {
        //删除角色表
        mybatisDao.deleteByIds(SysRole.class, ids);
        //删除用户角色关联表
        String sql = "delete from sys_user_role where role_id in (" + ids + ")";
        mybatisDao.deleteBySql(sql, null);
        //删除角色权限关联表
        String deleteRelSql = "delete from sys_role_permission where role_id in (" + ids + ")";
        mybatisDao.deleteBySql(deleteRelSql, null);
        // 清除用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
        return 1;
    }


}
