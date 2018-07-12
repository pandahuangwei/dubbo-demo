package com.simple.common.persistence.mybatis.dialect;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: MySql5Dialect.java.
 * @Package com.simple.common.persistence.mybatis.dialect.
 * @Description: MySql5Dialect.
 * 
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/1 18:04.
 **********************************************************************************/
public class MySql5Dialect extends Dialect {
	protected static final String SQL_END_DELIMITER = ";";

	public String getLimitString(String sql, boolean hasOffset) {
		return MySql5PageHepler.getLimitString(sql, -1, -1);
	}

	public String getLimitString(String sql, int offset, int limit) {
		return MySql5PageHepler.getLimitString(sql, offset, limit);
	}

	public boolean supportsLimit() {
		return true;
	}
}