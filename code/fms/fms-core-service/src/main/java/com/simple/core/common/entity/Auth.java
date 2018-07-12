package com.simple.core.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.21 15:09.
 */
public class Auth implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 公司二字码
     */
    private String carrierIata;
    /**
     * 公司三字码
     */
    private String carrierIcao;

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
    private boolean auth;
    /**
     * 生效日期
     */
    private Date effectiveDt;
    /**
     * 失效日期
     */
    private Date expireDt;

    public String getCarrierIata() {
        return carrierIata;
    }

    public void setCarrierIata(String carrierIata) {
        this.carrierIata = carrierIata;
    }

    public String getCarrierIcao() {
        return carrierIcao;
    }

    public void setCarrierIcao(String carrierIcao) {
        this.carrierIcao = carrierIcao;
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

    @Override
    public String toString() {
        return "Auth{" +
                "carrierIata='" + carrierIata + '\'' +
                ", carrierIcao='" + carrierIcao + '\'' +
                ", acReg='" + acReg + '\'' +
                ", msgType='" + msgType + '\'' +
                ", charging=" + auth +
                ", effectiveDt=" + effectiveDt +
                ", expireDt=" + expireDt +
                '}';
    }
}
