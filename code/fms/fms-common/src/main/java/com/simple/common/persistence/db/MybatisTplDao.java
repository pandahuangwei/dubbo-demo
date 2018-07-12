package com.simple.common.persistence.db;

import com.simple.common.persistence.page.Pagination;

import java.util.List;
import java.util.Map;

public interface MybatisTplDao {
    int insert(String paramString);

    int insert(String paramString, Object paramObject);

    int update(String paramString);

    int update(String paramString, Object paramObject);

    int delete(String paramString);

    int delete(String paramString, Object paramObject);

    <E> List<E> findList(String paramString);

    <E> List<E> findList(String paramString,
                         Object paramObject);

    <E> List<E> queryForPagination(int paramInt1,
                                   int paramInt2, String paramString);

    <E> List<E> queryForPagination(int paramInt1,
                                   int paramInt2, String paramString, Object paramObject);

    Pagination queryForPaginationExt(int paramInt1,
                                     int paramInt2, String paramString);

    Pagination queryForPaginationExt(int paramInt1,
                                     int paramInt2, String paramString, Object paramObject);

    <K, V> Map<K, V> queryForMap(String paramString);

    <K, V> Map<K, V> queryForMap(String paramString,
                                 Object paramObject);

    <T> T findOne(String paramString);

    <T> T findOne(String paramString, Object paramObject);
}
