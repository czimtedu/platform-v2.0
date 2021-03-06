/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.sys.bean;

import com.platform.framework.common.BaseEntity;

import java.util.List;

/**
 * 机构Entity
 * @author lufengc
 * @version 2016-08-31
 */
public class SysOffice extends BaseEntity<SysOffice> {

	private static final long serialVersionUID = 1L;
	private String id;
	private String parentId;    // 父级编号
	private String parentIds;   // 所有父级编号
	private String areaId;	// 归属区域
	private String code; 	// 机构编码
	private String name; 	// 机构名称
	private Integer sort;	// 排序
	private Integer type; 	// 机构类型（1：公司；2：部门；3：小组）
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String master; 	// 负责人
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String email; 	// 邮箱
	private String useable; //是否可用
	private Integer primaryPerson;  //主负责人
	private Integer deputyPerson;   //副负责人
	private List<String> childDeptList; //快速添加子部门

    @NoDbColumn
    private String parentName;
    @NoDbColumn
    private String areaName;
    @NoDbColumn
    private String deputyPersonName;
    @NoDbColumn
    private String primaryPersonName;

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

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public Integer getType() {
        return type;
    }

    @Override
    public void setType(Integer type) {
        this.type = type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUseable() {
        return useable;
    }

    public void setUseable(String useable) {
        this.useable = useable;
    }

    public Integer getPrimaryPerson() {
        return primaryPerson;
    }

    public void setPrimaryPerson(Integer primaryPerson) {
        this.primaryPerson = primaryPerson;
    }

    public Integer getDeputyPerson() {
        return deputyPerson;
    }

    public void setDeputyPerson(Integer deputyPerson) {
        this.deputyPerson = deputyPerson;
    }

    public List<String> getChildDeptList() {
        return childDeptList;
    }

    public void setChildDeptList(List<String> childDeptList) {
        this.childDeptList = childDeptList;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setDeputyPersonName(String deputyPersonName) {
        this.deputyPersonName = deputyPersonName;
    }

    public String getDeputyPersonName() {
        return deputyPersonName;
    }

    public void setPrimaryPersonName(String primaryPersonName) {
        this.primaryPersonName = primaryPersonName;
    }

    public String getPrimaryPersonName() {
        return primaryPersonName;
    }
}