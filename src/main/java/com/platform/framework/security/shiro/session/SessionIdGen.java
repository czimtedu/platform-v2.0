/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.security.shiro.session;

import java.io.Serializable;
import java.util.UUID;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

/**
 * sessionId 生成器
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class SessionIdGen implements SessionIdGenerator {

	/** 
	 * 实现generateId方法
	 * @param session session
	 * @return Serializable
	 * @see org.apache.shiro.session.mgt.eis.SessionIdGenerator#generateId(org.apache.shiro.session.Session)
	 */
	@Override
	public Serializable generateId(Session session) {
		return UUID.randomUUID().toString();
	}

}
