/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.gen.service.impl;

import com.platform.core.gen.bean.*;
import com.platform.core.gen.service.GenSchemeService;
import com.platform.core.gen.service.GenTableService;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisBaseDaoImpl;
import com.platform.framework.util.Encodes;
import com.platform.core.gen.utils.GenUtils;
import com.platform.framework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 生成方案Service实现类
 *
 * @author lufengc
 * @date 2016/7/29 0:27
 */
@Service
@Transactional(readOnly = true)
public class GenSchemeServiceImpl extends BaseServiceImpl<GenScheme> implements GenSchemeService {

    @Autowired
    private MybatisBaseDaoImpl mybatisBaseDaoImpl;

    @Autowired
    private GenTableService genTableService;

    @Override
    @Transactional
    public String save(GenScheme object) throws Exception {
        if (StringUtils.isBlank(object.getId())) {
            object.setId(Encodes.uuid());
            mybatisBaseDaoImpl.insert(object);
        } else {
            mybatisBaseDaoImpl.update(object);
        }
        // 生成代码
        if ("1".equals(object.getFlag())) {
            return generateCode(object);
        }
        return object.getId();
    }

    @Override
    @Transactional
    public String delete(String ids) throws Exception {
        mybatisBaseDaoImpl.deleteByIds(GenScheme.class, ids);
        return "";
    }

    private String generateCode(GenScheme genScheme) throws Exception {
        StringBuilder result = new StringBuilder();
        // 查询主表及字段列
        GenTable genTable = genTableService.get(GenTable.class, genScheme.getGenTable().getId());
        List<GenTableColumn> genTableColumList = mybatisBaseDaoImpl.selectListByIds(GenTableColumn.class, genTable.getId());
        genTable.setColumnList(genTableColumList);
        // 获取所有代码模板
        GenConfig config = GenUtils.getConfig();
        // 获取模板列表
        List<GenTemplate> templateList = GenUtils.getTemplateList(config, genScheme.getCategory(), false);
        List<GenTemplate> childTableTemplateList = GenUtils.getTemplateList(config, genScheme.getCategory(), true);
        // 如果有子表模板，则需要获取子表列表
        /*if (childTableTemplateList.size() > 0) {
            List<GenTable> genTableList = mybatisBaseDaoImpl.selectListByConditions(GenTable.class, "parent_table=" + genTable.getName());
            genTable.setChildList(genTableList);
        }*/
        // 生成子表模板代码
        /*for (GenTable childTable : genTable.getChildList()) {
            childTable.setParent(genTable);
            List<GenTableColumn> genChildTableColumList = mybatisBaseDaoImpl.selectListByIds(GenTableColumn.class, childTable.getId());
            childTable.setColumnList(genChildTableColumList);
            genScheme.setGenTable(childTable);
            Map<String, Object> childTableModel = GenUtils.getDataModel(genScheme);
            for (GenTemplate tpl : childTableTemplateList) {
                result.append(GenUtils.generateToFile(tpl, childTableModel, genScheme.getReplaceFile()));
            }
        }*/
        // 生成主表模板代码
        genScheme.setGenTable(genTable);
        Map<String, Object> model = GenUtils.getDataModel(genScheme);
        for (GenTemplate tpl : templateList) {
            result.append(GenUtils.generateToFile(tpl, model, genScheme.getReplaceFile()));
        }
        return result.toString();
    }

}
