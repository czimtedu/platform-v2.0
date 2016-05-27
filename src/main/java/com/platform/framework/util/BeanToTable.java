/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.framework.util;

/**
 * bean与表之前的属性转换
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class BeanToTable {

	/**
	 * 名称: beanToTable 
	 * 描述: 驼峰式转换为下划线格式 (如：sysUser->sys_user,SysUser->sys_user)
	 * 作者：lufengcheng
	 * @param name
	 * @return
	 */
	public static String beanToTable(String name) {
		return name.replaceAll(String.format("%s|%s|%s",
				"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), "_").toLowerCase();
	}

	/**
	 * 名称: tableNameToClassName 
	 * 描述: 表名转类名 （下划线格式装驼峰式，第一个字母大写，如：sys_user->SysUser）
	 * 作者：lufengcheng
	 * @param name
	 * @return
	 */
	public static String tableNameToClassName(String name) {
		String[] split = name.split("_");
    	StringBuilder className = new StringBuilder();
    	String firstChar = "";
    	for (String str : split) {
    		firstChar = str.charAt(0) + "";
    		className.append(firstChar.toUpperCase() + str.substring(1));
		}
    	return className.toString();
	}
	
	/**
	 * 名称: tableFieldToClassProperty 
	 * 描述: 表字段转类属性（下划线格式装驼峰式，第一个字母小写,如：sys_user->sysUser）
	 * 作者：lufengcheng
	 * @param name
	 * @return
	 */
	public static String tableFieldToClassProperty(String name) {
		String[] split = name.split("_");
    	StringBuilder className = new StringBuilder();
    	String firstChar = "";
    	for (String str : split) {
    		firstChar = str.charAt(0) + "";
    		className.append(firstChar.toUpperCase() + str.substring(1));
		}
    	firstChar = className.charAt(0) + "";
    	return firstChar.toLowerCase() + className.substring(1);
	}
	
}
