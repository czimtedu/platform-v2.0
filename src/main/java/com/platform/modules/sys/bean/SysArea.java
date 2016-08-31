/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.sys.bean;

import com.platform.framework.common.BaseEntity;

/**
 * 区域Entity
 * @author lufengc
 * @version 2016-08-31
 */
public class SysArea extends BaseEntity<SysArea> {

	private static final long serialVersionUID = 1L;
    private String id;
	private String parentId;	// 父级编号
	private String parentIds; // 所有父级编号
	private String code; 	// 区域编码
	private String name; 	// 区域名称
	private Integer sort;		// 排序
	private Integer type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）

    @NoDbColumn
    private String parentName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentName() {
        return parentName;
    }
}