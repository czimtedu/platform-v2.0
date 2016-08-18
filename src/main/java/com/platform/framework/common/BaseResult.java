/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.common;

import com.platform.framework.util.DateUtils;
import com.platform.framework.util.StringUtils;

import java.util.Date;

/**
 * 客户端返回消息基类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public abstract class BaseResult {
    protected String code;
    protected String message;
    private String systime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSystime() {
        if(StringUtils.isEmpty(systime)){
            systime = DateUtils.formatDateTime(new Date());
        }
        return systime;
    }

    public void setSystime(String systime) {
        this.systime = systime;
    }
}
