package com.simple.core.fpn.service.impl;

import com.alibaba.fastjson.JSON;
import com.simple.common.config.BufferConfigMgr;
import com.simple.common.config.ConfigBufferReader;
import com.simple.common.utils.StringUtil;
import com.simple.core.common.entity.Flight;
import com.simple.core.common.entity.MsgBody;
import com.simple.core.common.enums.FlightEnum;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.common.enums.MessageEnum.MessageType;
import com.simple.core.common.service.AbstractCoreMsgService;
import com.simple.core.flightimport.constant.FlightConst;
import com.simple.core.fpn.entity.DownLinkFPN;
import com.simple.core.fpn.entity.UpLinkFPN;
import com.simple.core.fpn.entity.Waypoint;
import com.simple.core.per.entity.UpLinkPER;
import com.simple.core.per.service.impl.PerServiceImpl;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: FpnService.java.
 * @Package com.simple.core.fpn.service.
 * @Description: fpn航路上传服务.
 *
 * @author Administrator.
 * @version 1.0.
 * @created 2017/3/16 10:43.
 **********************************************************************************/
@Service("fpnService")
public class FpnServiceImpl extends AbstractCoreMsgService {
    private static final Logger logger = Logger.getLogger(FpnServiceImpl.class);
    private static boolean GetMainRoutePoint=true;
    private static boolean MaxAlterAirportFuel=true;
    private static boolean UplinkEverytime = true;

    @Override
    public Pair<MessageEnum.MsgReasonEnum, Flight> filterFlight(List<Flight> list) {
        //放行的航班
        return  getValidFlightByDispach(list);
    }

    /**
     * 内部报文解释
     * @param flight   航班实体列表
     * @param msgContext 报文内容
     * @return 报文体
     */
    @Override
    public List<MsgBody> parseMessage(Flight flight, String msgContext) {
        if(null==flight){
            return makeMsgBody(MessageEnum.MsgReasonEnum.SERVICE_FLIGHT_PARAM_MISSING);
        }

        //读取配置
        PropertiesConfiguration confMap = BufferConfigMgr.getInstance().getConfig("service");
        if(confMap!=null) {
            GetMainRoutePoint = ConfigBufferReader.getBoolean(confMap, "fpn.GetMainRoutePoint", true);
            MaxAlterAirportFuel=ConfigBufferReader.getBoolean(confMap, "fpn.MaxAlterAirportFuel", true);
            UplinkEverytime=ConfigBufferReader.getBoolean(confMap, "fpn.UplinkEverytime", true);
        }
        //针对一个航班，只允许成功上传一个报文，如需再次上传需要飞行员联系签派进行手工上传
        if(!UplinkEverytime &&
                (flight.getFpnState()== MessageEnum.MsgStep.SEND.getKey()
                        || flight.getFpnState()== MessageEnum.MsgStep.AK.getKey()
                        || flight.getFpnState()== MessageEnum.MsgStep.AC.getKey())){
            return makeMsgBody(MessageEnum.MsgReasonEnum.SERVICE_UPLINK_MANUAL);
        }

        List<MsgBody> retList = new ArrayList<>();
        //伴随上传PER:非空中，必要数据不能全部缺失
        if(!FlightEnum.FlightState.FLYING.eq(flight.getFlightState().getKey())
                && !(StringUtil.isBlank(flight.getAvPayload())&&
                        StringUtil.isBlank(flight.getOpnlWeight())&&
                        StringUtil.isBlank(flight.getCruiseAltitude())&&
                        StringUtil.isBlank(flight.getFobFuel())&&
                        StringUtil.isBlank(flight.getCostIndex())&&
                        StringUtil.isBlank(flight.getTxoFuel())&&
                        StringUtil.isBlank(flight.getRouteAvgTemp()))){
            UpLinkPER per=PerServiceImpl.convertToPer(flight);
            MsgBody msg=new MsgBody();
            msg.setMsgType(MessageType.PER.getKey());
            msg.setErr(MessageEnum.MsgReasonEnum.SERVICE_REQ_SUCC);
            msg.setContent(per);
            msg.setOutput(per.toOutputString());
            retList.add(msg);
        }


        //FPN
        UpLinkFPN uplink = convertToFpn(flight);
        retList.add(makeFpnMsgBody(uplink));

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
        DownLinkFPN request = convertRequest(msgContext);
        if(null==request || null==flight){
            return makeMsgBody(MessageEnum.MsgReasonEnum.SERVICE_FLIGHT_PARAM_MISSING);
        }

        //FPN
        List<MsgBody> retList = new ArrayList<>();
        UpLinkFPN uplink = convertToFpnManual(flight,request);
        MsgBody msg=makeFpnMsgBody(uplink);
        msg.setInput(request.toInputString());
        retList.add(msg);

        return retList;
    }

    /**
     * 将报文构建为实体，得到报文头与报文体
     *
     * @param message 解密后的报文
     * @return 原始报文实体
     */
    private static DownLinkFPN convertRequest(String message) {
        DownLinkFPN request = JSON.parseObject(message, DownLinkFPN.class);
        return request;
    }

    /**
     * 转换成FPN对象
     * @param flight
     * @return
     */
    private static UpLinkFPN convertToFpn(Flight flight){
        UpLinkFPN resq = new UpLinkFPN();
        resq.setmFPNType(FlightConst.MAIN_PLAN);//主计划
        resq.setmFliNo(flight.getFlightNo());  //航班号
        resq.setmPCompanyRoute(flight.getMainCompanyRoute());//主航路公司航路名称
        resq.setmPDepAirport(flight.getDepartureAirport());//起飞机场
        resq.setmPArrAirport(flight.getArrivalAirport());//着陆机场
        if(GetMainRoutePoint || FlightConst.ROUTE_SPECIAL == flight.getMainSpecial()
                || StringUtil.isBlank(flight.getMainCompanyRoute())){
            resq.setmFWaypointList(splitRoutePoint(flight.getMainRoutePoint()));//主航路的航路点
        }

        //取备降油耗最大的bei备降场
        resq.setmRaDepAirport(flight.getArrivalAirport());//备降起飞机场
        if(MaxAlterAirportFuel){
            String airport = "";
            int alterSpecial = FlightConst.ROUTE_NORMAL;
            String alterCompanyRoute = "";
            String alterRoutePoint = "";

            int fuel = 0;
            if(!StringUtil.isBlank(flight.getAlterRoute1Fuel())){
                fuel=Integer.valueOf(flight.getAlterRoute1Fuel());
                airport = flight.getAlterAirport1();
                alterSpecial = flight.getAlter1Special();
                alterCompanyRoute = flight.getAlterCompanyRoute1();
                alterRoutePoint = flight.getAlterRoute1Point();
            }

            if(!StringUtil.isBlank(flight.getAlterRoute2Fuel())){
                int tmp =Integer.valueOf(flight.getAlterRoute2Fuel());
                if(tmp>fuel){
                    fuel=tmp;
                    airport = flight.getAlterAirport2();
                    alterSpecial = flight.getAlter2Special();
                    alterCompanyRoute = flight.getAlterCompanyRoute2();
                    alterRoutePoint = flight.getAlterRoute2Point();
                }
            }

            if(!StringUtil.isBlank(flight.getAlterRoute3Fuel())){
                int tmp =Integer.valueOf(flight.getAlterRoute3Fuel());
                if(tmp>fuel){
                    fuel=tmp;
                    airport = flight.getAlterAirport3();
                    alterSpecial = flight.getAlter3Special();
                    alterCompanyRoute = flight.getAlterCompanyRoute3();
                    alterRoutePoint = flight.getAlterRoute3Point();
                }
            }

            resq.setmRaArrAirport(airport);
            resq.setmRaCompanyRoute(alterCompanyRoute);
            if(FlightConst.ROUTE_SPECIAL == alterSpecial
                    || StringUtil.isBlank(alterCompanyRoute)){
                resq.setmFBWaypointList1(splitRoutePoint(alterRoutePoint));

            }
        }else {
            resq.setmRaArrAirport(flight.getAlterAirport1()); //备降着落机场1
            resq.setmRaCompanyRoute(flight.getAlterCompanyRoute1());//备降公司航路名称
            if(FlightConst.ROUTE_SPECIAL == flight.getAlter1Special()
                    || StringUtil.isBlank(flight.getAlterCompanyRoute1())){
                resq.setmFBWaypointList1(splitRoutePoint(flight.getAlterRoute1Point()));//备用航路航路点1
            }
        }

        return resq;
    }

    private static UpLinkFPN convertToFpnManual(Flight flight,DownLinkFPN request){
        UpLinkFPN resq = new UpLinkFPN();
        resq.setmFliNo(flight.getFlightNo());  //航班号

        resq.setmFPNType(request.getmFPNType());
        resq.setmPCompanyRoute(request.getmPCompanyRoute());//主航路公司航路名称
        resq.setmPDepAirport(request.getmPDepAirport());//起飞机场
        resq.setmPArrAirport(request.getmPArrAirport());//着陆机场
        resq.setmFWaypointList(splitRoutePoint(request.getmFWaypointList()));//主航路的航路点

        resq.setmRaDepAirport(request.getmRaDepAirport());//备降起飞机场
        resq.setmRaArrAirport(request.getmRaArrAirport()); //备降着落机场1
        resq.setmRaCompanyRoute(request.getmRaCompanyRoute());//备降公司航路名称
        resq.setmFBWaypointList1(splitRoutePoint(request.getmFBWaypointList1()));//备用航路航路点1

        return resq;
    }

    private MsgBody makeFpnMsgBody(UpLinkFPN uplink) {
        MsgBody msg=new MsgBody();
        msg.setMsgType(MessageType.FPN.getKey());
        //航班必要信息缺失
        if(StringUtil.isBlank(uplink.getmFliNo()) ||
                (StringUtil.isBlank(uplink.getmPCompanyRoute())
                        || CollectionUtils.isEmpty(uplink.getmFWaypointList())
                        || uplink.getmFWaypointList().size()<5)){
            msg.setErr(MessageEnum.MsgReasonEnum.SERVICE_FLIGHT_PARAM_MISSING);
        }else{
            msg.setErr(MessageEnum.MsgReasonEnum.SERVICE_REQ_SUCC);
            msg.setContent(uplink);
            msg.setOutput(uplink.toOutputString());//输出
        }
        return msg;
    }

    /**
     * 分割航路点
     * @param allRoutePoint
     * @return
     */
    private static List<Waypoint> splitRoutePoint(String allRoutePoint){
        if(StringUtils.isBlank(allRoutePoint)) return null;

        List<Waypoint> list=new ArrayList<Waypoint>();
        String [] mianPointSplit= StringUtils.split(allRoutePoint,",");
        for(int i=0;i<mianPointSplit.length;i++){
            if(StringUtils.isBlank(mianPointSplit[i]))continue;

            Waypoint point=new Waypoint();
            point.setmName(mianPointSplit[i]);
            list.add(point);
        }
        return list.size()>0?list:null;
    }
}
