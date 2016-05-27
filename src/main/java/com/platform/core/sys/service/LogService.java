/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.core.sys.service;

import javax.servlet.http.HttpServletRequest;

import com.platform.core.sys.bean.SysLog;
import com.platform.framework.common.BaseService;

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
     * @param title 日志标题
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
