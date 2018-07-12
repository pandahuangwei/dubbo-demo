package com.simple.core.foc.dao.impl;

import com.simple.common.persistence.db.MybatisTplDao;
import com.simple.common.persistence.page.Pagination;
import com.simple.core.foc.dao.FocFlightDao;
import com.simple.core.foc.entity.FocFlight;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/22.
 */
@Repository("focFlightDao")
public class FocFlightDaoImpl implements FocFlightDao {

    @Resource(name = "mybatisTplDao")
    private MybatisTplDao dao;
    private String NAMESPACE = "FocFlightMapper.";

    public void setCarrierIATA(String iata) {
        this.NAMESPACE = iata + "FocFlightMapper.";
    }

    /**
     * 扫描foc的航班动态表：根据给定起始时间,必须按std升序排列
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Pagination selectFlightByTime(Date startTime, Date endTime, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return dao.queryForPaginationExt(pageNo, pageSize, NAMESPACE + "selectFlightByStartEndTime", map);
    }


    /**
     * 扫描foc的航班动态表：根据最后更新时间,必须按更新时间升序排列
     *
     * @param lastUpdateTime
     * @return
     */
    @Override
    public Pagination selectFlightByTime(Date lastUpdateTime, int pageNo, int pageSize) {
        return dao.queryForPaginationExt(pageNo, pageSize, NAMESPACE + "selectFlightByUpdateTime", lastUpdateTime);
    }


    /**
     * 扫描foc的飞行计划表：根据起始时间，必须按std升序排列
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public Pagination selectCfpByTime(Date start, Date end, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", start);
        map.put("endTime", end);
        return dao.queryForPaginationExt(pageNo, pageSize, NAMESPACE + "selectCfpByStartEndTime", map);
    }

    /**
     * 扫描foc的飞行计划表：根据更新时间,必须按更新时间升序排列
     *
     * @param lastUpdateTime
     * @param
     * @return
     */
    @Override
    public Pagination selectCfpByTime(Date lastUpdateTime, int pageNo, int pageSize) {
        return dao.queryForPaginationExt(pageNo, pageSize, NAMESPACE + "selectCfpByUpdateTime", lastUpdateTime);
    }

    /**
     * 查询ids中在Foc存在的航班记录
     * @param list
     * @return
     */
    @Override
    public List<Integer> getFocFlightIds(List<String> list) {
        return  dao.findList(NAMESPACE + "getFocFlightIds",list);
    }

}
