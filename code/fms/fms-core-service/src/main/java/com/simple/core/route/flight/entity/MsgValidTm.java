package com.simple.core.route.flight.entity;

/**
 * 用于验证报文调用验证
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.15 19:10.
 */
public class MsgValidTm {
    /**
     * 航空公司二字码
     */
    private String carrierIata;
    /**
     * 报文类型
     */
    private String msgType;
    /**
     * 航前(1有效，0无效)
     */
    private boolean deptState;

    /**
     * 飞行中(1有效，0无效)
     */
    private boolean flyState;
    /**
     * 到达(1有效，0无效)
     */
    private boolean arriveState;
    /**
     * 航前开始时间(分钟数)
     */
    private int deptBegin;
    /**
     * 到达结束时间(分钟数)
     */
    private int arriveEnd;

    private boolean active;

    public String getCarrierIata() {
        return carrierIata;
    }

    public void setCarrierIata(String carrierIata) {
        this.carrierIata = carrierIata;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public boolean isDeptState() {
        return deptState;
    }

    public void setDeptState(boolean deptState) {
        this.deptState = deptState;
    }

    public boolean isFlyState() {
        return flyState;
    }

    public void setFlyState(boolean flyState) {
        this.flyState = flyState;
    }

    public boolean isArriveState() {
        return arriveState;
    }

    public void setArriveState(boolean arriveState) {
        this.arriveState = arriveState;
    }

    public int getDeptBegin() {
        return deptBegin;
    }

    public void setDeptBegin(int deptBegin) {
        this.deptBegin = deptBegin;
    }

    public int getArriveEnd() {
        return arriveEnd;
    }

    public void setArriveEnd(int arriveEnd) {
        this.arriveEnd = arriveEnd;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "MsgValidTm{" +
                "carrierIata='" + carrierIata + '\'' +
                ", msgType='" + msgType + '\'' +
                ", deptState=" + deptState +
                ", flyState=" + flyState +
                ", arriveState=" + arriveState +
                ", deptBegin=" + deptBegin +
                ", arriveEnd=" + arriveEnd +
                ", active=" + active +
                '}';
    }
}
