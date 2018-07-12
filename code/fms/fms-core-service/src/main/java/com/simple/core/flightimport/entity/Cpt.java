package com.simple.core.flightimport.entity;



import java.io.Serializable;
import java.util.Date;

/**
 * 航路点信息
 * simple.ZhuoMengLan
 */
public class Cpt implements Serializable {
    private static final long serialVersionUID = 14251112L;

    /**
     * 航路点名称
     */
    private String cptName;
    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 创建时间
     */
    private Date createTm;
    /**
     * 更新时间
     */
    private Date modifyTm;

    /**
     * 特殊航路点：经纬度
     */
    private boolean special = false;

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public String getCptName() {
        return cptName;
    }

    public void setCptName(String cptName) {
        this.cptName = cptName;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
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
}
