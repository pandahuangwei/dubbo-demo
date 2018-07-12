package com.simple.common.persistence.mybatis.interceptor;

import com.simple.common.exceptions.ApplicationException;
import com.simple.common.exceptions.BaseExceptionCode;
import com.simple.common.exceptions.BaseExceptionCodeString;
import com.simple.common.persistence.mybatis.dialect.*;
import com.simple.common.persistence.page.Pagination;
import com.simple.common.utils.ReflectHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: PaginationInterceptor.java.
 * @Package com.simple.common.persistence.mybatis.interceptor.
 * @Description: PaginationInterceptor.
 * 
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/1 18:09.
 **********************************************************************************/
@Intercepts({ @org.apache.ibatis.plugin.Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
//@Intercepts({@Signature(type=Executor.class,method="query",args={ MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })})
public class PaginationInterceptor implements Interceptor {
	private static final Log log = LogFactory
			.getLog(PaginationInterceptor.class);

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		BoundSql boundSql = statementHandler.getBoundSql();

		MetaObject metaStatementHandler = SystemMetaObject
				.forObject(statementHandler);
		Configuration configuration = (Configuration) metaStatementHandler
				.getValue("delegate.configuration");
		Dialect.Type databaseType = null;
		try {
			databaseType = Dialect.Type.valueOf(configuration.getVariables()
					.getProperty("dialect").toUpperCase());
		} catch (Exception e) {
			log.error(
					"The value of the dialect property in configuration.xml is not defined : "
							+ configuration.getVariables().getProperty(
									"dialect") + ".", e);
			throw new ApplicationException(
					BaseExceptionCode.UTIL_PAGINATION_INTERCEPTOR,
					BaseExceptionCodeString.UTIL_PAGINATION_INTERCEPTOR, e);
		}
		if (databaseType == null) {
			log.error("The value of the dialect property in configuration.xml is not defined : "
					+ configuration.getVariables().getProperty("dialect")
					+ ".{error reason:databaseType is null.}");
			throw new ApplicationException(
					BaseExceptionCode.UTIL_PAGINATION_INTERCEPTOR,
					BaseExceptionCodeString.UTIL_PAGINATION_INTERCEPTOR);
		}

		/*if (log.isInfoEnabled()) {
			String sql = createDebugSql(boundSql, databaseType);
			StringBuffer sb = new StringBuffer(
					"生成SQL : \r\n-------------------------------------------------------\r\n");
			sb.append(sql)
					.append("\r\n-------------------------------------------------------");
			log.debug(sb);
		}*/

		RowBounds rowBounds = (RowBounds) metaStatementHandler
				.getValue("delegate.rowBounds");
		if ((rowBounds == null) || (rowBounds == RowBounds.DEFAULT)) {
			return invocation.proceed();
		}

		Dialect dialect = null;
		switch (databaseType) {
		case MYSQL:
			dialect = new MySql5Dialect();
			break;
		case ORACLE:
			dialect = new OracleDialect();
			break;
		case POSTGRESQL:
			dialect = new PostgresqlDialect();
			break;
		default:
			return invocation.proceed();
		}

		String originalSql = (String) metaStatementHandler
				.getValue("delegate.boundSql.sql");
		metaStatementHandler.setValue("delegate.boundSql.sql", dialect
				.getLimitString(originalSql, rowBounds.getOffset(),
						rowBounds.getLimit()));
		metaStatementHandler.setValue("delegate.rowBounds.offset",
				Integer.valueOf(0));
		metaStatementHandler.setValue("delegate.rowBounds.limit",
				Integer.valueOf(2147483647));

		if ((rowBounds instanceof PaginationRowBounds)) {
			Connection connection = (Connection) invocation.getArgs()[0];
			String countSql = "select count(0) from (" + originalSql
					+ ")  tmp_count";
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
					.getValue("delegate.mappedStatement");
			PreparedStatement countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),
					countSql, boundSql.getParameterMappings(),
					boundSql.getParameterObject());

			setParameters(countStmt, mappedStatement, countBS,
					boundSql.getParameterObject());
			ResultSet rs = countStmt.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			countStmt.close();
			Pagination pt = ((PaginationRowBounds) rowBounds).getPagination();
			pt.setTotalCount(count);
		}

		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
	}

	private String createDebugSql(BoundSql boundSql, Dialect.Type dbType)
			throws Exception {
		List<ParameterMapping> lst = boundSql.getParameterMappings();
		String sql = boundSql.getSql();
		Object paramObj = boundSql.getParameterObject();

		if (paramObj == null)
			return sql;
		if ((isNormalType(paramObj)) || ((paramObj instanceof String))
				|| ((paramObj instanceof Date)))
			sql = replaceParam(sql, paramObj, dbType);
		else if ((paramObj instanceof Map)) {
			if (lst != null)
				for (ParameterMapping m : lst) {
					Object o = ((Map) paramObj).get(m.getProperty());
					sql = replaceParam(sql, o, dbType);
				}
		} else {
			for (ParameterMapping m : lst) {
				Object o = ReflectHelper.getValueByFieldName(paramObj,
						m.getProperty());
				sql = replaceParam(sql, o, dbType);
			}
		}
		sql = sql.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1");
		return sql;
	}

	private String replaceParam(String sql, Object param, Dialect.Type dbType)
			throws Exception {
		if (param == null)
			return sql.replaceFirst("\\?", "''");
		if (isNormalType(param))
			return sql.replaceFirst("\\?", param.toString());
		if ((param instanceof String))
			return sql.replaceFirst("\\?", "'" + (String) param + "'");
		if ((param instanceof Date)) {
			if (dbType.equals(Dialect.Type.ORACLE)) {
				return sql.replaceFirst(
						"\\?",
						"to_date('"
								+ dateToString((Date) param,
										"yyyy-MM-dd HH:mm:ss")
								+ "','yyyy-mm-dd hh24:mi:ss')");
			}
			return sql.replaceFirst(
					"\\?",
					"'"
							+ dateToString((Date) param,
									"yyyy-MM-dd HH:mm:ss") + "'");
		}

		return sql;
	}

	private String dateToString(Date forDate, String format)
			throws Exception {
		try {
			if (forDate == null) {
				return null;
			}
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(forDate);
		} catch (Exception e) {
			log.error("DateToString failure.{date=" + forDate + ",format="
					+ format + ";}", e);
			throw new Exception("DateToString failure.{date=" + forDate
					+ ",format=" + format + ";}", e);
		}

	}
	private boolean isNormalType(Object obj) {
		if (obj == null) {
			return false;
		}

		return ((obj instanceof Long)) || ((obj instanceof Integer))
				|| ((obj instanceof Byte)) || ((obj instanceof Short))
				|| ((obj instanceof Float)) || ((obj instanceof Double))
				|| ((obj instanceof Boolean));
	}

	private void setParameters(PreparedStatement ps,
			MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters")
				.object(mappedStatement.getParameterMap().getId());
		List parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration
					.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null
					: configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = (ParameterMapping) parameterMappings
						.get(i);
				if (parameterMapping.getMode() == ParameterMode.OUT)
					continue;
				String propertyName = parameterMapping.getProperty();
				PropertyTokenizer prop = new PropertyTokenizer(propertyName);
				Object value;
				if (parameterObject == null) {
					value = null;
				} else {
					if (typeHandlerRegistry.hasTypeHandler(parameterObject
							.getClass())) {
						value = parameterObject;
					} else {
						if (boundSql.hasAdditionalParameter(propertyName)) {
							value = boundSql
									.getAdditionalParameter(propertyName);
						} else if ((propertyName.startsWith("__frch_"))
								&& (boundSql.hasAdditionalParameter(prop
										.getName()))) {
							value = boundSql.getAdditionalParameter(prop
									.getName());
							if (value != null)
								value = configuration.newMetaObject(value)
										.getValue(
												propertyName.substring(prop
														.getName().length()));
						} else {
							value = metaObject == null ? null : metaObject
									.getValue(propertyName);
						}
					}
				}
				TypeHandler typeHandler = parameterMapping.getTypeHandler();
				if (typeHandler == null) {
					throw new ExecutorException(
							"There was no TypeHandler found for parameter "
									+ propertyName + " of statement "
									+ mappedStatement.getId());
				}
				typeHandler.setParameter(ps, i + 1, value,
						parameterMapping.getJdbcType());
			}
		}
	}
}
