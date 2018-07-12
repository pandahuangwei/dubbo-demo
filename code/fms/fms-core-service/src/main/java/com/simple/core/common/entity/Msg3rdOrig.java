package com.simple.core.common.entity;

import java.util.Date;

/**
 * 第三方调用上传报文输入参数
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.10 16:54.
 */
public class Msg3rdOrig extends MsgOrig {
    /**
     * 航班日期
     */
    private Date flightDate;
    /**
     * 航班号
     */
    private String flightNo;
    /**
     * 起飞站
     */
    private String departureAirport;
    /**
     * 落地站
     */
    private String arrivalAirport;
    /**
     * 计划起飞时间
     */
    private Date std;


    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Date getStd() {
        return std;
    }

    public void setStd(Date std) {
        this.std = std;
    }


}
