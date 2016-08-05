/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.framework.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * mybatis接口
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public interface MybatisBaseDao extends SqlMapper {

    /**
     * 根据指定表名(${tableName})、id列表(${conditions})获取数据集
     *
     * @return 数据集
     */
    @Select("select * from ${tableName} where id in (${conditions}) order by field(id,${conditions})")
    List<Map> selectByIds();


    /**
     * 根据指定表名获取当前表的记录数量
     *
     * @return 记录数量
     */
    @Select("SELECT COUNT(0) FROM ${_parameter}")
    List<Integer> selectCountByTableName();

    /**
     * 根据指定表名(${tableName})、id列表(${conditions})、表字段(${param})查询
     *
     * @return 数据集
     */
    @Select("select ${param} from ${tableName} where id in (${conditions}) order by field(id,${conditions})")
    List<Map> selectFieldByIds();

    /**
     * 根据指定表名(${tableName})、筛选条件(${conditions})获取id数据集
     *
     * @return id数据集
     */
    @Select("select id from ${tableName} where ${conditions}")
    List<String> selectIdsByConditions();

    /**
     * 执行SQL查询语句(${_parameter})
     *
     * @return 单列List数据集
     */
    @Select("${_parameter}")
    List<String> selectListBySql();

    /**
     * 执行SQL查询语句
     *
     * @return 多列Map数据集
     */
    @Select("${_parameter}")
    List<Map> selectMapBySql();

    /**
     * 执行插入语句，${tableName}：表名 ${param}：字段 ${conditions}：各字段的值
     */
    @Insert("insert into ${tableName} (${param}) values (${conditions})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert();

    /**
     * 执行更新语句，${tableName}：表名  ${param}：需要更新的字段和对应的值 ${conditions}：需要更新的记录条件
     */
    @Update("update ${tableName} set ${param} where ${conditions}")
    void update();

    /**
     * 根据ID删除 ，${tableName}：表名 ${conditions}：需要删除的记录id
     */
    @Delete("delete from ${tableName} where id in (${conditions})")
    void deleteByIds();

    /**
     * 执行SQL删除语句
     */
    @Delete("${_parameter}")
    void deleteBySql();

    /**
     * 执行SQL插入语句
     */
    @Insert("${_parameter}")
    void insertBySql();

    /**
     * 执行SQL更新语句
     */
    @Update("${_parameter}")
    void updateBySql();
}
