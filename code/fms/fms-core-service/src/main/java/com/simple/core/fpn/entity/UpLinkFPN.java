package com.simple.core.fpn.entity;

import com.simple.common.utils.StringUtil;
import com.sun.org.apache.xerces.internal.impl.dv.xs.StringDV;
import com.sun.tools.javac.jvm.Code;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */
public class UpLinkFPN implements Serializable {
    private static final long serialVersionUID = 154534474L;

    private int mFPNType = 0; //0:主计划 1：二计划
    //============主航路==========//
    private String mPCompanyRoute;//主航路公司航路名称 字母数字型字符串 1至10位  必填
    private String mPDepAirport;//主航路起飞机场 字母数字型字符串 固定4位
    private String mPArrAirport;//主航路着陆机场 字母数字型字符串 固定4位
    private String mPDepRunway;//主航路起飞跑道 XXA:	XX是2位数值，	A是1位字母  固定3位 xx取01至35 A：L,R,C,O
    private String mSid;//离场程序名称 字母数字型字符串 1至10位
    private String mSidTrans;//离场程序航路过渡 字母数字型字符串  1至10位
    private List<Waypoint> mFWaypointList;//主航路航路点
    private String mStar;//着陆程序进场程序 字母数字型字符串 1至10位
    private String mStarTrans;//着陆程序航路过渡 字母数字型字符串 1至10位
    private String mAppr;//进近程序 字母数字型字符串 1至10位
    private String mApprTrans;//进近程序过渡 字母数字型字符串 1至10位
    private String mArrivalRunway;//着落跑道 XXA	XX：2位数字，		A：1位字母 固定3位 XX取01至35，	A取L,R,C,O
    //============END 主航路==========//

    //============其他信息==========//
    private String mAlongTrackWaypoint;//沿航路点 aaaaa-Byyyy	aaaaa：1至5位字母数字，		B：1位字母，		yyyy：1至4位数字。 B取P(Plus)或者M(Minus)，	yyyy取0至9999 yyyy:0.1海里
    private String mReportingPoint;//报告点 Axxx or Axxx-yy 	A：1位字母		xxx：1至3位数字		yy：1至2位数字  3至7位  A取N或者S或者W或者E，	xxx取0至90，或者0至180，		yy取00至99  xxx：度 yy：海里
    private String mHWaypoint;//等待航路点 字母数字型字符串 1至5位
    private String mHoldSpeed;//等待速度，数值型 固定3位 0至250 节
    private String mHoldAltitude;//等待高度 AAxxxx 	AA：2位字母		xxxx：1到4位数字 3至6位 AA取：AB(At or Below)或者AT(At or Above) 	xxxx：0至PERF库中MAX_CERT_ALT参数设置值 xxxx：10英尺
    private String mHoldTargetSpeed;//等待目标速度 数值型 固定3位 0至250
    private String mHoldTurnDirection;//等待转弯方向 字母数字型字符 固定1位 L或者R
    private String mHoldInboundCourse;//等待航路切入航迹 数值型 固定3位 0至360 度
    private String mEFCTime;//预计批准进扬时间 XXYY	XX：2位数字		YY：2位数字 固定4位  XX:0至23	YY：0至59
    private String mHoldLegTime;//等待时间 数值型 固定2位 0至99 10分钟
    private String mHoldLegDistance;//等待距离 数值型 1至3位 0至999 10海里
    private String mVWaypoint;//航路点（FIX） 字母数字型字符串 1至5位
    private String mSpeedConstraint;//速度限制 数值型 固定3位 0至250
    private String mUpAltitudeConstraint;//高度限制（之上） AAxxxx	AA：2位字母	xxxx：1到4位数字 3至6位 AA取：AB或者AT xxxx：0值最大值 xxxx：10英尺
    private String mDownAltitudeConstraint;//高度限制（之下） AAxxxx	AA：2位字母	xxxx：1到4位数字 3至6位 AA取：AB或者AT xxxx：0值最大值 xxxx：10英尺
    private String mWSWaypoint;//梯级爬升航路点标签 字母数字型字符串 1至5位
    private String mWSAltitudeConstraint;//高度限制 数值型 固定3位 0至最大值 百英尺
    //============END 其他信息==========//

    //============备降航路==========//
    private String mRaCompanyRoute;//备用飞行计划公司航路 字母数字型字符串 1至10位
    private String mRaDepAirport;//备用飞行计划起飞机场四字码 字母数字型字符串  固定4位
    private String mRaArrAirport;//备用飞行计划着陆机场（四字码）字母数字型字符串 固定4位
    private List<Waypoint> mFBWaypointList1;//备用航路航路点
    //============END 备降航路==========//

    //////////////////模块3
    private String mMwDepAirport;//平均风 起飞机场 字母数字型字符串 固定4位
    private String mMwArrAirport;//平均风 着陆机场（四字码）字母数字型字符串
    private String mMwTripWind;//平均风 航路风 Axxx	A：1位字母 xxx：1到3位可变数字 2至4位 A:P或者M	xxx：0至250  xxx：节
    //////////////////

    ////////////////模块4
    private String mFliNo;//航班号 字母数值型字符串 1到8位（SA）1到10位（LR）  必填
    ///////////////////////

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

    public List<Waypoint> getmFWaypointList() {
        return mFWaypointList;
    }

    public void setmFWaypointList(List<Waypoint> mFWaypointList) {

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

    public String getmAlongTrackWaypoint() {
        return mAlongTrackWaypoint;
    }

    public void setmAlongTrackWaypoint(String mAlongTrackWaypoint) {
        this.mAlongTrackWaypoint = mAlongTrackWaypoint;
    }

    public String getmReportingPoint() {
        return mReportingPoint;
    }

    public void setmReportingPoint(String mReportingPoint) {
        this.mReportingPoint = mReportingPoint;
    }

    public String getmHWaypoint() {
        return mHWaypoint;
    }

    public void setmHWaypoint(String mHWaypoint) {
        this.mHWaypoint = mHWaypoint;
    }

    public String getmHoldSpeed() {
        return mHoldSpeed;
    }

    public void setmHoldSpeed(String mHoldSpeed) {
        this.mHoldSpeed = mHoldSpeed;
    }

    public String getmHoldAltitude() {
        return mHoldAltitude;
    }

    public void setmHoldAltitude(String mHoldAltitude) {
        this.mHoldAltitude = mHoldAltitude;
    }

    public String getmHoldTargetSpeed() {
        return mHoldTargetSpeed;
    }

    public void setmHoldTargetSpeed(String mHoldTargetSpeed) {
        this.mHoldTargetSpeed = mHoldTargetSpeed;
    }

    public String getmHoldTurnDirection() {
        return mHoldTurnDirection;
    }

    public void setmHoldTurnDirection(String mHoldTurnDirection) {
        this.mHoldTurnDirection = mHoldTurnDirection;
    }

    public String getmHoldInboundCourse() {
        return mHoldInboundCourse;
    }

    public void setmHoldInboundCourse(String mHoldInboundCourse) {
        this.mHoldInboundCourse = mHoldInboundCourse;
    }

    public String getmEFCTime() {
        return mEFCTime;
    }

    public void setmEFCTime(String mEFCTime) {
        this.mEFCTime = mEFCTime;
    }

    public String getmHoldLegTime() {
        return mHoldLegTime;
    }

    public void setmHoldLegTime(String mHoldLegTime) {
        this.mHoldLegTime = mHoldLegTime;
    }

    public String getmHoldLegDistance() {
        return mHoldLegDistance;
    }

    public void setmHoldLegDistance(String mHoldLegDistance) {
        this.mHoldLegDistance = mHoldLegDistance;
    }

    public String getmVWaypoint() {
        return mVWaypoint;
    }

    public void setmVWaypoint(String mVWaypoint) {
        this.mVWaypoint = mVWaypoint;
    }

    public String getmSpeedConstraint() {
        return mSpeedConstraint;
    }

    public void setmSpeedConstraint(String mSpeedConstraint) {
        this.mSpeedConstraint = mSpeedConstraint;
    }

    public String getmUpAltitudeConstraint() {
        return mUpAltitudeConstraint;
    }

    public void setmUpAltitudeConstraint(String mUpAltitudeConstraint) {
        this.mUpAltitudeConstraint = mUpAltitudeConstraint;
    }

    public String getmDownAltitudeConstraint() {
        return mDownAltitudeConstraint;
    }

    public void setmDownAltitudeConstraint(String mDownAltitudeConstraint) {
        this.mDownAltitudeConstraint = mDownAltitudeConstraint;
    }

    public String getmWSWaypoint() {
        return mWSWaypoint;
    }

    public void setmWSWaypoint(String mWSWaypoint) {
        this.mWSWaypoint = mWSWaypoint;
    }

    public String getmWSAltitudeConstraint() {
        return mWSAltitudeConstraint;
    }

    public void setmWSAltitudeConstraint(String mWSAltitudeConstraint) {
        this.mWSAltitudeConstraint = mWSAltitudeConstraint;
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

    public List<Waypoint> getmFBWaypointList1() {
        return mFBWaypointList1;
    }

    public void setmFBWaypointList1(List<Waypoint> mFBWaypointList1) {
        this.mFBWaypointList1 = mFBWaypointList1;
    }

    public String getmMwDepAirport() {
        return mMwDepAirport;
    }

    public void setmMwDepAirport(String mMwDepAirport) {
        this.mMwDepAirport = mMwDepAirport;
    }

    public String getmMwArrAirport() {
        return mMwArrAirport;
    }

    public void setmMwArrAirport(String mMwArrAirport) {
        this.mMwArrAirport = mMwArrAirport;
    }

    public String getmMwTripWind() {
        return mMwTripWind;
    }

    public void setmMwTripWind(String mMwTripWind) {
        this.mMwTripWind = mMwTripWind;
    }

    public String getmFliNo() {
        return mFliNo;
    }

    public void setmFliNo(String mFliNo) {
        this.mFliNo = mFliNo;
    }

    public String toOutputString(){
        try {
            String outPut="FPN UPLINK:";
            outPut +=(1==mFPNType)?"[Second plan] [":"["+mFliNo+","+mPDepAirport+"~"+mPArrAirport+","+mPCompanyRoute;
            String mainWayPoint=null;
            if (this.mFWaypointList != null) {
                for (Waypoint wp:  mFWaypointList) {
                    if(null==mainWayPoint) mainWayPoint=wp.getmName();
                    else mainWayPoint+=(","+wp.getmName());
                }
            }
            if(null!=mainWayPoint) outPut += ","+mainWayPoint+"] ";

            outPut +="[alternate:"+mRaArrAirport+","+mRaCompanyRoute;
            String alterWayPoint=null;
            if (this.mFBWaypointList1 != null) {
                for (Waypoint wp:  mFBWaypointList1) {
                    if(null==alterWayPoint) alterWayPoint=wp.getmName();
                    else alterWayPoint+=(","+wp.getmName());
                }
            }
            if(null!=alterWayPoint) outPut += ","+alterWayPoint+"]";

            return outPut;
        } catch (Exception e) {
            return "";
        }
    }
}
