package com.simple.common.persistence.mybatis.dialect;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: PostgresqlDialect.java.
 * @Package com.simple.common.persistence.mybatis.dialect.
 * @Description: PostgresqlDialect.
 *
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/1 18:09.
 **********************************************************************************/
public class PostgresqlDialect extends Dialect {
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		boolean isForUpdate = false;
		if (sql.toLowerCase().endsWith(" for update")) {
			sql = sql.substring(0, sql.length() - 11);
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer(sql.length());

		pagingSelect.append(sql);

		pagingSelect.append(" limit " + limit + " offset " + offset + " ");

		if (isForUpdate) {
			pagingSelect.append(" for update");
		}

		return pagingSelect.toString();
	}
}
