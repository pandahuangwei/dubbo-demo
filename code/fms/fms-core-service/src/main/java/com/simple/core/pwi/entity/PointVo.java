package com.simple.core.pwi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/23.
 */
public class PointVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String dLon;
    private String dLat;
    private String dAlt;
    private String windVal;

    public String getdLon() {
        return dLon;
    }

    public void setdLon(String dLon) {
        this.dLon = dLon;
    }

    public String getdLat() {
        return dLat;
    }

    public void setdLat(String dLat) {
        this.dLat = dLat;
    }

    public String getdAlt() {
        return dAlt;
    }

    public void setdAlt(String dAlt) {
        this.dAlt = dAlt;
    }

    public String getWindVal() {
        return windVal;
    }

    public void setWindVal(String windVal) {
        this.windVal = windVal;
    }

}
