/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.gen.bean;

import com.platform.modules.sys.bean.SysDict;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * 生成方案Entity
 *
 * @author ThinkGem
 * @version 2013-10-15
 */
@XmlRootElement(name = "config")
public class GenConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<GenCategory> categoryList;    // 代码模板分类
    private List<SysDict> javaTypeList;        // Java类型
    private List<SysDict> queryTypeList;        // 查询类型
    private List<SysDict> showTypeList;        // 显示类型

    public GenConfig() {
        super();
    }

    @XmlElementWrapper(name = "category")
    @XmlElement(name = "category")
    public List<GenCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<GenCategory> categoryList) {
        this.categoryList = categoryList;
    }

    @XmlElementWrapper(name = "javaType")
    @XmlElement(name = "dict")
    public List<SysDict> getJavaTypeList() {
        return javaTypeList;
    }

    public void setJavaTypeList(List<SysDict> javaTypeList) {
        this.javaTypeList = javaTypeList;
    }

    @XmlElementWrapper(name = "queryType")
    @XmlElement(name = "dict")
    public List<SysDict> getQueryTypeList() {
        return queryTypeList;
    }

    public void setQueryTypeList(List<SysDict> queryTypeList) {
        this.queryTypeList = queryTypeList;
    }

    @XmlElementWrapper(name = "showType")
    @XmlElement(name = "dict")
    public List<SysDict> getShowTypeList() {
        return showTypeList;
    }

    public void setShowTypeList(List<SysDict> showTypeList) {
        this.showTypeList = showTypeList;
    }

}