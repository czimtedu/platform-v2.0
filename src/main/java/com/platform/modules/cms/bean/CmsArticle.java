/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.bean;

import com.google.common.collect.Lists;
import com.platform.modules.sys.bean.NoDbColumn;
import com.platform.modules.sys.bean.SysLog;
import com.platform.framework.common.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 系统日志bean
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class CmsArticle extends BaseEntity<CmsArticle> {

    private static final long serialVersionUID = 1L;
    private String id;
    private String categoryId;// 分类编号
    private String title;    // 标题
    private String link;    // 外部链接
    private String color;    // 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
    private String image;    // 文章图片
    private String keywords;// 关键字
    private Integer weight;    // 权重，越大越靠前
    private Date weightDate;// 权重期限，超过期限，将weight设置为0
    private Integer hits;    // 点击数
    private String posid;    // 推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）
    private String customContentView;    // 自定义内容视图
    private String viewConfig;    // 视图参数
    private String author; // 作者

    @NoDbColumn
    private CmsArticleData cmsArticleData;
    @NoDbColumn
    public static final String DEFAULT_TEMPLATE = "frontViewArticle";
    @NoDbColumn
    private String categoryName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Date getWeightDate() {
        return weightDate;
    }

    public void setWeightDate(Date weightDate) {
        this.weightDate = weightDate;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public String getPosid() {
        return posid;
    }

    public void setPosid(String posid) {
        this.posid = posid;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public CmsArticleData getCmsArticleData() {
        return cmsArticleData;
    }

    public void setCmsArticleData(CmsArticleData cmsArticleData) {
        this.cmsArticleData = cmsArticleData;
    }

    public List<String> getPosidList() {
        List<String> list = Lists.newArrayList();
        if (posid != null) {
            Collections.addAll(list, StringUtils.split(posid, ","));
        }
        return list;
    }

    public void setPosidList(List<String> list) {
        posid = "," + StringUtils.join(list, ",") + ",";
    }
}
