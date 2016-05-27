/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.cms.action.front;

import com.platform.core.cms.bean.CmsArticle;
import com.platform.core.cms.service.ArticleService;
import com.platform.framework.common.BaseFrontAction;
import com.platform.framework.common.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping(value = "${frontPath}")
public class FrontController extends BaseFrontAction {

    @Autowired
    private ArticleService articleService;

    /**
     * 网站首页
     */
    @RequestMapping
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<CmsArticle> page = articleService.getPage(new Page<CmsArticle>(request, response), new CmsArticle(), "");
        model.addAttribute("page", page);
        return "cms/front/pages/index";
    }

    /**
     * 网站首页
     */
    @RequestMapping("article/{id}")
    public String article(Model model, @PathVariable("id")String id) throws Exception {
        CmsArticle bean = articleService.get(CmsArticle.class, id);
        model.addAttribute("bean", bean);
        model.addAttribute("content", articleService.getContent(id));
        return "cms/front/pages/article";
    }

}
