package com.simple.core.pwi.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/15.
 */
public class Grib2WdInfo implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String wdId;
    /**
     * 开始时间（本航班计划信息起始有效时间)
     */
    private Date startTime;

    /**
     * 结束时间（本航班计划信息终止有效时间）
     */
    private Date endTime;

    /**
     * 风类型
     */
    private String windType;

    /**
     * 大气压强
     */
    private BigInteger atmosPressure;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 风速
     */
    private BigDecimal windVel;

    public String getWdId() {
        return wdId;
    }

    public void setWdId(String wdId) {
        this.wdId = wdId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getWindType() {
        return windType;
    }

    public void setWindType(String windType) {
        this.windType = windType;
    }

    public BigInteger getAtmosPressure() {
        return atmosPressure;
    }

    public void setAtmosPressure(BigInteger atmosPressure) {
        this.atmosPressure = atmosPressure;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getWindVel() {
        return windVel;
    }

    public void setWindVel(BigDecimal windVel) {
        this.windVel = windVel;
    }

    public Object clone() {
        Grib2WdInfo o = null;
        try {
            o = (Grib2WdInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
