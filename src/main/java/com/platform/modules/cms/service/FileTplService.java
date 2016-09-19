/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.service;

import com.platform.framework.common.BaseService;
import com.platform.modules.cms.bean.CmsCategory;
import com.platform.modules.cms.bean.CmsFileTpl;

import java.util.List;

/**
 * User: songlai
 * Date: 13-8-27
 * Time: 下午4:56
 */
public interface FileTplService extends BaseService<CmsFileTpl> {

    List<String> getNameListByPrefix(String path);

    List<CmsFileTpl> getListByPath(String path, boolean directory);

    List<CmsFileTpl> getListForEdit(String path);

    void getAllDirectory(List<CmsFileTpl> result, List<CmsFileTpl> list);

    CmsFileTpl getFileTpl(String name);
}
