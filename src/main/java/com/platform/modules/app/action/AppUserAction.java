/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.app.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.framework.common.Result;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @version 2016/9/12
 */
@Controller
@RequestMapping(value = "api")
public class AppUserAction {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/appLogin", produces = "text/html;charset=UTF-8")
    public String appLogin(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        String username = request.getParameter("username");
        json.put("username", username);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/getUserInfo", produces = "text/html;charset=UTF-8")
    public String getUserInfo(HttpServletRequest request, String id) {
        Result result;
        SysUser user;
        try {
            user = userService.get(id);
            if (user != null) {
                result = new Result("0", "获取用户信息成功！", user);
            } else {
                result = new Result("0", "用户信息不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result("200", "获取用户信息出错！");
        }
        return JSON.toJSONString(result);
    }


}
