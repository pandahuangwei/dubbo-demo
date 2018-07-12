package com.simple.common.persistence.mybatis.dialect;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: Dialect.java.
 * @Package com.simple.common.persistence.mybatis.dialect.
 * @Description: 数据库类型定义.
 * 
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/1 17:58.
 **********************************************************************************/
public abstract class Dialect {
	public abstract String getLimitString(String paramString, int paramInt1,
			int paramInt2);

	public static enum Type {
		MYSQL, ORACLE, POSTGRESQL, SQLSERVER, SQLSERVER2008;
	}
}
