package com.simple.core.fpn.facade.impl;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.fpn.service.impl.FpnServiceImpl;
import com.simple.core.common.entity.Flight;
import com.simple.core.common.entity.MsgBody;
import com.simple.core.common.facade.ParseMsgFacade;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: FpnFacade.java.
 * @Package com.simple.core.fpn.facade.
 * @Description: fpn航路上传服务.
 *
 * @author Administrator.
 * @version 1.0.
 * @created 2017/3/16 13:52.
 **********************************************************************************/
@Path("fpnFacade")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Component("fpnFacade")
public class FpnFacadeImpl implements ParseMsgFacade {
    private static final Logger logger = LoggerFactory.getLogger(FpnFacadeImpl.class);

    @Resource(name = "fpnService")
    private FpnServiceImpl fpnService;

    @POST
    @Override
    public Pair<MessageEnum.MsgReasonEnum, Flight> filterFlight(List<Flight> flight) {
        try {
            return fpnService.filterFlight(flight);
        } catch (Exception e) {
            logger.error("FPN filterFlight error:"+e.getMessage(),e);
            return null;
        }
    }

    @POST
    @Override
    public List<MsgBody> parseMessage(Flight flight, String msgContext) {
        try {
            return fpnService.parseMessage(flight,msgContext);
        } catch (Exception e) {
            logger.error("FPN request error:"+e.getMessage(),e);
            return null;
        }
    }

    @POST
    @Override
    public List<MsgBody> parseMessageManual(Flight flight, String msgContext) {
        try {
            return fpnService.parseMessageManual(flight,msgContext);
        } catch (Exception e) {
            logger.error("FPN request by manual error:"+e.getMessage(),e);
            return null;
        }
    }
}
