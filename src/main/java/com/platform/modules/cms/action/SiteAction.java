/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.action;

import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import com.platform.framework.util.CookieUtils;
import com.platform.modules.cms.bean.CmsSite;
import com.platform.modules.cms.service.SiteService;
import com.platform.modules.sys.bean.Param;
import com.platform.modules.sys.utils.UserUtils;
import org.apache.commons.lang.StringUtils;
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
 * 站点
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/site")
public class SiteAction extends BaseAction<CmsSite> {

    @Autowired
    private SiteService siteService;

    /**
     * 数据绑定
     *
     * @param id ID
     * @return CmsSite
     * @throws Exception
     */
    @Override
    @ModelAttribute
    public CmsSite get(@RequestParam(required = false) String id) throws Exception {
        CmsSite cmsSite;
        if (StringUtils.isNotEmpty(id)) {
            cmsSite = siteService.get(id);
        } else {
            cmsSite = new CmsSite();
        }
        return cmsSite;
    }

    /**
     * 列表
     *
     * @param model    Model
     * @param object   object
     * @param request  request
     * @param response @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = {"list", ""})
    @RequiresPermissions("cms:site:view")
    public String list(Model model, CmsSite object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<CmsSite> page = siteService.getPage(new Page<CmsSite>(request, response), object, "");
        model.addAttribute("page", page);
        return "modules/cms/siteList";
    }

    /**
     * 编辑视图
     *
     * @param model  Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "form")
    @RequiresPermissions("cms:site:edit")
    public String form(Model model, CmsSite object) throws Exception {
        return "modules/cms/siteForm";
    }

    /**
     * 保存
     *
     * @param model  Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "save")
    @RequiresPermissions("cms:site:edit")
    protected String save(Model model, CmsSite object, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, object)) {
            return form(model, object);
        }
        siteService.save(object);
        addMessage(redirectAttributes, "保存成功");
        return "redirect:" + adminPath + "/cms/site/list?repage";
    }

    /**
     * 删除
     *
     * @param model  Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("cms:site:edit")
    public String delete(Model model, CmsSite object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        if (CmsSite.isDefault(object.getId())) {
            addMessage(redirectAttributes, "删除站点失败, 不允许删除默认站点");
        } else {
            siteService.delete(object.getId());
            addMessage(redirectAttributes, "删除成功");
        }
        return "redirect:" + adminPath + "/cms/site/list?repage";
    }

    /**
     * 选择站点
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "select")
    @RequiresPermissions("cms:site:edit")
    public String select(String id, boolean flag, HttpServletResponse response) {
        if (id != null) {
            UserUtils.putCache("siteId", id);
            // 保存到Cookie中，下次登录后自动切换到该站点
            CookieUtils.setCookie(response, "siteId", id);
        }
        if (flag) {
            return "redirect:" + adminPath;
        }
        return "modules/cms/siteList";
    }
}
