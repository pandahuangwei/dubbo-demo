package com.simple.core.flightimport.dao.impl;

import com.simple.common.persistence.db.MybatisTplDao;
import com.simple.core.flightimport.dao.FlightImportDao;
import com.simple.core.flightimport.entity.CfpExt;
import com.simple.core.flightimport.entity.Cpt;
import com.simple.core.common.entity.FlightBase;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/23.
 */
@Repository("flightImportDao")
public class FlightImportDaoImpl implements FlightImportDao {

    @Resource(name = "mybatisTplDao")
    private MybatisTplDao dao;
    private String NAMESPACE="FlightImportMapper.";

    /**
     * 根据Foc-FlightId 查找本地库的 Flight
     * @param id
     * @return
     */
    @Override
    public FlightBase selectFlightByFocFlightId(String id) {
        return (FlightBase) dao.findOne(NAMESPACE+"selectFlightByFocFlightId",id);
    }

    /**
     * 根据唯一键查找本地库的 Flight
     * @param fliDate
     * @param fliNo
     * @param pod
     * @param poa
     * @param std
     * @return
     */
    @Override
    public FlightBase selectFlightByUnique(Date fliDate,String fliNo,String pod,String poa,Date std){
        Map<String, Object> map = new HashMap<>();
        map.put("fliDate", fliDate);
        map.put("fliNo", fliNo);
        map.put("pod", pod);
        map.put("poa", poa);
        map.put("std", std);
        return (FlightBase) dao.findOne(NAMESPACE+"selectFlightByUnique",map);
    }

    /**
     * 将foc中的航班动态写到本地库
     * @param flight
     * @return
     */
    @Override
    public int insertFlight(FlightBase flight) {
        return dao.insert(NAMESPACE+"inserFlight",flight);
    }

    /**
     * 解析飞行计划后更新flight
     * @param flight
     * @return
     */
    @Override
    public int updateFlightForPlan(FlightBase flight) {
        return dao.update(NAMESPACE+"updateFlightForPlan",flight);
    }

    @Override
    public int updateFlight(FlightBase flight) {

        return dao.update(NAMESPACE+"updateFlight",flight);
    }

    /**
     * 根据Foc-flightID 查找本地库的飞行计划文件
     * @param focFlightId
     * @param planNo
     * @return
     */
    @Override
    public CfpExt findCfpByPlanNo(String focFlightId,String planNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("focFlightId", focFlightId);
        map.put("planNo", planNo);
        return (CfpExt) dao.findOne(NAMESPACE+"findCfpByFocFlightIdPlanNo",map);
    }

    /**
     * cfp_ext:modifyTime最大值
     * @return
     */
    @Override
    public Date getMaxCfpModifyTime() {
        return (Date)dao.findOne(NAMESPACE+"getMaxCfpModifyTime");
    }

    /**
     * flight:modifyTime最大值
     * @return
     */
    @Override
    public Date getMaxFlightModifyTime() {
        return (Date)dao.findOne(NAMESPACE+"getMaxFlightModifyTime");
    }

    @Override
    public int insertCfp(CfpExt cfp) {
        return dao.insert(NAMESPACE+"insertCfpInfo",cfp);
    }

    @Override
    public int updateCfp(CfpExt cfp) {
        return dao.update(NAMESPACE+"updateCfpInfo",cfp);
    }

    /**
     * 解析完飞行计划的同时将航路点对应的经纬度写入到本地库
     * @param cpt
     * @return
     */
    @Override
    public int insertWayPoint(Cpt cpt) {
        return dao.insert(NAMESPACE+"insertWayPoint",cpt);
    }

    @Override
    public int updateWayPoint(Cpt cpt) {
        return dao.update(NAMESPACE+"updateWayPoint",cpt);
    }

    /**
     * 根据航路点名称查找经纬度
     * @param cptName
     * @return
     */
    @Override
    public int selectWayPointByCptName(String cptName) {
        return dao.findOne(NAMESPACE+"selectWayPointByCptName",cptName);
    }

    /**
     * 查找flight当天航班ID
     * @param flightDate
     * @return
     */
    @Override
    public List<String> getFlightIds(Date flightDate) {
        return dao.findList(NAMESPACE+"getFlightIds",flightDate);
    }

    /**
     * 根据ID删除航班
     * @param list
     * @return
     */
    @Override
    public int deleteFlightById(List<String> list) {
        return dao.delete(NAMESPACE+"deleteFlightByIds",list);
    }

    /**
     * 根据ID删除飞行计划
     * @param list
     * @return
     */
    @Override
    public int deleteCfpById(List<String> list) {
        return dao.delete(NAMESPACE+"deleteCfpByIds",list);
    }
}





