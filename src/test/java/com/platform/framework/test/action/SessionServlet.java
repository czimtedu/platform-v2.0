/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

/**
 * 文件名：SessionServlet.java
 *    包：com.platform.framework.test.action
 *   描述：
 *   作者：lufengcheng
 *   日期：2015-10-30 下午1:38:02
 *   版本：V1.0
 */
package com.platform.framework.test.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类名：SessionServlet
 * 描述：
 * 作者：lufengcheng
 * 日期：2015-10-30 下午1:38:02
 */
public class SessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String attributeName = request.getParameter("name");
            String attributeValue = request.getParameter("value");
            request.getSession().setAttribute(attributeName, attributeValue);
            System.out.println(request.getSession().getAttribute("test1"));
            response.sendRedirect(request.getContextPath() + "/");
    }

    private static final long serialVersionUID = 2878267318695777395L;
}
