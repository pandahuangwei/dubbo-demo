package com.simple.core.common.entity;

import com.simple.core.common.enums.FlightEnum.FlightState;

import java.util.Date;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.23 10:41.
 */
public class Flight extends FlightBase {
    private static final long serialVersionUID = 2L;
    /**
     * FPN最新状态
     */
    private int fpnState;
    /**
     * 最新状态更新时间
     */
    private Date fpnTm;
    /**
     * LDI最新状态
     */
    private int ldiState;
    /**
     * LDI最新状态更新时间
     */
    private Date ldiTm;
    /**
     * PWI最新状态
     */
    private int pwiState;
    /**
     * 最新状态更新时间
     */
    private Date pwiTm;
    /**
     * PER最新状态
     */
    private int perState;
    /**
     * PER最新状态更新时间
     */
    private Date perTm;
    /**
     * 航班状态(FlightState)0已执行,1执行中,2未执行
     */
    private FlightState flightState;
    /**
     * 航班报文有效开始时间.
     */
    private Date msgBeginTm;
    /**
     * 航班报文有效结束时间.
     */
    private Date msgEndTm;

    /**
     * 依次取atd ,etd,std不为null的值
     */
    public Date getFlightTd() {
        return getAtd() != null ? getAtd() : (getEtd() != null ? getEtd() : getStd());
    }

    /**
     * 依次取ata ,eta,sta不为null的值
     */
    public Date getFlightTa() {
        return getAta() != null ? getAta() : (getEta() != null ? getEta() : getSta());
    }

    public int getFpnState() {
        return fpnState;
    }

    public void setFpnState(int fpnState) {
        this.fpnState = fpnState;
    }

    public Date getFpnTm() {
        return fpnTm;
    }

    public void setFpnTm(Date fpnTm) {
        this.fpnTm = fpnTm;
    }

    public int getLdiState() {
        return ldiState;
    }

    public void setLdiState(int ldiState) {
        this.ldiState = ldiState;
    }

    public Date getLdiTm() {
        return ldiTm;
    }

    public void setLdiTm(Date ldiTm) {
        this.ldiTm = ldiTm;
    }

    public int getPwiState() {
        return pwiState;
    }

    public void setPwiState(int pwiState) {
        this.pwiState = pwiState;
    }

    public Date getPwiTm() {
        return pwiTm;
    }

    public void setPwiTm(Date pwiTm) {
        this.pwiTm = pwiTm;
    }

    public int getPerState() {
        return perState;
    }

    public void setPerState(int perState) {
        this.perState = perState;
    }

    public Date getPerTm() {
        return perTm;
    }

    public void setPerTm(Date perTm) {
        this.perTm = perTm;
    }

    public FlightState getFlightState() {
        return flightState;
    }

    public void setFlightState(FlightState flightState) {
        this.flightState = flightState;
    }

    public Date getMsgBeginTm() {
        return msgBeginTm;
    }

    public void setMsgBeginTm(Date msgBeginTm) {
        this.msgBeginTm = msgBeginTm;
    }

    public Date getMsgEndTm() {
        return msgEndTm;
    }

    public void setMsgEndTm(Date msgEndTm) {
        this.msgEndTm = msgEndTm;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "fpnState=" + fpnState +
                ", fpnTm=" + fpnTm +
                ", ldiState=" + ldiState +
                ", ldiTm=" + ldiTm +
                ", pwiState=" + pwiState +
                ", pwiTm=" + pwiTm +
                ", perState=" + perState +
                ", perTm=" + perTm +
                ", flightState=" + flightState +
                ", msgBeginTm=" + msgBeginTm +
                ", msgEndTm=" + msgEndTm +
                "} " + super.toString();
    }
}
