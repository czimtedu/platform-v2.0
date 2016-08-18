/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * entity基类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Integer createBy;    // 创建者
    protected Date createTime;    // 创建日期
    protected Integer updateBy;    // 更新者
    protected Date updateTime;    // 更新日期
    protected Integer status;    // 状态
    protected Integer type;        // 类型
    protected String description;//描述

    public Integer getStatus() {
        return status;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
