package com.simple.common.persistence.mybatis.dialect;

import com.simple.common.persistence.page.Pagination;

import org.apache.ibatis.session.RowBounds;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: PaginationRowBounds.java.
 * @Package com.simple.common.persistence.mybatis.dialect.
 * @Description: PaginationRowBounds.
 * 
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/1 18:09.
 **********************************************************************************/
public class PaginationRowBounds extends RowBounds {
	private Pagination pagination;

	public Pagination getPagination() {
		return this.pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public PaginationRowBounds(int offset, int limit) {
		super(offset, limit);
	}
}
