/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.action;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import com.platform.framework.mapper.JsonMapper;
import com.platform.modules.cms.bean.CmsArticle;
import com.platform.modules.cms.bean.CmsArticleData;
import com.platform.modules.cms.bean.CmsCategory;
import com.platform.modules.cms.bean.CmsSite;
import com.platform.modules.cms.service.ArticleService;
import com.platform.modules.cms.service.CategoryService;
import com.platform.modules.cms.service.FileTplService;
import com.platform.modules.cms.service.SiteService;
import com.platform.modules.cms.utils.CmsUtils;
import com.platform.modules.cms.utils.TplUtils;
import com.platform.modules.sys.bean.Param;
import com.platform.modules.sys.bean.SysOffice;
import com.platform.modules.sys.bean.SysPermission;
import com.platform.modules.sys.service.OfficeService;
import com.platform.modules.sys.utils.UserUtils;
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
import java.util.*;

/**
 * 栏目Action
 *
 * @author lufengc
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/category")
public class CategoryAction extends BaseAction<CmsCategory> {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileTplService fileTplService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private OfficeService officeService;

    /**
     * 数据绑定
     *
     * @param id 主键
     * @return CmsCategory
     * @throws Exception
     */
    @Override
    @ModelAttribute
    public CmsCategory get(@RequestParam(required = false) String id) throws Exception {
        if (StringUtils.isNotEmpty(id)) {
            return categoryService.get(id);
        } else {
            return new CmsCategory();
        }
    }

    /**
     * 列表
     *
     * @param model    Model
     * @param object   object
     * @param request  request
     * @param response HttpServletResponse
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = {"list", ""})
    @RequiresPermissions("cms:category:view")
    public String list(Model model, CmsCategory object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<CmsCategory> sourcelist = categoryService.getByUser(true, null);
        List<CmsCategory> list = Lists.newArrayList();
        Collections.sort(sourcelist,  new Comparator<CmsCategory>() {
            public int compare(CmsCategory o1, CmsCategory o2) {
                // 按sortId升序排序
                if (o1.getSort() > o2.getSort()) {
                    return 1;
                } else if (Objects.equals(o1.getSort(), o2.getSort())) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        CmsCategory.sortList(list, sourcelist, "1");
        model.addAttribute("list", list);
        return "modules/cms/categoryList";
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
    @RequiresPermissions("cms:category:view")
    public String form(Model model, CmsCategory object) throws Exception {
        if (object.getParentId() == null) {
            object.setParentId("1");
        }
        if (object.getId() == null) {
            List<CmsCategory> list = Lists.newArrayList();
            List<CmsCategory> sourcelist = categoryService.getList(new CmsCategory());
            CmsCategory.sortList(list, sourcelist, object.getParentId());
            if (list.size() > 0) {
                if ("0".equals(object.getParentId())) {
                    object.setSort(list.get(list.size() - 1).getSort() + 10000);
                } else {
                    object.setSort(list.get(list.size() - 1).getSort() + 10);
                }
            } else {
                object.setSort(categoryService.get(object.getParentId()).getSort() + 10);
            }
        }
        CmsCategory parent = categoryService.get(object.getParentId());
        if (parent != null) {
            object.setParentName(parent.getName());
        } else {
            object.setParentName("顶级栏目");
        }
        if (object.getOfficeId() != null) {
            SysOffice office = officeService.get(object.getOfficeId());
            object.setOfficeName(office.getName());
        }
        model.addAttribute("cmsCategory", object);
        model.addAttribute("listViewList", getTplContent(CmsCategory.DEFAULT_TEMPLATE));
        model.addAttribute("category_DEFAULT_TEMPLATE", CmsCategory.DEFAULT_TEMPLATE);
        model.addAttribute("contentViewList", getTplContent(CmsArticle.DEFAULT_TEMPLATE));
        model.addAttribute("article_DEFAULT_TEMPLATE", CmsArticle.DEFAULT_TEMPLATE);
        return "modules/cms/categoryForm";
    }

    private List<String> getTplContent(String prefix) throws Exception {
        List<String> tplList = fileTplService.getNameListByPrefix(siteService.get(CmsSite.getCurrentSiteId()).getSolutionPath());
        tplList = TplUtils.tplTrim(tplList, prefix, "");
        return tplList;
    }

    /**
     * 保存
     *
     * @param model              Model
     * @param object             object
     * @param redirectAttributes RedirectAttributes
     * @return ModelAndView
     * @throws Exception
     */
    @Override
    protected String save(Model model, CmsCategory object, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, object)) {
            return form(model, object);
        }
        categoryService.save(object);
        addMessage(redirectAttributes, "保存栏目成功");
        return "redirect:" + adminPath + "/cms/category/";
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
    public String delete(Model model, CmsCategory object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        if (CmsCategory.isRoot(object.getId())) {
            addMessage(redirectAttributes, "删除栏目失败, 不允许删除顶级栏目或编号为空");
        } else {
            categoryService.delete(object.getId());
            addMessage(redirectAttributes, "删除栏目成功");
        }
        return "redirect:" + adminPath + "/cms/category/";
    }

    /**
     * 批量修改栏目排序
     */
    @RequestMapping(value = "updateSort")
    @RequiresPermissions("sys:category:edit")
    public String updateSort(Integer[] ids, Integer[] sortIds, RedirectAttributes redirectAttributes) {
        categoryService.updateSort(ids, sortIds);
        addMessage(redirectAttributes, "保存排序成功!");
        return "redirect:" + adminPath + "/sys/category/";
    }

    /**
     * 栏目树数据
     *
     * @param extId      id
     * @return List
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(String module, @RequestParam(required = false) String extId) throws Exception {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<CmsCategory> list = categoryService.getByUser(true, module);
        for (CmsCategory bean : list) {
            if (extId == null || (!extId.equals(bean.getId()) && !bean.getParentIds().contains("," + extId + ","))) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", bean.getId());
                map.put("pId", bean.getParentId() != null ? bean.getParentId() : 0);
                map.put("name", bean.getName());
                map.put("module", bean.getModule());
                mapList.add(map);
            }
        }
        return mapList;
    }

}
