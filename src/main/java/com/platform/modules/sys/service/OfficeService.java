/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.sys.service;

import com.platform.framework.common.BaseService;
import com.platform.modules.sys.bean.SysOffice;

import java.util.List;

/**
 * 机构Service
 *
 * @author jeeplus
 * @version 2014-05-16
 */
public interface OfficeService extends BaseService<SysOffice> {

    SysOffice get(String id) throws Exception;

    List<SysOffice> getList(Boolean isAll);

    List<SysOffice> getByUserId(int userId);

    List<SysOffice> getByParentIdsLike(String parentIds);

    List<SysOffice> getByParentId(String parentId) throws Exception;
}
