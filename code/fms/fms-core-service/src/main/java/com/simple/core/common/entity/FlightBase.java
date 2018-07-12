package com.simple.core.common.entity;

import com.simple.common.utils.StringUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 航班信息
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.14 17:14.
 */
public class FlightBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 航班ID
     */
    private String flightId;
    /**
     * 航班日期
     */
    private Date flightDate;
    /**
     * 航班号
     */
    private String flightNo;
    /**
     * 起飞站
     */
    private String departureAirport;
    /**
     * 落地站
     */
    private String arrivalAirport;
    /**
     * 计划起飞时间
     */
    private Date std;
    /**
     * 计划到达时间
     */
    private Date sta;
    /**
     * 预计起飞时间
     */
    private Date etd;
    /**
     * 预计到达时间
     */
    private Date eta;
    /**
     * 实际起飞时间
     */
    private Date atd;
    /**
     * 实际到达时间
     */
    private Date ata;
    /**
     * 创建时间
     */
    private Date createTm;
    /**
     * 修改时间
     */
    private Date modifyTm;
    /**
     * 对应FOC下航班动态更新时间：不允许修改
     */
    private Date focModifyTm;
    /**
     * 飞机号
     */
    private String acReg;
    /**
     * 机型
     */
    private String acType;
    /**
     * 国内D/国际I
     */
    private String dOrI;
    /**
     * 实际到达时间
     */
    private Date dispatchPassTime;

    /**
     * 是否签派放行1是,0否,2取不到值
     */
    private String dispatch;
    /**
     * 机组到位时间
     */
    private Date crewArrivalTime;
    /**
     * 机组是否到位1是,0否,2取不到值
     */
    private String crewArrival;
    /**
     * V-备降 R-返航
     */
    private String flgVR;

    /**
     * 备降 ,返航
     */
    private String flgVR1;
    /**
     * 动态取消标志
     */
    private String flgCS;
    /**
     * foc航班ID
     */
    private String focFlightId;

    /**
     * 主备航路变化1主2备3主备,缺省null
     */
    private String routeChange;

    /**
     * 航路变化时间
     */
    private Date routeChangeTm;

    /**
     * 主航路名称
     */
    private String mainRoute;
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
     * 主航路点
     */
    private String mainRoutePoint;
    /**
     * 备降航路点1
     */
    private String alterRoute1Point;
    /**
     * 备降航路点2
     */
    private String alterRoute2Point;
    /**
     * 备降航路点3
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
     * 航空公司二字码
     */
    private String carrierIata;

    /**
     * 主航路是否特殊航路1是0否
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
     *备降航路1的油耗
     */
    private String alterRoute1Fuel;
    /**
     *备降航路2的油耗
     */
    private String alterRoute2Fuel;
    /**
     *备降航路3的油耗
     */
    private String alterRoute3Fuel;

    public boolean isCfpDataValid(){
        if(StringUtil.isBlank(planNo)
                || StringUtil.isBlank(mainCompanyRoute) || StringUtil.isBlank(mainRoutePoint) ||
                StringUtil.isBlank(alterAirport1) || StringUtil.isBlank(alterRoute1Point))
            return false;
        return true;
    }

    public String getFlgVR1() {
        return flgVR1;
    }

    public void setFlgVR1(String flgVR1) {
        this.flgVR1 = flgVR1;
    }

    public int getMainSpecial() {
        return mainSpecial;
    }

    public void setMainSpecial(int mainSpecial) {
        this.mainSpecial = mainSpecial;
    }
    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Date getStd() {
        return std;
    }

    public void setStd(Date std) {
        this.std = std;
    }

    public Date getSta() {
        return sta;
    }

    public void setSta(Date sta) {
        this.sta = sta;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getAtd() {
        return atd;
    }

    public void setAtd(Date atd) {
        this.atd = atd;
    }

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
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

    public String getAcReg() {
        return acReg;
    }

    public void setAcReg(String acReg) {
        this.acReg = acReg;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getdOrI() {
        return dOrI;
    }

    public void setdOrI(String dOrI) {
        this.dOrI = dOrI;
    }

    public Date getDispatchPassTime() {
        return dispatchPassTime;
    }

    public void setDispatchPassTime(Date dispatchPassTime) {
        this.dispatchPassTime = dispatchPassTime;
    }

    public String getDispatch() {
        return dispatch;
    }

    public void setDispatch(String dispatch) {
        this.dispatch = dispatch;
    }

    public Date getCrewArrivalTime() {
        return crewArrivalTime;
    }

    public void setCrewArrivalTime(Date crewArrivalTime) {
        this.crewArrivalTime = crewArrivalTime;
    }

    public String getCrewArrival() {
        return crewArrival;
    }

    public void setCrewArrival(String crewArrival) {
        this.crewArrival = crewArrival;
    }

    public String getFlgVR() {
        return flgVR;
    }

    public void setFlgVR(String flgVR) {
        this.flgVR = flgVR;
    }

    public String getFlgCS() {
        return flgCS;
    }

    public void setFlgCS(String flgCS) {
        this.flgCS = flgCS;
    }

    public String getFocFlightId() {
        return focFlightId;
    }

    public void setFocFlightId(String focFlightId) {
        this.focFlightId = focFlightId;
    }

    public String getRouteChange() {
        return routeChange;
    }

    public void setRouteChange(String routeChange) {
        this.routeChange = routeChange;
    }

    public Date getRouteChangeTm() {
        return routeChangeTm;
    }

    public void setRouteChangeTm(Date routeChangeTm) {
        this.routeChangeTm = routeChangeTm;
    }

    public String getMainRoute() {
        return mainRoute;
    }

    public void setMainRoute(String mainRoute) {
        this.mainRoute = mainRoute;
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

    public String getMainRoutePoint() {
        return mainRoutePoint;
    }

    public void setMainRoutePoint(String mainRoutePoint) {
        this.mainRoutePoint = mainRoutePoint;
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

    public String getCarrierIata() {
        return carrierIata;
    }

    public void setCarrierIata(String carrierIata) {
        this.carrierIata = carrierIata;
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

    @Override
    public String toString() {
        return "FlightBase{" +
                "flightId='" + flightId + '\'' +
                ", flightDate=" + flightDate +
                ", flightNo='" + flightNo + '\'' +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", std=" + std +
                ", acReg='" + acReg + '\'' +
                '}';
    }
}
