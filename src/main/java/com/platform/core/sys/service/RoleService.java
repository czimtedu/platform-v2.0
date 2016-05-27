/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.core.sys.service;

import com.platform.core.sys.bean.SysRole;
import com.platform.core.sys.bean.SysUser;
import com.platform.framework.common.BaseService;

import java.util.List;

/**
 * 系统角色service
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public interface RoleService extends BaseService<SysRole> {

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID
     * @return List
     */
    List<SysRole> getByUserId(Integer userId);

    /**
     * 给角色分配用户
     * @param role
     * @param ids
     */
    void assignUserToRole(SysRole role, String ids);

    /**
     * 给角色移除用户
     * @param role
     * @param user
     */
    boolean outUserInRole(SysRole role, SysUser user);
}
