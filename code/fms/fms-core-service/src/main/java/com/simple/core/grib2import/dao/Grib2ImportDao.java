package com.simple.core.grib2import.dao;

import com.simple.core.pwi.entity.Grib2WdInfo;

import java.util.List;

/**
 * @author Administrator.
 * @version 1.0 .
 * @since 2017.03.13 10:02.
 */
public interface Grib2ImportDao {

     int saveGrib2Data(List<Grib2WdInfo> list);

     long countByTimeAlt(String startTime, String endTime, int alt);

     long deleteByTimeAlt(String startTime, String endTime, int alt);


}
