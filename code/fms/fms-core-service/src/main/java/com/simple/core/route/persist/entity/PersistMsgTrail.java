package com.simple.core.route.persist.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 持久化报文轨迹
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.22 16:46.
 */
public class PersistMsgTrail implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    private String trailId;

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
     * 报文类型
     */
    private String msgType;
    /**
     * 报文状态
     */
    private Integer msgState;
    /**
     * 报文状态
     */
    private Integer msgReason;

    /**
     * 报文通道
     */
    private String msgChannel;
    /**
     * 应答报文
     */
    private String replyContent;
    /**
     * 0Send,1AC,2AK
     */
    private Integer msgGroup;

    public void setMsgState(boolean state) {
        this.msgState = state ? 1 : 0;
    }


    public String getTrailId() {
        return trailId;
    }

    public void setTrailId(String trailId) {
        this.trailId = trailId;
    }

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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Integer getMsgState() {
        return msgState;
    }

    public void setMsgState(Integer msgState) {
        this.msgState = msgState;
    }

    public Integer getMsgReason() {
        return msgReason;
    }

    public void setMsgReason(Integer msgReason) {
        this.msgReason = msgReason;
    }

    public String getMsgChannel() {
        return msgChannel;
    }

    public void setMsgChannel(String msgChannel) {
        this.msgChannel = msgChannel;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Integer getMsgGroup() {
        return msgGroup;
    }

    public void setMsgGroup(Integer msgGroup) {
        this.msgGroup = msgGroup;
    }
}
