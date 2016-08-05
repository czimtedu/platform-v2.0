/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.gen.action;

import com.platform.core.gen.bean.GenTable;
import com.platform.core.gen.service.GenTableService;
import com.platform.core.gen.utils.GenUtils;
import com.platform.core.sys.bean.Param;
import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import com.platform.framework.util.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 业务表Action
 *
 * @author lufengc
 * @date 2016/7/28 23:29
 */
@Controller
@RequestMapping(value = "${adminPath}/gen/genTable")
public class GenTableAction extends BaseAction<GenTable> {

    @Autowired
    private GenTableService genTableService;

    @ModelAttribute
    public GenTable get(@RequestParam(required=false) String id) throws Exception {
        if (StringUtils.isNotBlank(id)){
            return genTableService.get(GenTable.class, id);
        }else{
            return new GenTable();
        }
    }

    @Override
    @RequestMapping(value = {"list", ""})
    public String list(Model model, GenTable genTable, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<GenTable> page = genTableService.getPage(new Page<GenTable>(request, response), genTable, "");
        model.addAttribute("page", page);
        return "gen/genTableList";
    }

    @Override
    @RequestMapping(value = "form")
    public String form(Model model, GenTable genTable) {
        // 获取物理表列表
        List<GenTable> tableList = genTableService.findTableListFormDb(new GenTable());
        model.addAttribute("tableList", tableList);
        // 验证表是否存在
        if (StringUtils.isBlank(genTable.getId()) && !genTableService.checkTableName(genTable.getName())){
            addMessage(model, "下一步失败！" + genTable.getName() + " 表已经添加！");
            genTable.setName("");
        }
        // 获取物理表字段
        else{
            genTable = genTableService.getTableFormDb(genTable);
        }
        model.addAttribute("genTable", genTable);
        model.addAttribute("config", GenUtils.getConfig());
        return "modules/gen/genTableForm";
    }

    @Override
    @RequestMapping(value = "save")
    @RequiresPermissions("gen:genTable:edit")
    public String save(Model model, GenTable genTable, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, genTable)){
            return form(model, genTable);
        }
        // 验证表是否已经存在
        if (StringUtils.isBlank(genTable.getId()) && !genTableService.checkTableName(genTable.getName())){
            addMessage(model, "保存失败！" + genTable.getName() + " 表已经存在！");
            genTable.setName("");
            return form(model, genTable);
        }
        genTableService.save(genTable);
        addMessage(redirectAttributes, "保存业务表'" + genTable.getName() + "'成功");
        return "redirect:" + adminPath + "/gen/genTable/list?repage";
    }

    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("gen:genTable:edit")
    public String delete(Model model, GenTable genTable, Param param, RedirectAttributes redirectAttributes) throws Exception {
        genTableService.delete(param.getIds());
        addMessage(redirectAttributes, "删除业务表成功");
        return "redirect:" + adminPath + "/gen/genTable/list?repage";
    }
}
