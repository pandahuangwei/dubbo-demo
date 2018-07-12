package com.simple.core.grib2import.entity;

import java.io.Serializable;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.13 14:15.
 */
public class Grib2CalcParam  implements Serializable {
    private static final long serialVersionUID = 1L;
    private String pathTemp;
    private String pathUV;
    private int iCurAtoms;

    public Grib2CalcParam(String pathTemp, String pathUV, int iCurAtoms) {
        this.pathTemp = pathTemp;
        this.pathUV = pathUV;
        this.iCurAtoms = iCurAtoms;
    }

    public String getPathTemp() {
        return pathTemp;
    }

    public void setPathTemp(String pathTemp) {
        this.pathTemp = pathTemp;
    }

    public String getPathUV() {
        return pathUV;
    }

    public void setPathUV(String pathUV) {
        this.pathUV = pathUV;
    }

    public int getiCurAtoms() {
        return iCurAtoms;
    }

    public void setiCurAtoms(int iCurAtoms) {
        this.iCurAtoms = iCurAtoms;
    }
}
