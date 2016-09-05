/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.framework.test.domain;

import com.platform.framework.common.SysConfigManager;

/**
 * 注释格式示例
 *
 * @author lufengcheng
 * @date 2016-01-20 17:19:18
 */
public class Test {

    /**
     * 获取用户名
     *
     * @param id 用户ID
     * @return 用户名
     */
    public String getUserName(Integer id) {
        System.out.println();
        System.out.println();
        return "";
    }

    public static void main(String[] args) {
        System.out.println(SysConfigManager.getClassPath());
    }
}
