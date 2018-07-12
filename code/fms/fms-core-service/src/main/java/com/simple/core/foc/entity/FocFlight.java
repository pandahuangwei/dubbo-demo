package com.simple.core.foc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/23.
 */
public class FocFlight implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * seqT2001
     */
    private String focFlightId;
    /**
     * 航班号
     */
    private String flightNo;
    /**
     * 航班日期
     */
    private Date flightDate;
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
    /**
     * 预计起飞时间
     */
    private Date etd;
    /**
     * 实际起飞时间
     */
    private Date atd;
    /**
     * 计划到达时间
     */
    private Date sta;
    /**
     * 预计到达时间
     */
    private Date eta;
    /**
     * 实际到达时间
     */
    private Date ata;
    /**
     * 机号
     */
    private String acReg;
    /**
     * 机型
     */
    private String acType;
    /**
     * 国内国际
     */
    private String dOrI;
    /**
     * 发送RLS标志:Y/N
     */
    private String flagRLS;
    /**
     * 签派放行时间
     */
    private Date dispatchPassTime;
    /**
     * 机组到位时间
     */
    private Date crewArrivalTime;
    /**
     * 备降返航标志
     */
    private String flgVR;

    /**
     * 备降返航标志
     */
    private String flgVR1;

    /**
     * 动态取消标志
     */
    private String flgCS;
    /**
     * 修改时间
     */
    private Date modifyTm;

    public String getFlgVR1() {
        return flgVR1;
    }

    public void setFlgVR1(String flgVR1) {
        this.flgVR1 = flgVR1;
    }

    public String getFocFlightId() {
        return focFlightId;
    }

    public void setFocFlightId(String focFlightId) {
        this.focFlightId = focFlightId;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
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

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getAtd() {
        return atd;
    }

    public void setAtd(Date atd) {
        this.atd = atd;
    }

    public Date getSta() {
        return sta;
    }

    public void setSta(Date sta) {
        this.sta = sta;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
    }

    public String getAcReg() {
        return acReg;
    }

    public void setAcReg(String acReg) {
        this.acReg = acReg;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getdOrI() {
        return dOrI;
    }

    public void setdOrI(String dOrI) {
        this.dOrI = dOrI;
    }

    public Date getDispatchPassTime() {
        return dispatchPassTime;
    }

    public void setDispatchPassTime(Date dispatchPassTime) {
        this.dispatchPassTime = dispatchPassTime;
    }

    public Date getCrewArrivalTime() {
        return crewArrivalTime;
    }

    public void setCrewArrivalTime(Date crewArrivalTime) {
        this.crewArrivalTime = crewArrivalTime;
    }

    public String getFlgVR() {
        return flgVR;
    }

    public void setFlgVR(String flgVR) {
        this.flgVR = flgVR;
    }

    public String getFlgCS() {
        return flgCS;
    }

    public void setFlgCS(String flgCS) {
        this.flgCS = flgCS;
    }

    public Date getModifyTm() {
        return modifyTm;
    }

    public void setModifyTm(Date modifyTm) {
        this.modifyTm = modifyTm;
    }

    public String getFlagRLS() {
        return flagRLS;
    }

    public void setFlagRLS(String flagRLS) {
        this.flagRLS = flagRLS;
    }

    @Override
    public String toString() {
        return "FocFlight{" +
                "focFlightId='" + focFlightId + '\'' +
                ", flightNo='" + flightNo + '\'' +
                ", flightDate=" + flightDate +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", std=" + std +
                ", etd=" + etd +
                ", atd=" + atd +
                ", sta=" + sta +
                ", eta=" + eta +
                ", ata=" + ata +
                ", acReg='" + acReg + '\'' +
                ", acType='" + acType + '\'' +
                ", dOrI='" + dOrI + '\'' +
                ", dispatchPassTime=" + dispatchPassTime +
                ", crewArrivalTime=" + crewArrivalTime +
                ", flgVR='" + flgVR + '\'' +
                ", flgCS='" + flgCS + '\'' +
                ", modifyTm=" + modifyTm +
                '}';
    }
}
