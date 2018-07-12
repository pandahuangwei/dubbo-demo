package com.simple.core.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知报文实体
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.06 14:52.
 */
public class NoticeMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 飞机号
     */
    private String acReg;
    /**
     * 报文标识
     */
    private String uuid;
    /**
     * 报文类型
     */
    private String msgType;
    /**
     * 报文时间
     */
    private Date msgTm;
    /**
     * 报文通道
     */
    private String channel;

    /**
     * 空地通讯系统维护的报文状态false失败，true成功
     */
    private boolean status;

    /**
     * 错误原因
     */
    private int errReason;
    /**
     * 原始报文内容
     */
    private String origMsg;
    /**
     * 报文内容
     */
    private String content;
    /**
     * 报文大小(B)
     */
    private int msgSize = 0;
    /**
     * 对应枚举 MsgStep 的key
     */
    private int msgStep;

    public String getAcReg() {
        return acReg;
    }

    public void setAcReg(String acReg) {
        this.acReg = acReg;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMsgType() {
        return msgType;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public String getOrigMsg() {
        return origMsg;
    }

    public void setOrigMsg(String origMsg) {
        this.origMsg = origMsg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMsgSize() {
        return msgSize;
    }

    public void setMsgSize(int msgSize) {
        this.msgSize = msgSize;
    }

    public int getMsgStep() {
        return msgStep;
    }

    public void setMsgStep(int msgStep) {
        this.msgStep = msgStep;
    }
}
