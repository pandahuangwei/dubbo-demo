package com.simple.core.per.entity;

import java.io.Serializable;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: UpLinkPER.java.
 * @Package com.simple.core.per.entity.
 * @Description: UpLinkPER.
 *
 * @author Administrator.
 * @version 1.0.
 * @created 2017/4/7 16:41.
 **********************************************************************************/
public class DownLinkPER implements Serializable {
    private static final long serialVersionUID = 154534784L;

    private String zfw; //无油重量(百磅):来源飞行计划中PLD+OPNLWT
    private String cruiseAltitude;//巡航高度(百英尺)
    private String blockFuel;//轮挡油量(百磅):来源飞行计划中FOB
    private String reserveFuel;//剩余备份燃油,一般指国际航线备份油10%(百磅)：可为空
    private String costIndex;//成本指数
    private String cruiseTemperature;//巡航高度温度(摄氏度)
    private String taxiFuel; //滑行油量(百磅)：可为空
    private String tropopauseAltitude; //对流层顶高(百英尺)：可为空
    private String zfwCG; //无油重心：可为空

    public String getZfw() {
        return zfw;
    }

    public void setZfw(String zfw) {
        this.zfw = zfw;
    }

    public String getCruiseAltitude() {
        return cruiseAltitude;
    }

    public void setCruiseAltitude(String cruiseAltitude) {
        this.cruiseAltitude = cruiseAltitude;
    }

    public String getBlockFuel() {
        return blockFuel;
    }

    public void setBlockFuel(String blockFuel) {
        this.blockFuel = blockFuel;
    }

    public String getReserveFuel() {
        return reserveFuel;
    }

    public void setReserveFuel(String reserveFuel) {
        this.reserveFuel = reserveFuel;
    }

    public String getCostIndex() {
        return costIndex;
    }

    public void setCostIndex(String costIndex) {
        this.costIndex = costIndex;
    }

    public String getCruiseTemperature() {
        return cruiseTemperature;
    }

    public void setCruiseTemperature(String cruiseTemperature) {
        this.cruiseTemperature = cruiseTemperature;
    }

    public String getTaxiFuel() {
        return taxiFuel;
    }

    public void setTaxiFuel(String taxiFuel) {
        this.taxiFuel = taxiFuel;
    }

    public String getTropopauseAltitude() {
        return tropopauseAltitude;
    }

    public void setTropopauseAltitude(String tropopauseAltitude) {
        this.tropopauseAltitude = tropopauseAltitude;
    }

    public String getZfwCG() {
        return zfwCG;
    }

    public void setZfwCG(String zfwCG) {
        this.zfwCG = zfwCG;
    }

    public String toInputString(){
        try {
            String outPut="PER MANUAL:";
            outPut+="[ZFW="+zfw+","+
                    "CruiseAltitude="+cruiseAltitude+","+
                    "BlockFuel="+blockFuel+","+
                    "ReserveFuel="+reserveFuel+","+
                    "CostIndex="+costIndex+","+
                    "CruiseTemperature="+cruiseTemperature+","+
                    "TaxiFuel="+taxiFuel+","+
                    "TropopauseAltitude="+tropopauseAltitude+","+
                    "ZFWCG="+zfwCG+"]";
            return outPut;
        } catch (Exception e) {
            return "";
        }
    }
}
