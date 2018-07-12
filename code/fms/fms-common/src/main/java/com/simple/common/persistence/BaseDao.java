package com.simple.common.persistence;

import com.simple.common.persistence.db.MybatisTplDao;

import javax.annotation.Resource;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.19 9:53.
 */
public class BaseDao {
    @Resource(name = "mybatisTplDao")
    protected MybatisTplDao dao;

    /**
     * 构建查询sql
     *
     * @param nameSpace 命名空间
     * @param sqlId     sqlId
     * @return nameSpace.sqlId
     */
    protected String getStatement(String nameSpace, String sqlId) {
        StringBuffer sb = new StringBuffer();
        sb.append(nameSpace).append(".").append(sqlId);
        return sb.toString();
    }

    /**
     * 获取Mapper命名空间.<br>
     * 使用当前类路径当命名空间 (Eg:com.*.*DaoImpl)
     *
     * @param sqlId sqlId
     * @return com.*.*DaoImpl.sqlId
     */
    protected String getStatement(String sqlId) {
        String name = this.getClass().getName();
        StringBuffer sb = new StringBuffer();
        sb.append(name).append(".").append(sqlId);
        String statement = sb.toString();
        return statement;
    }
}
