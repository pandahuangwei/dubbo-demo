package com.simple.core.common.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 报文明文
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.31 9:12.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageClear<T extends MsgBody> extends Message implements Serializable {
    private static final long serialVersionUID = 12L;

    /**
     * 报文标识
     */
    private String uuid;
    /**
     * 航班标识5个字段
     */
    private String flightDt;
    private String flightNo;
    private String pod;
    private String poa;
    private String std;
    /**
     * 飞机号
     */
    private String acReg;
    /**
     * 报文类型
     */
    private String msgType;
    /**
     * 报文时间
     */
    private String msgTm;
    /**
     * 报文通道
     */
    private String channel;
    /**
     * 主动发起的报文才有意义：0/1(否/是),可缺失
     */
    private String forceChannel;
    /**
     * 成功2/失败3
     */
    private boolean status;
    /**
     * 错误原因
     */
    private int errReason;
    /**
     * 错误原因
     */
    private String errReasonText;
    /**
     * 报文体
     */
    private List<T> contentList;

    /**
     * 添加报文体
     *
     * @param e
     */
    public void addBody(T e) {
        contentList = contentList != null ? contentList : new ArrayList<T>();
        contentList.add(e);
    }

    public void addAllBody(List<T> list) {
        contentList = contentList != null ? contentList : new ArrayList<T>();
        contentList.addAll(list);
    }

    /**
     * 设值航班标识
     *
     * @param flightDt    航班日期
     * @param flightNo    航班号
     * @param origAirPort 起飞机场
     * @param destAirPort 着陆机场
     * @param std         预计起飞时间
     */
    public void setFlight(String flightDt, String flightNo, String origAirPort, String destAirPort, String std) {
        this.flightDt = flightDt;
        this.flightNo = flightNo;
        this.pod = origAirPort;
        this.poa = destAirPort;
        this.std = std;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlightDt() {
        return flightDt;
    }

    public void setFlightDt(String flightDt) {
        this.flightDt = flightDt;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPoa() {
        return poa;
    }

    public void setPoa(String poa) {
        this.poa = poa;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getAcReg() {
        return acReg;
    }

    public void setAcReg(String acReg) {
        this.acReg = acReg;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgTm() {
        return msgTm;
    }

    public void setMsgTm(String msgTm) {
        this.msgTm = msgTm;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getForceChannel() {
        return forceChannel;
    }

    public void setForceChannel(String forceChannel) {
        this.forceChannel = forceChannel;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getErrReason() {
        return errReason;
    }

    public void setErrReason(int errReason) {
        this.errReason = errReason;
    }

    public String getErrReasonText() {
        return errReasonText;
    }

    public void setErrReasonText(String errReasonText) {
        this.errReasonText = errReasonText;
    }

    public List<T> getContentList() {
        return contentList;
    }

    public void setContentList(List<T> contentList) {
        this.contentList = contentList;
    }

    @Override
    public String toString() {
        return "MessageClear{" +
                "uuid='" + uuid + '\'' +
                ", flightDt='" + flightDt + '\'' +
                ", flightNo='" + flightNo + '\'' +
                ", pod='" + pod + '\'' +
                ", poa='" + poa + '\'' +
                ", std='" + std + '\'' +
                ", acReg='" + acReg + '\'' +
                ", msgType='" + msgType + '\'' +
                ", msgTm='" + msgTm + '\'' +
                ", channel='" + channel + '\'' +
                ", forceChannel='" + forceChannel + '\'' +
                ", status=" + status +
                ", errReason=" + errReason +
                ", errReasonText='" + errReasonText + '\'' +
                ", contentList=" + contentList +
                '}';
    }
}
