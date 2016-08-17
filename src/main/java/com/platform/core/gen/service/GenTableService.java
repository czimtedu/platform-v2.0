/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.gen.service;

import com.platform.core.gen.bean.GenScheme;
import com.platform.core.gen.bean.GenTable;
import com.platform.core.gen.bean.GenTemplate;
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
