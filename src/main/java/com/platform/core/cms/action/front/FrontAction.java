/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.cms.action.front;

import com.platform.core.cms.bean.CmsArticle;
import com.platform.core.cms.bean.CmsArticleData;
import com.platform.core.cms.bean.CmsGuestbook;
import com.platform.core.cms.service.ArticleService;
import com.platform.core.cms.service.GuestbookService;
import com.platform.core.sys.bean.Param;
import com.platform.framework.common.BaseFrontAction;
import com.platform.framework.common.Global;
import com.platform.framework.common.Page;
import com.platform.framework.common.SysConfigManager;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 网站Controller
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${frontPath}")
public class FrontAction extends BaseFrontAction {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private GuestbookService guestbookService;

    /**
     * 网站首页
     */
    @RequestMapping
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<CmsArticle> page = articleService.getPage(new Page<CmsArticle>(request, response, 1000), new CmsArticle(), "");
        model.addAttribute("page", page);
        return "cms/front/pages/index";
    }

    /**
     * 文章列表
     */
    @RequestMapping("article")
    public String article(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<CmsArticle> page = articleService.getPage(new Page<CmsArticle>(request, response), new CmsArticle(), "");
        model.addAttribute("page", page);
        return "cms/front/pages/article-list";
    }

    /**
     * 文章详情
     */
    @RequestMapping("article/{id}")
    public String article(Model model, @PathVariable("id")String id) throws Exception {
        CmsArticle bean = articleService.get(CmsArticle.class, id);
        if(bean == null || bean.getStatus() == 0){
            return "error/404";
        }
        // 文章阅读次数+1
        bean.setHits(bean.getHits() + 1);
        articleService.updateArticle(bean);
        model.addAttribute("bean", bean);
        model.addAttribute("content", articleService.getContent(id));
        return "cms/front/pages/article";
    }

    /**
     * 关于我
     */
    @RequestMapping("about")
    public String about(Model model) throws Exception {
        return "cms/front/pages/about";
    }

    /**
     * 留言板首页
     */
    @RequestMapping(value = "guestbook", method= RequestMethod.GET)
    public String guestbook(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<CmsGuestbook> page = guestbookService.getPage(new Page<CmsGuestbook>(request, response), new CmsGuestbook(), "");
        model.addAttribute("page", page);
        return "cms/front/pages/guestbook";
    }

    /**
     * 留言板-保存留言信息
     */
    @RequestMapping(value = "guestbook", method=RequestMethod.POST)
    public String guestbookSave(Model model, CmsGuestbook guestbook,
                                HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        guestbook.setIp(request.getRemoteAddr());
        guestbook.setDelFlag("0");
        guestbookService.save(guestbook);
        addMessage(redirectAttributes, "提交成功，谢谢！");
        return "redirect:"+ frontPath +"/guestbook";
    }

    /**
     * data
     */
    @RequestMapping("data")
    public String data(Model model) throws Exception {
        return "cms/front/pages/data";
    }

    /**
     * grz
     */
    @RequestMapping("grz")
    public String grz(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<CmsArticle> page = articleService.getPage(new Page<CmsArticle>(request, response, 1000), new CmsArticle(), "");
        model.addAttribute("page", page);
        return "cms/front/pages/grz";
    }

    /**
     * write page
     *
     * @return view
     * @throws Exception
     */
    @RequestMapping(value = "write")
    public String write(Model model,String id) throws Exception {
        CmsArticle object;
        if (StringUtils.isNotEmpty(id)) {
            object = articleService.get(CmsArticle.class, id);
        } else {
            object =  new CmsArticle();
        }
        String content = "";
        if(StringUtils.isNotEmpty(object.getId())){
            content = articleService.getContent(object.getId());
        }
        model.addAttribute("cmsArticle", object);
        model.addAttribute("content", content);
        return "cms/front/pages/write";
    }

    /**
     * 文章详情
     */
    @RequestMapping("/grz/article/{id}")
    public String grzArticle(Model model, @PathVariable("id")String id) throws Exception {
        CmsArticle bean = articleService.get(CmsArticle.class, id);
        if(bean == null || bean.getStatus() == 0){
            return "error/404";
        }
        // 文章阅读次数+1
        bean.setHits(bean.getHits() + 1);
        articleService.updateArticle(bean);
        model.addAttribute("bean", bean);
        model.addAttribute("content", articleService.getContent(id));
        return "cms/front/pages/grz-article";
    }

    /**
     * save
     *
     * @param model Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @RequestMapping(value = "/article/save")
    public String save(Model model, CmsArticle object, CmsArticleData articleData,
                       RedirectAttributes redirectAttributes) throws Exception {
        object.setCategoryId("心情随笔");
        object.setAuthor("grz");
        object.setType(2);
        object.setStatus(1);
        String id = articleService.save(object, articleData);
        addMessage(redirectAttributes, "保存成功");
        return "redirect:" + frontPath + "/grz/article/" + id;
    }

    /**
     * 删除
     *
     * @return view
     * @throws Exception
     */
    @RequestMapping(value = "/grz/article/delete/{id}")
    public String delete(@PathVariable("id")String id, RedirectAttributes redirectAttributes) throws Exception {
        CmsArticle bean = new CmsArticle();
        bean.setId(id);
        bean.setStatus(0);
        articleService.updateArticle(bean);
        //articleService.delete(id);
        addMessage(redirectAttributes, "删除成功");
        return "redirect:" + frontPath + "/grz?repage";
    }
}
