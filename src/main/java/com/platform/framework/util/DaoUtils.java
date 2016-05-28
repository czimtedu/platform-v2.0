/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.framework.util;

import com.platform.framework.common.PropertyFilter;
import com.platform.framework.common.PropertyFilter.MatchType;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * DaoUtil工具类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class DaoUtils {

    /**
     * 拼接COUNT SQL语句 weixin
     *
     * @param ql 需要拼接的SQL语句
     * @return String
     */
    public static String getCountQL(String ql) {
        int start = getFirstIndex(ql, "from");
        int end = getLastIndex(ql, "order by");
        int end2 = getLastIndex(ql, "group by");
        //拼接新的统计语句
        if (end < 0 && end2 < 0)
            return "select count(*) " + ql.substring(start);
        else if (end < 0 && end2 >= 0)
            return "select count(*) " + ql.substring(start, end2);
        else if (end >= 0 && end2 < 0)
            return "select count(*) " + ql.substring(start, end);
        else
            return "select count(*) " + ql.substring(start, end2);
    }

    /**
     * 拼接COUNT SQL语句
     *
     * @param ql 需要拼接的SQL语句
     * @return String
     */
    public static String getCountQL2(String ql) {
        int end = getLastIndex(ql, "order by");
        //拼接新的统计语句
        if (end < 0)
            return ql;
        else
            return ql.substring(0, end);
    }

    /**
     * 查询指定keyword在ql中首次出现的位置
     *
     * @param ql      SQL
     * @param keyword keyword
     * @return String
     */
    private static int getFirstIndex(String ql, String keyword) {
        int index = ql.indexOf(keyword);
        int index2 = ql.indexOf(keyword.toUpperCase());
        if (index < 0 || (index2 >= 0 && index > index2))
            index = index2;
        return index;
    }

    /**
     * 查询指定keyword在ql中最后一次出现的位置
     *
     * @param ql      SQL
     * @param keyword keyword
     * @return String
     */
    private static int getLastIndex(String ql, String keyword) {
        int index = ql.lastIndexOf(keyword);
        int index2 = ql.lastIndexOf(keyword.toUpperCase());
        if (index < 0 || (index2 >= 0 && index < index2)) {
            index = index2;
        }
        return index;
    }

    /**
     * 拼接SQL语句
     *
     * @param sb                 StringBuffer对象用以保存SQL语句
     * @param propertyFilterList SQL语句中的筛选条件
     * @return StringBuffer
     */
    public static StringBuffer getWhereClauseBuf(StringBuffer sb, List<PropertyFilter> propertyFilterList) {
        //判断是否已经拼写了where语句
        int index = sb.toString().toUpperCase().indexOf("WHERE");

        int lastIndex = sb.toString().toUpperCase().lastIndexOf("WHERE");
        //判断含有GROUP语句
        int groupIndex = sb.toString().toUpperCase().lastIndexOf("GROUP BY");
        String sb2 = "";
        //添加新的where语句，否则添加新的条件
        if (index == -1) {
            sb.append(" where 1=1");
        } else if (groupIndex > lastIndex) {
            //Hql语句含有GROUP，将GROUP之前的与之后的语句分开
            String newSb = sb.toString();
            sb.delete(0, sb.length());
            sb.append(newSb.substring(0, groupIndex));
            sb2 = newSb.substring(groupIndex, newSb.length());
        }

        if (propertyFilterList.size() > 0) {
            int num = 0;
            for (PropertyFilter filter : propertyFilterList) {
                //buildWhereClause(sb, filter.getPropertyName(), filter.getTableName(), filter.getMatchType(), num);
                //System.out.println("filter.getPropertyName -- "+filter.getPropertyName()+"\r\nfilter.getTableName() -- "+filter.getTableName());
                buildWhereClause2(sb, filter.getPropertyName(), filter.getMatchType(), filter.getMatchValue(), filter.getFieldRelationType());
                num++;
            }
        }
        //重新拼接hql语句
        sb.append(" " + sb2);

        //SQL语句格式修正
        //example:
        //如果在拼接的过程中存在以下SQL语句:SELECT *  FROM TABLENAME  WHERE 1=1 OR FIELDNAME=FIELDVALUE,
        // 则由于1=1筛选条件的存在导致OR语句之后的查询逻辑出现未预期错误
        //因为需要在SQL语句前，进行语句修正，变更为一下格式:SELECT *  FROM TABLENAME  WHERE 1=1 AND FIELDNAME=FIELDVALUE

        //(1)修正SQL语句中多个空格为一个空格。
        //(2)转换为大写形式
        //(3)执行语句替换
        String sqlCommand = sb.toString().replaceAll("\\s+", " ").replaceAll("WHERE 1=1 OR", "WHERE 1=1 AND");
        sb.setLength(0);
        sb.append(sqlCommand);
        return sb;
    }


    /**
     * 拼接SQL语句
     *
     * @param sb           StringBuffer对象用以保存SQL语句
     * @param propertyName 字段名称
     * @param tableName    表名
     * @param matchType    匹配类型 <code>MatchType </code>
     * @param num          ???????????????????
     * @return StringBuffer
     */
    private static StringBuffer buildWhereClause(StringBuffer sb, String propertyName,
                                                 String tableName, MatchType matchType, int num) {
        switch (matchType) {
            case EQ:
                sb.append(" and " + tableName + "." + propertyName + " = :" + tableName + propertyName + num);
                break;
            case NEQ:
                sb.append(" and " + tableName + "." + propertyName + " <> :" + tableName + propertyName + num);
                break;
            case LIKE:
                sb.append(" and " + tableName + "." + propertyName + " like :" + tableName + propertyName + num);
                //System.out.println("sb.string --  "+sb.toString());
                break;
            case LE:
                sb.append(" and " + tableName + "." + propertyName + " <= :" + tableName + propertyName + num);
                break;
            case LT:
                sb.append(" and " + tableName + "." + propertyName + " < :" + tableName + propertyName + num);
                break;
            case GE:
                sb.append(" and " + tableName + "." + propertyName + " >= :" + tableName + propertyName + num);
                break;
            case GT:
                sb.append(" and " + tableName + "." + propertyName + " > :" + tableName + propertyName + num);
        }
        return sb;
    }

    /**
     * 拼接SQL语句
     *
     * @param sb             StringBuffer对象用以保存SQL语句
     * @param filterProperty ???????????????????
     * @param matchType      匹配类型 <code>MatchType </code>
     * @param num            ???????????????????
     * @return StringBuffer
     */
    private static StringBuffer buildWhereClause1(StringBuffer sb, String filterProperty, MatchType matchType, int num) {
        switch (matchType) {
            case EQ:
                sb.append(" and " + filterProperty + " = :" + filterProperty.replace(".", "") + num);
                break;
            case NEQ:
                sb.append(" and " + filterProperty + " <> :" + filterProperty.replace(".", "") + num);
                break;
            case LIKE:
                sb.append(" and " + filterProperty + " like :" + filterProperty.replace(".", "") + num);
                //System.out.println("sb.string --  "+sb.toString());
                break;
            case LE:
                sb.append(" and " + filterProperty + " <= :" + filterProperty.replace(".", "") + num);
                break;
            case LT:
                sb.append(" and " + filterProperty + " < :" + filterProperty.replace(".", "") + num);
                break;
            case GE:
                sb.append(" and " + filterProperty + " >= :" + filterProperty.replace(".", "") + num);
                break;
            case GT:
                sb.append(" and " + filterProperty + " > :" + filterProperty.replace(".", "") + num);
        }
        return sb;
    }

    /**
     * 拼接SQL 筛选条件语句
     *
     * @param sb             StringBuffer对象用以保存SQL语句
     * @param filterProperty 字段名称
     * @param matchType      匹配类型 <code>MatchType </code>
     * @param matchValue     值
     * @return StringBuffer
     */
    private static StringBuffer buildWhereClause2(StringBuffer sb, String filterProperty, MatchType matchType, Object matchValue) {
        switch (matchType) {
            case EQ:
                sb.append(" and " + filterProperty + " = " + matchValue);
                break;
            case NEQ:
                sb.append(" and " + filterProperty + " <> " + matchValue);
                break;
            case LIKE:
                sb.append(" and " + filterProperty + " like '%" + matchValue + "%'");
                //System.out.println("sb.string --  "+sb.toString());
                break;
            case LE:
                sb.append(" and " + filterProperty + " <= '" + matchValue + "'");
                break;
            case LT:
                sb.append(" and " + filterProperty + " < " + matchValue);
                break;
            case GE:
                sb.append(" and " + filterProperty + " >= '" + matchValue + "'");
                break;
            case GT:
                sb.append(" and " + filterProperty + " > " + matchValue);
            case IN:
                sb.append(" and " + filterProperty + " in " + matchValue);
        }
        return sb;
    }


    /**
     * 拼接SQL 筛选条件语句
     *
     * @param sb                StringBuffer对象用以保存SQL语句
     * @param filterProperty    字段名称
     * @param matchType         匹配类型 <code>PropertyFilter.MatchType </code>
     * @param matchValue        值
     * @param fieldRelationType 当前字段的匹配规则<code>PropertyFilter.FieldRelationType</code>
     * @return StringBuffer
     */
    private static StringBuffer buildWhereClause2(StringBuffer sb, String filterProperty, MatchType matchType, Object matchValue, PropertyFilter.FieldRelationType fieldRelationType) {
        switch (matchType) {
            case EQ:
                sb.append(" " + fieldRelationType.toString() + " " + filterProperty + " = " + matchValue);
                break;
            case NEQ:
                sb.append(" " + fieldRelationType.toString() + " " + filterProperty + " <> " + matchValue);
                break;
            case LIKE:
                sb.append(" " + fieldRelationType.toString() + " " + filterProperty + " like '%" + matchValue + "%'");
                //System.out.println("sb.string --  "+sb.toString());
                break;
            case LE:
                sb.append(" " + fieldRelationType.toString() + " " + filterProperty + " <= '" + matchValue + "'");
                break;
            case LT:
                sb.append(" " + fieldRelationType.toString() + " " + filterProperty + " < " + matchValue);
                break;
            case GE:
                sb.append(" " + fieldRelationType.toString() + " " + filterProperty + " >= '" + matchValue + "'");
                break;
            case GT:
                sb.append(" " + fieldRelationType.toString() + " " + filterProperty + " > " + matchValue);
            case IN:
                sb.append(" " + fieldRelationType.toString() + " " + filterProperty + " in " + matchValue);
        }
        return sb;
    }

    /**
     * 拼接SQL 筛选条件语句，主要拼接order by，以及direction(ASC,DESC)参数
     *
     * @param sb        StringBuffer对象用以保存SQL语句
     * @param orderBy   字段名称
     * @param direction 排序(ASC,DESC)
     * @return StringBuffer
     */
    public static StringBuffer getOrderBy(StringBuffer sb, String orderBy, String direction) {
        if (sb.toString().indexOf("order by") == -1) {
            if (StringUtils.isNotEmpty(orderBy)) {
                sb.append(" order by " + orderBy + " " + direction.replace(".", ""));
            }
        }
        return sb;
    }

    /**
     * 拼接SQL 筛选条件语句，主要拼接order by，direction(ASC,DESC)依赖于数据库默认规则
     *
     * @param sb      StringBuffer对象用以保存SQL语句
     * @param orderBy 字段名称
     * @return StringBuffer
     */
    public static StringBuffer getOrderBy2(StringBuffer sb, String orderBy) {
        if (sb.toString().indexOf("order by") == -1) {
            if (StringUtils.isNotEmpty(orderBy)) {
                sb.append(" order by " + orderBy);
            }
        }
        return sb;
    }

}
