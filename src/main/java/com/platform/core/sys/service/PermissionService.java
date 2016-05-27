/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.core.sys.service;

import com.platform.core.sys.bean.SysPermission;
import com.platform.framework.common.BaseService;

import java.util.List;

/**
 * 系统权限sevice
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public interface PermissionService extends BaseService<SysPermission> {

    /**
     * 根据角色ID获取权限
     *
     * @param roleId 角色ID
     * @return List
     */
    List<SysPermission> getByRoleId(Integer roleId);

    /**
     * 根据用户ID获取权限
     *
     * @param userId 用户ID
     * @return List
     */
    List<SysPermission> getByUserId(Integer userId);

    /**
     * 根据parentId获取权限
     *
     * @param parentId 父ID
     * @return List
     */
    List<SysPermission> getByParentId(int parentId);
}
