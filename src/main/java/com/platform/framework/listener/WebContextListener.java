/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.listener;

import com.platform.framework.common.SysConfigManager;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * 自定义webContext监听器
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class WebContextListener extends ContextLoaderListener {

    @Override
    public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
        if (!SysConfigManager.printKeyLoadMessage()) {
            return null;
        }
        return super.initWebApplicationContext(servletContext);
    }
}
