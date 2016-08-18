/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.modules.sys.bean;

import com.google.common.collect.Lists;
import com.platform.framework.cache.DataCached;
import com.platform.framework.common.BaseEntity;
import com.platform.framework.util.Collections3;
import com.platform.framework.util.excel.ExcelField;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 系统用户bean
 *
 * @author lufengcheng
 * @date 2016-01-13 09:41:50
 */
@DataCached(type = DataCached.CachedType.REDIS_CACHED)
public class SysUser extends BaseEntity<SysUser> {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String realName;
    private String mobile;
    private String email;
    private String photo;
    private String password;
    private Integer deviceId;
    private String token;
    @NoDbColumn
    private List<Integer> roleIdList;
    @NoDbColumn
    private List<SysRole> roleList = Lists.newArrayList();


    @ExcelField(title = "ID", type = 1, align = 2, sort = 1)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Length(min = 1, max = 20, message = "用户名长度必须介于 1 和 20 之间")
    @ExcelField(title = "用户名", align = 2, sort = 30)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    @ExcelField(title = "姓名", align = 2, sort = 40)
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @ExcelField(title = "手机", align = 2, sort = 60)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @ExcelField(title = "邮箱", align = 1, sort = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @NotNull(message = "密码不能为空")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public List<Integer> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<Integer> roleIdList) {
        this.roleIdList = roleIdList;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }

    public String getRoleNames() {
        return Collections3.extractToString(roleList, "roleName", ",");
    }

    public boolean isAdmin() {
        return isAdmin(id);
    }

    public static boolean isAdmin(Integer id) {
        return id != null && id == 1;
    }
}