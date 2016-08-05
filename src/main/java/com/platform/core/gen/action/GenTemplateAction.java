/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.gen.action;

import com.platform.core.gen.bean.GenTemplate;
import com.platform.core.gen.service.GenTemplateService;
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
 * 代码模板Action
 *
 * @author lufengc
 * @date 2016/7/28 23:29
 */
@Controller
@RequestMapping(value = "${adminPath}/gen/genTemplate")
public class GenTemplateAction extends BaseAction<GenTemplate> {

    @Autowired
    private GenTemplateService genTemplateService;

    @Override
    @ModelAttribute
    public GenTemplate get(@RequestParam(required=false) String id) throws Exception {
        if (StringUtils.isNotBlank(id)){
            return genTemplateService.get(GenTemplate.class, id);
        }else{
            return new GenTemplate();
        }
    }

    @Override
    @RequestMapping(value = {"list", ""})
    public String list(Model model, GenTemplate genTemplate, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<GenTemplate> page = genTemplateService.getPage(new Page<GenTemplate>(request, response), genTemplate, "");
        model.addAttribute("page", page);
        return "modules/gen/genTemplateList";
    }

    @Override
    @RequestMapping(value = "form")
    public String form(Model model, GenTemplate genTemplate) {
        model.addAttribute("genTemplate", genTemplate);
        return "modules/gen/genTemplateForm";
    }

    @Override
    @RequestMapping(value = "save")
    @RequiresPermissions("gen:genTemplate:edit")
    public String save(Model model, GenTemplate genTemplate, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, genTemplate)){
            return form(model, genTemplate);
        }
        genTemplateService.save(genTemplate);
        addMessage(redirectAttributes, "保存代码模板'" + genTemplate.getName() + "'成功");
        return "redirect:" + adminPath + "/gen/genTemplate/?repage";
    }


    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("gen:genTemplate:edit")
    public String delete(Model model, GenTemplate genTemplate, Param param, RedirectAttributes redirectAttributes) throws Exception {
        genTemplateService.delete(param.getIds());
        addMessage(redirectAttributes, "删除代码模板成功");
        return "redirect:" + adminPath + "/gen/genTemplate/list?repage";
    }
}
