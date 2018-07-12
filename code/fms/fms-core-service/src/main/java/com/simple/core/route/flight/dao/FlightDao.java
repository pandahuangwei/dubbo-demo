package com.simple.core.route.flight.dao;


import com.simple.core.route.flight.entity.MsgValidTm;
import com.simple.core.common.entity.Flight;

import java.util.Date;
import java.util.List;

/**
 * 航班信息DAO接口
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.15 19:36.
 */
public interface FlightDao {

    /**
     * 获取航班列表
     * @param acReg 飞机号
     * @param flightDt 航班日期
     * @return 航班列表
     */
    List<Flight> findFlights(String acReg, Date flightDt);

    /**
     * 获取航班报文类型的有效时间范围配置
     * @return list
     */
    List<MsgValidTm> findMsgValidTms();

    Flight getFlight(Date flightDate, String flightNo, String departureAirport, String arrivalAirport, Date std);
}
