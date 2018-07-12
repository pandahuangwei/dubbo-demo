package com.simple.core.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.18 13:44.
 */
public class MQMsgEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 飞机号
     */
    private String acReg;

    /**
     * 航班ID
     */
    private String flightId;

    /**
     * 航班日期
     */
    private Date flightDt;

    /**
     * 航班号
     */
    private String flightNo;

    /**
     * 起飞机场
     */
    private String deptAirport;

    /**
     * 落地机场
     */
    private String arriveAirport;

    /**
     * 计划起飞时间
     */
    private Date std;
    /**
     * 报文类型
     */
    private String msgType;
    /**
     * 报文状态(与其他保持一致)
     */
    private int msgStep;

    public String getAcReg() {
        return acReg;
    }

    public void setAcReg(String acReg) {
        this.acReg = acReg;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
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

    public String getArriveAirport() {
        return arriveAirport;
    }

    public void setArriveAirport(String arriveAirport) {
        this.arriveAirport = arriveAirport;
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

    public int getMsgStep() {
        return msgStep;
    }

    public void setMsgStep(int msgStep) {
        this.msgStep = msgStep;
    }

    @Override
    public String toString() {
        return "MQMsgEntity{" +
                "acReg='" + acReg + '\'' +
                ", flightId='" + flightId + '\'' +
                ", flightDt=" + flightDt +
                ", flightNo='" + flightNo + '\'' +
                ", deptAirport='" + deptAirport + '\'' +
                ", arriveAirport='" + arriveAirport + '\'' +
                ", std=" + std +
                ", msgType='" + msgType + '\'' +
                ", msgStep=" + msgStep +
                '}';
    }
}
