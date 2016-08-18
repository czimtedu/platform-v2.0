/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service;

import com.platform.modules.sys.bean.SysRole;
import com.platform.modules.sys.bean.SysUser;
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
