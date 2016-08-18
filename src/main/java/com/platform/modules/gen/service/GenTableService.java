/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.gen.service;

import com.platform.modules.gen.bean.GenScheme;
import com.platform.modules.gen.bean.GenTable;
import com.platform.framework.common.BaseService;

import java.util.List;

/**
 * 业务表Service
 *
 * @author lufengc
 * @date 2016/7/29 0:33
 */
public interface GenTableService extends BaseService<GenTable> {

    List<GenTable> findTableListFormDb(GenTable genTable);

    boolean checkTableName(String name);

    GenTable getTableFormDb(GenTable genTable);

    String genCode(GenScheme genScheme) throws Exception;

}
