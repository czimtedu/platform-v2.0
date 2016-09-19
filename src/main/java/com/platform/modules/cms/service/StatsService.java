/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.service;

import com.platform.framework.common.BaseService;
import com.platform.modules.cms.bean.CmsArticle;
import com.platform.modules.cms.bean.CmsCategory;

import java.util.List;
import java.util.Map;

/**
 * 统计Service
 *
 * @author ThinkGem
 * @version 2013-05-21
 */
public interface StatsService extends BaseService<CmsArticle> {

    List<CmsCategory> article(Map<String, Object> paramMap);
}
