package com.simple.common.persistence.db.impl;

import com.simple.common.persistence.db.MybatisTplDao;
import com.simple.common.persistence.mybatis.dialect.PaginationRowBounds;
import com.simple.common.persistence.page.Pagination;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: MybatisTplDaoImpl.java.
 * @Package com.simple.common.persistence.db.impl.
 * @Description: dao操作模板类：在Dao层引用.
 *                      @Resource(name = "mybatisTplDao")
 *                      private MybatisTplDao dao;
 *
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/1 17:56.
 **********************************************************************************/
@Repository("mybatisTplDao")
public class MybatisTplDaoImpl implements MybatisTplDao {
	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public int insert(String sqlId) {
		
		return this.sqlSessionTemplate.insert(sqlId);
	}

	public int insert(String sqlId, Object parameter) {
		
		return this.sqlSessionTemplate.insert(sqlId, parameter);
	}

	public int update(String sqlId) {
		
		return this.sqlSessionTemplate.update(sqlId);
	}

	public int update(String sqlId, Object parameter) {
		
		return this.sqlSessionTemplate.update(sqlId, parameter);
	}

	public int delete(String sqlId) {
		
		return this.sqlSessionTemplate.delete(sqlId);
	}

	public int delete(String sqlId, Object parameter) {
		
		return this.sqlSessionTemplate.delete(sqlId, parameter);
	}

	public <E> List<E> findList(String sqlId) {
		
		return this.sqlSessionTemplate.selectList(sqlId);
	}

	public <E> List<E> findList(String sqlId, Object parameter) {
		return this.sqlSessionTemplate.selectList(sqlId, parameter);
	}

	public <E> List<E> queryForPagination(int pageNo, int pageSize, String sqlId) {
		if (pageNo > 0) {
			pageNo--;
		}
		RowBounds s = new RowBounds(pageNo * pageSize, pageSize);
		return this.sqlSessionTemplate.selectList(sqlId, null, s);
	}

	public <E> List<E> queryForPagination(int pageNo, int pageSize,
			String sqlId, Object parameter) {
		if (pageNo > 0) {
			pageNo--;
		}
		RowBounds s = new RowBounds(pageNo * pageSize, pageSize);
		return this.sqlSessionTemplate.selectList(sqlId, parameter, s);
	}

	public Pagination queryForPaginationExt(int pageNo, int pageSize,
			String sqlId) {
		return queryForPaginationExt(pageNo, pageSize, sqlId, null);
	}

	public Pagination queryForPaginationExt(int pageNo, int pageSize,
			String sqlId, Object parameter) {
		if (pageNo > 0) {
			pageNo--;
		}
		Pagination p = new Pagination();

		PaginationRowBounds s = new PaginationRowBounds(pageNo * pageSize,pageSize);
		s.setPagination(p);
		List rtn = this.sqlSessionTemplate.selectList(sqlId, parameter, s);
		p.setList(rtn);

		return p;
	}

	public <K, V> Map<K, V> queryForMap(String sqlId) {
		return (Map) this.sqlSessionTemplate.selectOne(sqlId);
	}

	public <K, V> Map<K, V> queryForMap(String sqlId, Object parameter) {
		return (Map) this.sqlSessionTemplate.selectOne(sqlId, parameter);
	}

	public <T> T findOne(String sqlId) {
		
		return this.sqlSessionTemplate.selectOne(sqlId);
	}

	public <T> T findOne(String sqlId, Object parameter) {
		
		return this.sqlSessionTemplate.selectOne(sqlId, parameter);
	}
}
