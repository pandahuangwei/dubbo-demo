package com.simple.core.pwi.entity;

import com.simple.common.utils.StringUtil;

import java.io.Serializable;

/**
 * @author Administrator.
 * @version 1.0 .
 * @since 2017.03.13 15:17.
 */
public class PwiPoint implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 高度
     */
    private String alt;
    /**
     *经度
     */
    private String lon;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 航路点名称
     */
    private String cptname;
    /**
     * 风向
     */
    private String winddir;
    /**
     * 风速
     */
    private String windvel;
    /**
     * 温度
     */
    private String temperature;

    /**
     * 是否有效
     * @return
     */
    public boolean isValid(){
        return !StringUtil.isBlank(cptname)
                && !StringUtil.isBlank(alt)
                && !StringUtil.isBlank(winddir)
                && !StringUtil.isBlank(windvel)
                && !StringUtil.isBlank(temperature);
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCptname() {
        return cptname;
    }

    public void setCptname(String cptname) {
        this.cptname = cptname;
    }

    public String getWinddir() {
        return winddir;
    }

    public void setWinddir(String winddir) {
        this.winddir = winddir;
    }

    public String getWindvel() {
        return windvel;
    }

    public void setWindvel(String windvel) {
        this.windvel = windvel;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
