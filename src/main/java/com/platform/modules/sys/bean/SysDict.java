/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.bean;

import com.platform.framework.cache.DataCached;
import org.hibernate.validator.constraints.Length;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;
import java.util.Date;

/**
 * 字典bean
 *
 * @author lufengc
 * @date 2016/1/16 9:41
 */
@DataCached(type = DataCached.CachedType.REDIS_CACHED)
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String label;
    private String value;
    private String enName;
    private String name;
    private Integer parentId;
    private Integer sortId;
    private String description; //描述
    private Integer createBy;    // 创建者
    private Date createTime;    // 创建日期
    private Integer updateBy;    // 更新者
    private Date updateTime;    // 更新日期
    @NoDbColumn
    private Integer actionType; // 表单类型

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlAttribute
    @Length(min=1, max=100)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlAttribute
    @Length(min=1, max=100)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Length(min=1, max=100)
    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    @XmlAttribute
    @Length(min=0, max=100)
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

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String toString() {
        return label;
    }
}
