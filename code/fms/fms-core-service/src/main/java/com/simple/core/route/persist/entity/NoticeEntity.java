package com.simple.core.route.persist.entity;

import com.simple.common.entity.BaseEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.01.18 15:09.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NoticeEntity extends BaseEntity {

    /**
     * 报文标识
     */
    private String uuid;
    /**
     * 报文类型=通知报文
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
     * 报文状态
     */
    private int msgStatus;

    /**
     * 原因
     */
    private int msgReason;

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

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public int getMsgReason() {
        return msgReason;
    }

    public void setMsgReason(int msgReason) {
        this.msgReason = msgReason;
    }



    @Override
    public Date getCreateTm() {
        return super.getCreateTm();
    }

    @Override
    public void setCreateTm(Date createTm) {
        super.setCreateTm(createTm);
    }

    @Override
    public String toString() {
        return "NoticeEntity{" +
                "uuid='" + uuid + '\'' +
                ", msgType='" + msgType + '\'' +
                ", msgTm='" + msgTm + '\'' +
                ", channel='" + channel + '\'' +
                ", msgStatus=" + msgStatus +
                ", msgReason=" + msgReason +
                '}';
    }
}
