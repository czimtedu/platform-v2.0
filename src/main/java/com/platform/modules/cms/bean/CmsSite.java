/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.cms.bean;

import com.platform.framework.common.BaseEntity;
import com.platform.modules.sys.bean.NoDbColumn;
import com.platform.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 站点Entity
 *
 * @author lufengc
 * @version 2013-05-15
 */
public class CmsSite extends BaseEntity<CmsSite> {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;    // 站点名称
    private String title;    // 站点标题
    private String logo;    // 站点logo
    private String domain; //站点域名
    private String description;// 描述，填写有助于搜索引擎优化
    private String keywords;// 关键字，填写有助于搜索引擎优化
    private String theme;    // 主题
    private String copyright;// 版权信息
    private String customIndexView;// 自定义首页视图文件

    public CmsSite() {
    }

    public CmsSite(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCustomIndexView() {
        return customIndexView;
    }

    public void setCustomIndexView(String customIndexView) {
        this.customIndexView = customIndexView;
    }

    /**
     * 获取默认站点ID
     */
    public static String defaultSiteId() {
        return "1";
    }

    /**
     * 判断是否为默认（主站）站点
     */
    public static boolean isDefault(String id) {
        return id != null && id.equals(defaultSiteId());
    }

    /**
     * 获取当前编辑的站点编号
     */
    public static String getCurrentSiteId() {
        String siteId = (String) UserUtils.getCache("siteId");
        return StringUtils.isNotBlank(siteId) ? siteId : defaultSiteId();
    }

    /**
     * 模板路径
     */
    @NoDbColumn
    public static final String TPL_BASE = "/WEB-INF/views/modules/cms/front/themes";

    /**
     * 获得模板方案路径。如：/WEB-INF/views/modules/cms/front/themes/jeesite
     *
     * @return
     */
    public String getSolutionPath() {
        return TPL_BASE + "/" + getTheme();
    }

}