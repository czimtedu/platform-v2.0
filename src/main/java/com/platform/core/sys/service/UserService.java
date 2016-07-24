/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.core.sys.service;

import com.platform.core.sys.bean.SysUser;
import com.platform.framework.common.BaseService;

import java.util.List;

/**
 * 用户service实现类
 *
 * @author lufengcheng
 * @date 2016-01-13 09:41:50
 */
public interface UserService extends BaseService<SysUser> {


    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return SysUSer
     */
    SysUser getByUsername(String username);

    /**
     * 检查用户名
     *
     * @param username 用户名
     * @return SysUSer
     */
    SysUser checkUsername(String username, Integer id);


    /**
     * 根据真实姓名查询
     *
     * @param realName 真名
     * @return List
     */
    List<SysUser> getByRealName(String realName);

    /**
     * 根据角色ID查询用户列表
     * @param id roleId
     * @return
     */
    List<SysUser> getByRoleId(Integer id);

    /**
     * 更新当前用户信息
     * @param currentUser 当前用户
     */
    void updateUserInfo(SysUser currentUser);
}
