/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.gen.action;

import com.platform.core.gen.bean.GenScheme;
import com.platform.core.gen.bean.GenTable;
import com.platform.core.gen.service.GenSchemeService;
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

/**
 * 生成方案Action
 *
 * @author lufengc
 * @date 2016/7/28 23:29
 */
@Controller
@RequestMapping(value = "${adminPath}/gen/genScheme")
public class GenSchemeAction extends BaseAction<GenScheme> {

    @Autowired
    private GenSchemeService genSchemeService;

    @Autowired
    private GenTableService genTableService;

    @Override
    @ModelAttribute
    public GenScheme get(@RequestParam(required = false) String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            return genSchemeService.get(GenScheme.class, id);
        } else {
            return new GenScheme();
        }
    }

    @Override
    @RequestMapping(value = {"list", ""})
    public String list(Model model, GenScheme genScheme, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<GenScheme> page = genSchemeService.getPage(new Page<GenScheme>(request, response), genScheme, "");
        model.addAttribute("page", page);
        return "gen/genSchemeList";
    }

    @Override
    @RequestMapping(value = "form")
    public String form(Model model, GenScheme genScheme) throws Exception {
        if (StringUtils.isBlank(genScheme.getPackageName())) {
            genScheme.setPackageName("com.thinkgem.jeesite.modules");
        }
//		if (StringUtils.isBlank(genScheme.getFunctionAuthor())){
//			genScheme.setFunctionAuthor(UserUtils.getUser().getName());
//		}
        model.addAttribute("genScheme", genScheme);
        model.addAttribute("config", GenUtils.getConfig());
        model.addAttribute("tableList", genTableService.getList(new GenTable()));
        return "gen/genSchemeForm";
    }

    @Override
    @RequestMapping(value = "save")
    @RequiresPermissions("gen:genScheme:edit")
    public String save(Model model, GenScheme genScheme, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, genScheme)) {
            return form(model, genScheme);
        }
        String result = genSchemeService.save(genScheme);
        addMessage(redirectAttributes, "操作生成方案'" + genScheme.getName() + "'成功<br/>" + result);
        return "redirect:" + adminPath + "/gen/genScheme/list?repage";
    }

    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("gen:genScheme:edit")
    public String delete(Model model, GenScheme genScheme, Param param, RedirectAttributes redirectAttributes) throws Exception {
        genSchemeService.delete(param.getIds());
        addMessage(redirectAttributes, "删除生成方案成功");
        return "redirect:" + adminPath + "/gen/genScheme/list?repage";
    }

}
