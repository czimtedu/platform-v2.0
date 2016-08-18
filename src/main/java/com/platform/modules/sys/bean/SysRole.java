/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.bean;

import com.platform.framework.cache.DataCached;
import com.platform.framework.common.BaseEntity;

/**
 * 系统角色bean
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@DataCached(type = DataCached.CachedType.REDIS_CACHED)
public class SysRole extends BaseEntity<SysRole> {
	
    /**
	 * 参数： serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String roleName;

    private String roleSign;

	@NoDbColumn
	private String permissionIds;

	@NoDbColumn
	private String checkedPermissionIds;

	public Integer getId() {
		return id;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getRoleSign() {
		return roleSign;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setRoleSign(String roleSign) {
		this.roleSign = roleSign;
	}

	public String getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(String permissionIds) {
		this.permissionIds = permissionIds;
	}

	public String getCheckedPermissionIds() {
		return checkedPermissionIds;
	}

	public void setCheckedPermissionIds(String checkedPermissionIds) {
		this.checkedPermissionIds = checkedPermissionIds;
	}
}