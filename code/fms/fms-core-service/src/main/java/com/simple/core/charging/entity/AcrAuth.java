package com.simple.core.charging.entity;

import com.simple.common.entity.BaseEntity;
import com.simple.core.common.entity.Auth;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.06 14:21.
 */
public class AcrAuth extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 飞机号
     */
    private String acReg;
    /**
     * 报文类型
     */
    private String msgType;
    /**
     * 是否授权:1是，0否
     */
    private boolean auth = true;
    /**
     * 生效日期
     */
    private Date effectiveDt;
    /**
     * 失效日期
     */
    private Date expireDt;


    public AcrAuth() {
    }

    public AcrAuth(Auth auth) {
        super();
        super.init();
        this.acReg = auth.getAcReg();
        this.msgType = auth.getMsgType();
        this.auth = auth.isAuth();
        this.effectiveDt = auth.getEffectiveDt();
        this.expireDt = auth.getExpireDt();
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public String getCreateBy() {
        return super.getCreateBy();
    }

    @Override
    public String getModifyBy() {
        return super.getModifyBy();
    }

    @Override
    public Date getCreateTm() {
        return super.getCreateTm();
    }

    @Override
    public Date getModifyTm() {
        return super.getModifyTm();
    }

    @Override
    public boolean isDel() {
        return super.isDel();
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

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public Date getEffectiveDt() {
        return effectiveDt;
    }

    public void setEffectiveDt(Date effectiveDt) {
        this.effectiveDt = effectiveDt;
    }

    public Date getExpireDt() {
        return expireDt;
    }

    public void setExpireDt(Date expireDt) {
        this.expireDt = expireDt;
    }
}
