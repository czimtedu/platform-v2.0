/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.cms.bean;

import com.google.common.collect.Lists;
import com.platform.framework.common.BaseEntity;
import com.platform.framework.common.Global;
import com.platform.modules.cms.utils.CmsUtils;
import com.platform.modules.sys.bean.NoDbColumn;
import com.platform.modules.sys.bean.SysLog;
import com.sun.org.apache.xerces.internal.util.Status;

import java.util.Date;
import java.util.List;

/**
 * 栏目Entity
 *
 * @author ThinkGem
 * @version 2013-05-15
 */
public class CmsCategory extends BaseEntity<CmsCategory> {

    private static final long serialVersionUID = 1L;
    private String id;
    private String parentId;// 父级菜单
    private String parentIds;// 所有父级编号
    private String siteId;        // 归属站点
    private String officeId;    // 归属部门
    private String module;    // 栏目模型（article：文章；picture：图片；download：下载；link：链接；special：专题）
    private String name;    // 栏目名称
    private String image;    // 栏目图片
    private String href;    // 链接
    private String target;    // 目标（ _blank、_self、_parent、_top）
    private String description;    // 描述，填写有助于搜索引擎优化
    private String keywords;    // 关键字，填写有助于搜索引擎优化
    private Integer sort;        // 排序（升序）
    private String inMenu;        // 是否在导航中显示（1：显示；0：不显示）
    private String inList;        // 是否在分类页中显示列表（1：显示；0：不显示）
    private String showModes;    // 展现方式（0:有子栏目显示栏目列表，无子栏目显示内容列表;1：首栏目内容列表；2：栏目第一条内容）
    private String allowComment;// 是否允许评论
    private String isAudit;    // 是否需要审核
    private String customListView;        // 自定义列表视图
    private String customContentView;    // 自定义内容视图
    private String viewConfig;    // 视图参数

    @NoDbColumn
    public static final String DEFAULT_TEMPLATE = "frontList";
    @NoDbColumn
    private Date beginDate;	// 开始时间
    @NoDbColumn
    private Date endDate;	// 结束时间
    @NoDbColumn
    private String cnt;//信息量
    @NoDbColumn
    private String hits;//点击量
    @NoDbColumn
    private List<CmsCategory> childList = Lists.newArrayList();    // 拥有子分类列表

    @NoDbColumn
    private String parentName;
    @NoDbColumn
    private String officeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getInMenu() {
        return inMenu;
    }

    public void setInMenu(String inMenu) {
        this.inMenu = inMenu;
    }

    public String getInList() {
        return inList;
    }

    public void setInList(String inList) {
        this.inList = inList;
    }

    public String getShowModes() {
        return showModes;
    }

    public void setShowModes(String showModes) {
        this.showModes = showModes;
    }

    public String getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(String allowComment) {
        this.allowComment = allowComment;
    }

    public String getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(String isAudit) {
        this.isAudit = isAudit;
    }

    public String getCustomListView() {
        return customListView;
    }

    public void setCustomListView(String customListView) {
        this.customListView = customListView;
    }

    public String getCustomContentView() {
        return customContentView;
    }

    public void setCustomContentView(String customContentView) {
        this.customContentView = customContentView;
    }

    public String getViewConfig() {
        return viewConfig;
    }

    public void setViewConfig(String viewConfig) {
        this.viewConfig = viewConfig;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public List<CmsCategory> getChildList() {
        return childList;
    }

    public void setChildList(List<CmsCategory> childList) {
        this.childList = childList;
    }

    public static void sortList(List<CmsCategory> list, List<CmsCategory> sourcelist, String parentId) {
        for (int i = 0; i < sourcelist.size(); i++) {
            CmsCategory e = sourcelist.get(i);
            if (e.getParentId() != null && e.getParentId().equals(parentId)) {
                list.add(e);
                // 判断是否还有子节点, 有则继续获取子节点
                for (int j = 0; j < sourcelist.size(); j++) {
                    CmsCategory child = sourcelist.get(j);
                    if (child.getParentId() != null && child.getParentId().equals(e.getId())) {
                        sortList(list, sourcelist, e.getId());
                        break;
                    }
                }
            }
        }
    }

    public String getIds() {
        return (getParentIds() != null ? this.getParentIds().replaceAll(",", " ") : "")
                + (this.getId() != null ? this.getId() : "");
    }

    public boolean isRoot() {
        return isRoot(id);
    }

    public static boolean isRoot(String id) {
        return id != null && id.equals("1");
    }

    public String getUrl() {
        return CmsUtils.getUrlDynamic(this);
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }
}