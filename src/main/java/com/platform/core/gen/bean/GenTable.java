/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */
package com.platform.core.gen.bean;

import com.google.common.collect.Lists;
import com.platform.core.sys.bean.NoDbColumn;
import com.platform.framework.common.BaseEntity;
import com.platform.framework.util.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 业务表Entity
 *
 * @author ThinkGem
 * @version 2013-10-15
 */
public class GenTable extends BaseEntity<GenTable> {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;    // 名称
    private String comments;        // 描述
    private String className;        // 实体类名称
    private String parentTable;        // 关联父表
    private String parentTableFk;        // 关联父表外键

    @NoDbColumn
    private List<GenTableColumn> columnList = Lists.newArrayList();    // 表列
    @NoDbColumn
    private List<String> pkList; // 当前表主键列表

    public GenTable() {
        super();
    }

    public GenTable(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Length(min = 1, max = 200)
    public String getName() {
        return StringUtils.lowerCase(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParentTable() {
        return StringUtils.lowerCase(parentTable);
    }

    public void setParentTable(String parentTable) {
        this.parentTable = parentTable;
    }

    public String getParentTableFk() {
        return StringUtils.lowerCase(parentTableFk);
    }

    public void setParentTableFk(String parentTableFk) {
        this.parentTableFk = parentTableFk;
    }

    public List<String> getPkList() {
        return pkList;
    }

    public void setPkList(List<String> pkList) {
        this.pkList = pkList;
    }

    public List<GenTableColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<GenTableColumn> columnList) {
        this.columnList = columnList;
    }

    /**
     * 获取列名和说明
     *
     * @return
     */
    public String getNameAndComments() {
        return getName() + (comments == null ? "" : "  :  " + comments);
    }

    /**
     * 获取导入依赖包字符串
     *
     * @return
     */
    public List<String> getImportList() {
        List<String> importList = Lists.newArrayList(); // 引用列表
        for (GenTableColumn column : getColumnList()) {
            if (column.getIsNotBaseField() || ("1".equals(column.getIsQuery()) && "between".equals(column.getQueryType())
                    && ("createDate".equals(column.getSimpleJavaField()) || "updateDate".equals(column.getSimpleJavaField())))) {
                // 导入类型依赖包， 如果类型中包含“.”，则需要导入引用。
                if (StringUtils.indexOf(column.getJavaType(), ".") != -1 && !importList.contains(column.getJavaType())) {
                    importList.add(column.getJavaType());
                }
            }
            if (column.getIsNotBaseField()) {
                // 导入JSR303、Json等依赖包
                for (String ann : column.getAnnotationList()) {
                    if (!importList.contains(StringUtils.substringBeforeLast(ann, "("))) {
                        importList.add(StringUtils.substringBeforeLast(ann, "("));
                    }
                }
            }
        }
        return importList;
    }

    /**
     * 是否存在create_date列
     *
     * @return
     */
    public Boolean getCreateDateExists() {
        for (GenTableColumn c : columnList) {
            if ("create_date".equals(c.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在update_date列
     *
     * @return
     */
    public Boolean getUpdateDateExists() {
        for (GenTableColumn c : columnList) {
            if ("update_date".equals(c.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在del_flag列
     *
     * @return
     */
    public Boolean getDelFlagExists() {
        for (GenTableColumn c : columnList) {
            if ("del_flag".equals(c.getName())) {
                return true;
            }
        }
        return false;
    }
}


