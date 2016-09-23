/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.bean;

import com.platform.modules.sys.bean.NoDbColumn;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 留言Entity
 *
 * @author ThinkGem
 * @version 2013-05-15
 */
public class CmsGuestbook implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String type;    // 留言分类（咨询、建议、投诉、其它）
    private String content; // 留言内容
    private String name;    // 姓名
    private String email;    // 邮箱
    private String phone;    // 电话
    private String workunit;// 单位
    private String ip;        // 留言IP
    private Date createDate;// 留言时间
    private Integer reUserId; // 回复人
    private Date reDate;    // 回复时间
    private String reContent;// 回复内容
    private String delFlag;    // 删除标记删除标记（0：正常；1：删除；2：审核）

    @NoDbColumn
    private String reUserName;

    public CmsGuestbook() {
        this.delFlag = "0";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Length(min = 1, max = 100)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Length(min = 1, max = 2000)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Length(min = 1, max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Email
    @Length(min = 0, max = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(min = 0, max = 100)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(min = 0, max = 100)
    public String getWorkunit() {
        return workunit;
    }

    public void setWorkunit(String workunit) {
        this.workunit = workunit;
    }

    @Length(min = 1, max = 100)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getReUserId() {
        return reUserId;
    }

    public void setReUserId(Integer reUserId) {
        this.reUserId = reUserId;
    }

    public String getReContent() {
        return reContent;
    }

    public void setReContent(String reContent) {
        this.reContent = reContent;
    }

    public Date getReDate() {
        return reDate;
    }

    public void setReDate(Date reDate) {
        this.reDate = reDate;
    }

    @Length(min = 1, max = 1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public void setReUserName(String reUserName) {
        this.reUserName = reUserName;
    }

    public String getReUserName() {
        return reUserName;
    }
}


