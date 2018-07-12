package com.simple.core.route.flight.service.impl;


import com.simple.common.utils.DateTimeUtil;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.route.flight.cache.MsgValidCache;
import com.simple.core.route.flight.dao.FlightDao;
import com.simple.core.route.flight.entity.MsgValidTm;
import com.simple.core.route.flight.service.FlightService;
import com.simple.core.common.entity.Flight;
import com.simple.core.common.enums.FlightEnum;
import com.simple.core.common.enums.FlightEnum.FlightState;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 航班信息获取实现
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.15 19:33.
 */
@Service("flightService")
public class FlightServiceImpl implements FlightService {
    private static Logger logger = LoggerFactory.getLogger(FlightServiceImpl.class);

    @Resource(name = "flightDao")
    private FlightDao flightDao;

    /**
     * 1. 根据报文时间，计算航班日期.(如果 d+05:00 <= t <= (d+1)+05:00，则航班日期=t,否航班日期=t-1);<br>
     * 2. 根据航班日期，获取该航班日期的该飞机号的所有航班;<br>
     * 3. 对航班列表按std升序排列;<br>
     * 4. 过滤航班:<br>
     *
     * @param carrierIata 航空公司二字码
     * @param acReg       航班号
     * @param msgType     报文类型
     * @param msgTm       报文请求时间
     * @return
     */
    @Override
    public Pair<MessageEnum.MsgReasonEnum,List<Flight>> getFlight(String carrierIata,
                                                                  String acReg, String msgType, Date msgTm) {
        if (!isValidParam(acReg, msgType, msgTm)) {
            return Pair.of(MessageEnum.MsgReasonEnum.REQ_ROUTE_PARAMINVALID,null);
        }

        List<Flight> list = findFlightList(acReg, msgTm);
        if (list == null || list.isEmpty()) {
            return Pair.of(MessageEnum.MsgReasonEnum.ROUTE_MSG_FLIGHT_NOTFOUND,null);
        }
        sortByMsgStdTm(list);
        List<Flight> lst = filterByState(list, carrierIata, msgType);
        return filterConfig(lst, msgType, msgTm);
    }

    @Override
    public List<MsgValidTm> findMsgValidTms() {
        return flightDao.findMsgValidTms();
    }

    @Override
    public Flight getFlight(Date flightDate, String flightNo, String departureAirport, String arrivalAirport, Date std) {
        return flightDao.getFlight(flightDate, flightNo, departureAirport, arrivalAirport, std);
    }

    /**
     * 获取有效航班:H:已执行;I:执行中;W:未执行.<br>
     * 航班三种情况:
     * 1. W1,W2,W3,W4 全部未执行，取W1;<br>
     * 2. H1,W2,W3,W4 有已/未执行，取H1,W2;<br>
     * 3. H1,I2,W3,W4 有已/正执行，取I2.<br>
     * 4. H1,H2,H3,H4 全部已执行，取H4.<br>
     *
     * @param list        航班列表
     * @param carrierIata 航空公司二字码
     * @param msgType     报文类型
     * @return 有效航班
     */
    private List<Flight> filterByState(List<Flight> list, String carrierIata, String msgType) {
        List<Flight> lst = new ArrayList<>();
        Pair<Integer, Integer> pair = MsgValidCache.getInstance().getRangeMinute(carrierIata, msgType);

        //倒序遍历
        Collections.reverse(list);
        for (Flight f : list) {
            //过滤取消，备降，返航的航班
            if (f == null || FlightEnum.FLIGHT_CANCEL.equals(f.getFlgCS())
                    || FlightEnum.FLIGHT_ALTER.equals(f.getFlgVR())
                    || FlightEnum.FLIGHT_RETURN.equals(f.getFlgVR())) {
                continue;
            }
            setTmState(f, pair);
            f.setCarrierIata(carrierIata);

            if (add2List(lst, f)) {
                break;
            }
        }

        //最多取前两条航班
        Collections.reverse(lst);
        if(lst.size()>=2) return lst.subList(0,2);
        return lst;
    }

    /**
     * 根据不同条件往结果列表中添加航班
     *
     * @param lst    航班列表
     * @param flight 航班
     * @return boolean:true=终止
     */
    private boolean add2List(List<Flight> lst, Flight flight) {
        if (FlightState.isFlying(flight.getFlightState().getKey())) {
            lst.clear();
            lst.add(flight);
            return true;
        }
        if (FlightState.isLand(flight.getFlightState().getKey())) {
            lst.add(flight);
            return true;
        }

        lst.add(flight);
        return false;
    }


    /**
     * 筛选报文类型、航班状态、报文请求时间符合的航班
     *
     * @param list    航班列表
     * @param msgType 报文类型
     * @param msgTm   报文时间
     * @return 航班列表
     */
    private Pair<MessageEnum.MsgReasonEnum,List<Flight>> filterConfig(List<Flight> list, String msgType, Date msgTm) {
        List<Flight> lst = new ArrayList<>();
        MessageEnum.MsgReasonEnum reason = MessageEnum.MsgReasonEnum.ROUTE_MSG_FLIGHT_NOTFOUND;
        for (Flight e : list) {
            logger.info("Cur Flight state :{}, and flight detail info:{}",e.getFlightState().getValue(),e.toString());
            //是否允许
            if (!MsgValidCache.getInstance().isPermit(e.getCarrierIata(), msgType, e.getFlightState())) {
                reason = MessageEnum.MsgReasonEnum.ROUTE_MSG_FLIGHTSTATUS_NOTPERMIT;
                continue;
            }

            //航后：在时间范围内
            if (FlightState.isLand(e.getFlightState().getKey())
                &&!DateTimeUtil.isBetweenTwoDate(msgTm, e.getMsgBeginTm(), e.getMsgEndTm())) {
                reason = MessageEnum.MsgReasonEnum.ROUTE_MSG_FLIGHT_OUTRANGE;
                continue;
            }
            //空中：>开始时间
            if (FlightState.isFlying(e.getFlightState().getKey())
                    &&msgTm.before(e.getMsgBeginTm())) {
                reason = MessageEnum.MsgReasonEnum.ROUTE_MSG_FLIGHT_OUTRANGE;
                continue;
            }
            //航前：>开始时间
            if (FlightState.isPreparing(e.getFlightState().getKey())
                    &&msgTm.before(e.getMsgBeginTm())) {
                reason = MessageEnum.MsgReasonEnum.ROUTE_MSG_FLIGHT_OUTRANGE;
                continue;
            }

            lst.add(e);
        }
        if(lst.size()<1) return Pair.of(reason,null);
        return Pair.of(null,lst);
    }

    /**
     * 设置航班状态、航班允许的报文时间范围.
     *
     * @param flight 航班
     * @param pair   时间范围
     */
    private void setTmState(Flight flight, Pair<Integer, Integer> pair) {
        flight.setMsgBeginTm(DateTimeUtil.subMinutes(flight.getFlightTd(), pair.getKey()));
        flight.setMsgEndTm(DateTimeUtil.addMinutes(flight.getFlightTa(), pair.getValue()));

        if (flight.getAta() != null) {
            flight.setFlightState(FlightState.LAND);
            return;
        }
        if (flight.getAtd() != null) {
            flight.setFlightState(FlightState.FLYING);
            return;
        }
        flight.setFlightState(FlightState.PREPARING);
    }

    /**
     * if crewArrivalTime; dispatchPassTime
     * 根据报文时间计算航班日期，根据飞机号与航班日期获取航班列表
     *
     * @param acReg 飞机号
     * @param msgTm 报文时间
     * @return 航班列表
     */
    private List<Flight> findFlightList(String acReg, Date msgTm) {
        Date flightDt = calcFlightDt(msgTm);
        List<Flight> list = flightDao.findFlights(acReg, flightDt);
        if (list == null || list.isEmpty()) {
            logger.debug("acReg:{},msgTm:{},flightDt:{},can not find Flight list.", acReg, msgTm, flightDt);
            return new ArrayList<>();
        }

        return list;
    }

    /**
     * 将航班按std升序排列
     *
     * @param list 航班列表
     */
    private void sortByMsgStdTm(List<Flight> list) {
        if (list == null || list.size() == 1) {
            return;
        }
        Collections.sort(list, new Comparator<Flight>() {
            @Override
            public int compare(Flight o1, Flight o2) {
                return o1.getStd().compareTo(o2.getStd());
            }
        });
    }

    /**
     * 验证参数是否有效
     *
     * @param acReg   航班号
     * @param msgTm   报文请求时间
     * @param msgType 报文类型
     * @return true/false
     */
    private boolean isValidParam(String acReg, String msgType, Date msgTm) {
        return acReg != null && msgType != null && msgTm != null;
    }

    /**
     * 计算航班日期.<br>
     * if (msgDt 05:00) < msgTm < (msgDt+1 05:00) .<br>
     * then flightDt = msgDt.<br>
     * else flightDt = msgDt - 1.<br>
     *
     * @param msgTm 报文时间
     * @return 航班日期
     */
    private Date calcFlightDt(Date msgTm) {
        int hours = 5;
        Date curDate = DateTimeUtil.truncDate(msgTm);
        Date flightDtBegin = DateTimeUtil.addHours(curDate, hours);
        Date flightDtEnd = DateTimeUtil.getNextDt(flightDtBegin);
        return DateTimeUtil.isBetweenTwoDate(msgTm, flightDtBegin, flightDtEnd) ? curDate : DateTimeUtil.getYesterdayDt(curDate);
    }
}
