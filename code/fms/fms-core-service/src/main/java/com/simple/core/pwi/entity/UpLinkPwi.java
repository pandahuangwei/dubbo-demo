package com.simple.core.pwi.entity;

import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author ZhuoMengLan.
 * @version 1.0 .
 * @since 2017.03.17 19:24.
 */
public class UpLinkPwi implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<PwiAltitudeWind> climbAltitudeWindList;//爬升高度/风列表
    private List<PwiCruiseLevel> cruiseLevelList;//巡航高度层高度/风/温度列表
    private List<PwiAltitudeWind> descentAltitudeWindList;//下降高度/风列表
    private List<PwiAltitudeWind> alternateWindList;//备降高度/风列表

    public List<PwiAltitudeWind> getClimbAltitudeWindList() {
        return climbAltitudeWindList;
    }

    public void setClimbAltitudeWindList(List<PwiAltitudeWind> climbAltitudeWindList) {
        this.climbAltitudeWindList = climbAltitudeWindList;
    }

    public List<PwiCruiseLevel> getCruiseLevelList() {
        return cruiseLevelList;
    }

    public void setCruiseLevelList(List<PwiCruiseLevel> cruiseLevelList) {
        this.cruiseLevelList = cruiseLevelList;
    }

    public List<PwiAltitudeWind> getDescentAltitudeWindList() {
        return descentAltitudeWindList;
    }

    public void setDescentAltitudeWindList(List<PwiAltitudeWind> descentAltitudeWindList) {
        this.descentAltitudeWindList = descentAltitudeWindList;
    }

    public List<PwiAltitudeWind> getAlternateWindList() {
        return alternateWindList;
    }

    public void setAlternateWindList(List<PwiAltitudeWind> alternateWindList) {
        this.alternateWindList = alternateWindList;
    }

    public String toOutputString(){
        try {
            String output = "PWI UPLINK:";

            //爬升高度/风列表
            if(!CollectionUtils.isEmpty(climbAltitudeWindList)){
                output +="[climb:";
                int i=0;
                for(PwiAltitudeWind aw: climbAltitudeWindList){
                    if(i++>0) output +=",";
                    output +=(aw.getAlt()+"."+aw.getWinddir()+"."+aw.getWindvel()+"."+aw.getLon()+aw.getLat());
                }
                output +="] ";
            }

            //巡航高度层高度/风/温度列表
            if(!CollectionUtils.isEmpty(cruiseLevelList)){
                output +="\r\n[cruise:";
                int i=0;
                for(PwiCruiseLevel level: cruiseLevelList){
                    List<PwiPoint> points = level.getPointList();
                    if (points == null || points.isEmpty()) {
                        continue;
                    }
                    if(i++>0) output +=",";
                    output +=level.getAlt()+"[";

                    int kk=0;
                    for(PwiPoint p:points){
                        if(kk++>0) output +=",";
                        output +=(p.getCptname()+"."+p.getWinddir()+"."+p.getWindvel()+"."+p.getTemperature());
                    }
                    output +="]\r\n";
                }
                output +="] ";
            }

            //下降高度/风列表
            if(!CollectionUtils.isEmpty(descentAltitudeWindList)){
                output +="\r\n[descent:";
                int i=0;
                for(PwiAltitudeWind aw: descentAltitudeWindList){
                    if(i++>0) output +=",";
                    output +=(aw.getAlt()+"."+aw.getWinddir()+"."+aw.getWindvel()+"."+aw.getLon()+aw.getLat());
                }
                output +="] ";
            }

            //备降高度/风列表
            if(!CollectionUtils.isEmpty(alternateWindList)){
                output +="\r\n[alternate:";
                int i=0;
                for(PwiAltitudeWind aw: alternateWindList){
                    if(i++>0) output +=",";
                    output +=(aw.getAlt()+"."+aw.getWinddir()+"."+aw.getWindvel()+"."+aw.getAlternateAirport());
                }
                output +="] ";
            }

            return output;
        } catch (Exception e) {
            return "";
        }
    }

    public String toInvalidString(){
        try {
            String output = "";

            //巡航高度层高度/风/温度列表
            if(!CollectionUtils.isEmpty(cruiseLevelList)){
                output +="Cruise invalid points:";
                int i=0;
                for(PwiCruiseLevel level: cruiseLevelList){
                    List<PwiPoint> points = level.getInvalidPoints();
                    if (points == null || points.isEmpty()) {
                        continue;
                    }
                    if(i++>0) output +=",";
                    output +=level.getAlt()+"[";

                    int kk=0;
                    for(PwiPoint p:points){
                        if(kk++>0) output +=",";
                        output +=(p.getCptname()+"."+p.getWinddir()+"."+p.getWindvel()+"."+p.getTemperature());
                    }
                    output +="]\r\n";
                }
            }
            return output;
        } catch (Exception e) {
            return "";
        }
    }
}
