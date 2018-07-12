package com.simple.core.common.entity;

import java.io.Serializable;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.06 10:47.
 */
public class ReqParameter implements Serializable {
    private static final long serialVersionUID = 11L;

    private String carrierIata;
    private boolean allFlag;
    private int startIndex = 0;
    private int pageSize = 50;
    public ReqParameter(){}

    public ReqParameter(String carrierIata) {
        this.carrierIata = carrierIata;
    }
    public ReqParameter(String carrierIata, boolean allFlag, int startIndex, int pageSize) {
        this.carrierIata = carrierIata;
        this.allFlag = allFlag;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public String getCarrierIata() {
        return carrierIata;
    }

    public void setCarrierIata(String carrierIata) {
        this.carrierIata = carrierIata;
    }

    public boolean isAllFlag() {
        return allFlag;
    }

    public void setAllFlag(boolean allFlag) {
        this.allFlag = allFlag;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageSize() {
        return pageSize <= 0 ? 50 : pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "ReqParameter{" +
                "carrierIata='" + carrierIata + '\'' +
                ", allFlag=" + allFlag +
                ", startIndex=" + startIndex +
                ", pageSize=" + pageSize +
                '}';
    }
}