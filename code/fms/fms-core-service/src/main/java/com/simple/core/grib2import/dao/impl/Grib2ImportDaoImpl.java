package com.simple.core.grib2import.dao.impl;

import com.simple.common.persistence.db.MybatisTplDao;
import com.simple.core.grib2import.dao.Grib2ImportDao;
import com.simple.core.pwi.entity.Grib2WdInfo;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator.
 * @version 1.0 .
 * @since 2017.03.13 10:02.
 */
@Repository("grib2ImportDao")
public class Grib2ImportDaoImpl implements Grib2ImportDao {

    @Resource(name = "mybatisTplDao")
    private MybatisTplDao dao;
    private String NAMESPACE="Grib2ImportMapper.";

    /**
     * 风温数据入库
     * @param list
     * @return
     */
    @Override
    public int saveGrib2Data(List<Grib2WdInfo> list) {
        if(CollectionUtils.isEmpty(list))return -1;
        return dao.insert(NAMESPACE+"storeGrib2Data",list);
    }

    /**
     * Function : 按起止有效时间、高度层计算数据库中记录数量
     * param startTime : 起始时间
     * param endTime : 终止时间
     * param alt : 高度层
     * return long : 记录数量
     */
    @Override
    public long countByTimeAlt(String startTime, String endTime, int alt) {
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("alt", alt);
        return Long.valueOf(dao.findOne(NAMESPACE+"countByTimeAlt",map).toString());
    }

    /**
     * Function : 按起止有效时间、高度层删除数据
     * param startTime : 起始时间
     * param endTime : 终止时间
     * param alt : 高度层
     * return long : 删除记录数量
     */
    @Override
    public long deleteByTimeAlt(String startTime, String endTime, int alt) {
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("alt", alt);
        return dao.delete(NAMESPACE+"deleteByTimeAlt",map);
    }
}
