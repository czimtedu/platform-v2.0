/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.cms.bean;

import com.platform.framework.common.BaseEntity;

import java.util.Date;

/**
 * 链接Entity
 *
 * @author ThinkGem
 * @version 2013-05-15
 */
public class CmsLink extends BaseEntity<CmsLink> {

    private static final long serialVersionUID = 1L;
    private String id;
    private String categoryId;// 分类编号
    private String title;    // 链接名称
    private String color;    // 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
    private String image;    // 链接图片
    private String href;    // 链接地址
    private Integer weight;    // 权重，越大越靠前
    private Date weightDate;// 权重期限，超过期限，将weight设置为0

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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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
}