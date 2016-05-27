/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.cms.service;

import com.platform.core.cms.bean.CmsArticle;
import com.platform.core.cms.bean.CmsArticleData;
import com.platform.framework.common.BaseService;

/**
 * 系统角色service
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public interface ArticleService extends BaseService<CmsArticle> {


    void save(CmsArticle object, CmsArticleData articleData) throws Exception;

    String getContent(String id);
}
