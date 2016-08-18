/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.orm.dialect;

/**
 * MSSQL 数据库方言
 * 
 * @author lfc
 * @since 2015-06-16
 **/
public class MSDialect extends Dialect {

    protected static final String SQL_END_DELIMITER = ";";

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return MSPageHepler.getLimitString(sql, offset, limit);
    }

    @Override
    public String getCountString(String sql) {
        return MSPageHepler.getCountString(sql);
    }
}
