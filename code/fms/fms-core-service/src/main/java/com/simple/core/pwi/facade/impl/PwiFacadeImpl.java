package com.simple.core.pwi.facade.impl;


import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.pwi.service.impl.PwiServiceImpl;
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
 * @author zhuomenglan.
 * @version 1.0 .
 * @since 2017.03.13 10:08.
 */
@Path("pwiFacade")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Component("pwiFacade")
public class PwiFacadeImpl implements ParseMsgFacade {
    private static final Logger logger = LoggerFactory.getLogger(com.simple.core.per.facade.PerFacadeImpl.class);

    @Resource(name = "pwiService")
    private PwiServiceImpl pwiService;

    @POST
    @Override
    public Pair<MessageEnum.MsgReasonEnum, Flight> filterFlight(List<Flight> list) {
        try {
            return pwiService.filterFlight(list);
        } catch (Exception e) {
            logger.error("PWI filterFlight error:"+e.getMessage(),e);
            return null;
        }
    }

    @POST
    @Override
    public List<MsgBody> parseMessage(Flight flight, String msgContext) {
        try {
            return pwiService.parseMessage(flight,msgContext);
        } catch (Exception e) {
            logger.error("PWI request error:"+e.getMessage(),e);
            return null;
        }
    }

    @POST
    @Override
    public List<MsgBody> parseMessageManual(Flight flight, String msgContext) {
        try {
            return pwiService.parseMessageManual(flight,msgContext);
        } catch (Exception e) {
            logger.error("PWI request by manual error:"+e.getMessage(),e);
            return null;
        }
    }

}
