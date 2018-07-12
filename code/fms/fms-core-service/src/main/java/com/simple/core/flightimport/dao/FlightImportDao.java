package com.simple.core.flightimport.dao;

import com.simple.core.flightimport.entity.CfpExt;
import com.simple.core.flightimport.entity.Cpt;
import com.simple.core.common.entity.FlightBase;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */
public interface  FlightImportDao {

    FlightBase selectFlightByFocFlightId(String id);
    FlightBase selectFlightByUnique(Date fliDate,String fliNo,String pod,String poa,Date std);

    int insertFlight(FlightBase flight);

    int updateFlightForPlan(FlightBase flight);

    int updateFlight(FlightBase flight);

    CfpExt findCfpByPlanNo(String focFlightId,String planNo);

    Date getMaxCfpModifyTime();
    Date getMaxFlightModifyTime();

    int insertCfp(CfpExt cfp);
    int updateCfp(CfpExt cfp);

    int selectWayPointByCptName(String cptName);
    int insertWayPoint(Cpt cpt);
    int updateWayPoint(Cpt cpt);

    List<String> getFlightIds(Date flightDate);
    int deleteFlightById(List<String> list);
    int deleteCfpById(List<String> list);
}
