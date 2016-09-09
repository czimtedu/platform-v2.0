/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.Encodes;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.bean.SysUserRole;
import com.platform.modules.sys.service.RoleService;
import com.platform.modules.sys.service.UserService;
import com.platform.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户service实现类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:53:27
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<SysUser> implements UserService {

    @Autowired
    private MybatisDao mybatisDao;
    @Autowired
    private RoleService roleService;

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return SysUser
     */
    @Override
    public SysUser getByUsername(String username) {
        List<SysUser> list = mybatisDao.selectListByConditions(SysUser.class, "username = '" + username + "'");
        SysUser user = null;
        if (list != null && list.size() > 0) {
            user = list.get(0);
            user.setRoleList(roleService.getByUserId(user.getId()));
        }
        return user;
    }

    /**
     * 根据真实姓名查询
     *
     * @param realName 姓名
     * @return List<SysUser>
     */
    @Override
    public List<SysUser> getByRealName(String realName) {
        return mybatisDao.selectListByConditions(
                SysUser.class, "real_name LIKE '%" + realName + "%'");
    }

    /**
     * 根据角色ID获取用户信息
     *
     * @param roleId 角色ID
     * @return List<SysUser>
     */
    @Override
    public List<SysUser> getByRoleId(Integer roleId) {
        String sql = "select * from sys_user_role where role_id = " + roleId;
        List<SysUserRole> sysUserRoleList = mybatisDao.selectListBySql(SysUserRole.class, sql);
        StringBuilder ids = new StringBuilder();
        for (SysUserRole sysUserRole : sysUserRoleList) {
            if (ids.length() == 0) {
                ids.append(sysUserRole.getUserId());
            } else {
                ids.append(",").append(sysUserRole.getUserId());
            }
        }
        return mybatisDao.selectListByIds(SysUser.class, ids.toString());
    }

    /**
     * 更新当前用户信息
     *
     * @param currentUser 当前用户
     */
    @Override
    public void updateUserInfo(SysUser currentUser) {
        mybatisDao.update(currentUser);
    }

    /**
     * 根据机构ID获取用户信息
     *
     * @param officeId 机构ID
     * @return List<SysUser>
     */
    @Override
    public List<SysUser> getUserByOfficeId(String officeId) {
        return mybatisDao.selectListByConditions(SysUser.class, "office_id='" + officeId + "'");
    }

    /**
     * 保存用户信息
     *
     * @param object SysUser
     * @throws Exception
     */
    @Override
    public String save(SysUser object) throws Exception {
        String id;
        if (object.getId() != null) {
            id = object.getId().toString();
            // 如果新密码为空，则不更换密码
            if (StringUtils.isNotBlank(object.getPassword())) {
                object.setPassword(Encodes.entryptPassword(object.getPassword()));
            } else {
                object.setPassword(null);
            }
            mybatisDao.update(object);
            //删除用户角色关联
            String deleteSql = "delete from sys_user_role where user_id =" + id;
            mybatisDao.deleteBySql(deleteSql, null);
        } else {
            object.setPassword(Encodes.entryptPassword(object.getPassword()));
            id = mybatisDao.insert(object);
        }
        //更新用户角色关联
        StringBuilder values = new StringBuilder();
        for (Integer roleId : object.getRoleIdList()) {
            if (values.length() == 0) {
                values.append("(").append(id).append(",").append(roleId).append(")");
            } else {
                values.append(",").append("(").append(id).append(",").append(roleId).append(")");
            }
        }
        String saveSql = "insert into sys_user_role (user_id, role_id) values " + values.toString();
        mybatisDao.insertBySql(saveSql);
        // 清除用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
        return id;
    }

    /**
     * 删除用户信息（逻辑删除）
     *
     * @param ids 要删除的ID
     * @throws Exception
     */
    @Override
    public int delete(String ids) throws Exception {
        mybatisDao.updateByConditions(SysUser.class, "status=0", "id in(" + ids + ")");
        // 清除用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
        return 1;
    }


}
