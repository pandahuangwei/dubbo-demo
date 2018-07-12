package com.simple.core.flightimport.entity;


import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 解析飞行计划文件
 * simple.ZhuoMengLan
 */
public class CfpExt implements Serializable {
    private static final long serialVersionUID = 145467856L;


    /**
     * ID
     */
    private String cfpId;
    /**
     * 对应FOC下航班ID
     */
    private String focFlightId;
    /**
     * 飞行计划文件
     */
    private byte[] cfpInfo;
    /**
     * 飞行计划文件加密
     */
    private String md5;
    /**
     * 创建时间
     */
    private Date createTm;
    /**
     * 更新时间
     */
    private Date modifyTm;
    /**
     * 对应FOC下飞行计划更新时间：不允许修改
     */
    private Date focModifyTm;

    /**
     * 主备航路变化1主2备3主备,缺省null
     */
    private String routeChange;


    /**
     * 主备航路变化1主2备3主备,缺省null
     */
    private Date routeChangeTm;
    /**
     * 主航路名称
     */
    private String mainRoute;
    /**
     * 主航路点
     */
    private String mainRoutePoint;
    /**
     * 备降航路1名称
     */
    private String alterRoute1;
    /**
     * 备降航路2名称
     */
    private String alterRoute2;
    /**
     * 备降航路3名称
     */
    private String alterRoute3;
    /**
     * 备降航路点1
     */
    private String alterRoute1Point;
    /**
     * 备降航路点2
     */
    private String alterRoute2Point;
    /**
     * 备降航路航路点3
     */
    private String alterRoute3Point;
    /**
     * 主公司航路
     */
    private String mainCompanyRoute;

    /**
     * 备降公司航路1
     */
    private String alterCompanyRoute1;

    /**
     * 备降公司航路2
     */
    private String alterCompanyRoute2;

    /**
     * 备降公司航路3
     */
    private String alterCompanyRoute3;

    /**
     * 备降机场1
     */
    private String alterAirport1;

    /**
     * 备降机场2
     */
    private String alterAirport2;

    /**
     * 备降机场3
     */
    private String alterAirport3;

    /**
     * 航路点集合
     */
    private Map<String,Cpt> cptMap;
    /**
     * 是否特殊航路1是0否
     */
    private int  mainSpecial;
    /**
     * 备降航路1是否特殊航路1是0否
     */
    private int  alter1Special;
    /**
     * 备降航路2是否特殊航路1是0否
     */
    private int  alter2Special;
    /**
     * 备降航路3是否特殊航路1是0否
     */
    private int  alter3Special;

    /**
     * 成本指数
     */
    private String costIndex;
    /**
     * 航程油量
     */
    private String traFuel;
    /**
     * 起飞重量
     */
    private String takeoffWeight;
    /**
     * 着陆重量
     */
    private String landWeight;
    /**
     * 可用业载
     */
    private String avPayload;
    /**
     * 使用空重
     */
    private String opnlWeight;
    /**
     * 备降油量
     */
    private String altFuel;
    /**
     * 等待油量
     */
    private String hldFuel;
    /**
     * 备份油量
     */
    private String resFuel;
    /**
     * 额外油量
     */
    private String xtrFuel;
    /**
     * 地面APU油量
     */
    private String apuFuel;
    /**
     * 滑出油量
     */
    private String txoFuel;
    /**
     * 滑入油量
     */
    private String txiFuel;
    /**
     * 总油量
     */
    private String fobFuel;

    /**
     * 巡航高度
     */
    private String cruiseAltitude;
    /**
     * 航路平均温度:M负数，P正数
     */
    private String routeAvgTemp;
    /**
     * 航路平均风:M负数，P正数
     */
    private String routeAvgWind;

    /**
     *飞行计划编号
     */
    private String planNo;


    /**
     *备降航路1高度层
     */
    private String alterRoute1Fl;

    /**
     *备降航路2高度层
     */
    private String alterRoute2Fl;
    /**
     *备降航路3高度层
     */
    private String alterRoute3Fl;
    /**
     *备降航路1风分量
     */
    private String alterRoute1Wc;
    /**
     *备降航路2风分量
     */
    private String alterRoute2Wc;
    /**
     *备降航路3风分量
     */
    private String alterRoute3Wc;

    /**
     *备降航路1油耗
     */
    private String alterRoute1Fuel;
    /**
     *备降航路1油耗
     */
    private String alterRoute2Fuel;
    /**
     *备降航路1油耗
     */
    private String alterRoute3Fuel;


    public String getCfpId() {
        return cfpId;
    }

    public void setCfpId(String cfpId) {
        this.cfpId = cfpId;
    }

    public int getMainSpecial() {
        return mainSpecial;
    }

    public void setMainSpecial(int mainSpecial) {
        this.mainSpecial = mainSpecial;
    }

    public Map<String, Cpt> getCptMap() {
        return cptMap;
    }

    public void addCpt(Cpt cptInfo) {
        if(null==cptInfo || !StringUtils.isNotBlank(cptInfo.getCptName())) return;
        if(null==this.cptMap)
            this.cptMap = new HashMap<>();
        if(this.cptMap.containsKey(cptInfo.getCptName())) return;
        this.cptMap.put(cptInfo.getCptName(),cptInfo);
    }

    public String getFocFlightId() {
        return focFlightId;
    }

    public void setFocFlightId(String focFlightId) {
        this.focFlightId = focFlightId;
    }

    public byte[] getCfpInfo() {
        return cfpInfo;
    }

    public void setCfpInfo(byte[] cfpInfo) {
        this.cfpInfo = cfpInfo;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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

    public String getMainRoute() {
        return mainRoute;
    }

    public void setMainRoute(String mainRoute) {
        this.mainRoute = mainRoute;
    }

    public String getMainRoutePoint() {
        return mainRoutePoint;
    }

    public void setMainRoutePoint(String mainRoutePoint) {
        this.mainRoutePoint = mainRoutePoint;
    }

    public String getAlterRoute1() {
        return alterRoute1;
    }

    public void setAlterRoute1(String alterRoute1) {
        this.alterRoute1 = alterRoute1;
    }

    public String getAlterRoute2() {
        return alterRoute2;
    }

    public void setAlterRoute2(String alterRoute2) {
        this.alterRoute2 = alterRoute2;
    }

    public String getAlterRoute3() {
        return alterRoute3;
    }

    public void setAlterRoute3(String alterRoute3) {
        this.alterRoute3 = alterRoute3;
    }

    public String getAlterRoute1Point() {
        return alterRoute1Point;
    }

    public void setAlterRoute1Point(String alterRoute1Point) {
        this.alterRoute1Point = alterRoute1Point;
    }

    public String getAlterRoute2Point() {
        return alterRoute2Point;
    }

    public void setAlterRoute2Point(String alterRoute2Point) {
        this.alterRoute2Point = alterRoute2Point;
    }

    public String getAlterRoute3Point() {
        return alterRoute3Point;
    }

    public void setAlterRoute3Point(String alterRoute3Point) {
        this.alterRoute3Point = alterRoute3Point;
    }

    public Date getRouteChangeTm() {
        return routeChangeTm;
    }

    public void setRouteChangeTm(Date routeChangeTm) {
        this.routeChangeTm = routeChangeTm;
    }

    public String getMainCompanyRoute() {
        return mainCompanyRoute;
    }

    public void setMainCompanyRoute(String mainCompanyRoute) {
        this.mainCompanyRoute = mainCompanyRoute;
    }

    public String getAlterCompanyRoute1() {
        return alterCompanyRoute1;
    }

    public void setAlterCompanyRoute1(String alterCompanyRoute1) {
        this.alterCompanyRoute1 = alterCompanyRoute1;
    }

    public String getAlterCompanyRoute2() {
        return alterCompanyRoute2;
    }

    public void setAlterCompanyRoute2(String alterCompanyRoute2) {
        this.alterCompanyRoute2 = alterCompanyRoute2;
    }

    public String getAlterCompanyRoute3() {
        return alterCompanyRoute3;
    }

    public void setAlterCompanyRoute3(String alterCompanyRoute3) {
        this.alterCompanyRoute3 = alterCompanyRoute3;
    }

    public String getAlterAirport1() {
        return alterAirport1;
    }

    public void setAlterAirport1(String alterAirport1) {
        this.alterAirport1 = alterAirport1;
    }

    public String getAlterAirport2() {
        return alterAirport2;
    }

    public void setAlterAirport2(String alterAirport2) {
        this.alterAirport2 = alterAirport2;
    }

    public String getAlterAirport3() {
        return alterAirport3;
    }

    public void setAlterAirport3(String alterAirport3) {
        this.alterAirport3 = alterAirport3;
    }

    public Date getFocModifyTm() {
        return focModifyTm;
    }

    public void setFocModifyTm(Date focModifyTm) {
        this.focModifyTm = focModifyTm;
    }

    public int getAlter1Special() {
        return alter1Special;
    }

    public void setAlter1Special(int alter1Special) {
        this.alter1Special = alter1Special;
    }

    public int getAlter2Special() {
        return alter2Special;
    }

    public void setAlter2Special(int alter2Special) {
        this.alter2Special = alter2Special;
    }

    public int getAlter3Special() {
        return alter3Special;
    }

    public void setAlter3Special(int alter3Special) {
        this.alter3Special = alter3Special;
    }

    public String getCostIndex() {
        return costIndex;
    }

    public void setCostIndex(String costIndex) {
        this.costIndex = costIndex;
    }

    public String getTraFuel() {
        return traFuel;
    }

    public void setTraFuel(String traFuel) {
        this.traFuel = traFuel;
    }

    public String getTakeoffWeight() {
        return takeoffWeight;
    }

    public void setTakeoffWeight(String takeoffWeight) {
        this.takeoffWeight = takeoffWeight;
    }

    public String getLandWeight() {
        return landWeight;
    }

    public void setLandWeight(String landWeight) {
        this.landWeight = landWeight;
    }

    public String getAvPayload() {
        return avPayload;
    }

    public void setAvPayload(String avPayload) {
        this.avPayload = avPayload;
    }

    public String getOpnlWeight() {
        return opnlWeight;
    }

    public void setOpnlWeight(String opnlWeight) {
        this.opnlWeight = opnlWeight;
    }

    public String getAltFuel() {
        return altFuel;
    }

    public void setAltFuel(String altFuel) {
        this.altFuel = altFuel;
    }

    public String getHldFuel() {
        return hldFuel;
    }

    public void setHldFuel(String hldFuel) {
        this.hldFuel = hldFuel;
    }

    public String getResFuel() {
        return resFuel;
    }

    public void setResFuel(String resFuel) {
        this.resFuel = resFuel;
    }

    public String getXtrFuel() {
        return xtrFuel;
    }

    public void setXtrFuel(String xtrFuel) {
        this.xtrFuel = xtrFuel;
    }

    public String getApuFuel() {
        return apuFuel;
    }

    public void setApuFuel(String apuFuel) {
        this.apuFuel = apuFuel;
    }

    public String getTxoFuel() {
        return txoFuel;
    }

    public void setTxoFuel(String txoFuel) {
        this.txoFuel = txoFuel;
    }

    public String getTxiFuel() {
        return txiFuel;
    }

    public void setTxiFuel(String txiFuel) {
        this.txiFuel = txiFuel;
    }

    public String getFobFuel() {
        return fobFuel;
    }

    public void setFobFuel(String fobFuel) {
        this.fobFuel = fobFuel;
    }

    public String getCruiseAltitude() {
        return cruiseAltitude;
    }

    public void setCruiseAltitude(String cruiseAltitude) {
        this.cruiseAltitude = cruiseAltitude;
    }

    public String getRouteAvgTemp() {
        return routeAvgTemp;
    }

    public void setRouteAvgTemp(String routeAvgTemp) {
        this.routeAvgTemp = routeAvgTemp;
    }

    public String getRouteAvgWind() {
        return routeAvgWind;
    }

    public void setRouteAvgWind(String routeAvgWind) {
        this.routeAvgWind = routeAvgWind;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getAlterRoute1Fl() {
        return alterRoute1Fl;
    }

    public void setAlterRoute1Fl(String alterRoute1Fl) {
        this.alterRoute1Fl = alterRoute1Fl;
    }

    public String getAlterRoute2Fl() {
        return alterRoute2Fl;
    }

    public void setAlterRoute2Fl(String alterRoute2Fl) {
        this.alterRoute2Fl = alterRoute2Fl;
    }

    public String getAlterRoute3Fl() {
        return alterRoute3Fl;
    }

    public void setAlterRoute3Fl(String alterRoute3Fl) {
        this.alterRoute3Fl = alterRoute3Fl;
    }

    public String getAlterRoute1Wc() {
        return alterRoute1Wc;
    }

    public void setAlterRoute1Wc(String alterRoute1Wc) {
        this.alterRoute1Wc = alterRoute1Wc;
    }

    public String getAlterRoute2Wc() {
        return alterRoute2Wc;
    }

    public void setAlterRoute2Wc(String alterRoute2Wc) {
        this.alterRoute2Wc = alterRoute2Wc;
    }

    public String getAlterRoute3Wc() {
        return alterRoute3Wc;
    }

    public void setAlterRoute3Wc(String alterRoute3Wc) {
        this.alterRoute3Wc = alterRoute3Wc;
    }

    public String getAlterRoute1Fuel() {
        return alterRoute1Fuel;
    }

    public void setAlterRoute1Fuel(String alterRoute1Fuel) {
        this.alterRoute1Fuel = alterRoute1Fuel;
    }

    public String getAlterRoute2Fuel() {
        return alterRoute2Fuel;
    }

    public void setAlterRoute2Fuel(String alterRoute2Fuel) {
        this.alterRoute2Fuel = alterRoute2Fuel;
    }

    public String getAlterRoute3Fuel() {
        return alterRoute3Fuel;
    }

    public void setAlterRoute3Fuel(String alterRoute3Fuel) {
        this.alterRoute3Fuel = alterRoute3Fuel;
    }

    public String getRouteChange() {
        return routeChange;
    }

    public void setRouteChange(String routeChange) {
        this.routeChange = routeChange;
    }
}
