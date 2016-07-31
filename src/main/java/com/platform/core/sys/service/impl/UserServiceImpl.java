/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.core.sys.service.impl;

import com.platform.core.sys.bean.SysUser;
import com.platform.core.sys.bean.SysUserRole;
import com.platform.core.sys.service.UserService;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisBaseDaoImpl;
import com.platform.framework.util.Encodes;
import com.platform.framework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户service实现类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:53:27
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends BaseServiceImpl<SysUser> implements UserService {

    @Autowired
    private MybatisBaseDaoImpl mybatisBaseDaoImpl;

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return SysUser
     */
    @Override
    @SuppressWarnings("unchecked")
    public SysUser getByUsername(String username) {
        List<SysUser> list = mybatisBaseDaoImpl.selectListDbAndCacheByConditions(
                SysUser.class, "username = '" + username + "'");
        SysUser user = null;
        if (list != null && list.size() > 0) {
            user = list.get(0);
        }
        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SysUser checkUsername(String username, Integer id) {
        List<SysUser> list = mybatisBaseDaoImpl.selectListDbAndCacheByConditions(
                SysUser.class, "username = '" + username + "' AND id <> " + id);
        SysUser user = null;
        if (list != null && list.size() > 0) {
            user = list.get(0);
        }
        return user;
    }

    /**
     * 根据真实姓名查询
     *
     * @param realName 真名
     * @return List
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<SysUser> getByRealName(String realName) {
        return mybatisBaseDaoImpl.selectListDbAndCacheByConditions(
                SysUser.class, "real_name LIKE '%" + realName + "%'");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SysUser> getByRoleId(Integer id) {
        String sql = "select * from sys_user_role where role_id = " + id;
        List<SysUserRole> sysUserRoleList = mybatisBaseDaoImpl.selectBySql(SysUserRole.class, sql);
        StringBuilder ids = new StringBuilder();
        for (SysUserRole sysUserRole : sysUserRoleList) {
            if (ids.length() == 0) {
                ids.append(sysUserRole.getUserId());
            } else {
                ids.append(",").append(sysUserRole.getUserId());
            }
        }
        return mybatisBaseDaoImpl.selectListDbAndCacheByIds(SysUser.class, ids.toString());
    }

    /**
     * 更新当前用户信息
     * @param currentUser 当前用户
     */
    @Override
    @Transactional()
    public void updateUserInfo(SysUser currentUser) {
        mybatisBaseDaoImpl.updateDbAndCache(currentUser);
    }

    /**
     * 保存用户信息
     *
     * @param object SysUser
     * @throws Exception
     */
    @Override
    @Transactional()
    public String save(SysUser object) throws Exception {
        String id;
        if (object.getId() != null) {
            id = object.getId().toString();
            // 如果新密码为空，则不更换密码
            if (StringUtils.isNotBlank(object.getPassword())) {
                object.setPassword(Encodes.entryptPassword(object.getPassword()));
            }
            mybatisBaseDaoImpl.updateDbAndCache(object);
            //删除用户角色关联
            String deleteSql = "delete from sys_user_role where user_id =" + id;
            mybatisBaseDaoImpl.deleteBySql(deleteSql);
        } else {
            object.setPassword(Encodes.entryptPassword(object.getPassword()));
            id = mybatisBaseDaoImpl.insertDb(object);
        }
        //更新用户角色关联
        StringBuilder values = new StringBuilder();
        for (Integer roleId : object.getRoleIdList()) {
            if(values.length() == 0){
                values.append("(").append(id).append(",").append(roleId).append(")");
            }else{
                values.append(",").append("(").append(id).append(",").append(roleId).append(")");
            }
        }
        String saveSql = "insert into sys_user_role (user_id, role_id) values " + values.toString();
        mybatisBaseDaoImpl.insertBySql(saveSql);
        return id;
    }

    /**
     * 删除用户信息（逻辑删除）
     *
     * @param ids 要删除的ID
     * @throws Exception
     */
    @Override
    @Transactional()
    public String delete(String ids) throws Exception {
        mybatisBaseDaoImpl.updateDbAndCacheByConditions(SysUser.class, "status=3", "id in(" + ids + ")");
        return "";
    }


}
