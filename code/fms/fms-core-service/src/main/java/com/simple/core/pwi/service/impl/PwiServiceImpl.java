package com.simple.core.pwi.service.impl;

import com.alibaba.fastjson.JSON;
import com.simple.common.config.BufferConfigMgr;
import com.simple.common.config.ConfigBufferReader;
import com.simple.common.utils.ListUtil;
import com.simple.common.utils.StringUtil;
import com.simple.core.common.entity.Flight;
import com.simple.core.common.entity.MsgBody;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.common.service.AbstractCoreMsgService;
import com.simple.core.common.util.GribUtil;
import com.simple.core.flightimport.entity.Cpt;
import com.simple.core.pwi.dao.PwiDao;
import com.simple.core.pwi.entity.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Administrator.
 * @version 1.0 .
 * @since 2017.03.13 10:08.
 */
@Repository("pwiService")
public class PwiServiceImpl extends AbstractCoreMsgService {
    private static final Logger logger = LoggerFactory.getLogger(PwiServiceImpl.class);
    private static final String WIND_TYPE_UGRD="UGRD";
    private static final String WIND_TYPE_VGRD="VGRD";
    private static final String WIND_TYPE_TMP="TMP";
    private static boolean UplinkEverytime = true;

    @Resource(name = "pwiDao")
    private PwiDao dao;

    /**
     * 过滤航班：航前，空中都可以
     * @param list 航班
     * @return Pair
     */
    @Override
    public Pair<MessageEnum.MsgReasonEnum, Flight> filterFlight(List<Flight> list) {
        //放行的航班
        return getValidFlightByDispach(list);
    }

    /**
     * 内部报文解释
     * @param flight   航班实体列表
     * @param msgContext 报文内容
     * @return 报文体
     */
    @Override
    public List<MsgBody> parseMessage(Flight flight, String msgContext){
        if(null==flight){
            return makeMsgBody(MessageEnum.MsgReasonEnum.SERVICE_FLIGHT_PARAM_MISSING);
        }

        //读取配置
        PropertiesConfiguration confMap = BufferConfigMgr.getInstance().getConfig("service");
        if(confMap!=null) {
            UplinkEverytime=ConfigBufferReader.getBoolean(confMap, "pwi.UplinkEverytime", true);
        }
        //针对一个航班，只允许成功上传一个报文，如需再次上传需要飞行员联系签派进行手工上传
        if(!UplinkEverytime &&
                (flight.getFpnState()== MessageEnum.MsgStep.SEND.getKey()
                        || flight.getFpnState()== MessageEnum.MsgStep.AK.getKey()
                        || flight.getFpnState()== MessageEnum.MsgStep.AC.getKey())){
            return makeMsgBody(MessageEnum.MsgReasonEnum.SERVICE_UPLINK_MANUAL);
        }

        //计算风温
        List<MsgBody> retList = new ArrayList<>();
        DownLinkPwi request = convertRequest(msgContext);
        UpLinkPwi uplink = convertToPwi(flight,request);
        retList.add(makePwiMsgBody(request, uplink));

        return retList;
    }

    /**
     * 手动报文请求
     * @param flight
     * @param msgContext
     * @return
     */
    @Override
    public List<MsgBody> parseMessageManual(Flight flight, String msgContext) {
        DownLinkPwi request = convertRequest(msgContext);
        if(null==request || null==flight){
            return makeMsgBody(MessageEnum.MsgReasonEnum.SERVICE_FLIGHT_PARAM_MISSING);
        }

        //计算风温
        List<MsgBody> retList = new ArrayList<>();
        UpLinkPwi uplink = convertToPwi(flight,request);
        retList.add(makePwiMsgBody(request, uplink));

        return retList;
    }

    /**
     * 返回的风温数据
     * @param flight 航班
     * @param request 请求
     * @return 风温数据
     */
    private UpLinkPwi convertToPwi(Flight flight,DownLinkPwi request) {
        UpLinkPwi resp = new UpLinkPwi();
        resp.setClimbAltitudeWindList(calcClimborDescentAltitudeWind(request.getClimbAltitudeList()));
        resp.setDescentAltitudeWindList(calcClimborDescentAltitudeWind(request.getDescentAltitudeList()));

        if(!ListUtil.isStringListBlank(request.getAlternateAirportList())) {
            resp.setAlternateWindList(calcAlternateAirportAltitudeWind(request.getAlternateAirportList(),flight));
        }else{
            List<String> altList = new ArrayList<>();
            //请求备降场为空时，默认使用航班中的备降场1
            if(StringUtils.isNotBlank(flight.getAlterAirport1())){
                altList.add(flight.getAlterAirport1());
                resp.setAlternateWindList(calcAlternateAirportAltitudeWind(altList,flight));
            }
        }

        //请求航路点为空时，使用航班中的主航路
        if(!ListUtil.isStringListBlank(request.getCruiseLevelWaypointList())) {
            resp.setCruiseLevelList(calcCruiseLevel(request.getCruiseLevelAltitudeList(),request.getCruiseLevelWaypointList()));
        }else{
            List<String> pointList = new ArrayList<>();
            for (String item: flight.getMainRoutePoint().split(",")) {
                pointList.add(item);
            }
            //去掉首尾两个点
            if(null!=flight.getDepartureAirport() && flight.getDepartureAirport().equals(pointList.get(0)))
                pointList.remove(0);
            if(null!=flight.getArrivalAirport() && flight.getArrivalAirport().equals(pointList.get(pointList.size()-1)))
                pointList.remove(pointList.size()-1);
            resp.setCruiseLevelList(calcCruiseLevel(request.getCruiseLevelAltitudeList(),pointList));
        }
        return resp;
    }

    private MsgBody makePwiMsgBody(DownLinkPwi request, UpLinkPwi uplink) {
        //PWI
        MsgBody msg=new MsgBody();
        msg.setMsgType(MessageEnum.MessageType.PWI.getKey());
        if(null==uplink ||
                (CollectionUtils.isEmpty(uplink.getClimbAltitudeWindList())&&
                        CollectionUtils.isEmpty(uplink.getCruiseLevelList()) &&
                        CollectionUtils.isEmpty(uplink.getDescentAltitudeWindList()) &&
                        CollectionUtils.isEmpty(uplink.getAlternateWindList())) ){
            msg.setErr(MessageEnum.MsgReasonEnum.SERVICE_FLIGHT_PARAM_MISSING);
        }else {
            msg.setErr(MessageEnum.MsgReasonEnum.SERVICE_REQ_SUCC);
            msg.setContent(uplink);
            msg.setInput(request.toInputString());
            msg.setOutput(uplink.toOutputString());
            msg.setErrCause(uplink.toInvalidString());
        }
        return msg;
    }

    /**
     * 计算爬升或者下降高度层的风
     * @param altitudeList:百英尺
     * @return 爬升或者下降高度层的风
     */
    private List<PwiAltitudeWind> calcClimborDescentAltitudeWind(List<String> altitudeList) {
        //如果altitudeList为空，则采用配置的高度层
        //采用算法，计算爬升或者下降段各高度层的一个特征点（经纬度）
        return null;
    }

    /**
     * 计算巡航高度层各航路点的风温
     * @param altitudeList:百英尺
     * @param waypointList:航路点名称（含经纬度点）
     * @return
     */
    private List<PwiCruiseLevel> calcCruiseLevel(List<String> altitudeList, List<String> waypointList) {
        List<PwiCruiseLevel> list = new ArrayList<>();
        for(String altitude:altitudeList){
            if(StringUtil.isBlank(altitude)) continue;

            PwiCruiseLevel level = new PwiCruiseLevel();
            level.setAlt(altitude);
            list.add(level);

            //求各航路点的风温
            for(String cptname:waypointList){
                if(StringUtil.isBlank(cptname)) continue;
                PwiPoint point = getWindTemp(cptname,altitude+"00");
                if(null==point || !point.isValid()){
                    level.addInvalidPoint(point);
                }
                else {
                    level.addPoint(point);
                }
            }
        }
        return list;
    }

    /**
     * 计算备降机场高度层的风
     * 机场标高：英尺
     * @param alternateAirportList
     * @return
     */
    private List<PwiAltitudeWind> calcAlternateAirportAltitudeWind(List<String> alternateAirportList,Flight flight) {
        /*
        List<PwiAltitudeWind> list = new ArrayList<>();
        for(String airport:alternateAirportList){
            if(StringUtils.isBlank(airport)) continue;

            PwiAltitudeWind aw = new PwiAltitudeWind();
            aw.setAlternateAirport(airport);

            if(airport.equals(flight.getAlterAirport1())){
                aw.setAlt(flight.getAlterRoute1Fl());
                aw.setWindvel(flight.getAlterRoute1Wc());
            }
            else if(airport.equals(flight.getAlterAirport2())){
                aw.setAlt(flight.getAlterRoute2Fl());
                aw.setWindvel(flight.getAlterRoute2Wc());
            }
            else if(airport.equals(flight.getAlterAirport3())){
                aw.setAlt(flight.getAlterRoute3Fl());
                aw.setWindvel(flight.getAlterRoute3Wc());
            }
            list.add(aw);
        }
        return list;
        */
        return null;
    }

    /**
     * 将报文构建为实体，得到报文头与报文体
     *
     * @param message 解密后的报文
     * @return 原始报文实体
     */
    private static DownLinkPwi convertRequest(String message) {
        DownLinkPwi request = JSON.parseObject(message, DownLinkPwi.class);
        return request;
    }

    /**
     * 得到风温数据
     * @param cptName
     * @param altitude:英尺
     * @return
     */
    public PwiPoint getWindTemp(String cptName, String altitude){
        double alt = Double.valueOf(altitude).doubleValue();
        BigDecimal grid2Alt = GribUtil.fl2hpa(new BigDecimal(alt));

        PwiPoint fd=new PwiPoint();
        fd.setCptname(cptName);//航路点名称
        fd.setAlt(String.valueOf((int)(alt/100)));//高度层

        //风温数据结果：风速XXX,风向YYY,温度ZZ(M负数，P正数),经度,纬度
        String responseStr = windCalculation(cptName,
                grid2Alt.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        if (!StringUtil.isBlank(responseStr)) {
            String responseAarray[] = responseStr.split(",");
            if (responseAarray != null) {
                if(responseAarray.length>0) fd.setWindvel(responseAarray[0]);//风速XXX
                if(responseAarray.length>1) fd.setWinddir(responseAarray[1]);//风向YYY
                if(responseAarray.length>2) fd.setTemperature(responseAarray[2]);//温度ZZ(M负数，P正数)
                if(responseAarray.length>3) fd.setLon(responseAarray[3]);//经度
                if(responseAarray.length>4) fd.setLat(responseAarray[4]);//纬度
            }
        }
        return fd;
    }

    /**
     * 计算指定高度层、经度、纬度的点的V/U/温度值
     * @param cptName 航路点名称
     * @param alt   高度
     * @return  风温数据
     */
    private String windCalculation(String cptName, double alt){
        List lonLatList;
        if (GribUtil.nameIsLonLat(cptName)) {
            logger.info(cptName + "是含有经纬度的航路点名称！");
            lonLatList = GribUtil.getLonLatList(cptName);
        } else {
            // 从数据库中查询当前航路点的经纬度
            lonLatList = getLonAndLat(cptName);
        }

        if (lonLatList != null) {
            double lon = Double.valueOf(lonLatList.get(0).toString());
            double lat = Double.valueOf(lonLatList.get(1).toString());

            //************************测试用************************
            //风速XXX,风向YYY,温度ZZ(M负数，P正数),经度,纬度
            Random rd = new Random();
            return String.format("%03d",rd.nextInt(50))+","
                    +String.format("%03d",rd.nextInt(360))+","
                    +String.format("M%02d",rd.nextInt(99))+","
                    +lonLatList.get(2)+","+lonLatList.get(3);
            /*************************************************************/

            /************************等有grib2数据后打开************************
            // 获取指定“经纬度点、高度层”周围的八个“标准气压高度层、经纬度点”的UVT数据
            Map pointMap = getEightPointsUVT(lon, lat, alt);
            if (pointMap != null) {
                List ugrdList = (List) pointMap.get(WIND_TYPE_UGRD);
                List vgrdList = (List) pointMap.get(WIND_TYPE_VGRD);
                List tmpList = (List) pointMap.get(WIND_TYPE_TMP);
                logger.info("====[windCalculation] - [开始] 计算指定航路点、高度层UVT分量：cptName:{},lon:{},lat:{}",cptName,lon,lat);
                BigDecimal uvalue = calcWindInfo(ugrdList, lon, lat, alt);
                BigDecimal vvalue = calcWindInfo(vgrdList, lon, lat, alt);
                BigDecimal tvalue = calcWindInfo(tmpList, lon, lat, alt);
                //风速，风向
                String returnStr = GribUtil.ComputeWindVelDirByUV(uvalue, vvalue);
                logger.info("====[windCalculation] - [结束] 计算指定航路点、高度层UVT分量：u:{},v:{},t:{}",uvalue,vvalue,tvalue);

                //风速XXX,风向YYY,温度ZZ(M负数，P正数),经度,纬度
                return returnStr + "," + String.valueOf(tvalue.setScale(0, BigDecimal.ROUND_HALF_EVEN))
                        +","+lonLatList.get(2)+","+lonLatList.get(3);
            }
            /*************************************************************/
        }
        return null;
    }

    /**
     * 根据航路点取得其经纬度
     * @param cptname  航路点名称
     * @return 经纬度
     */
    private List getLonAndLat(String cptname) {

        List returnList = new ArrayList<>();
        double lon = 0.0;
        double lat = 0.0;

        // 飞行计划中的 经纬度以“度、分”的格式存放，其中“分”含有秒的小数表示
        Cpt cpt = dao.findByCptnameForWayPoint(cptname);    //根据航路点名称取出的经度纬度
        if (cpt != null) {
            lon = GribUtil.convertLonLat(cpt.getLng(),2);
            lat = GribUtil.convertLonLat(cpt.getLat(),2);
        }
        // NAIP表 经纬度以“度、分、秒”的格式存放
        else{
             cpt = dao.findByCptnameForNaip(cptname);
            if (cpt != null) {
                lon = GribUtil.convertLonLat(cpt.getLng(),1);
                lat = GribUtil.convertLonLat(cpt.getLat(),1);
            }else {
                logger.info("航路点名称为：" + cptname + " 在数据库中没有查询到！");
                return null;
            }

        }

        returnList.add(lon);
        returnList.add(lat);

        //补充原始格式的经纬度
        returnList.add(cpt.getLng());
        returnList.add(cpt.getLat());

        return returnList;
    }

    /**
     * function : 取得给定点周围八个点的VGRD UGRD TMP信息
     * @param alt 高度
     * @return 一个包含了三个LIST的map；
     *         每个LIST包含了8个点 U or V or T 一种数据
     */
    private Map getEightPointsUVT(double lon, double lat, double alt) {
        Map returnMap = new HashMap();
        List ugrdList = new ArrayList();
        List vgrdList = new ArrayList();
        List tmpList = new ArrayList();

/*        GribUtil.getTwoStandardLons(lon);
        GribUtil.getTwoStandardLats(lat);
        GribUtil.getTwoStandardAltitudes(alt);*/

        // 根据经、纬度、高度取出8个点
        List pointList = GribUtil.getPointList(lon, lat, alt);
        // 取得给定经、纬、高度的8个点的UGRD、VGRD、TMP信息
        List valueList = dao.findByLonLatAlt(GribUtil.getTwoStandardLons(lon), GribUtil.getTwoStandardLats(lat), GribUtil.getTwoStandardAltitudes(alt));
        if (valueList == null || valueList.size() != 24) {
            //数据库中的grid2数据不完整，直接返回
            logger.error("无法在数据库中查询到点（经度：" + lon + "，纬度：" + lat + "，高度层：" + alt + "）周围的8个格点grid2数据！");
            return null;
        }
        // 遍历pointList将URRD、VGRD、TMP数据放入三个list中
        for (int i = 0; i < pointList.size(); i++) {
            Object[] obj = (Object[]) pointList.get(i);
            double dLon = Double.valueOf(obj[0].toString());
            double dLat = Double.valueOf(obj[1].toString());
            int dAlt = Integer.parseInt(obj[2].toString());

            for (int j = 0; j < valueList.size(); j++) {
                Object[] objValue = (Object[]) valueList.get(j);
                if (dLon == Double.valueOf(objValue[0].toString())
                        && dLat == Double.valueOf(objValue[1].toString())
                        && dAlt == Integer.parseInt(objValue[2].toString())) {

                    PointVo pointVo = new PointVo();
                    pointVo.setdAlt(String.valueOf(dAlt));
                    pointVo.setdLat(String.valueOf(dLat));
                    pointVo.setdLon(String.valueOf(dLon));
                    pointVo.setWindVal(String.valueOf(objValue[3]));

                    if ((WIND_TYPE_UGRD).equals(objValue[4])) {
                        ugrdList.add(pointVo);
                    } else if ((WIND_TYPE_VGRD).equals(objValue[4])) {
                        vgrdList.add(pointVo);
                    } else if ((WIND_TYPE_TMP).equals(objValue[4])) {
                        tmpList.add(pointVo);
                    }
                }
            }
        }

        returnMap.put(WIND_TYPE_UGRD, ugrdList);
        returnMap.put(WIND_TYPE_VGRD, vgrdList);
        returnMap.put(WIND_TYPE_TMP, tmpList);

        return returnMap;
    }

    /**
     * @param valueList 待求点周围的8个点，V or U or 温度值的值列表
     * @param lon       待求点经度
     * @param lat       待求点纬度
     * @param alt       待求点高度层
     * @return BigDecimal
     */
    private BigDecimal calcWindInfo(List valueList, double lon, double lat, double alt) {
        List list = new ArrayList();
        List altList = GribUtil.getTwoStandardAltitudes(alt);
        BigDecimal[] altObject = new BigDecimal[]{new BigDecimal(altList.get(0).toString()), new BigDecimal(altList.get(1).toString())};

        // 通过“冒泡比较”的方法：筛选出四组经纬度都相同且高度层不同的点，后再组内分别利用对数计算公式求目标点的值
        for (int i = 0; i < valueList.size(); i++) {
            for (int j = 0; j < valueList.size(); j++) {
                PointVo pointVo1 = (PointVo) valueList.get(i);
                PointVo pointVo2 = (PointVo) valueList.get(j);
                if (new BigDecimal(pointVo1.getdLon()).compareTo(new BigDecimal(pointVo2.getdLon())) == 0
                        && new BigDecimal(pointVo1.getdLat()).compareTo(new BigDecimal(pointVo2.getdLat())) == 0) {

                    PointVo pointUp = new PointVo();
                    PointVo pointDown = new PointVo();
                    PointVo newPoint = new PointVo();

                    if (Double.valueOf(pointVo1.getdAlt()) > Double.valueOf(pointVo2.getdAlt())) {
                        pointDown = pointVo2;
                        pointUp = pointVo1;
                        BigDecimal[] value = new BigDecimal[]{new BigDecimal(pointUp.getWindVal()), new BigDecimal(pointDown.getWindVal())};
                        BigDecimal[] calcValue = GribUtil.ComputeLogarithmValue(altObject, 2, value, new BigDecimal[]{new BigDecimal(alt)}, 1);

                        newPoint.setdLon(pointUp.getdLon());
                        newPoint.setdLat(pointUp.getdLat());
                        newPoint.setWindVal(String.valueOf(calcValue[0]));
                        list.add(newPoint);
                    }
                }
            }
        }
        // 对目标高度层上的四个经纬度相同的点进行分组，经度不同、纬度相同的为一组
        List lineList = new ArrayList();
        int temp = 0;
        for (int tempi = 0; tempi < list.size(); tempi++) {
            //避免重复
            if (temp != tempi) {
                for (int tempj = 0; tempj < list.size(); tempj++) {
                    PointVo vo1 = (PointVo) list.get(tempi);
                    PointVo vo2 = (PointVo) list.get(tempj);
                    if ((new BigDecimal(vo1.getdLon()).compareTo(new BigDecimal(vo2.getdLon())) != 0
                            && new BigDecimal(vo1.getdLat()).compareTo(new BigDecimal(vo2.getdLat())) == 0)) {
                        temp = tempj;
                        Map map = new HashMap();
                        map.put("P1", vo1);
                        map.put("P2", vo2);
                        lineList.add(map);
                    }
                }
            }
        }
        if (lineList.size() != 2) {
            logger.error("线性插值运算的两个点数目不正确！");
            return null;
        }
        return GribUtil.biLinearInterpolate(lineList, new BigDecimal(lon), new BigDecimal(lat));
    }

}
