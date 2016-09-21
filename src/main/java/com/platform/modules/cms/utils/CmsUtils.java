/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.cms.utils;

import com.google.common.collect.Lists;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.Global;
import com.platform.framework.common.Page;
import com.platform.framework.common.SpringContextHolder;
import com.platform.framework.common.SysConfigManager;
import com.platform.framework.mapper.JsonMapper;
import com.platform.framework.util.StringUtils;
import com.platform.modules.cms.bean.CmsArticle;
import com.platform.modules.cms.bean.CmsCategory;
import com.platform.modules.cms.bean.CmsLink;
import com.platform.modules.cms.bean.CmsSite;
import com.platform.modules.cms.service.ArticleService;
import com.platform.modules.cms.service.CategoryService;
import com.platform.modules.cms.service.LinkService;
import com.platform.modules.cms.service.SiteService;
import org.springframework.ui.Model;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 内容管理工具类
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
public class CmsUtils {

    private static SiteService siteService = SpringContextHolder.getBean(SiteService.class);
    private static CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class);
    private static ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
    private static LinkService linkService = SpringContextHolder.getBean(LinkService.class);
    private static ServletContext context = SpringContextHolder.getBean(ServletContext.class);

    private static final String SITE_LIST = "siteList";

    /**
     * 获得站点列表
     */
    public static List<CmsSite> getSiteList() {
        @SuppressWarnings("unchecked")
        List<CmsSite> siteList = (List<CmsSite>) JedisUtils.getObject(SITE_LIST);
        if (siteList == null) {
            try {
                siteList = siteService.getList(new CmsSite());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(siteList != null){
            JedisUtils.setObject(SITE_LIST, siteList, 0);
        } else {
            siteList = new ArrayList<>();
        }
        return siteList;
    }

    /**
     * 获得站点信息
     *
     * @param siteId 站点编号
     */
    public static CmsSite getSite(String siteId) {
        String id = "1";
        if (StringUtils.isNotBlank(siteId)) {
            id = siteId;
        }
        for (CmsSite site : getSiteList()) {
            if (site.getId().equals(id)) {
                return site;
            }
        }
        return new CmsSite(id);
    }

    /**
     * 获得主导航列表
     *
     * @param siteId 站点编号
     */
    public static List<CmsCategory> getMainNavList(String siteId) {
        @SuppressWarnings("unchecked")
        List<CmsCategory> mainNavList = (List<CmsCategory>) JedisUtils.getObject("mainNavList_" + siteId);
        if (mainNavList == null) {
            CmsCategory category = new CmsCategory();
            category.setSiteId(siteId);
            category.setParentId("1");
            category.setInMenu(Global.SHOW);
            try {
                mainNavList = categoryService.getList(category);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(mainNavList != null){
            JedisUtils.setObject("mainNavList_" + siteId, mainNavList, 0);
        } else {
            mainNavList = new ArrayList<>();
        }
        return mainNavList;
    }

    /**
     * 获取栏目
     *
     * @param categoryId 栏目编号
     * @return CmsCategory
     */
    public static CmsCategory getCategory(String categoryId) {
        try {
            return categoryService.get(categoryId);
        } catch (Exception e) {
            return new CmsCategory();
        }
    }

    /**
     * 获得栏目列表
     *
     * @param siteId   站点编号
     * @param parentId 分类父编号
     * @param number   获取数目
     * @param param    预留参数，例： key1:'value1', key2:'value2' ...
     */
    public static List<CmsCategory> getCategoryList(String siteId, String parentId, int number, String param) {
        CmsCategory category = new CmsCategory();
        category.setSiteId(siteId);
        category.setParentId(parentId);
        if (StringUtils.isNotBlank(param)) {
            @SuppressWarnings({"unused", "rawtypes"})
            Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
        }
        try {
            return categoryService.getList(category);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 获取栏目
     *
     * @param categoryIds 栏目编号
     * @return List<CmsCategory>
     */
    public static List<CmsCategory> getCategoryListByIds(String categoryIds) {
        try {
            return categoryService.getByIds(categoryIds);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 获取文章
     *
     * @param articleId 文章编号
     * @return CmsArticle
     */
    public static CmsArticle getArticle(String articleId) {
        try {
            return articleService.get(articleId);
        } catch (Exception e) {
            return new CmsArticle();
        }
    }

    /**
     * 获取文章列表
     *
     * @param siteId     站点编号
     * @param categoryId 分类编号
     * @param number     获取数目
     * @param param      预留参数，例： key1:'value1', key2:'value2' ...
     *                   posid	推荐位（1：首页焦点图；2：栏目页文章推荐；）
     *                   image	文章图片（1：有图片的文章）
     *                   orderBy 排序字符串
     * @return ${fnc:getArticleList(category.site.id, category.id, not empty pageSize?pageSize:8, 'posid:2, orderBy: \"hits desc\"')}"
     */
    public static List<CmsArticle> getArticleList(String siteId, String categoryId, int number, String param) {
        Page<CmsArticle> page = new Page<>(1, number, -1);
        CmsCategory category = new CmsCategory();
        category.setId(categoryId);
        category.setSiteId(siteId);
        category.setParentIds(categoryId);
        CmsArticle article = new CmsArticle();
        if (StringUtils.isNotBlank(param)) {
            @SuppressWarnings({"rawtypes"})
            Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
            if (new Integer(1).equals(map.get("posid")) || new Integer(2).equals(map.get("posid"))) {
                article.setPosid(String.valueOf(map.get("posid")));
            }
            if (new Integer(1).equals(map.get("image"))) {
                article.setImage(Global.YES);
            }
            if (StringUtils.isNotBlank((String) map.get("orderBy"))) {
                page.setOrderBy((String) map.get("orderBy"));
            }
        }
        article.setStatus(Global.STATUS_NORMAL);
        try {
            page = articleService.getPage(page, article, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page.getList();
    }

    /**
     * 获取链接
     *
     * @param linkId 文章编号
     * @return
     */
    public static CmsLink getLink(String linkId) {
        try {
            return linkService.get(linkId);
        } catch (Exception e) {
            return new CmsLink();
        }
    }

    /**
     * 获取链接列表
     *
     * @param siteId     站点编号
     * @param categoryId 分类编号
     * @param number     获取数目
     * @param param      预留参数，例： key1:'value1', key2:'value2' ...
     * @return
     */
    public static List<CmsLink> getLinkList(String siteId, String categoryId, int number, String param) {
        Page<CmsLink> page = new Page<CmsLink>(1, number, -1);
        CmsLink link = new CmsLink();
        link.setCategoryId(categoryId);
        if (StringUtils.isNotBlank(param)) {
            @SuppressWarnings({"unused", "rawtypes"})
            Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
        }
        link.setStatus(Global.STATUS_NORMAL);
        try {
            page = linkService.getPage(page, link, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page.getList();
    }

    // ============== Cms Cache ==============

    public static Object getCache(String key) {
        return JedisUtils.get(key);
    }

    public static void putCache(String key, Object value) {
        JedisUtils.setObject(key, value, 0);
    }

    public static void removeCache(String key) {
        JedisUtils.delObject(key);
    }
    public static void removeCacheLike(String key) {
        JedisUtils.delKeysLike(key);
    }

    /**
     * 获得文章动态URL地址
     *
     * @param article
     * @return url
     */
    public static String getUrlDynamic(CmsArticle article) {
        if (StringUtils.isNotBlank(article.getLink())) {
            return article.getLink();
        }
        return context.getContextPath() + SysConfigManager.getFrontPath() +
                "/view-" + article.getCategoryId() + "-" + article.getId() + SysConfigManager.getUrlSuffix();
    }

    /**
     * 获得栏目动态URL地址
     *
     * @param category CmsCategory
     * @return url
     */
    public static String getUrlDynamic(CmsCategory category) {
        if (StringUtils.isNotBlank(category.getHref())) {
            if (!category.getHref().contains("://")) {
                return context.getContextPath() + SysConfigManager.getFrontPath() + category.getHref();
            } else {
                return category.getHref();
            }
        }
        return context.getContextPath() + SysConfigManager.getFrontPath() +
                "/list-" + category.getId() + SysConfigManager.getUrlSuffix();
    }

    /**
     * 从图片地址中去除ContextPath地址
     *
     * @param src 图片地址
     * @return src
     */
    public static String formatImageSrcToDb(String src) {
        if (StringUtils.isBlank(src)) return src;
        if (src.startsWith(context.getContextPath() + "/uploads")) {
            return src.substring(context.getContextPath().length());
        } else {
            return src;
        }
    }

    /**
     * 从图片地址中加入ContextPath地址
     *
     * @param src 图片地址
     * @return src
     */
    public static String formatImageSrcToWeb(String src) {
        if (StringUtils.isBlank(src)) return src;
        if (src.startsWith(context.getContextPath() + "/userfiles")) {
            return src;
        } else {
            return context.getContextPath() + src;
        }
    }

    public static void addViewConfigAttribute(Model model, String param) {
        if (StringUtils.isNotBlank(param)) {
            @SuppressWarnings("rawtypes")
            Map map = JsonMapper.getInstance().fromJson(param, Map.class);
            if (map != null) {
                for (Object o : map.keySet()) {
                    model.addAttribute("viewConfig_" + o.toString(), map.get(o));
                }
            }
        }
    }

    public static void addViewConfigAttribute(Model model, CmsCategory category) {
        List<CmsCategory> categoryList = Lists.newArrayList();
        CmsCategory c = category;
        boolean goon = true;
        do {
            if (c.getParentId() == null || CmsCategory.isRoot(c.getParentId())) {
                goon = false;
            }
            categoryList.add(c);
            c = getCategory(c.getParentId());
        } while (goon);
        Collections.reverse(categoryList);
        for (CmsCategory ca : categoryList) {
            addViewConfigAttribute(model, ca.getViewConfig());
        }
    }
}