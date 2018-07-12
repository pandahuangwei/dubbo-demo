package com.simple.core.freetext.facade.impl;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.freetext.service.impl.FreeTextServiceImpl;
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

/**
 * Created by Administrator on 2017/3/1.
 */
@Path("freetextFacade")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Component("freetextFacade")
public class FreeTextFacadeImpl implements ParseMsgFacade {
    private static final Logger logger = LoggerFactory.getLogger(FreeTextFacadeImpl.class);

    @Resource(name = "freetextService")
    private FreeTextServiceImpl freetextService;

    @POST
    @Override
    public Pair<MessageEnum.MsgReasonEnum, Flight> filterFlight(List<Flight> list) {
        try {
            return freetextService.filterFlight(list);
        } catch (Exception e) {
            logger.error("FREETEXT filterFlight error:"+e.getMessage(),e);
            return null;
        }
    }

    @POST
    @Override
    public List<MsgBody> parseMessage(Flight flight, String msgContext) {
        try {
            return freetextService.parseMessage(flight,msgContext);
        } catch (Exception e) {
            logger.error("FREETEXT request error:"+e.getMessage(),e);
            return null;
        }
    }

    @POST
    @Override
    public List<MsgBody> parseMessageManual(Flight flight, String msgContext) {
        try {
            return freetextService.parseMessageManual(flight,msgContext);
        } catch (Exception e) {
            logger.error("FREETEXT request by manual error:"+e.getMessage(),e);
            return null;
        }
    }
}
