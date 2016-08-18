/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.bean;

import java.io.Serializable;

/**
 * 角色权限关联表
 *
 * @author lufengc
 * @date 2016/1/26 20:00
 */
public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer permissionId;

    private Integer roleId;

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public SysRolePermission() {

    }

    public SysRolePermission(Integer permissionId, Integer roleId) {
        super();
        this.permissionId = permissionId;
        this.roleId = roleId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((permissionId == null) ? 0 : permissionId.hashCode());
        result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SysRolePermission other = (SysRolePermission) obj;
        if (permissionId == null) {
            if (other.permissionId != null)
                return false;
        } else if (!permissionId.equals(other.permissionId))
            return false;
        if (roleId == null) {
            if (other.roleId != null)
                return false;
        } else if (!roleId.equals(other.roleId))
            return false;
        return true;
    }

}