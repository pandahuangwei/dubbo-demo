package com.simple.core.route.flight.service;


import com.simple.core.common.entity.Flight;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.route.flight.entity.MsgValidTm;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.List;

/**
 * 航班信息获取接口
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.15 19:28.
 */
public interface FlightService {

    /**
     * 取航班信息
     *
     * @param carrierIata 航空公司二字码
     * @param acReg       航班号
     * @param msgTm       报文请求时间
     * @param msgType     报文类型
     * @return 航班信息
     */
    Pair<MessageEnum.MsgReasonEnum, List<Flight>> getFlight(String carrierIata, String acReg, String msgType, Date msgTm);

    /**
     * 获取报文类型的时间验证
     *
     * @return list
     */
    List<MsgValidTm> findMsgValidTms();

    /**
     * 航班号
     *
     * @param flightDate       航班日期
     * @param flightNo         航班号
     * @param departureAirport 起飞站
     * @param arrivalAirport   落地站
     * @param std              计划起飞时间
     * @return
     */
    Flight getFlight(Date flightDate, String flightNo, String departureAirport, String arrivalAirport, Date std);

}
