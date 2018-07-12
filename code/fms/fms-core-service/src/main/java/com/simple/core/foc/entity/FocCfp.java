package com.simple.core.foc.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/23.
 */
public class FocCfp implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    private String flightId;
    /**
     * 飞行计划文件
     */
    private byte[] cfpInfo;
    /**
     * 最新修改时间
     */
    private Date modifyTm;

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public byte[] getCfpInfo() {
        return cfpInfo;
    }

    public void setCfpInfo(byte[] cfpInfo) {
        this.cfpInfo = cfpInfo;
    }

    public Date getModifyTm() {
        return modifyTm;
    }

    public void setModifyTm(Date modifyTm) {
        this.modifyTm = modifyTm;
    }

    @Override
    public String toString() {
        return "FocCfp{" +
                "flightId='" + flightId + '\'' +
                ", cfpInfo=" + Arrays.toString(cfpInfo) +
                ", modifyTm=" + modifyTm +
                '}';
    }
}
