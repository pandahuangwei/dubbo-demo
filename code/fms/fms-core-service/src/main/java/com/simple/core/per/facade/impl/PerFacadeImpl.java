package com.simple.core.per.facade;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.per.service.impl.PerServiceImpl;
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
@Path("perFacade")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Component("perFacade")
public class PerFacadeImpl implements ParseMsgFacade {
    private static final Logger logger = LoggerFactory.getLogger(PerFacadeImpl.class);

    @Resource(name = "perService")
    private PerServiceImpl perService;

    @Override
    public Pair<MessageEnum.MsgReasonEnum, Flight> filterFlight(List<Flight> list) {
        try {
            return perService.filterFlight(list);
        } catch (Exception e) {
            logger.error("PER filterFlight error:"+e.getMessage(),e);
            return null;
        }
    }

    @POST
    @Override
    public List<MsgBody> parseMessage(Flight flight, String msgContext) {
        try {
            return perService.parseMessage(flight,msgContext);
        } catch (Exception e) {
            logger.error("PER request error:"+e.getMessage(),e);
            return null;
        }
    }

    @POST
    @Override
    public List<MsgBody> parseMessageManual(Flight flight, String msgContext) {
        try {
            return perService.parseMessageManual(flight,msgContext);
        } catch (Exception e) {
            logger.error("PER request by manual error:"+e.getMessage(),e);
            return null;
        }
    }
}
