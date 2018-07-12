package com.simple.core.pwi.entity;

import com.simple.common.utils.StringUtil;

import java.io.Serializable;

/**
 * @author ZhuoMengLan.
 * @version 1.0 .
 * @since 2017.03.17 19:51.
 */
public class PwiAltitudeWind implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 高度层
     */
    private String alt;
    /**
     *经度:计算点
     */
    private String lon;
    /**
     * 纬度:计算点
     */
    private String lat;
    /**
     * 备降机场
     */
    private String alternateAirport;
    /**
     * 风向
     */
    private String winddir;
    /**
     * 风速
     */
    private String windvel;

    /**
     * 是否有效
     * @return
     */
    public boolean isValid(){
        return !StringUtil.isBlank(alt)
                && !StringUtil.isBlank(winddir)
                && !StringUtil.isBlank(windvel);
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
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

    public String getAlternateAirport() {
        return alternateAirport;
    }

    public void setAlternateAirport(String alternateAirport) {
        this.alternateAirport = alternateAirport;
    }
}
