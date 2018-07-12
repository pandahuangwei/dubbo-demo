package com.simple.core.per.service.impl;

import com.alibaba.fastjson.JSON;
import com.simple.common.config.BufferConfigMgr;
import com.simple.common.config.ConfigBufferReader;
import com.simple.common.utils.StringUtil;
import com.simple.core.common.entity.Flight;
import com.simple.core.common.entity.MsgBody;
import com.simple.core.common.enums.FlightEnum;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.common.enums.MessageEnum.MessageType;
import com.simple.core.common.enums.MessageEnum.MsgReasonEnum;
import com.simple.core.common.service.AbstractCoreMsgService;
import com.simple.core.per.entity.DownLinkPER;
import com.simple.core.per.entity.UpLinkPER;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */
@Service("perService")
public class PerServiceImpl extends AbstractCoreMsgService {
    private static boolean UplinkEverytime = true;

    @Override
    public Pair<MessageEnum.MsgReasonEnum, Flight> filterFlight(List<Flight> list) {
        //放行的航班
        Pair<MessageEnum.MsgReasonEnum, Flight> pair = getValidFlightByDispach(list);

        //空中不允许PER请求
        if(null!=pair && pair.getRight()!=null &&
                FlightEnum.FlightState.FLYING.eq(pair.getRight().getFlightState().getKey())){
            return Pair.of(MsgReasonEnum.SERVICE_REJECT_FLIGHT_INFLYING,null);
        }
        return pair;
    }

    /**
     *
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
            UplinkEverytime=ConfigBufferReader.getBoolean(confMap, "per.UplinkEverytime", true);
        }
        //针对一个航班，只允许成功上传一个报文，如需再次上传需要飞行员联系签派进行手工上传
        if(!UplinkEverytime &&
                (flight.getFpnState()== MessageEnum.MsgStep.SEND.getKey()
                        || flight.getFpnState()== MessageEnum.MsgStep.AK.getKey()
                        || flight.getFpnState()== MessageEnum.MsgStep.AC.getKey())){
            return makeMsgBody(MessageEnum.MsgReasonEnum.SERVICE_UPLINK_MANUAL);
        }

        //PER
        UpLinkPER uplink=convertToPer(flight);
        List<MsgBody> retList = new ArrayList<>();
        retList.add(makePerMsgBody(uplink));
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
        DownLinkPER request = convertRequest(msgContext);
        if(null==request || null==flight){
            return makeMsgBody(MessageEnum.MsgReasonEnum.SERVICE_FLIGHT_PARAM_MISSING);
        }

        //PER
        UpLinkPER uplink=convertToPerManual(flight,request);
        List<MsgBody> retList = new ArrayList<>();
        MsgBody msg=makePerMsgBody(uplink);
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
    private static DownLinkPER convertRequest(String message) {
        DownLinkPER request = JSON.parseObject(message, DownLinkPER.class);
        return request;
    }

    /**
     * 转换成PER对象
     * 高度单位：百英尺
     * 重量单位：KG->百磅
     * @param flight
     * @return
     */
    public static UpLinkPER convertToPer(Flight flight){
        UpLinkPER resq = new UpLinkPER();
        try {
            int pld = Integer.parseInt(flight.getAvPayload())+Integer.parseInt(flight.getOpnlWeight());
            resq.setZfw(String.valueOf(KGToLB100(pld)));
        } catch (NumberFormatException e) {}
        resq.setCruiseAltitude(flight.getCruiseAltitude());
        try {resq.setBlockFuel(String.valueOf(KGToLB100(Integer.parseInt(flight.getFobFuel()))));} catch (NumberFormatException e) {}
        try {resq.setCostIndex(String.valueOf(KGToLB100(Integer.parseInt(flight.getCostIndex()))));} catch (NumberFormatException e) {}
        try {resq.setTaxiFuel(String.valueOf(KGToLB100(Integer.parseInt(flight.getTxoFuel()))));} catch (NumberFormatException e) {}
        resq.setCruiseTemperature(flight.getRouteAvgTemp());
        return resq;
    }

    private static UpLinkPER convertToPerManual(Flight flight, DownLinkPER request){
        UpLinkPER resq = new UpLinkPER();
        resq.setZfw(request.getZfw());
        resq.setCruiseAltitude(request.getCruiseAltitude());
        resq.setBlockFuel(request.getBlockFuel());
        resq.setCostIndex(request.getCostIndex());
        resq.setTaxiFuel(request.getTaxiFuel());
        resq.setCruiseTemperature(request.getCruiseTemperature());
        resq.setReserveFuel(request.getReserveFuel());
        resq.setTropopauseAltitude(request.getTropopauseAltitude());
        resq.setZfwCG(request.getZfwCG());
        return resq;
    }

    private MsgBody makePerMsgBody(UpLinkPER uplink) {
        MsgBody msg=new MsgBody();
        msg.setMsgType(MessageType.PER.getKey());
        if(StringUtil.isBlank(uplink.getZfw())&&
                StringUtil.isBlank(uplink.getCruiseAltitude())&&
                StringUtil.isBlank(uplink.getBlockFuel())&&
                StringUtil.isBlank(uplink.getCostIndex())&&
                StringUtil.isBlank(uplink.getCruiseTemperature())){
            msg.setErr(MsgReasonEnum.SERVICE_FLIGHT_PARAM_MISSING);
        }else {
            msg.setErr(MsgReasonEnum.SERVICE_REQ_SUCC);
            msg.setContent(uplink);
            msg.setOutput(uplink.toOutputString());
        }
        return msg;
    }
}
