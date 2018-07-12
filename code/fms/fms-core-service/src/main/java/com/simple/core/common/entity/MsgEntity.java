package com.simple.core.common.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 报文解释实体
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.01.12 11:02.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MsgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private String msgTm;
    /**
     * 报文通道
     */
    private String channel;
    /**
     * 原始报文
     */
    private String origMsg;
    /**
     * 报文内容
     */
    private String content;

    /**
     * 飞机号
     */
    private String acrNo;

    public MsgEntity() {
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

    public String getAcrNo()
    {
        return acrNo;
    }

    public void setAcrNo(String acrNo)
    {
        this.acrNo = acrNo;
    }
}
