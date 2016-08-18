/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识bean中非数据列
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDbColumn {

}
