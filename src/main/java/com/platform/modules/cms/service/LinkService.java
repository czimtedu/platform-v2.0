/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.service;

import com.platform.framework.common.BaseService;
import com.platform.modules.cms.bean.CmsCategory;
import com.platform.modules.cms.bean.CmsLink;

import java.util.List;

/**
 * 链接Service
 *
 * @author ThinkGem
 * @version 2013-01-15
 */
public interface LinkService extends BaseService<CmsLink> {


    List<Object[]> findByIds(String ids);
}
