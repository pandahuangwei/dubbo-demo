package com.simple.core.charging.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.14 17:24.
 */
public class MsgSize implements Serializable {
    private static final long serialVersionUID = 1L;

    private String msgId;

    private Date createTm;
    /**
     * 公司二字码
     */
    private String carrierIata;
    /**
     * 飞机号
     */
    private String acReg;
    /**
     * 航班日期
     */
    private Date flightDt;
    /**
     * 航班号
     */
    private String flightNo;
    /**
     * 起飞站
     */
    private String deptAirport;
    /**
     * 落地站
     */
    private String arrivalAirport;
    /**
     * 计划起飞时间
     */
    private Date std;
    /**
     * 报文类型
     */
    private String msgType;
    /**
     * 报文大小B
     */
    private Integer msgSize;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Date getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Date createTm) {
        this.createTm = createTm;
    }

    public String getCarrierIata() {
        return carrierIata;
    }

    public void setCarrierIata(String carrierIata) {
        this.carrierIata = carrierIata;
    }

    public String getAcReg() {
        return acReg;
    }

    public void setAcReg(String acReg) {
        this.acReg = acReg;
    }

    public Date getFlightDt() {
        return flightDt;
    }

    public void setFlightDt(Date flightDt) {
        this.flightDt = flightDt;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getDeptAirport() {
        return deptAirport;
    }

    public void setDeptAirport(String deptAirport) {
        this.deptAirport = deptAirport;
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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Integer getMsgSize() {
        return msgSize;
    }

    public void setMsgSize(Integer msgSize) {
        this.msgSize = msgSize;
    }
}
