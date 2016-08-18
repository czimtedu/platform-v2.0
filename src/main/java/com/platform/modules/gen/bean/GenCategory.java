/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.gen.bean;

import com.platform.modules.sys.bean.SysDict;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 生成方案Entity
 * @author ThinkGem
 * @version 2013-10-15
 */
@XmlRootElement(name="cgory")
public class GenCategory extends SysDict {
	
	private static final long serialVersionUID = 1L;
	private List<String> template;			// 主表模板
	
	public static String CATEGORY_REF = "category-ref:";

	public GenCategory() {
		super();
	}

	@XmlElement(name = "template")
	public List<String> getTemplate() {
		return template;
	}

	public void setTemplate(List<String> template) {
		this.template = template;
	}

}


