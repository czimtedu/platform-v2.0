/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.framework.common;

/**
 * 向客户端传递状态(客户端只接收状态)
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class ErrorResult extends BaseResult {

    public ErrorResult(String code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }
}
