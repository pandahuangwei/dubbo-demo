/**************************************************************************/
/*                                                                        */
/*                 Copyright (c) 2015-2020  Dcits Company             　　*/
/*                         神州数码有限公司版权所有 2015-2020                      */
/*                                                                        */
/* PROPRIETARY RIGHTS of Simple Company are involved in the  　　　　　　 */
/* subject matter of this material.  All manufacturing, reproduction, use,*/
/* and sales rights pertaining to this subject matter are governed by the */
/* license agreement.  The recipient of this software implicitly accepts  */
/* the terms of the license.                                              */
/*                本软件文档资料是神州数码公司的资产,任何人士阅读和使用本资料                                                             */
/*                必须获得相应的书面授权,承担保密责任和接受相应的法律约束.                   */
/*                                                                        */
/**************************************************************************/

/*
 * System Abbrev :
 * System Name   :
 * Component No  :
 * Component Name：
 * File name     : Pagination.java
 * Author        : yushaocan
 * Date          : 2015年4月9日
 * Description   :  <description>
 */

/* Updation record 1:
 * Updation date   :  2015年4月9日
 * Updator         :  yushaocan
 * Trace No        :  <Trace No>
 * Updation No     :  <Updation No>
 * Updation Content:  <List all contents of updation and all methods updated.>
 */
package com.simple.common.persistence.page;

import java.io.Serializable;
import java.util.List;

/**
 * <Description functions in a word> <Detail description>
 * 
 * @author yushaocan
 * @version [Version NO, 2015年4月9日]
 * @see [Related classes/methods]
 * @since [product/Module version]
 */
public class Pagination implements Serializable {
	private int pageNo;
	private int pageSize;
	private int totalCount;
	private List list;

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Pagination() {
	}

	public Pagination(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public List getList() {
		return this.list;
	}

	public void setList(List list) {
		this.list = list;
	}
}
