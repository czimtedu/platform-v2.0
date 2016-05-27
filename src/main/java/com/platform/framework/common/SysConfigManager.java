/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */
package com.platform.framework.common;

import com.google.common.collect.Maps;
import com.platform.framework.util.PropertiesLoader;
import com.platform.framework.util.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 系统配置管理类,负责系统整个配置的读取、缓存、修改
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class SysConfigManager {

    private SysConfigManager() {}

    private static class SysConfigManagerLoader {
        private static final SysConfigManager instance = new SysConfigManager();
    }

    public static SysConfigManager getInstance() {
        return SysConfigManagerLoader.instance;
    }

    //保存全局属性值
    private static Map<String, String> map = Maps.newHashMap();

    //属性文件加载对象
    private static PropertiesLoader loader = new PropertiesLoader("application.properties", "messages.properties");

    //系统部署路径，其它功能模板调用该属性获取当前部署目录
    private static String sysRootPath;

    /**
     * 获取当前系统部署目录
     *
     * @return 路径
     */
    public static String getSysRootPath() {
        if (sysRootPath == null) {
            sysRootPath = System.getProperty("platform.root");
        }
        return sysRootPath;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * 获取上传文件的根目录
     * @return path
     */
    public static String getFileUploadPath() {
        String dir = getConfig("file.path.upload");
        if (StringUtils.isBlank(dir)){
            dir = getSysRootPath() + "/uploads/";
        }
        return dir;
    }

    /**
     * 获取文件服务器地址
     * @return url
     */
    public static String getFileAccessPath() {
        String url = getConfig("file.path.access");
        if (StringUtils.isBlank(url)){
            url = "/";
        }
        return url;
    }

    /**
     * 获取管理端根路径
     */
    public static String getAdminPath() {
        String path = getConfig("adminPath");
        if (StringUtils.isBlank(path)){
            path = "/";
        }
        return path;
    }

    /**
     * 获取前端根路径
     */
    public static String getFrontPath() {
        String path = getConfig("frontPath");
        if (StringUtils.isBlank(path)){
            path = "/";
        }
        return path;
    }

    /**
     * 获取URL后缀
     */
    public static String getUrlSuffix() {
        return getConfig("urlSuffix");
    }

    /**
     * 获取工程路径
     * @return 路径
     */
    public static String getProjectPath(){
        // 如果配置了工程路径，则直接返回，否则自动获取。
        String projectPath = getConfig("projectPath");
        if (StringUtils.isNotBlank(projectPath)){
            return projectPath;
        }
        try {
            File file = new DefaultResourceLoader().getResource("").getFile();
            if (file != null){
                while(true){
                    File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
                    if (f.exists()){
                        break;
                    }
                    if (file.getParentFile() != null){
                        file = file.getParentFile();
                    }else{
                        break;
                    }
                }
                projectPath = file.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectPath;
    }

    /**
     * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
     */
    public static boolean isDemoMode() {
        String dm = getConfig("demoMode");
        return "true".equals(dm) || "1".equals(dm);
    }

    /**
     * 获取Key加载信息
     */
    public static boolean printKeyLoadMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n======================================================================\r\n");
        sb.append("\r\n    欢迎使用 " + getConfig("productName") + " @"
                + getConfig("copyrightYear") + "  - Powered By http://www.bjldwx.com.cn\r\n");
        sb.append("\r\n======================================================================\r\n");
        System.out.println(sb.toString());
        return true;
    }
}

