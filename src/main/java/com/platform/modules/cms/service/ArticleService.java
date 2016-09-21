/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.service;

import com.platform.framework.common.BaseService;
import com.platform.modules.cms.bean.CmsArticle;
import com.platform.modules.cms.bean.CmsArticleData;

import java.util.List;

/**
 * 系统角色service
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public interface ArticleService extends BaseService<CmsArticle> {

    String save(CmsArticle object, CmsArticleData articleData) throws Exception;

    CmsArticleData getArticleData(String id);

    void updateArticle(CmsArticle object);

    List<Object[]> getByIds(String ids);

    void updateWeight();
}
