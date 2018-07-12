package com.simple.core.pwi.entity;

import java.io.Serializable;

/**
 * @author Administrator.
 * @version 1.0 .
 * @since 2017.03.13 15:17.
 */
public class FMSData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 高度
     */
    private String alt;
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
    private String windtep;


    public String getAlt() {
        return alt;
    }
    public void setAlt(String alt) {
        this.alt = alt;
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
    public String getWindtep() {
        return windtep;
    }
    public void setWindtep(String windtep) {
        this.windtep = windtep;
    }
}
