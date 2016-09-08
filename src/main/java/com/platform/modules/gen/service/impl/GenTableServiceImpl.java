/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.gen.service.impl;

import com.platform.modules.gen.bean.*;
import com.platform.modules.gen.service.GenTableService;
import com.platform.modules.gen.utils.GenUtils;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.Global;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.BeanToTable;
import com.platform.framework.util.Encodes;
import com.platform.framework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业务表Service实现类
 *
 * @author lufengc
 * @date 2016/7/29 0:33
 */
@Service
public class GenTableServiceImpl extends BaseServiceImpl<GenTable> implements GenTableService {

    @Autowired
    private MybatisDao mybatisDao;

    @Override
    public GenTable get(String id) throws Exception {
        GenTable genTable = super.get(id);
        List<GenTableColumn> columnList = mybatisDao.selectListByConditions(GenTableColumn.class,
                "gen_table_id='" + genTable.getId() + "' order by sort_id asc");
        genTable.setColumnList(columnList);
        return genTable;
    }

    @Override
    public String save(GenTable object) throws Exception {
        if (StringUtils.isBlank(object.getId())) {
            object.setId(Encodes.uuid());
            mybatisDao.insert(object);
        } else {
            mybatisDao.update(object);
        }
        // 保存列
        for (GenTableColumn column : object.getColumnList()) {
            column.setGenTable(object);
            if (StringUtils.isBlank(column.getId())) {
                column.setId(Encodes.uuid());
                column.setGenTableId(object.getId());
                mybatisDao.insert(column);
            } else {
                mybatisDao.update(column);
            }
        }
        return object.getId();
    }

    @Override
    public int delete(String ids) throws Exception {
        mybatisDao.deleteByIds(GenTable.class, ids);
        String deleteSql = "delete from gen_table_column where gen_table_id in (" + StringUtils.idsToString(ids) + ")";
        mybatisDao.deleteBySql(deleteSql, GenTableColumn.class);
        return 1;
    }

    /**
     * 获取物理数据表列表
     *
     * @param genTable GenTable
     * @return List<GenTable>
     */
    public List<GenTable> findTableListFormDb(GenTable genTable) {
        String sql = "SELECT t.table_name AS name,t.TABLE_COMMENT AS comments " +
                "FROM information_schema.`TABLES` t " +
                "WHERE t.TABLE_SCHEMA = (select database()) ";
        if (StringUtils.isNoneEmpty(genTable.getName())) {
            sql += "AND t.TABLE_NAME = '" + genTable.getName().toUpperCase() + "' ";
        }
        sql += "ORDER BY t.TABLE_NAME";
        return mybatisDao.selectListBySql(GenTable.class, sql);
    }

    /**
     * 验证表名是否可用
     *
     * @param tableName 表名
     * @return boolean 如果已存在，则返回false
     */
    public boolean checkTableName(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return true;
        }
        GenTable genTable = new GenTable();
        genTable.setName(tableName);
        List<GenTable> list = mybatisDao.selectListByConditions(GenTable.class, "name='" + tableName + "'");
        return !(list != null && list.size() > 0);
    }

    /**
     * 获取物理数据表列表
     *
     * @param genTable GenTable
     * @return GenTable
     */
    public GenTable getTableFormDb(GenTable genTable) {
        // 如果有表名，则获取物理表
        if (StringUtils.isNotBlank(genTable.getName())) {
            List<GenTable> list = findTableListFormDb(genTable);
            if (list.size() > 0) { // 表已存在
                // 如果是新增，初始化表属性
                if (StringUtils.isBlank(genTable.getId())) {
                    genTable = list.get(0);
                    // 设置字段说明
                    if (StringUtils.isBlank(genTable.getComments())) {
                        genTable.setComments(genTable.getName());
                    }
                    genTable.setClassName(BeanToTable.tableNameToClassName(genTable.getName()));
                }
                // 添加新列
                //List<GenTableColumn> columnList = genDataBaseDictDao.findTableColumnList(genTable);
                String sql = "SELECT t.COLUMN_NAME AS name, " +
                        "(CASE WHEN t.IS_NULLABLE = 'YES' THEN '1' ELSE '0' END) AS is_null," +
                        "(t.ORDINAL_POSITION * 10) AS sort_id," +
                        "t.COLUMN_COMMENT AS comments," +
                        "t.COLUMN_TYPE AS jdbc_type " +
                        "FROM information_schema.`COLUMNS` t " +
                        "WHERE t.TABLE_SCHEMA = (select database()) ";
                if (StringUtils.isNoneEmpty(genTable.getName())) {
                    sql += "AND t.TABLE_NAME = '" + genTable.getName().toUpperCase() + "' ";
                }
                sql += "ORDER BY t.ORDINAL_POSITION";
                List<GenTableColumn> columnList = mybatisDao.selectListBySql(GenTableColumn.class, sql);
                // 同步已存在的表的最新列数据到genTableColumn中
                // 添加表中已存在 数据中不存在的列
                for (GenTableColumn column : columnList) {
                    boolean b = false;
                    for (GenTableColumn e : genTable.getColumnList()) {
                        if (e.getName().equals(column.getName())) {
                            b = true;
                        }
                    }
                    if (!b) {
                        genTable.getColumnList().add(column);
                    }
                }
                // 删除表中不存在 数据中存在的列
                for (GenTableColumn e : genTable.getColumnList()) {
                    boolean b = false;
                    for (GenTableColumn column : columnList) {
                        if (column.getName().equals(e.getName())) {
                            b = true;
                        }
                    }
                    if (!b) {
                        e.setStatus(Global.STATUS_DELETE);
                    }
                }
                // 获取主键
                //genTable.setPkList(genDataBaseDictDao.findTablePK(genTable));
                String findTablePKSql = "SELECT lower(au.COLUMN_NAME) AS columnName " +
                        "FROM information_schema.`COLUMNS` au " +
                        "WHERE au.TABLE_SCHEMA = (select database())  " +
                        "AND au.COLUMN_KEY='PRI' AND au.TABLE_NAME = upper('" +
                        genTable.getName().toUpperCase() + "')";
                genTable.setPkList(mybatisDao.selectStringBySql(findTablePKSql));
                // 初始化列属性字段
                GenUtils.initColumnField(genTable);
            }
        }
        return genTable;
    }

    /**
     * 生成代码
     * @param genScheme GenScheme
     * @return String
     * @throws Exception
     */
    public String genCode(GenScheme genScheme) throws Exception {
        StringBuilder result = new StringBuilder();
        // 获取所有代码模板
        GenConfig config = GenUtils.getConfig();
        // 获取模板列表
        List<GenTemplate> templateList = GenUtils.getTemplateList(config, genScheme.getCategory(), false);
        // 生成主表模板代码
        Map<String, Object> model = GenUtils.getDataModel(genScheme);
        for (GenTemplate tpl : templateList) {
            result.append(GenUtils.generateToFile(tpl, model, genScheme.getReplaceFile()));
        }
        return result.toString();
    }

}
