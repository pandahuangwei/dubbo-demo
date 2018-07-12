package com.simple.core.route.facade.impl;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.simple.core.common.entity.Message;
import com.simple.core.common.entity.MessageCipher;
import com.simple.core.common.entity.MessageClear;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.common.facade.AnalyzeMsgFacade;
import com.simple.core.route.service.RouteMsgService;
import com.simple.core.route.util.RoutePropertyUtil;
import com.simple.core.route.util.SendMsgWsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.22 11:31.
 */
@Path("analyze")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Service("analyzeMsgRestService")
public class AnalyzeMsgRouteRestImpl implements AnalyzeMsgFacade {
    private static Logger logger = LoggerFactory.getLogger(AnalyzeMsgRouteRestImpl.class);

    @Resource(name = "routeMsgService")
    private RouteMsgService routeMsgService;

    @POST
    @Path("downLinkMsgNotice")
    @Override
    public Message downLinkMsgNotice(String message) {
        Message msg;
        try {
            msg = routeMsgService.downLinkMsgNotice(message);
        } catch (Exception e) {
            msg = getErrResult();
            logger.error("AnalyzeMessage is Err:{}", e.toString());
        }
        return encryptMsg(msg);
    }

    /**
     * 3.报文上行状态通知接口
     *
     * @param message 报文
     * @return 报文
     */
    @POST
    @Path("upLinkMsgStateNotice")
    @Override
    public Message upLinkMsgStateNotice(String message) {
        Message msg;
        try {
            msg = routeMsgService.upLinkMsgStateNotice(message);
        } catch (Exception e) {
            msg = getErrResult();
            logger.error("upLinkMsgStateNotice is Err:{}", e.getMessage());
        }
        return encryptMsg(msg);
    }

    /**
     * 5.报文下行异常通知(1.报文超时；2.接口异常（对应1）)
     *
     * @param message 报文
     * @return 报文
     */
    @POST
    @Path("downLinkMsgNoticeExc")
    @Override
    public Message downLinkMsgNoticeExc(String message) {
        Message msg;
        try {
            msg = routeMsgService.downLinkMsgNoticeExc(message);
        } catch (Exception e) {
            msg = getErrResult();
            logger.error("downLinkMsgNoticeExc is Err:{}", e.getMessage());
        }
        return encryptMsg(msg);
    }

    /**
     * 6.报文上行状态异常通知
     *
     * @param message 报文
     * @return 报文
     */
    @POST
    @Path("upLinkMsgStateNoticeExc")
    @Override
    public Message upLinkMsgStateNoticeExc(String message) {
        Message msg;
        try {
            msg = routeMsgService.upLinkMsgStateNoticeExc(message);
        } catch (Exception e) {
            msg = getErrResult();
            logger.error("upLinkMsgStateNoticeExc is Err:{}", e.getMessage());
        }
        return encryptMsg(msg);
    }


    /**
     * 4.飞机拒绝报文下行通知
     *
     * @param message 报文
     * @return 报文
     */
    @POST
    @Path("rejectMsgNotice")
    @Override
    public Message rejectMsgNotice(String message) {
        Message msg;
        try {
            msg = routeMsgService.rejectMsgNotice(message);
        } catch (Exception e) {
            msg = getErrResult();
            logger.error("rejectMsgNotice is Err:{}", e.getMessage());
        }
        return encryptMsg(msg);
    }

    /**
     * 7.飞机拒绝报文下行异常通知（对应4的通知）
     *
     * @param message 报文
     * @return 报文
     */
    @POST
    @Path("rejectMsgNoticeExc")
    @Override
    public Message rejectMsgNoticeExc(String message) {
        Message msg;
        try {
            msg = routeMsgService.rejectMsgNoticeExc(message);
        } catch (Exception e) {
            msg = getErrResult();
            logger.error("rejectMsgNoticeExc is Err:{}", e.getMessage());
        }
        return encryptMsg(msg);
    }

    @POST
    @Path("upLinkMsgInitiative")
    @Override
    public Message upLinkMsgInitiative(String message) {
        Message msg;
        try {
            msg =  routeMsgService.upLinkMsgInitiative(message);
        } catch (Exception e) {
            msg = getErrResult();
            logger.error("upLinkMsgInitiative is Err:{}", e.getMessage());
        }
        msg =encryptMsg(msg);
        logger.info("Start to Send to ....");
        try {
            String s = SendMsgWsUtil.getInstance().sendAxisProxy(JSON.toJSONString(msg));
        } catch (Exception e) {
            logger.info("upLinkMsgInitiative is,{}", e.toString());
            msg = getErrResult();
        }

        return msg;
    }

    @POST
    @Path("answerMsgNotice")
    @Override
    public Message answerMsgNotice(String message) {
        Message msg;
        try {
            return routeMsgService.answerMsgNotice(message);
        } catch (Exception e) {
            msg = getErrResult();
            logger.error("answerMsgNotice is Err:{}", e.getMessage());
        }
        return encryptMsg(msg);
    }

    private Message encryptMsg(Message msg) {
        return RoutePropertyUtil.getInstance().isMsgEncrypt() ? new MessageCipher("entry", msg) : msg;
    }

    /**
     * 增加接口内部错误时返回的实体
     *
     * @return 接口内部错误时返回实体
     */
    private Message getErrResult() {
        MessageClear rs = new MessageClear<>();
        rs.setStatus(false);
        rs.setErrReason(MessageEnum.MsgReasonEnum.SERVICE_INNER_ERR.getKey());
        return rs;
    }

}