package com.simple.core.fpn.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/28.
 */
public class Waypoint  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mName;//航路点名称
    private String mLng;//经度
    private String mLat;//纬度
    private String mAirwayEntry;//航线起点
    private String mAirway;//航线
    private String mAirwayExit;//航线结束点

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmLng() {
        return mLng;
    }

    public void setmLng(String mLng) {
        this.mLng = mLng;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmAirwayEntry() {
        return mAirwayEntry;
    }

    public void setmAirwayEntry(String mAirwayEntry) {
        this.mAirwayEntry = mAirwayEntry;
    }

    public String getmAirway() {
        return mAirway;
    }

    public void setmAirway(String mAirway) {
        this.mAirway = mAirway;
    }

    public String getmAirwayExit() {
        return mAirwayExit;
    }

    public void setmAirwayExit(String mAirwayExit) {
        this.mAirwayExit = mAirwayExit;
    }
}
