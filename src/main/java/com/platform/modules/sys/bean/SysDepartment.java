/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.modules.sys.bean;

import com.platform.framework.cache.DataCached;
import com.platform.framework.common.BaseEntity;

/**
 * 部门管理
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@DataCached(type = DataCached.CachedType.REDIS_CACHED)
public class SysDepartment extends BaseEntity<SysDepartment> {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Integer parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
