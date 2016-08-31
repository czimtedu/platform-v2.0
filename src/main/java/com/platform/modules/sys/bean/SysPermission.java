/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platform.framework.cache.DataCached;
import com.platform.framework.common.BaseEntity;

import java.util.List;
import java.util.Objects;

/**
 * 系统权限bean
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@DataCached(type = DataCached.CachedType.REDIS_CACHED)
public class SysPermission extends BaseEntity<SysPermission> {

	private static final long serialVersionUID = 1L;
	private Integer id;
    private String permissionName;
    private String permissionSign;
    private Integer parentId;
    private String thumbImg;
    private String funUrl;
    private Integer isMenu;
    private Integer sortId;
    private Integer isShow;
    private String parentIds;

	@NoDbColumn
	private String parentName;

	@JsonIgnore
	public static void sortList(List<SysPermission> list, List<SysPermission> sourcelist, Integer parentId, boolean cascade){
		for (SysPermission bean : sourcelist) {
			if(Objects.equals(bean.getParentId(), parentId)){
				list.add(bean);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (SysPermission p : sourcelist) {
						if(Objects.equals(p.getParentId(), bean.getId())){
							sortList(list, sourcelist, bean.getId(), true);
							break;
						}
					}
				}
			}
		}
	}

	@JsonIgnore
	public static Integer getRootId(){
		return 0;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getId() {
		return id;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public String getPermissionSign() {
		return permissionSign;
	}

	public Integer getParentId() {
		return parentId;
	}

	public String getThumbImg() {
		return thumbImg;
	}

	public String getFunUrl() {
		return funUrl;
	}

	public Integer getIsMenu() {
		return isMenu;
	}

	public Integer getSortId() {
		return sortId;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public String getParentIds() {
		if(parentId == null){

		}
		return parentIds;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public void setPermissionSign(String permissionSign) {
		this.permissionSign = permissionSign;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public void setThumbImg(String thumbImg) {
		this.thumbImg = thumbImg;
	}

	public void setFunUrl(String funUrl) {
		this.funUrl = funUrl;
	}

	public void setIsMenu(Integer isMenu) {
		this.isMenu = isMenu;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
    


}