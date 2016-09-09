/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
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
    private String companyId;
    private String officeId;
    private String username;
    private String realName;
    private String mobile;
    private String email;
    private String photo;
    private String password;
    private Integer deviceId;
    private String token;
    private String loginIp;
    private Date loginTime;
    @NoDbColumn
    private List<Integer> roleIdList;
    @NoDbColumn
    private List<SysRole> roleList = Lists.newArrayList();
    @NoDbColumn
    private String officeName;
    @NoDbColumn
    private String companyName;


    @ExcelField(title = "ID", type = 1, align = 2, sort = 1)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
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

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
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

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }
}