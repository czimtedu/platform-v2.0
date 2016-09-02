/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.common;

import com.platform.framework.exception.CommonException;
import com.platform.framework.util.DateUtils;
import com.platform.framework.util.StringUtils;
import ognl.OgnlOps;

import java.math.BigDecimal;
import java.util.Date;

public class PropertyFilter {
    /**
     * 类型 值 表别名 比较 属性名
     * 属性比较类型.
     * EQ:等于
     * LIKE：模糊查询
     * LT：小于
     * GT：大于
     * LE：小于等于
     * GE：大于等于
     * IN:等于多个
     */
    public enum MatchType {
        EQ, LIKE, LT, GT, LE, GE, NEQ, IN
    }


    /**
     * 表示当前字段的筛选规则
     */
    public enum FieldRelationType {
        AND, OR;

        /**
         * 获取当前枚举值的描述
         */
        @Override
        public String toString() {
            String enumValue = "";
            switch (this) {
                case AND: {
                    enumValue = "AND";
                }
                break;
                case OR: {
                    enumValue = "OR";
                }
                break;
                default:
                    break;
            }
            return enumValue;
        }
    }

    /**
     * 属性数据类型.
     */
    public enum PropertyType {
        S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class), G(BigDecimal.class);
        private Class<?> clazz;

        PropertyType(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getValue() {
            return clazz;
        }
    }

    /**
     * 属性的比较类型
     */
    private MatchType matchType = null;
    /**
     * 属性的值
     */
    private Object matchValue = null;
    /**
     * 属性的数据类型
     */
    private Class<?> propertyClass;
    /**
     * sql语句中的表别名字
     */
    private String tableName;
    /**
     * 属性参数的名字
     */
    private String propertyName;
    /**
     * 传过来的字符串
     */
    private String filterProperty;

    /**
     * 字段匹配关系
     * 默认为:FieldRelationType.AND
     */
    private FieldRelationType fieldRelationType = FieldRelationType.AND;

    public PropertyFilter() {
        super();
    }

    public PropertyFilter(final String filterProperty, final String value) {
        if (StringUtils.isEmpty(filterProperty)) {
            throw new CommonException("filter属性" + filterProperty + "没有按规则编写,无法得到属性值类型.");
        }

        this.filterProperty = StringUtils.substringBefore(filterProperty, ":$");
        String lastPart = StringUtils.substringAfter(filterProperty, ":$");
        String matchTypeCode = StringUtils.substring(lastPart, 0, lastPart.length() - 1);
        String propertyTypeCode = StringUtils.substring(lastPart, lastPart.length() - 1, lastPart.length());
        try {
            matchType = Enum.valueOf(MatchType.class, matchTypeCode);
        } catch (RuntimeException e) {
            throw new CommonException("filter属性" + filterProperty + "没有按规则编写,无法得到属性比较类型.");
        }
        try {
            propertyClass = Enum.valueOf(PropertyType.class, propertyTypeCode).getValue();
        } catch (RuntimeException e) {
            throw new CommonException("filter属性" + filterProperty + "没有按规则编写,无法得到属性值类型.", e);
        }
        //判断转换类型，如果时间类型调用自己的方法
        if (propertyClass == Date.class) {
            if (value.contains(" ")) {
                this.matchValue = DateUtils.parseDate(value);
                if (matchTypeCode.equals("LE")) {
                    this.matchValue = DateUtils.addDate((Date) this.matchValue, 1);
                }
            } else {
                this.matchValue = DateUtils.parseDate(value);
                if (matchTypeCode.equals("LE")) {
                    this.matchValue = DateUtils.addDate((Date) this.matchValue, 1);
                }
            }
        } else {
            this.matchValue = OgnlOps.convertValue(value, propertyClass);
        }
    }

    /**
     * 专门用于内容权限部分构造查询对象
     */
    public PropertyFilter(final String filterProperty, final String value, final String type) {
        this.filterProperty = filterProperty;
        matchType = Enum.valueOf(MatchType.class, type);
        propertyClass = Enum.valueOf(PropertyType.class, "S").getValue();
        this.matchValue = OgnlOps.convertValue(value, propertyClass);
    }

    /**
     * 获取比较方式.
     */
    public MatchType getMatchType() {
        return matchType;
    }

    /**
     * 获取比较值.
     */
    public Object getMatchValue() {
        return matchValue;
    }

    /**
     * 获取表的别名.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 获取属性参数名字.
     */
    public String getPropertyName() {
        return propertyName;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public void setMatchValue(Object matchValue) {
        this.matchValue = matchValue;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Class<?> getPropertyClass() {
        return propertyClass;
    }

    public void setPropertyClass(Class<?> propertyClass) {
        this.propertyClass = propertyClass;
    }

    public String getFilterProperty() {
        return filterProperty;
    }

    public void setFilterProperty(String filterProperty) {
        this.filterProperty = filterProperty;
    }

    public FieldRelationType getFieldRelationType() {
        return fieldRelationType;
    }

    public void setFieldRelationType(FieldRelationType fieldRelationType) {
        this.fieldRelationType = fieldRelationType;
    }
}
