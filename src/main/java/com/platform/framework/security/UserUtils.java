/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.framework.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.platform.core.sys.bean.SysPermission;
import com.platform.core.sys.bean.SysRole;
import com.platform.core.sys.bean.SysUser;
import com.platform.core.sys.service.PermissionService;
import com.platform.core.sys.service.RoleService;
import com.platform.core.sys.service.UserService;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.SpringContextHolder;
import com.platform.framework.security.SecurityRealm.Principal;

import static com.platform.core.sys.service.impl.LogServiceImpl.CACHE_PERMISSION_NAME_PATH_MAP;

/**
 * 用户工具类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class UserUtils {

    private static UserService userService = SpringContextHolder.getBean(UserService.class);
    private static RoleService roleService = SpringContextHolder.getBean(RoleService.class);
    private static PermissionService permissionService = SpringContextHolder.getBean(PermissionService.class);

    private static final String CACHE_ROLE_LIST = "roleList";
    private static final String CACHE_PERMISSION_LIST = "permissionList";


    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 取不到返回null
     */
    public static SysUser get(int id) {
        SysUser user = null;
        try {
            user = userService.get(SysUser.class, id + "");
            if(user != null){
                user.setRoleList(roleService.getByUserId(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 获取当前用户
     *
     * @return 取不到返回 new User()
     */
    public static SysUser getUser() {
        Principal principal = getPrincipal();
        if (principal != null) {
            SysUser user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return new SysUser();
        }
        return new SysUser();
    }

    /**
     * 根据登录名获取用户
     *
     * @param loginName 登录名
     * @return 取不到返回null
     */
    public static SysUser getByLoginName(String loginName) {
        return userService.getByUsername(loginName);
    }

    /**
     * 根据真实姓名获取用户
     *
     * @param realName 真实姓名
     * @return 用户列表
     */
    public static List<SysUser> getByRealName(String realName) {
        return userService.getByRealName(realName);
    }

    /**
     * 获取当前用户角色列表
     *
     * @return 角色列表
     */
    public static List<SysRole> getRoleList() {
        @SuppressWarnings("unchecked")
        List<SysRole> roleList = (List<SysRole>) getCache(CACHE_ROLE_LIST);
        try {
            if (roleList == null) {
                SysUser user = getUser();
                if (user.isAdmin()) {
                    roleList = roleService.getList(new SysRole());
                } else {
                    roleList = roleService.getByUserId(getUserId());
                }
                putCache(CACHE_ROLE_LIST, roleList);
            }
        } catch (Exception e) {
            roleList = new ArrayList<>();
        }
        return roleList;
    }

    /**
     * 获取当前用户授权菜单
     *
     * @return 权限菜单列表
     */
    public static List<SysPermission> getMenuList() {
        @SuppressWarnings("unchecked")
        List<SysPermission> permissionList = (List<SysPermission>) getCache(CACHE_PERMISSION_LIST);
        try {
            if (permissionList == null) {
                SysUser user = getUser();
                if (user.isAdmin()) {
                    permissionList = permissionService.getList(new SysPermission());
                } else {
                    permissionList = permissionService.getByUserId(getUserId());
                }
                putCache(CACHE_PERMISSION_LIST, permissionList);
            }
        } catch (Exception e) {
            permissionList = new ArrayList<>();
        }
        return permissionList;
    }

    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录者ID
     *
     * @return 取不到返回 new User()
     */
    public static int getUserId() {
        Principal principal = getPrincipal();
        int userId = 0;
        if (principal != null) {
            userId = principal.getId();
        }
        return userId;
    }

    /**
     * 获取当前登录者对象
     */
    public static Principal getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        return (Principal) subject.getPrincipal();
    }

    /**
     * 获取当前用户session
     *
     * @return 当前用户session
     */
    public static Session getSession() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        if (session == null) {
            session = subject.getSession();
        }
        return session;
    }

    // ============== User Cache ==============

    /**
     * 清除当前用户缓存
     */
    public static void clearCache() {
        removeCache(CACHE_ROLE_LIST);
        removeCache(CACHE_PERMISSION_LIST);
    }

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        Object obj = getSession().getAttribute(key);
        return obj == null ? defaultValue : obj;
    }

    public static void putCache(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static void removeCache(String key) {
        getSession().removeAttribute(key);
    }


}

