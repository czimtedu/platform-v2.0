/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.common;

/**
 * 向客户端传递状态(客户端只接收状态)
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class Result extends BaseResult {

    private Object data;

    public Result() {
    }

    public Result(String code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public Result(String code, String message, Object data) {
        this.setCode(code);
        this.setMessage(message);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
