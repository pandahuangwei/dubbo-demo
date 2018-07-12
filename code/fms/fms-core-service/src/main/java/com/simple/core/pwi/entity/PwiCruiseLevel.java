package com.simple.core.pwi.entity;

import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuoMengLan.
 * @version 1.0 .
 * @since 2017.03.17 19:43.
 */
public class PwiCruiseLevel implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 高度层
     */
    private String alt;
    /**
     * 该高度层下的航路点数据
     */
    private List<PwiPoint> pointList;

    /**
     * 无效的航路点：风温缺失
     */
    private List<PwiPoint> invalidPointList;

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public List<PwiPoint> getPointList() {
        return pointList;
    }

    public void addPoint(PwiPoint point) {
        if(null==point) return;
        if(null == pointList) pointList = new ArrayList<>();
        this.pointList.add(point);
    }

    public List<PwiPoint> getInvalidPoints() {
        return invalidPointList;
    }

    public void addInvalidPoint(PwiPoint point) {
        if(null==point) return;
        if(null == invalidPointList) invalidPointList = new ArrayList<>();
        this.invalidPointList.add(point);
    }
}
