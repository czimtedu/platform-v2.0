/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.action;

import com.platform.framework.common.PropertyFilter;
import com.platform.modules.cms.bean.CmsArticle;
import com.platform.modules.cms.bean.CmsArticleData;
import com.platform.modules.cms.service.ArticleService;
import com.platform.modules.sys.bean.Param;
import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * 网站Controller
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/article")
public class ArticleAction extends BaseAction<CmsArticle> {

    @Autowired
    private ArticleService articleService;

    /**
     * 数据绑定
     *
     * @param id ID
     * @return CmsArticle
     * @throws Exception
     */
    @Override
    @ModelAttribute
    public CmsArticle get(@RequestParam(required = false) String id) throws Exception {
        CmsArticle cmsArticle;
        if (StringUtils.isNotEmpty(id)) {
            cmsArticle = articleService.get(CmsArticle.class, id);
        } else {
            cmsArticle =  new CmsArticle();
        }
        return cmsArticle;
    }

    /**
     * 列表
     *
     * @param model Model
     * @param object object
     * @param request request
     *@param response @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = {"list", ""})
    public String list(Model model, CmsArticle object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<CmsArticle> page = articleService.getPage(new Page<CmsArticle>(request, response), object, null, "");
        model.addAttribute("page", page);
        return "modules/cms/articleList";
    }

    /**
     * 编辑视图
     *
     * @param model Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "form")
    public String form(Model model, CmsArticle object) throws Exception {
        String content = "";
        if(StringUtils.isNotEmpty(object.getId())){
            content = articleService.getContent(object.getId());
        }
        model.addAttribute("content", content);
        return "modules/cms/articleForm";
    }

    @Override
    protected String save(Model model, CmsArticle object, RedirectAttributes redirectAttributes) throws Exception {
        return null;
    }

    /**
     * 保存
     *
     * @param model Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @RequestMapping(value = "save")
    public String save(Model model, CmsArticle object, CmsArticleData articleData,
                       RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, object)) {
            return form(model, object);
        }
        articleService.save(object, articleData);
        addMessage(redirectAttributes, "保存成功");
        return "redirect:" + adminPath + "/cms/article/list?repage";
    }

    /**
     * 删除
     *
     * @param model Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "delete")
    public String delete(Model model, CmsArticle object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        articleService.delete(param.getIds());
        addMessage(redirectAttributes, "删除成功");
        return "redirect:" + adminPath + "/cms/article/list?repage";
    }

}
