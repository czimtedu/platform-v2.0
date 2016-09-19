/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.cms.service.impl;


import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.DateUtils;
import com.platform.modules.cms.bean.CmsArticle;
import com.platform.modules.cms.bean.CmsCategory;
import com.platform.modules.cms.bean.CmsSite;
import com.platform.modules.cms.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 统计Service
 *
 * @author lufengc
 * @version 2016-09-12
 */
@Service
public class StatsServiceImpl extends BaseServiceImpl<CmsArticle> implements StatsService {

    @Autowired
    private MybatisDao mybatisDao;

    @Override
    public List<CmsCategory> article(Map<String, Object> paramMap) {
        CmsCategory category = new CmsCategory();
        category.setSiteId(CmsSite.getCurrentSiteId());

        Date beginDate = DateUtils.parseDate(paramMap.get("beginDate"));
        if (beginDate == null) {
            beginDate = DateUtils.setDays(new Date(), 1);
            paramMap.put("beginDate", DateUtils.formatDate(beginDate, "yyyy-MM-dd"));
        }
        category.setBeginDate(beginDate);
        Date endDate = DateUtils.parseDate(paramMap.get("endDate"));
        if (endDate == null) {
            endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
            paramMap.put("endDate", DateUtils.formatDate(endDate, "yyyy-MM-dd"));
        }
        category.setEndDate(endDate);

        String categoryId = (String) paramMap.get("categoryId");
        if (categoryId != null && !("".equals(categoryId))) {
            category.setId(categoryId);
            category.setParentIds(categoryId);
        }

        String officeId = (String) (paramMap.get("officeId"));
        if (officeId != null && !("".equals(officeId))) {
            category.setOfficeId(officeId);
        }

        List<CmsCategory> list = mybatisDao.selectListByConditions(CmsCategory.class, "");
        return list;
    }

    @Override
    public String save(CmsArticle object) throws Exception {
        return null;
    }
}
