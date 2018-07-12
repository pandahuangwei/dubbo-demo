package com.simple.core.common.entity;

import java.util.Date;

/**
 * 原始报文
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.06 19:04.
 */
public class MsgOrig {
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
    private Date msgTm;
    /**
     * 报文通道
     */
    private String channel;
    /**
     * 报文内容
     */
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MsgOrig{" +
                "acReg='" + acReg + '\'' +
                ", msgType='" + msgType + '\'' +
                ", msgTm=" + msgTm +
                ", channel='" + channel + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
