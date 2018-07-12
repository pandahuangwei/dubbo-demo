package com.simple.core.route.persist.entity;

import com.simple.common.utils.StringUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 持久化报文
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.22 16:13.
 */
public class PersistMsgInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 报文标识(报文UUID)
     */
    private String msgId;

    /**
     * 创建时间
     */
    private Date createTm;
    /**
     * 修改时间
     */
    private Date modifyTm;

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
     * 下行报文类型
     */
    private String downMsgType;

    /**
     * 下行原始报文内容
     */
    private String downMsgContent;
    /**
     * 下行报文通道
     */
    private String downMsgChannel;
    /**
     * 下行报文接收时间
     */
    private Date downReceiveTm;

    /**
     * 上行报文类型
     */
    private String upMsgType;

    /**
     * 上行原始报文内容
     */
    private String upMsgContent;
    /**
     * 上行报文通道
     */
    private String upMsgChannel;
    /**
     * 上行报文发送时间
     */
    private Date upSendTm;

    /**
     * 报文最新状态
     */
    private int msgStep;
    private int msgReason;

    /**
     * 报文类型
     */
    private String msgType;
    /**
     * 报文时间
     */
    private Date msgTm;
    private int msgGroup;
    private int msgSize = 0;

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

    public Date getModifyTm() {
        return modifyTm;
    }

    public void setModifyTm(Date modifyTm) {
        this.modifyTm = modifyTm;
    }

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

    public String getDownMsgType() {
        return downMsgType;
    }

    public void setDownMsgType(String downMsgType) {
        this.downMsgType = downMsgType;
    }

    public String getDownMsgContent() {
        return downMsgContent;
    }

    public void setDownMsgContent(String downMsgContent) {
        this.downMsgContent = downMsgContent;
    }

    public String getDownMsgChannel() {
        return downMsgChannel;
    }

    public void setDownMsgChannel(String downMsgChannel) {
        this.downMsgChannel = downMsgChannel;
    }

    public Date getDownReceiveTm() {
        return downReceiveTm;
    }

    public void setDownReceiveTm(Date downReceiveTm) {
        this.downReceiveTm = downReceiveTm;
    }

    public String getUpMsgType() {
        return upMsgType;
    }

    public void setUpMsgType(String upMsgType) {
        this.upMsgType = upMsgType;
    }

    public String getUpMsgContent() {
        return upMsgContent;
    }

    public void setUpMsgContent(String upMsgContent) {
        this.upMsgContent = upMsgContent;
    }

    public String getUpMsgChannel() {
        return upMsgChannel;
    }

    public void setUpMsgChannel(String upMsgChannel) {
        this.upMsgChannel = upMsgChannel;
    }

    public Date getUpSendTm() {
        return upSendTm;
    }

    public void setUpSendTm(Date upSendTm) {
        this.upSendTm = upSendTm;
    }

    public String getMsgType() {
        return StringUtil.isEmpty(msgType)?getDownMsgType():msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Date getMsgTm() {
        return msgTm;
    }

    public void setMsgTm(Date msgTm) {
        this.msgTm = msgTm;
    }

    public int getMsgStep() {
        return msgStep;
    }

    public void setMsgStep(int msgStep) {
        this.msgStep = msgStep;
    }

    public int getMsgReason() {
        return msgReason;
    }

    public void setMsgReason(int msgReason) {
        this.msgReason = msgReason;
    }

    public int getMsgGroup() {
        return msgGroup;
    }

    public void setMsgGroup(int msgGroup) {
        this.msgGroup = msgGroup;
    }

    public int getMsgSize() {
        return msgSize;
    }

    public void setMsgSize(int msgSize) {
        this.msgSize = msgSize;
    }

    @Override
    public String toString() {
        return "PersistMsgInfo{" +
                "msgId='" + msgId + '\'' +
                ", createTm=" + createTm +
                ", msgTm=" + msgTm +
                ", msgGroup=" + msgGroup +
                ", msgStep=" + msgStep +
                ", msgReason=" + msgReason +
                ", msgType='" + msgType + '\'' +
                '}';
    }
}
