/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service;

import com.platform.modules.sys.bean.SysPermission;
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
     * 保存或更新操作
     * @param object Object
     */
    String save(SysPermission object) throws Exception;

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

    /**
     * 更新排序
     * @param ids Integer[]
     * @param sorts Integer[]
     */
    void updatePermissionSort(Integer[] ids, Integer[] sorts);
}
