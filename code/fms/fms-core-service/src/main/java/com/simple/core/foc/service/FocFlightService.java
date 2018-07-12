package com.simple.core.foc.service;


import com.simple.core.foc.entity.FocFlight;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/22.
 */
public interface FocFlightService {

    void setCarrierIATA(String iata);

    boolean sendFocFlightToImporter(Date start, Date end,boolean hasDispatchTime,boolean hasCrewArrivalTime);
    boolean sendFocFlightToImporter(Date lastUpdateTime,boolean hasDispatchTime,boolean hasCrewArrivalTime);

    boolean sendFocCfpToImporter(Date start, Date end);
    boolean sendFocCfpToImporter(Date lastUpdateTime);

    Date getMaxFlightModifyTimeFromImporter(int updateTimeDefault);
    Date getMaxCfpModifyTimeFromImporter(int updateTimeDefault);

    void syncDeletedFlights();
}
