/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.action;

import com.platform.framework.mapper.JsonMapper;
import com.platform.modules.cms.bean.CmsArticle;
import com.platform.modules.cms.bean.CmsArticleData;
import com.platform.modules.cms.bean.CmsSite;
import com.platform.modules.cms.service.ArticleService;
import com.platform.modules.cms.service.FileTplService;
import com.platform.modules.cms.service.SiteService;
import com.platform.modules.cms.utils.CmsUtils;
import com.platform.modules.cms.utils.TplUtils;
import com.platform.modules.sys.bean.Param;
import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    @Autowired
    private FileTplService fileTplService;
    @Autowired
    private SiteService siteService;

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
            cmsArticle = articleService.get(id);
        } else {
            cmsArticle =  new CmsArticle();
        }
        return cmsArticle;
    }

    @RequestMapping(value = "")
    @RequiresPermissions("cms:article:view")
    public String index() {
        return "modules/cms/cmsIndex";
    }

    @RequestMapping(value = "none")
    @RequiresPermissions("cms:article:view")
    public String none() {
        return "modules/cms/cmsNone";
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
    @RequestMapping(value = "list")
    @RequiresPermissions("cms:article:view")
    public String list(Model model, CmsArticle object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        articleService.updateWeight();
        Page<CmsArticle> page = articleService.getPage(new Page<CmsArticle>(request, response), object, "");
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
    @RequiresPermissions("cms:article:edit")
    public String form(Model model, CmsArticle object) throws Exception {
        CmsArticleData articleData = null;
        if(StringUtils.isNotEmpty(object.getId())){
            articleData = articleService.getArticleData(object.getId());
        }
        model.addAttribute("articleData", articleData);
        model.addAttribute("contentViewList",getTplContent());
        model.addAttribute("article_DEFAULT_TEMPLATE",CmsArticle.DEFAULT_TEMPLATE);
        model.addAttribute("article", object);
        CmsUtils.addViewConfigAttribute(model, CmsUtils.getCategory(object.getCategoryId()));
        return "modules/cms/articleForm";
    }

    private List<String> getTplContent() throws Exception {
        List<String> tplList = fileTplService.getNameListByPrefix(siteService.get(CmsSite.getCurrentSiteId()).getSolutionPath());
        tplList = TplUtils.tplTrim(tplList, CmsArticle.DEFAULT_TEMPLATE, "");
        return tplList;
    }

    @Override
    @RequiresPermissions("cms:article:view")
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
    @RequiresPermissions("cms:article:edit")
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
    @RequiresPermissions("cms:article:edit")
    public String delete(Model model, CmsArticle object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        articleService.delete(param.getIds());
        addMessage(redirectAttributes, "删除成功");
        return "redirect:" + adminPath + "/cms/article/list?repage";
    }

    /**
     * 文章选择列表
     */
    @RequestMapping(value = "selectList")
    @RequiresPermissions("cms:article:view")
    public String selectList(CmsArticle article, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        list(model,article, request, response);
        return "modules/cms/articleSelectList";
    }

    /**
     * 通过编号获取文章标题
     */
    @ResponseBody
    @RequestMapping(value = "findByIds")
    @RequiresPermissions("cms:article:view")
    public String findByIds(String ids) {
        List<Object[]> list = articleService.getByIds(ids);
        return JsonMapper.nonDefaultMapper().toJson(list);
    }

}
