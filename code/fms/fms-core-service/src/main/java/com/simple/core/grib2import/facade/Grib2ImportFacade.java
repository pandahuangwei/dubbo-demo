package com.simple.core.grib2import.facade;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.simple.core.grib2import.entity.Grib2TimeVo;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Administrator.
 * @version 1.0 .
 * @since 2017.03.13 10:02.
 */
@Path("grib2ImportFacade")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface Grib2ImportFacade {

    @POST
    void saveGrib2Data(List dataList, Grib2TimeVo grib2TimeVo, int iCurAtoms);
}
