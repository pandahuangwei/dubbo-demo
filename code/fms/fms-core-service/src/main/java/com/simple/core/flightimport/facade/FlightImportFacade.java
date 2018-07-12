package com.simple.core.flightimport.facade;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.simple.core.foc.entity.FocCfp;
import com.simple.core.foc.entity.FocFlight;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
@Path("flightImportFacade")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface FlightImportFacade {
    @POST
    boolean saveFocFlight(List<FocFlight> list,boolean hasDispatchTime,boolean hasCrewArrivalTime);
    @POST
    boolean saveFocCfp(List<FocCfp> list);
    @POST
    Date getMaxCfpModifyTime();
    @POST
    Date getMaxFlightModifyTime();
    @POST
    List<String> getFlightIds(Date flightDate);
    @POST
    int deleteFlightByIds(List<String> ids);
    @POST
    int deleteCfpByIds(List<String> ids);
}
