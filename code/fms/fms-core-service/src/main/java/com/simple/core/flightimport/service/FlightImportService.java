package com.simple.core.flightimport.service;

import com.simple.core.common.entity.FlightBase;
import com.simple.core.foc.entity.FocCfp;
import com.simple.core.foc.entity.FocFlight;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public interface FlightImportService {

    boolean saveFocFlight(List<FocFlight> list, boolean hasDispatchTime, boolean hasCrewArrivalTime);
    boolean saveFocCfp(List<FocCfp> list);
    boolean storeOneFocFlight(FocFlight flight,boolean hasDispatchTime,boolean hasCrewArrivalTime);
    boolean storeOneFocCfp(FocCfp cfp);
    Date getMaxCfpModifyTime();
    Date getMaxFlightModifyTime();

    List<String> getFlightIds(Date flightDate);
    int deleteFlightByIds(List<String> ids);
    int deleteCfpByIds(List<String> ids);

}
