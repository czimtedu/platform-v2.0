/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */
package com.platform.framework.test.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.platform.framework.util.PropertiesLoader;
import com.platform.framework.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

/**
 * 描述： 
 * 类名：TableToBean
 * 作者：lufengcheng
 * 日期：2015-11-16 下午2:27:46
 * 版本：V0.1
 */
public class TableToBean {
	private final static String TEMPLATE_NAME = "entity.ftl";
    private static PropertiesLoader loader = new PropertiesLoader("application.properties");
    
    private static Map<String, String> map = Maps.newHashMap();
    public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}
    
    public static String tableName = "test";

    List<String> excludeField = new ArrayList<String>(){{
        add("createBy");
        add("createTime");
        add("updateBy");
        add("updateTime");
        add("status");
        add("type");
        add("description");
    }};

    @Test
    public void test(){
    	String driverClassName = getConfig("jdbc.driver");
		String url = getConfig("jdbc.url");
		String username = getConfig("jdbc.username");
		String password = getConfig("jdbc.password");
		
		String beanPath = "C:\\Users\\lufengc\\intellij\\platform\\src\\main\\java\\com\\platform\\common\\domain";
    	TableToBean bean = new TableToBean();
    	List<Attribute> attributes = bean.getAttributes(driverClassName, url, username, password, tableName);
    	try {
			boolean process = bean.process(attributes, tableName, beanPath);
			System.out.println(process);
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
    }
    
    public List<Attribute> getAttributes(String driverClassName, 
    		String url, String username, String password, String tableName) {
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		List<Attribute> attributes = new ArrayList<>();
		try {
			Class.forName(driverClassName);
			conn = DriverManager.getConnection(url, username, password);
			pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE ROW_COUNT()=1");
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			if (rsmd != null) {
				int colCount = rsmd.getColumnCount();
				String colName;
				String colType;
				for (int i = 1; i <= colCount; i++) {
					colName = rsmd.getColumnName(i).toLowerCase();
					colName = turnAttributeName(colName);
					colType = rsmd.getColumnTypeName(i).toLowerCase();
					colType = trunType(colType);
                    if(!excludeField.contains(colName)){
                        attributes.add(new Attribute(colName, colType));
                    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rsmd = null;
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return attributes;
	}
    
    public boolean process(List<Attribute> attributes, String tableName, 
    		String beanPath) throws IOException, TemplateException{  
    	if(attributes == null || attributes.size() == 0){
    		return false;
    	}
        Configuration configuration = new Configuration(new Version(2, 3, 22));
		String templatePath = System.getProperty("user.dir") + "\\src\\main\\resources\\template";
    	configuration.setDirectoryForTemplateLoading(new File(templatePath));  

        //多个逗号分割
        String imports = "com.platform.framework.common.BaseEntity";
        
        Map<String, Object> root = new HashMap<>();
        StringBuilder packageName = new StringBuilder();
        String[] split = beanPath.split("\\\\");
        int packageNameIndex = 0;
        for (int i = 0; i < split.length; i++) {
			if("java".equals(split[i])){
				packageNameIndex = i + 1;
				break;
			}
		}
        for (int i = packageNameIndex; i < split.length; i++) {
        	packageName.append(".").append(split[i]);
		}
        String className = turnClassName(tableName);
        root.put("packageName", packageName.substring(1));
        root.put("className", className);
        root.put("imports", imports.split(","));
        root.put("attributes", attributes);  
        
        Template template = configuration.getTemplate(TEMPLATE_NAME);  
        Writer out = new BufferedWriter(new OutputStreamWriter(
        		new FileOutputStream(
        				new File(beanPath + "\\" + className + ".java")), "utf-8"));
        template.process(root, out);
        return true;
    }
    
    public static String trunType(String colType){
        switch (colType) {
            case "char":
            case "varchar":
            case "varchar2":
            case "text":
            case "nchar":
            case "nvarchar2":
            case "enum":
            case "set":
                colType = "String";
                break;
            case "int":
            case "integer":
            case "tinyint":
            case "smallint":
            case "mediumint":
            case "number":
                colType = "Integer";
                break;
            case "bigint":
            case "id":
                colType = "Long";
                break;
            case "float":
                colType = "Float";
                break;
            case "double":
                colType = "Double";
                break;
            case "bit":
                colType = "boolean";
                break;
            case "blob":
                colType = "byte[]";
                break;
            case "date":
                colType = "Date";
                break;
            default:
                colType = "String";
                break;
        }
    	return colType;
    }
    
    public static String turnClassName(String name){
    	String[] split = name.split("_");
    	StringBuilder className = new StringBuilder();
    	String firstChar;
    	for (String str : split) {
    		firstChar = str.charAt(0) + "";
    		className.append(firstChar.toUpperCase()).append(str.substring(1));
		}
    	return className.toString();
    }
    
    public static String turnAttributeName(String name){
    	String[] split = name.split("_");
    	StringBuilder className = new StringBuilder();
    	String firstChar;
    	for (String str : split) {
    		firstChar = str.charAt(0) + "";
    		className.append(firstChar.toUpperCase()).append(str.substring(1));
    	}
    	firstChar = className.charAt(0) + "";
    	
    	return firstChar.toLowerCase() + className.substring(1);
    }
}
