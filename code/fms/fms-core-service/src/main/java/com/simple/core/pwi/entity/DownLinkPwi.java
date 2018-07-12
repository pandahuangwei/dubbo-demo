package com.simple.core.pwi.entity;

import com.simple.common.utils.StringUtil;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuoMengLan.
 * @version 1.0 .
 * @since 2017.03.17 19:15.
 */
public class DownLinkPwi implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> climbAltitudeList;//爬升点高度列表:XXX,百英尺
    private List<String> cruiseLevelAltitudeList;//目标风层高度列表:XXX,百英尺
    private List<String> cruiseLevelWaypointList;//风层航路点列表:航路点名称，或者经纬度点
    private List<String> descentAltitudeList;//下降点高度列表:XXX,百英尺
    private List<String> alternateAirportList;//备降场列表

    public List<String> getClimbAltitudeList() {
        return climbAltitudeList;
    }

    public void setClimbAltitudeList(List<String> climbAltitudeList) {
        this.climbAltitudeList = climbAltitudeList;
    }

    public List<String> getCruiseLevelAltitudeList() {
        return cruiseLevelAltitudeList;
    }

    public void setCruiseLevelAltitudeList(List<String> cruiseLevelAltitudeList) {
        this.cruiseLevelAltitudeList = cruiseLevelAltitudeList;
    }

    public List<String> getCruiseLevelWaypointList() {
        return cruiseLevelWaypointList;
    }

    public void setCruiseLevelWaypointList(List<String> cruiseLevelWaypointList) {
        this.cruiseLevelWaypointList = cruiseLevelWaypointList;
    }

    public List<String> getDescentAltitudeList() {
        return descentAltitudeList;
    }

    public void setDescentAltitudeList(List<String> descentAltitudeList) {
        this.descentAltitudeList = descentAltitudeList;
    }

    public List<String> getAlternateAirportList() {
        return alternateAirportList;
    }

    public void setAlternateAirportList(List<String> alternateAirportList) {
        this.alternateAirportList = alternateAirportList;
    }

    public String toInputString(){
        try {
            String output="PWI MANUAL:";
            //爬升点高度列表:XXX,百英尺
            if(!CollectionUtils.isEmpty(climbAltitudeList)){
                output +="[climbAltitude:";
                int i=0;
                for(String cl: climbAltitudeList){
                    if(i++>0) output +=",";
                    output +=cl;
                }
                output +="] ";
            }

            //目标风层高度列表:XXX,百英尺
            if(!CollectionUtils.isEmpty(cruiseLevelAltitudeList)){
                output +="\r\n[cruiseLevelAltitude:";
                int i=0;
                for(String cl: cruiseLevelAltitudeList){
                    if(i++>0) output +=",";
                    output +=cl;
                }
                output +="] ";
            }

            ////风层航路点列表:航路点名称，或者经纬度点
            if(!CollectionUtils.isEmpty(cruiseLevelWaypointList)){
                output +="\r\n[cruiseLevelWaypoint:";
                int i=0;
                for(String cl: cruiseLevelWaypointList){
                    if(i++>0) output +=",";
                    output +=cl;
                }
                output +="] ";
            }

            //下降点高度列表:XXX,百英尺
            if(!CollectionUtils.isEmpty(descentAltitudeList)){
                output +="\r\n[descentAltitude:";
                int i=0;
                for(String cl: descentAltitudeList){
                    if(i++>0) output +=",";
                    output +=cl;
                }
                output +="] ";
            }
            //备降场列表
            if(!CollectionUtils.isEmpty(alternateAirportList)){
                output +="\r\n[alternateAirport:";
                int i=0;
                for(String cl: alternateAirportList){
                    if(i++>0) output +=",";
                    output +=cl;
                }
                output +="] ";
            }
            return output;
        } catch (Exception e) {
            return "";
        }
    }
}
