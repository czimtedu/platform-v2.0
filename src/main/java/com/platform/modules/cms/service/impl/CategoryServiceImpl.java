/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.cms.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.Encodes;
import com.platform.framework.util.StringUtils;
import com.platform.modules.cms.bean.CmsCategory;
import com.platform.modules.cms.bean.CmsSite;
import com.platform.modules.cms.service.CategoryService;
import com.platform.modules.cms.utils.CmsUtils;
import com.platform.modules.sys.bean.SysOffice;
import com.platform.modules.sys.bean.SysPermission;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.service.OfficeService;
import com.platform.modules.sys.service.impl.LogServiceImpl;
import com.platform.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 栏目Service实现类
 *
 * @author lufengc
 * @version 2016-09-12
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CmsCategory> implements CategoryService {

    public static final String CACHE_CATEGORY_LIST = "categoryList";

    @Autowired
    MybatisDao mybatisDao;
    @Autowired
    OfficeService officeService;

    @Override
    @SuppressWarnings("unchecked")
    public List<CmsCategory> getByUser(boolean isCurrentSite, String module) throws Exception {
        List<CmsCategory> list = (List<CmsCategory>) UserUtils.getCache(CACHE_CATEGORY_LIST);
        if (list == null) {
            list = getList(new CmsCategory());
            for (CmsCategory next : list) {
                next.setOfficeName(officeService.get(next.getOfficeId()).getName());
            }
            if(list.size() > 0){
                UserUtils.putCache(CACHE_CATEGORY_LIST, list);
            }
        }

        if (isCurrentSite) {
            List<CmsCategory> categoryList = Lists.newArrayList();
            for (CmsCategory e : list) {
                if (CmsCategory.isRoot(e.getId()) || (e.getSiteId() != null && e.getSiteId().equals(CmsSite.getCurrentSiteId()))) {
                    if (StringUtils.isNotEmpty(module)) {
                        if (module.equals(e.getModule()) || "".equals(e.getModule())) {
                            categoryList.add(e);
                        }
                    } else {
                        categoryList.add(e);
                    }
                }
            }
            return categoryList;
        }
        return list;
    }

    @Override
    public List<CmsCategory> getByParentId(String parentId, String siteId) {
        return mybatisDao.selectListByConditions(CmsCategory.class, "parent_id=" + parentId + " and site_id=" + siteId);
    }

    @Override
    public String save(CmsCategory category) throws Exception {
        category.setSiteId(CmsSite.getCurrentSiteId());
        if (StringUtils.isNotBlank(category.getViewConfig())) {
            category.setViewConfig(StringEscapeUtils.unescapeHtml4(category.getViewConfig()));
        }
        if (StringUtils.isNotEmpty(category.getId())) {
            super.update(category);
        } else {
            category.setId(Encodes.uuid());
            super.insert(category);
        }
        UserUtils.removeCache(CACHE_CATEGORY_LIST);
        CmsUtils.removeCache("mainNavList_" + category.getSiteId());
        return category.getId();
    }

    @Override
    public int delete(String id) throws Exception {
        String ids = id;
        //获取子节点集合
        List<CmsCategory> childList = new ArrayList<>();
        CmsCategory category = CmsUtils.getCategory(id);
        getChildList(childList, CmsUtils.getCategoryList(category.getSiteId(), null, -1, null), id);
        for (CmsCategory object : childList) {
            ids += "," + object.getId();
        }

        super.delete(ids);
        UserUtils.removeCache(CACHE_CATEGORY_LIST);
        CmsUtils.removeCache("mainNavList_" + category.getSiteId());
        return 1;
    }

    /**
     * 获取某个父节点下面的所有子节点
     *
     * @param childList 用户保存子节点的集合
     * @param allList   总数据结合
     * @param parentId  父ID
     */
    private void getChildList(List<CmsCategory> childList, List<CmsCategory> allList, String parentId) {
        for (CmsCategory object : allList) {
            if (parentId.equals(object.getParentId())) {
                getChildList(childList, allList, object.getId());
                childList.add(object);
            }
        }
    }

    /**
     * 通过编号获取栏目列表
     */
    @Override
    public List<CmsCategory> getByIds(String ids) throws Exception {
        return mybatisDao.selectListByIds(CmsCategory.class, ids);
    }

    @Override
    public void updateSort(Integer[] ids, Integer[] sorts) {
        String sql;
        for (int i = 0; i < ids.length; i++) {
            sql = "UPDATE sys_permission SET sort = " + sorts[i] + " WHERE id = " + ids[i];
            mybatisDao.updateBySql(sql, CmsCategory.class);
        }
        UserUtils.removeCache(CACHE_CATEGORY_LIST);
        CmsUtils.removeCacheLike("mainNavList_");
    }

}
