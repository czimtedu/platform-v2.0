/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.Exceptions;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.SysLog;
import com.platform.modules.sys.bean.SysPermission;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.service.LogService;
import com.platform.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 系统日志service实现类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Service
public class LogServiceImpl extends BaseServiceImpl<SysLog> implements LogService {

    @Autowired
    private MybatisDao mybatisDao;

    static final String CACHE_PERMISSION_NAME_PATH_MAP = "permissionNamePathMap";

    @Override
    public String save(SysLog object) throws Exception {
        return null;
    }

    /**
     * 保存日志
     *
     * @param request HttpServletRequest
     * @param title   日志标题
     * @throws Exception
     */
    @Override
    public void save(HttpServletRequest request, String title) throws Exception {
        save(request, null, null, title);
    }

    /**
     * 保存日志
     *
     * @param request HttpServletRequest
     * @param handler Object
     * @param ex      Exception
     * @param title   日志标题
     * @throws Exception
     */
    @Override
    public void save(HttpServletRequest request, Object handler, Exception ex, String title) throws Exception {
        SysUser user = UserUtils.getUser();
        if (user != null && user.getId() != null) {
            SysLog log = new SysLog();
            log.setTitle(title);
            log.setType(ex == null ? SysLog.TYPE_ACCESS : SysLog.TYPE_EXCEPTION);
            log.setRemoteAddr(StringUtils.getRemoteAddr(request));
            log.setUserAgent(request.getHeader("user-agent"));
            log.setRequestUri(request.getRequestURI());
            log.setParams(request.getParameterMap());
            log.setMethod(request.getMethod());
            // 异步保存日志
            new SaveLogThread(log, handler, ex).start();
        }
    }

    /**
     * 保存日志线程
     */
    private class SaveLogThread extends Thread {

        private SysLog log;
        private Object handler;
        private Exception ex;

        SaveLogThread(SysLog log, Object handler, Exception ex) {
            super(SaveLogThread.class.getSimpleName());
            this.log = log;
            this.handler = handler;
            this.ex = ex;
        }

        @Override
        public void run() {
            // 获取日志标题
            if (StringUtils.isBlank(log.getTitle())) {
                String permission = "";
                if (handler instanceof HandlerMethod) {
                    Method m = ((HandlerMethod) handler).getMethod();
                    RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
                    permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
                }
                log.setTitle(getMenuNamePath(log.getRequestUri(), permission));
            }
            // 如果有异常，设置异常信息
            String exception = Exceptions.getStackTraceAsString(ex);
            if (exception != null) {
                exception = exception.replaceAll("'", "\"");
            }
            log.setException(exception);
            // 如果无标题并无异常日志，则不保存信息
            if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())) {
                return;
            }
            // 保存日志信息
            try {
                mybatisDao.insert(log);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
     *
     * @param requestUri 请求URL
     * @param permission 权限
     * @return 菜单路径
     */
    private String getMenuNamePath(String requestUri, String permission) {
        String href = StringUtils.substringAfter(requestUri, "");
        href = href.substring(1, href.length());
        @SuppressWarnings("unchecked")
        Map<String, String> permissionMap = (Map<String, String>) JedisUtils.getObject(CACHE_PERMISSION_NAME_PATH_MAP);
        if (permissionMap == null) {
            permissionMap = Maps.newHashMap();
            List<SysPermission> permissions = mybatisDao.selectListByConditions(SysPermission.class, "");
            for (SysPermission bean : permissions) {
                // 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
                String namePath = "";
                if (bean.getParentIds() != null) {
                    List<String> namePathList = Lists.newArrayList();
                    for (String id : StringUtils.split(bean.getParentIds(), ",")) {
                        for (SysPermission m : permissions) {
                            if (m.getId() == Integer.valueOf(id).intValue()) {
                                namePathList.add(m.getPermissionName());
                                break;
                            }
                        }
                    }
                    namePathList.add(bean.getPermissionName());
                    namePath = StringUtils.join(namePathList, "-");
                }
                // 设置菜单名称路径
                if (StringUtils.isNotBlank(bean.getFunUrl())) {
                    permissionMap.put(bean.getFunUrl(), namePath);
                } else if (StringUtils.isNotBlank(bean.getPermissionSign())) {
                    for (String p : StringUtils.split(bean.getPermissionSign())) {
                        permissionMap.put(p, namePath);
                    }
                }
                JedisUtils.setObject(CACHE_PERMISSION_NAME_PATH_MAP, permissionMap, 0);
            }
        }
        String menuNamePath = permissionMap.get(href);
        if (menuNamePath == null) {
            for (String p : StringUtils.split(permission)) {
                menuNamePath = permissionMap.get(p);
                if (StringUtils.isNotBlank(menuNamePath)) {
                    break;
                }
            }
            if (menuNamePath == null) {
                return "";
            }
        }
        return menuNamePath;
    }

    /**
     * 删除日志信息
     *
     * @param ids 删除的ID
     * @throws Exception
     */
    @Override
    public int delete(String ids) throws Exception {
        return super.delete(ids);
    }

    /**
     * 清空所有日志信息
     */
    @Override
    public void empty() {
        mybatisDao.deleteBySql("DELETE FROM sys_log", null);
    }
}
