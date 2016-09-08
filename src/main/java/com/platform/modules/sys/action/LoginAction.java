/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.action;

import com.google.common.collect.Maps;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.Global;
import com.platform.framework.common.SysConfigManager;
import com.platform.framework.security.FormAuthenticationFilter;
import com.platform.framework.security.SecurityRealm;
import com.platform.framework.security.SecurityRealm.Principal;
import com.platform.framework.security.shiro.session.SessionDAO;
import com.platform.framework.servlet.ValidateCodeServlet;
import com.platform.framework.util.CookieUtils;
import com.platform.framework.util.Encodes;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

/**
 * 登录action
 *
 * @author lufengc
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}")
public class LoginAction {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${adminPath}")
    protected String adminPath;

    @Autowired
    private SessionDAO sessionDAO;

    /**
     * 登录页面
     *
     * @return view
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        Principal principal = UserUtils.getPrincipal();
        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            return "redirect:" + adminPath;
        }
        return "modules/sys/login";
    }

    /**
     * 登录(登录失败执行，真正的登录由过滤器完成)
     *
     * @param user    object
     * @param result  数据结果集
     * @param model   Model
     * @param request HttpServletRequest
     * @return view
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Valid SysUser user, BindingResult result, Model model, HttpServletRequest request) {
        /*try {
            Subject subject = UserUtils.getSubject();
            // 已登陆则 跳到首页
            if (subject.isAuthenticated()) {
                return "redirect:/";
            }
            if (result.hasErrors()) {
                model.addAttribute("error", "参数错误！");
                return "sys/login";
            }
            //验证登陆
            UserUtils.verifyLogin(user);
            request.getSession().setAttribute("userInfo", UserUtils.getByLoginName(user.getUsername()));
        	request.getSession().setAttribute("beanList", UserUtils.getMenuList());
        } catch (AuthenticationException e) {
            // 身份验证失败
            model.addAttribute("error", "用户名或密码错误 ！");
            return "sys/login";
        }*/

        Principal principal = UserUtils.getPrincipal();
        // 如果已经登录，则跳转到首页
        if (principal != null) {
            return "redirect:" + adminPath;
        }
        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
            message = "用户或密码错误, 请重试.";
        }

        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
        if (logger.isDebugEnabled()) {
            logger.debug("login fail, active session size: {}, message: {}, exception: {}",
                    sessionDAO.getActiveSessions(false).size(), message, exception);
        }

        // 非授权异常，登录失败，验证码加1。
        if (!UnauthorizedException.class.getName().equals(exception)) {
            model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
        }

        // 验证失败清空验证码
        request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, Encodes.uuid());

        return "modules/sys/login";
    }

    /**
     * 管理登录
     *
     * @throws IOException
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() throws IOException {
        SecurityRealm.Principal principal = UserUtils.getPrincipal();
        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            UserUtils.getSubject().logout();

        }
        return "redirect:" + adminPath + "/login";
    }

    /**
     * 登录成功，进入管理首页
     *
     * @return view
     */
    @RequestMapping("")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        SecurityRealm.Principal principal = UserUtils.getPrincipal();
        // 登录成功后，验证码计算器清零
        isValidateCodeLogin(principal.getLoginName(), false, true);

        if (logger.isDebugEnabled()) {
            logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }

        // 如果已登录，再次访问主页，则退出原账号。
        if (Global.TRUE.equals(SysConfigManager.getConfig("notAllowRefreshIndex"))) {
            String logined = CookieUtils.getCookie(request, "LOGINED");
            if (StringUtils.isBlank(logined) || Global.FALSE.equals(logined)) {
                CookieUtils.setCookie(response, "LOGINED", Global.TRUE);
            } else if (StringUtils.equals(logined, Global.TRUE)) {
                UserUtils.getSubject().logout();
                return "redirect:" + adminPath + "/login";
            }
        }

        return "modules/sys/index-ins";
    }

    /**
     * 首页内容
     *
     * @return view
     */
    @RequestMapping("/home")
    public String home() throws IOException {
        return "modules/sys/home";
    }


    /**
     * 是否是验证码登录
     *
     * @param useruame 用户名
     * @param isFail   计数加1
     * @param clean    计数清零
     * @return view
     */
    @SuppressWarnings("unchecked")
    public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean) {
        Map<String, Integer> loginFailMap = (Map<String, Integer>) JedisUtils.getObject("loginFailMap");
        if (loginFailMap == null) {
            loginFailMap = Maps.newHashMap();
        }
        Integer loginFailNum = loginFailMap.get(useruame);
        if (loginFailNum == null) {
            loginFailNum = 0;
        }
        if (isFail) {
            loginFailNum++;
            loginFailMap.put(useruame, loginFailNum);
        }
        if (clean) {
            loginFailMap.remove(useruame);
        }
        JedisUtils.setObject("loginFailMap", loginFailMap, 0);
        return loginFailNum >= 3;
    }

    /**
     * 获取主题方案
     */
    @RequestMapping(value = "/theme/{theme}")
    public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isNotBlank(theme)) {
            CookieUtils.setCookie(response, "theme", theme);
        } else {
            theme = CookieUtils.getCookie(request, "theme");
        }
        return "redirect:" + request.getParameter("url");
    }
}
