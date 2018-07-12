package com.simple.core.flightimport.service.impl;


import com.simple.common.utils.StringUtil;
import com.simple.common.utils.UuidUtil;
import com.simple.common.utils.crypt.FileEncodeUtil;
import com.simple.core.flightimport.constant.FlightConst;
import com.simple.core.flightimport.dao.FlightImportDao;
import com.simple.core.flightimport.entity.CfpExt;
import com.simple.core.flightimport.entity.Cpt;
import com.simple.core.flightimport.factory.CfpParserFactory;
import com.simple.core.flightimport.parsecfp.CfpParser;
import com.simple.core.flightimport.service.FlightImportService;
import com.simple.core.foc.entity.FocCfp;
import com.simple.core.foc.entity.FocFlight;
import com.simple.core.common.entity.FlightBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: FlightImportServiceImpl.java.
 * @Package com.simple.core.flightimport.service.impl.
 * @Description: FlightImportServiceImpl.
 *
 * @author Administrator.
 * @version 1.0.
 * @created 2017/3/23 16:32.
 **********************************************************************************/
@Service("flightImportService")
public class FlightImportServiceImpl implements FlightImportService {
    private static final Logger logger = LoggerFactory.getLogger(FlightImportServiceImpl.class);
    private static final String PLAN_NO_END="NONSTOP";

    @Resource(name = "flightImportDao")
    private FlightImportDao flightImportDao;

    /**
     * 将foc航班写入到本地库
     * @param list
     * @return
     */
    @Override
    public boolean saveFocFlight(List<FocFlight> list, boolean hasDispatchTime, boolean hasCrewArrivalTime) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        int saveCount = 0;
        for (FocFlight foc : list) {
            if(storeOneFocFlight(foc,hasDispatchTime,hasCrewArrivalTime)) saveCount++;
        }

        logger.info("saveFocFlight count="+saveCount);
        return true;
    }

    /**
     * 将foc飞行计划写入到本地库
     * @param list
     * @return
     */
    @Override
    public boolean saveFocCfp(List<FocCfp> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        int saveCount = 0;
        for (FocCfp foc : list) {
            if(storeOneFocCfp(foc)) saveCount++;
        }

        logger.info("saveFocCfp count="+saveCount);
        return true;
    }

    /**
     * 将一条航班写入到本地库：事务控制
     * @param foc
     * @param hasDispatchTime
     * @param hasCrewArrivalTime
     * @return
     */
    @Override
    public boolean storeOneFocFlight(FocFlight foc, boolean hasDispatchTime, boolean hasCrewArrivalTime) {
        //根据配置的航空公司代码过滤航班
        String carrierCode = getCarrierCode(foc.getFlightNo());
        if(!CfpParserFactory.getInstance().isCarrierDefined(carrierCode)) return false;

        //根据flightId查找本地库的航班动态
        //表有业务唯一键约束：航班号，航班日期，起飞机场，着陆机场，STD
        //表有业务唯一键约束：foc_flight_id
        FlightBase exist = flightImportDao.selectFlightByFocFlightId(foc.getFocFlightId());

        if(null==exist) exist = flightImportDao.selectFlightByUnique(foc.getFlightDate(),
                foc.getFlightNo(),foc.getDepartureAirport(),foc.getArrivalAirport(),foc.getStd());

        if (exist != null) {
            FlightBase f = convertFocFlight(foc,hasDispatchTime,hasCrewArrivalTime);
            f.setFlightId(exist.getFlightId());
            flightImportDao.updateFlight(f);
        } else {
            FlightBase f = convertFocFlight(foc,hasDispatchTime,hasCrewArrivalTime);
            f.setFlightId(UuidUtil.get32UUID());
            f.setCreateTm(new Date());//只有在insert时才需要creaTm,modifyTm,update时只需要modifyTm
            flightImportDao.insertFlight(f);
        }
        return true;
    }

    /**
     * 将一条CFP写入到本地库
     * @param foc
     * @return
     */
    @Override
    public boolean storeOneFocCfp(FocCfp foc) {

        //根据foc-flightId 查找本地库的flight
        FlightBase existFlight = flightImportDao.selectFlightByFocFlightId(foc.getFlightId());
        if (null == existFlight) return false;
        //解析Plan_no
        String planNo=parsePlanNo(foc.getCfpInfo());
        if (null == planNo) return false;
        //根据Plan_no查找本地库的飞行计划文件
        CfpExt exist = flightImportDao.findCfpByPlanNo(foc.getFlightId(),planNo);
        String md5 = null;
        if (null != exist && existFlight.isCfpDataValid() ) {
            md5 = FileEncodeUtil.GetHashMd5(foc.getCfpInfo(), -1);
            if (null != md5 && md5.equals(exist.getMd5())) return false;
        }

        //根据航班号提取航空公司代码
        //根据航空公司代码创建bean实例
        String carrierCode = getCarrierCode(existFlight.getFlightNo());
        CfpParser parser = CfpParserFactory.create(carrierCode);
        if (null == parser) return false;

        //解析飞行计划文件
        CfpExt cfpInfo = parser.parse(foc.getCfpInfo());
        if (null == cfpInfo) return false;

        //根据focflightId更新(解析飞行计划文件后入本地库fms_flight)
        FlightBase flightUpdate = fillFlightWithCfp(cfpInfo,existFlight);
        flightUpdate.setFocFlightId(foc.getFlightId());
        flightImportDao.updateFlightForPlan(flightUpdate);

        //way_point
        Map<String, Cpt> cptMap = cfpInfo.getCptMap();
        for(Cpt cpt : cptMap.values()){
            int count = flightImportDao.selectWayPointByCptName(cpt.getCptName());
            if (count>0) {
                //更新本地way_point
                flightImportDao.updateWayPoint(cpt);
            } else {
                //插入本地way_point
                cpt.setCreateTm(new Date());
                flightImportDao.insertWayPoint(cpt);
            }
        }
        //解析飞行计划文件后入本地库fms_cft_ext
        convertCfpExt(cfpInfo,existFlight,foc);
        if (null != exist ) {
            //更新本地飞行计划文件
            cfpInfo.setCfpId(exist.getCfpId());
            flightImportDao.updateCfp(cfpInfo);
        } else {
            cfpInfo.setCfpId(UuidUtil.get32UUID());
            cfpInfo.setCreateTm(new Date());
            //插入本地飞行计划文件
            flightImportDao.insertCfp(cfpInfo);
        }
        return true;
    }

    /**
     * 截取飞行计划编号
     * @param cfpArray
     * @return
     */
    private String parsePlanNo(byte[] cfpArray){
        String cfpArr = new String(cfpArray);
        String planNoStr=cfpArr.substring(0, cfpArr.indexOf(PLAN_NO_END));
        String[] planNoStrArr = planNoStr.split("\n");
        String[] planNoStrArr2 = planNoStrArr[0].split(" ");
        return planNoStrArr2[1];
    }

    @Override
    public Date getMaxCfpModifyTime() {
        return flightImportDao.getMaxCfpModifyTime();
    }

    @Override
    public Date getMaxFlightModifyTime() {
        return flightImportDao.getMaxFlightModifyTime();
    }

    /**
     * flight当天航班ID
     * @param flightDate
     * @return
     */
    @Override
    public List<String> getFlightIds(Date flightDate) {
        return flightImportDao.getFlightIds(flightDate);
    }

    /**
     * 按flight_id删除航班
     * @param ids
     * @return
     */
    @Override
    public int deleteFlightByIds(List<String> ids) {
        if(CollectionUtils.isEmpty(ids)) return 0;
        return flightImportDao.deleteFlightById(ids);

    }

    /**
     * 按flight_id 删除飞行计划
     * @param ids
     * @return
     */
    @Override
    public int deleteCfpByIds(List<String> ids) {
        if(CollectionUtils.isEmpty(ids)) return 0;
        return flightImportDao.deleteCfpById(ids);
    }

    /**
     * 将FocFlight转换成FlightBase
     *
     * @param foc
     * @return
     */
    private static FlightBase convertFocFlight(FocFlight foc,boolean hasDispatchTime,boolean hasCrewArrivalTime) {
        FlightBase f = new FlightBase();
        f.setFocFlightId(foc.getFocFlightId());
        f.setFlightNo(foc.getFlightNo());
        f.setFlightDate(foc.getFlightDate());
        f.setDepartureAirport(foc.getDepartureAirport());
        f.setArrivalAirport(foc.getArrivalAirport());
        f.setStd(foc.getStd());
        f.setEtd(foc.getEtd());
        f.setAtd(foc.getAtd());
        f.setSta(foc.getSta());
        f.setAta(foc.getAta());
        f.setEta(foc.getEta());
        f.setAcReg(foc.getAcReg());
        f.setAcType(foc.getAcType());
        f.setdOrI(foc.getdOrI());
        f.setDispatchPassTime(foc.getDispatchPassTime());
        f.setCrewArrivalTime(foc.getCrewArrivalTime());
        f.setFlgVR(foc.getFlgVR());
        f.setFlgVR1(foc.getFlgVR1());
        f.setFlgCS(foc.getFlgCS());
        f.setFocFlightId(foc.getFocFlightId());
        f.setModifyTm(new Date());
        f.setFocModifyTm(foc.getModifyTm());

        //签派放行1是,0否,2取不到值
        if(!hasDispatchTime) {
            f.setDispatch(FlightConst.DISPACH_NULL);
        }
        else{
            if(foc.getDispatchPassTime()!=null || "Y".equals(foc.getFlagRLS())){
                f.setDispatch(FlightConst.DISPACH_YES);
            }else{
                f.setDispatch(FlightConst.DISPACH_NO);
            }
        }
        //机组是否到位1是,0否,2取不到值
        if(!hasCrewArrivalTime) f.setCrewArrival(FlightConst.CREWARRIVAL_NULL);
        else{
            if(foc.getCrewArrivalTime()==null){
                f.setCrewArrival(FlightConst.CREWARRIVAL_NO);
            }else{
                f.setCrewArrival(FlightConst.CREWARRIVAL_YES);
            }
        }
        return f;
    }

    /**
     * 将CfpExt填充到FlightBase
     *
     * @param cfp
     * @return
     */
    private static FlightBase fillFlightWithCfp(CfpExt cfp,FlightBase existFlight) {
        if (null == cfp) return null;
        FlightBase f = new FlightBase();
        f.setFlightId(existFlight.getFlightId());

        //主公司航路
        f.setMainCompanyRoute(cfp.getMainCompanyRoute());
        //主航路描述
        f.setMainRoute(cfp.getMainRoute());
        //主航路点
        f.setMainRoutePoint(cfp.getMainRoutePoint());
        //备降航路描述
        f.setAlterRoute1(cfp.getAlterRoute1());
        f.setAlterRoute2(cfp.getAlterRoute2());
        f.setAlterRoute3(cfp.getAlterRoute3());
        //备降航路点
        f.setAlterRoute1Point(cfp.getAlterRoute1Point());
        f.setAlterRoute2Point(cfp.getAlterRoute2Point());
        f.setAlterRoute3Point(cfp.getAlterRoute3Point());
        //备降机场名称
        f.setAlterAirport1(cfp.getAlterAirport1());
        f.setAlterAirport2(cfp.getAlterAirport2());
        f.setAlterAirport3(cfp.getAlterAirport3());
        f.setModifyTm(new Date());
        f.setMainSpecial(cfp.getMainSpecial());
        //备降航路1是否特殊航路1是0否
        f.setAlter1Special(cfp.getAlter1Special());
        f.setAlter2Special(cfp.getAlter2Special());
        f.setAlter3Special(cfp.getAlter3Special());

        //油量
        f.setCostIndex(cfp.getCostIndex());
        f.setTraFuel(cfp.getTraFuel());
        f.setTakeoffWeight(cfp.getTakeoffWeight());
        f.setLandWeight(cfp.getLandWeight());
        f.setAvPayload(cfp.getAvPayload());
        f.setOpnlWeight(cfp.getOpnlWeight());
        f.setAltFuel(cfp.getAltFuel());
        f.setHldFuel(cfp.getHldFuel());
        f.setResFuel(cfp.getResFuel());
        f.setXtrFuel(cfp.getXtrFuel());
        f.setApuFuel(cfp.getApuFuel());
        f.setTxoFuel(cfp.getTxoFuel());
        f.setTxiFuel(cfp.getTxiFuel());
        f.setFobFuel(cfp.getFobFuel());
        f.setCruiseAltitude(cfp.getCruiseAltitude());
        f.setRouteAvgTemp(cfp.getRouteAvgTemp());
        f.setRouteAvgWind(cfp.getRouteAvgWind());
        //第一次解析飞行计划
        if(StringUtil.isEmpty(existFlight.getMainRoutePoint())
                && StringUtil.isEmpty(existFlight.getAlterRoute1Point())
                && StringUtil.isEmpty(existFlight.getAlterRoute2Point())
                && StringUtil.isEmpty(existFlight.getAlterRoute3Point())){
            f.setRouteChange(FlightConst.NO_PARAM);
            f.setRouteChangeTm(new Date());
        }else {
            //判断航路变化
            boolean bMainChange = !stringEqual(f.getMainRoutePoint(), existFlight.getMainRoutePoint());
            boolean bAlterChange = !stringEqual(f.getAlterRoute1Point(), existFlight.getAlterRoute1Point())
                    || !stringEqual(f.getAlterRoute2Point(), existFlight.getAlterRoute2Point())
                    || !stringEqual(f.getAlterRoute3Point(), existFlight.getAlterRoute3Point());
            if (bMainChange && bAlterChange){
                f.setRouteChange(FlightConst.CHANGE_MAIN_ALTER);
                f.setRouteChangeTm(new Date());
            }else if (bMainChange) {
                f.setRouteChange(FlightConst.CHANGE_MAIN);
                f.setRouteChangeTm(new Date());
            }else if (bAlterChange){
                f.setRouteChange(FlightConst.CHANGE_ALTER);
                f.setRouteChangeTm(new Date());
            }else{
                f.setRouteChange(FlightConst.CHANGE_OTHER);
                f.setRouteChangeTm(new Date());
            }
        }
        //飞行计划编号
        f.setPlanNo(cfp.getPlanNo());
        //备降航路高度层
        f.setAlterRoute1Fl(cfp.getAlterRoute1Fl());
        f.setAlterRoute2Fl(cfp.getAlterRoute2Fl());
        f.setAlterRoute3Fl(cfp.getAlterRoute3Fl());
        //备降航路风分量
        f.setAlterRoute1Wc(cfp.getAlterRoute1Wc());
        f.setAlterRoute2Wc(cfp.getAlterRoute2Wc());
        f.setAlterRoute3Wc(cfp.getAlterRoute3Wc());
        //备降航路的油耗
        f.setAlterRoute1Fuel(cfp.getAlterRoute1Fuel());
        f.setAlterRoute2Fuel(cfp.getAlterRoute2Fuel());
        f.setAlterRoute3Fuel(cfp.getAlterRoute3Fuel());
        return f;
    }

    private static void convertCfpExt(CfpExt f,FlightBase existFlight,FocCfp foc){
        f.setCfpInfo(foc.getCfpInfo());
        f.setMd5(FileEncodeUtil.GetHashMd5(foc.getCfpInfo(), -1));
        f.setFocFlightId(foc.getFlightId());
        f.setFocModifyTm(foc.getModifyTm());
        f.setModifyTm(new Date());
        //第一次解析飞行计划
        if(StringUtil.isEmpty(existFlight.getMainRoutePoint())
                && StringUtil.isEmpty(existFlight.getAlterRoute1Point())
                && StringUtil.isEmpty(existFlight.getAlterRoute2Point())
                && StringUtil.isEmpty(existFlight.getAlterRoute3Point())){
            f.setRouteChange(FlightConst.NO_PARAM);
            f.setRouteChangeTm(new Date());
        }else {
            //判断航路变化
            boolean bMainChange = !stringEqual(f.getMainRoutePoint(), existFlight.getMainRoutePoint());
            boolean bAlterChange = !stringEqual(f.getAlterRoute1Point(), existFlight.getAlterRoute1Point())
                    || !stringEqual(f.getAlterRoute2Point(), existFlight.getAlterRoute2Point())
                    || !stringEqual(f.getAlterRoute3Point(), existFlight.getAlterRoute3Point());
            if (bMainChange && bAlterChange) {
                f.setRouteChange(FlightConst.CHANGE_MAIN_ALTER);
                f.setRouteChangeTm(new Date());
            }else if (bMainChange) {
                f.setRouteChange(FlightConst.CHANGE_MAIN);
                f.setRouteChangeTm(new Date());
            }else if (bAlterChange) {
                f.setRouteChange(FlightConst.CHANGE_ALTER);
                f.setRouteChangeTm(new Date());
            }else {
                f.setRouteChange(FlightConst.CHANGE_OTHER);
                f.setRouteChangeTm(new Date());
            }
        }
    }

    private static boolean stringEqual(String a,String b){
        if(null==a && null==b) return true;
        if(null==a && null!=b) return false;
        if(null!=a && null==b) return false;

        return a.equalsIgnoreCase(b);
    }

    /**
     * 提取航班号中的公司代码 :注意CSZ444T，ZH444T
     *
     * @param flightNo
     * @return
     */
    private static String getCarrierCode(String flightNo) {
        if (null == flightNo) return null;

        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(flightNo);
        String digital = m.replaceAll("").trim();

        int pos = flightNo.indexOf(digital);
        return flightNo.substring(0, pos);
    }
}
