package com.simple.core.grib2import.service;

import com.simple.core.grib2import.entity.Grib2TimeVo;
import com.simple.core.pwi.entity.Grib2WdInfo;

import java.util.List;

/**
 * @author Administrator.
 * @version 1.0 .
 * @since 2017.03.13 10:03.
 */
public interface Grib2ImportService{

    void storeGrib2Data(List dataList, Grib2TimeVo grib2TimeVo, int iCurAtoms);
    int saveNewData(List dataList);
}
