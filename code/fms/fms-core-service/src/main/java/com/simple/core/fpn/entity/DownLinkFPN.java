package com.simple.core.fpn.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */
public class DownLinkFPN implements Serializable {
    private static final long serialVersionUID = 154531234L;

    private String mFliNo;//航班号
    private int mFPNType=0; //0:主计划 1：二计划
    //============主航路==========//
    private String mPCompanyRoute;//主航路公司航路名称 字母数字型字符串 1至10位  必填
    private String mPDepAirport;//主航路起飞机场 字母数字型字符串 固定4位
    private String mPArrAirport;//主航路着陆机场 字母数字型字符串 固定4位
    private String mPDepRunway;//主航路起飞跑道 XXA:	XX是2位数值，	A是1位字母  固定3位 xx取01至35 A：L,R,C,O
    private String mSid;//离场程序名称 字母数字型字符串 1至10位
    private String mSidTrans;//离场程序航路过渡 字母数字型字符串  1至10位
    private String mFWaypointList;//主航路航路点:,分隔
    private String mStar;//着陆程序进场程序 字母数字型字符串 1至10位
    private String mStarTrans;//着陆程序航路过渡 字母数字型字符串 1至10位
    private String mAppr;//进近程序 字母数字型字符串 1至10位
    private String mApprTrans;//进近程序过渡 字母数字型字符串 1至10位
    private String mArrivalRunway;//着落跑道 XXA	XX：2位数字，		A：1位字母 固定3位 XX取01至35，	A取L,R,C,O
    //============END 主航路==========//

    //============备降航路==========//
    private String mRaCompanyRoute;//备用飞行计划公司航路 字母数字型字符串 1至10位
    private String mRaDepAirport;//备用飞行计划起飞机场四字码 字母数字型字符串  固定4位
    private String mRaArrAirport;//备用飞行计划着陆机场（四字码）字母数字型字符串 固定4位
    private String mFBWaypointList1;//备用航路航路点:,分隔
    //============END 备降航路==========//

    public String getmFliNo() {
        return mFliNo;
    }

    public void setmFliNo(String mFliNo) {
        this.mFliNo = mFliNo;
    }

    public int getmFPNType() {
        return mFPNType;
    }

    public void setmFPNType(int mFPNType) {
        this.mFPNType = mFPNType;
    }

    public String getmPCompanyRoute() {
        return mPCompanyRoute;
    }

    public void setmPCompanyRoute(String mPCompanyRoute) {
        this.mPCompanyRoute = mPCompanyRoute;
    }

    public String getmPDepAirport() {
        return mPDepAirport;
    }

    public void setmPDepAirport(String mPDepAirport) {
        this.mPDepAirport = mPDepAirport;
    }

    public String getmPArrAirport() {
        return mPArrAirport;
    }

    public void setmPArrAirport(String mPArrAirport) {
        this.mPArrAirport = mPArrAirport;
    }

    public String getmPDepRunway() {
        return mPDepRunway;
    }

    public void setmPDepRunway(String mPDepRunway) {
        this.mPDepRunway = mPDepRunway;
    }

    public String getmSid() {
        return mSid;
    }

    public void setmSid(String mSid) {
        this.mSid = mSid;
    }

    public String getmSidTrans() {
        return mSidTrans;
    }

    public void setmSidTrans(String mSidTrans) {
        this.mSidTrans = mSidTrans;
    }

    public String getmFWaypointList() {
        return mFWaypointList;
    }

    public void setmFWaypointList(String mFWaypointList) {
        this.mFWaypointList = mFWaypointList;
    }

    public String getmStar() {
        return mStar;
    }

    public void setmStar(String mStar) {
        this.mStar = mStar;
    }

    public String getmStarTrans() {
        return mStarTrans;
    }

    public void setmStarTrans(String mStarTrans) {
        this.mStarTrans = mStarTrans;
    }

    public String getmAppr() {
        return mAppr;
    }

    public void setmAppr(String mAppr) {
        this.mAppr = mAppr;
    }

    public String getmApprTrans() {
        return mApprTrans;
    }

    public void setmApprTrans(String mApprTrans) {
        this.mApprTrans = mApprTrans;
    }

    public String getmArrivalRunway() {
        return mArrivalRunway;
    }

    public void setmArrivalRunway(String mArrivalRunway) {
        this.mArrivalRunway = mArrivalRunway;
    }

    public String getmRaCompanyRoute() {
        return mRaCompanyRoute;
    }

    public void setmRaCompanyRoute(String mRaCompanyRoute) {
        this.mRaCompanyRoute = mRaCompanyRoute;
    }

    public String getmRaDepAirport() {
        return mRaDepAirport;
    }

    public void setmRaDepAirport(String mRaDepAirport) {
        this.mRaDepAirport = mRaDepAirport;
    }

    public String getmRaArrAirport() {
        return mRaArrAirport;
    }

    public void setmRaArrAirport(String mRaArrAirport) {
        this.mRaArrAirport = mRaArrAirport;
    }

    public String getmFBWaypointList1() {
        return mFBWaypointList1;
    }

    public void setmFBWaypointList1(String mFBWaypointList1) {
        this.mFBWaypointList1 = mFBWaypointList1;
    }

    public String toInputString(){
        try {
            String outPut="FPN MANUAL:";
            outPut +=(1==mFPNType)?"[Second plan] [":"["+mFliNo+","+mPDepAirport+"~"+mPArrAirport+","+mPCompanyRoute;
            if(null!=mFWaypointList) outPut += ","+mFWaypointList+"] ";

            outPut +="[alternate:"+mRaArrAirport+","+mRaCompanyRoute;
            if(null!=mFBWaypointList1) outPut += ","+mFBWaypointList1+"]";

            return outPut;
        } catch (Exception e) {
            return "";
        }
    }
}
