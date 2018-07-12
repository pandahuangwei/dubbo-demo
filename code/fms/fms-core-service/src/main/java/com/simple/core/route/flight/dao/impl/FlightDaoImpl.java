package com.simple.core.route.flight.dao.impl;

import com.simple.common.persistence.BaseDao;
import com.simple.core.common.entity.Flight;
import com.simple.core.route.flight.dao.FlightDao;
import com.simple.core.route.flight.entity.MsgValidTm;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 航班信息DAO实现
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.15 19:37.
 */
@Repository("flightDao")
public class FlightDaoImpl extends BaseDao implements FlightDao {

    private String NAMESPACE = "FlightMapper";

    @Override
    public List<Flight> findFlights(String acReg, Date flightDt) {
        return dao.findList(getStatement("selectFlightBases"), createSearchMap(acReg, flightDt));
    }

    @Override
    public List<MsgValidTm> findMsgValidTms() {
        return dao.findList(getStatement("selectMsgValidTm"), null);
    }

    private Map<String, Object> createSearchMap(String acReg, Date flightDt) {
        Map<String, Object> map = new HashMap<>();
        map.put("acReg", acReg);
        map.put("flightDt", flightDt);
        return map;
    }

    @Override
    public Flight getFlight(Date flightDate, String flightNo, String departureAirport, String arrivalAirport, Date std) {
        return dao.findOne(getStatement("selectFlightBases2"), createGetFlightMap(flightDate, flightNo, departureAirport, arrivalAirport, std));
    }

    private Map<String, Object> createGetFlightMap(Date flightDate, String flightNo, String departureAirport, String arrivalAirport, Date std) {
        Map<String, Object> map = new HashMap<>();
        map.put("flightDate", flightDate);
        map.put("flightNo", flightNo);
        map.put("departureAirport", departureAirport);
        map.put("arrivalAirport", arrivalAirport);
        map.put("std", std);
        return map;
    }

    @Override
    protected String getStatement(String sqlId) {
        return super.getStatement(NAMESPACE, sqlId);
    }

}
