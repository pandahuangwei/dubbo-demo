package com.simple.core.common.service;

import com.simple.core.common.entity.Flight;
import com.simple.core.common.entity.MsgBody;
import com.simple.core.common.enums.FlightEnum.CrewArrivalStatus;
import com.simple.core.common.enums.FlightEnum.DispatchStatus;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.common.enums.MessageEnum.MsgReasonEnum;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: AbstractCoreMsgService.java.
 * @Package com.simple.core.common.service.
 * @Description: 核心报文服务抽象类.
 * 
 * @author Administrator.
 * @version 1.0.
 * @created 2017/3/15 19:22.
 **********************************************************************************/
public abstract class AbstractCoreMsgService{

    /**
     * 根据业务过滤航班
     * @param list
     * @return <错误代码,航班>
     */
    public abstract Pair<MessageEnum.MsgReasonEnum, Flight> filterFlight(List<Flight> list);

    /**
     * 内部报文解释
     * @param flight   航班实体列表
     * @param msgContext 报文内容
     * @return 报文体
     */
    public abstract List<MsgBody> parseMessage(Flight flight, String msgContext);

    /**
     * 手动发起报文请求服务
     * @param flight
     * @param msgContext
     * @return
     */
    public abstract List<MsgBody> parseMessageManual(Flight flight, String msgContext);

    /**
     * 根据业务过滤航班
     * 根据 签派放行、机组到位 状态过滤
     * FPN请求必须满足签派放行=1，且机组到位=1
     * @param list
     * @return
     */
    public static Pair<MessageEnum.MsgReasonEnum,Flight> getValidFlightByDispach(List<Flight> list) {
        //没有签派放行、机组到位状态时，返回最后一个航班
        if(CollectionUtils.isEmpty(list)) {
            return Pair.of(MsgReasonEnum.REQ_SERVICE_FAIL, null);
        }

        Flight test = list.get(0);
        if(DispatchStatus.NULL.eq(test.getDispatch())
                && CrewArrivalStatus.NULL.eq(test.getCrewArrival())){
            return Pair.of(MsgReasonEnum.SERVICE_REQ_SUCC,list.get(list.size()-1));
        }

        //有签派放行、机组到位状态时，返回最后一个符合条件的航班
        for(int i=list.size()-1;i>=0;i--){
            Flight flight = list.get(i);
            if(DispatchStatus.YES.eq(flight.getDispatch())
                    && CrewArrivalStatus.YES.eq(flight.getCrewArrival())){
                return Pair.of(MsgReasonEnum.SERVICE_REQ_SUCC,flight);
            }
            if(DispatchStatus.NULL.eq(flight.getDispatch())
                    && CrewArrivalStatus.YES.eq(flight.getCrewArrival())){
                return Pair.of(MsgReasonEnum.SERVICE_REQ_SUCC,flight);
            }
            if(DispatchStatus.YES.eq(flight.getDispatch())
                    && CrewArrivalStatus.NULL.eq(flight.getCrewArrival())){
                return Pair.of(MsgReasonEnum.SERVICE_REQ_SUCC,flight);
            }
        }
        return Pair.of(MsgReasonEnum.SERVICE_REJECT_FLIGHT_NOT_DISPACH,null);
    }

    public static List<MsgBody> makeMsgBody(MessageEnum.MsgReasonEnum reson){
        List<MsgBody> list = new ArrayList<>();
        MsgBody msg=new MsgBody();
        msg.setErr(reson);
        list.add(msg);
        return list;
    }

    /**
     * 千克转换成百磅
     * @param kg
     * @return
     */
    public static int KGToLB100(int kg){
        double lb = kg*2.2046226;
        return (int)(lb/100);
    }
}
