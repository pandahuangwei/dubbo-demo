package com.simple.core.charging.entity;

import com.simple.common.entity.BaseEntity;
import com.simple.core.common.entity.Auth;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.06 14:21.
 */
public class CpyAcr extends BaseEntity implements Serializable {
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

    public CpyAcr() {
    }

    public CpyAcr(String carrierIata, String carrierIcao, String acReg) {
        super();
        this.carrierIata = carrierIata;
        this.carrierIcao = carrierIcao;
        this.acReg = acReg;
    }

    public CpyAcr(Auth auth) {
        super();
        super.init();
        this.carrierIata = auth.getCarrierIata();
        this.carrierIcao = auth.getCarrierIcao();
        this.acReg = auth.getAcReg();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CpyAcr)) return false;

        CpyAcr cpyAcr = (CpyAcr) o;

        return new EqualsBuilder()
                .append(carrierIata, cpyAcr.carrierIata)
                .append(acReg, cpyAcr.acReg)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(carrierIata)
                .append(acReg)
                .toHashCode();
    }
}
