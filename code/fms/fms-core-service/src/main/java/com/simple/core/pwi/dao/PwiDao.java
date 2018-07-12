package com.simple.core.pwi.dao;

import com.simple.core.common.entity.Flight;
import com.simple.core.common.entity.FlightBase;
import com.simple.core.flightimport.entity.Cpt;
import com.simple.core.pwi.entity.Grib2WdInfo;

import java.text.ParseException;
import java.util.List;
/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: PwiDao.java.
 * @Package com.simple.core.pwi.dao.
 * @Description: 风温dao.
 *
 * @author Administrator.
 * @version 1.0.
 * @created 2017/3/21 14:37.
 **********************************************************************************/
public interface PwiDao {

    Cpt findByCptnameForWayPoint(String cptname);

    Cpt findByCptnameForNaip(String cptname);

    List<Grib2WdInfo> findByLonLatAlt(List lon, List lat, List alt);

}
