/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.framework.test.common;

public class Attribute {
	private String name;
	private String type;
	
	public Attribute(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
