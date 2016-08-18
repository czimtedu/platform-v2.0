/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.orm.dialect;

/**
 * 数据库方言抽象类
 * 
 * @author lfc
 * @since 2015-06-16 09:44:17
 **/
public abstract class Dialect {

    /**
     * 得到分页sql
     * 
     * @param sql
     * @param offset
     * @param limit
     * @return
     */
    public abstract String getLimitString(String sql, int offset, int limit);

    /**
     * 得到总数量 sql
     * 
     * @param sql
     * @return
     */
    public abstract String getCountString(String sql);

}
