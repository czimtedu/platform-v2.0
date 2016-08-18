/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.framework.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.service.UserService;
import com.platform.framework.test.common.TestSupport;

import java.util.List;

public class UserServiceTest extends TestSupport {

    @Autowired
    private UserService userService;

    @Test
    public void test_insert() {
        start();
        List<SysUser> admin = userService.getByRealName("admin");
        System.err.println(admin.size());
        end();
    }



}
