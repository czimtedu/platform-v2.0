/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service;

import com.platform.framework.common.BaseService;
import com.platform.modules.sys.bean.SysLog;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统日志service
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public interface LogService extends BaseService<SysLog> {

    /**
     * 保存日志
     *
     * @param request HttpServletRequest
     * @param title   日志标题
     */
    void save(HttpServletRequest request, String title) throws Exception;

    /**
     * 保存日志
     *
     * @param request HttpServletRequest
     * @param title   日志标题
     */
    void save(HttpServletRequest request, Object handler, Exception ex, String title) throws Exception;

    /**
     * 清空
     */
    void empty();
}
