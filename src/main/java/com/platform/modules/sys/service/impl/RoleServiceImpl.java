/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.modules.sys.bean.SysRole;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.bean.SysUserRole;
import com.platform.modules.sys.service.RoleService;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisBaseDaoImpl;
import com.platform.framework.util.StringUtils;
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
    public List<SysRole> getByUserId(Integer userId) {
        String sql = "select * from sys_user_role where user_id = " + userId;
        List<SysUserRole> sysUserRoleList = mybatisBaseDaoImpl.selectListBySql(SysUserRole.class, sql);
        StringBuilder ids = new StringBuilder();
        for (SysUserRole sysUserRole : sysUserRoleList) {
            if (ids.length() == 0) {
                ids.append(sysUserRole.getRoleId());
            } else {
                ids.append(",").append(sysUserRole.getRoleId());
            }
        }
        return mybatisBaseDaoImpl.selectListByIds(SysRole.class, ids.toString());
    }

    @Override
    @Transactional()
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
        mybatisBaseDaoImpl.insertBySql(saveSql);
    }

    @Override
    @Transactional()
    public boolean outUserInRole(SysRole role, SysUser user) {
        //删除用户角色关联
        String deleteSql = "delete from sys_user_role where user_id = " + user.getId() + " and role_id = " + role.getId();
        mybatisBaseDaoImpl.deleteBySql(deleteSql, null);
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
    @Transactional()
    public String save(SysRole object) throws Exception {
        String id;
        if (object.getId() != null) {
            id = object.getId().toString();
            mybatisBaseDaoImpl.update(object);
            //删除角色权限关联表
            String deleteSql = "delete from sys_role_permission where role_id =" + id;
            mybatisBaseDaoImpl.deleteBySql(deleteSql, null);
        } else {
            id = mybatisBaseDaoImpl.insert(object);
        }
        if(StringUtils.isNotEmpty(object.getPermissionIds())){
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
            mybatisBaseDaoImpl.insertBySql(saveSql);
        }
        return id;
    }

    /**
     * 删除角色
     *
     * @param ids 删除的ID
     * @throws Exception
     */
    @Override
    @Transactional()
    public String delete(String ids) throws Exception {
        //删除角色表
        mybatisBaseDaoImpl.deleteByIds(SysRole.class, ids);
        //删除用户角色关联表
        String sql = "delete from sys_user_role where role_id in (" + ids + ")";
        mybatisBaseDaoImpl.deleteBySql(sql, null);
        //删除角色权限关联表
        String deleteRelSql = "delete from sys_role_permission where role_id in (" + ids + ")";
        mybatisBaseDaoImpl.deleteBySql(deleteRelSql, null);
        // TODO: 2016/7/30 无缓存的更新删除操作需要手动清空缓存 ,可以在mybatis封装中清空该类的所有缓存
        return "";
    }


}
