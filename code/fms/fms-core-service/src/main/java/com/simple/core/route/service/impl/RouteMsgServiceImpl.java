package com.simple.core.route.service.impl;

import com.alibaba.fastjson.JSON;
import com.simple.common.utils.DateTimeUtil;
import com.simple.common.utils.StringUtil;
import com.simple.common.utils.UuidUtil;
import com.simple.core.common.entity.*;
import com.simple.core.common.enums.MessageEnum;
import com.simple.core.common.enums.MessageEnum.MsgReasonEnum;
import com.simple.core.common.enums.MessageEnum.MsgStep;
import com.simple.core.common.facade.ChargingFacade;
import com.simple.core.common.facade.ParseMsgFacade;
import com.simple.core.freetext.entity.UpLinkFreeText;
import com.simple.core.route.flight.service.FlightService;
import com.simple.core.route.persist.entity.PersistMsgInfo;
import com.simple.core.route.persist.service.PersistMsgService;
import com.simple.core.route.service.RouteMsgService;
import com.simple.core.route.util.ReferenceBeanFactory;
import com.simple.core.route.util.RoutePropertyUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 报文正常通知接口实现
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.22 11:21.
 */
@Service("routeMsgService")
public class RouteMsgServiceImpl implements RouteMsgService {
    private static Logger logger = LoggerFactory.getLogger(RouteMsgServiceImpl.class);

    @Resource(name = "flightService")
    private FlightService flightService;
    @Resource(name = "chargingRestService")
    private ChargingFacade chargingRestService;
    @Resource(name = "persistMsgService")
    private PersistMsgService persistMsgService;

    /**
     * 1.报文下行通知 -服务派发:<br>
     * 1、解密;<br>
     * 2、转实体，得到报文头部信息;<br>
     * 3、根据报文头(飞机号、报文类型)访问授权服务;<br>
     * 4、根据飞机号访问航班信息服务，得到航班信息实体;<br>
     * 5、根据规则过滤，判断报文是否有效;<br>
     * 6、根据类型选择相应服务;<br>
     * 7、得到报文体，组装报文,返回.<br>
     * 8、持久化.
     *
     * @param message 外部服务传入的原始报文
     * @return 解析后的报文
     */
    @Override
    public Message downLinkMsgNotice(String message) throws Exception {
        String msg = decryptMsg(message);
        MsgOrig msgOrig = convertMsg(msg);

        MessageClear<MsgBody> rsMsg = createMsg(msgOrig);
        PersistMsgInfo persistInfo = createPersistInfo(msgOrig, rsMsg);

        if (!packMsg(msgOrig, rsMsg, persistInfo)) {
            freetextNotify(true, rsMsg);
        }
        persistMessage(persistInfo);

        return rsMsg;
    }

    /**
     * 3.报文上行状态通知接口
     *
     * @param message 字符串报文
     */
    @Override
    public Message upLinkMsgStateNotice(String message) throws Exception {
        NoticeMsg noticeMsg = convertNoticeMsg(message);
        if (MsgReasonEnum.MSG_UPLINK_SEND_SUCC.eq(noticeMsg.getErrReason())) {
            noticeMsg.setMsgStep(MsgStep.SEND.getKey());
        }
        if (MsgReasonEnum.MSG_UPLINK_SEND_FAIL.eq(noticeMsg.getErrReason())) {
            noticeMsg.setMsgStep(MsgStep.SEND_FAIL.getKey());
        }

        MessageClear msg = createMsg(noticeMsg);
        try {
            persistMsgService.saveUpLinkMsgStateNotice(noticeMsg);
        } catch (Exception e) {
            logger.info("upLinkMsgStateNotice is,{}", e.getMessage());
            msg.setStatus(false);
            msg.setErrReason(MsgReasonEnum.SERVICE_INNER_ERR.getKey());
        }
        return msg;
    }

    /**
     * 5.报文下行异常通知(1.报文超时；2.接口异常（对应1）)
     *
     * @param message 报文
     * @return 报文
     */
    @Override
    public Message downLinkMsgNoticeExc(String message) throws Exception {
        NoticeMsg noticeMsg = convertNoticeMsg(message);
        noticeMsg.setMsgStep(MsgStep.REQ_FAIL.getKey());
        MessageClear msg = createMsg(noticeMsg);
        try {
            persistMsgService.saveDownLinkMsgNoticeExc(noticeMsg);
        } catch (Exception e) {
            logger.info("upLinkMsgStateNotice is,{}", e.getMessage());
            msg.setStatus(false);
            msg.setErrReason(MsgReasonEnum.SERVICE_INNER_ERR.getKey());
        }
        return msg;
    }

    /**
     * 6.报文上行状态异常通知
     *
     * @param message 报文
     * @return 报文
     */
    @Override
    public Message upLinkMsgStateNoticeExc(String message) throws Exception {
        NoticeMsg noticeMsg = convertNoticeMsg(message);
        noticeMsg.setMsgStep(MsgStep.SEND_FAIL.getKey());
        MessageClear msg = createMsg(noticeMsg);
        try {
            persistMsgService.saveUpLinkMsgStateNoticeExc(noticeMsg);
        } catch (Exception e) {
            logger.info("upLinkMsgStateNotice is,{}", e.getMessage());
            msg.setStatus(false);
            msg.setErrReason(MsgReasonEnum.SERVICE_INNER_ERR.getKey());
        }
        return msg;
    }

    /**
     * 4.飞机拒绝报文下行通知
     *
     * @param message 报文
     * @return 报文
     */
    @Override
    public Message rejectMsgNotice(String message) throws Exception {
        NoticeMsg noticeMsg = convertNoticeMsg(message);
        noticeMsg.setMsgStep(MsgStep.RJ.getKey());
        MessageClear msg = createMsg(noticeMsg);
        try {
            persistMsgService.saveRejectMsgNotice(noticeMsg);
        } catch (Exception e) {
            logger.info("upLinkMsgStateNotice is,{}", e.getMessage());
            msg.setStatus(false);
            msg.setErrReason(MsgReasonEnum.SERVICE_INNER_ERR.getKey());
        }
        return msg;
    }

    /**
     * 7.飞机拒绝报文下行异常通知（对应4的通知）
     *
     * @param message 报文
     * @return 报文
     */
    @Override
    public Message rejectMsgNoticeExc(String message) throws Exception {
        NoticeMsg noticeMsg = convertNoticeMsg(message);
        noticeMsg.setMsgStep(MsgStep.RJ.getKey());
        MessageClear msg = createMsg(noticeMsg);
        try {
            persistMsgService.saveRejectMsgNoticeExc(noticeMsg);
        } catch (Exception e) {
            logger.info("upLinkMsgStateNotice is,{}", e.getMessage());
            msg.setStatus(false);
            msg.setErrReason(MsgReasonEnum.SERVICE_INNER_ERR.getKey());
        }
        return msg;
    }

    @Override
    public Message upLinkMsgInitiative(String message) throws Exception {
        Msg3rdOrig msgOrig = JSON.parseObject(decryptMsg(message), Msg3rdOrig.class);

        MessageClear<MsgBody> rsMsg = createMsg(msgOrig);
        PersistMsgInfo persistInfo = createPersistInfo(msgOrig, rsMsg);

        packMsg3rd(msgOrig, rsMsg, persistInfo);
        persistMessage(persistInfo);

        return rsMsg;
    }

    @Override
    public Message answerMsgNotice(String message) throws Exception {
        NoticeMsg noticeMsg = convertNoticeMsg(message);
        MessageClear msg = createMsg(noticeMsg);
        try {
            persistMsgService.answerMsgNotice(noticeMsg);
        } catch (Exception e) {
            logger.info("answerMsgNotice is,{}", e.getMessage());
            msg.setStatus(false);
            msg.setErrReason(MsgReasonEnum.SERVICE_INNER_ERR.getKey());
        }
        return msg;
    }

    /**
     * 组装报文
     *
     * @param msgOrig     原始报文实体
     * @param rsMsg       返回报文实体
     * @param persistInfo 持久化报文实体
     * @throws Exception 异常
     */
    private boolean packMsg(MsgOrig msgOrig, MessageClear<MsgBody> rsMsg, PersistMsgInfo persistInfo) throws Exception {
        Auth auth = chargingRestService.getAuth(msgOrig.getAcReg(), msgOrig.getMsgType());
        boolean isEffect = isAuth(auth, msgOrig, rsMsg, persistInfo);
        if (!isEffect) {
            return false;
        }

        //以下情况，在无效时可以选择发送一个自由报：通告错误状态
        Pair<MessageEnum.MsgReasonEnum, List<Flight>> pairFlight =
                flightService.getFlight(auth.getCarrierIata(), msgOrig.getAcReg(),
                        msgOrig.getMsgType(), msgOrig.getMsgTm());
        if (null != pairFlight.getLeft()) {
            persistInfo.setMsgReason(pairFlight.getLeft().getKey());
            rsMsg.setErrReason(pairFlight.getLeft().getKey());
            rsMsg.setErrReasonText(pairFlight.getLeft().getValue2());
            return false;
        }
        List<Flight> list = pairFlight.getRight();
        isEffect = isEffectiveFlight(list, rsMsg, persistInfo);
        if (!isEffect) {
            return false;
        }

        Pair<MessageEnum.MsgReasonEnum, Flight> pair = filterFlight(list, msgOrig.getMsgType());
        isEffect = isEffectiveFlight(pair, rsMsg, persistInfo);
        if (!isEffect) {
            return false;
        }

        assert pair != null;
        Flight flight = pair.getValue();
        fillFlightIdentInfo(flight, rsMsg);
        List<MsgBody> msgBody = getMsgBody(flight, msgOrig.getContent(), msgOrig.getMsgType());
        isEffect = isEffectiveMsgBody(msgBody, rsMsg, persistInfo);
        if (!isEffect) {
            return false;
        }

        rsMsg.setStatus(MessageEnum.MessageStatus.SUCC.getKey());
        persistInfo.setMsgStep(MsgStep.REQ.getKey());
        rsMsg.setMsgTm(DateTimeUtil.getTime());
        rsMsg.addAllBody(msgBody);
        setUpInfoAndSaveTrail(persistInfo, rsMsg, msgBody);
        return true;
    }

    /**
     * 第三方手动上传报文
     *
     * @param msgOrig     原始报文实体
     * @param rsMsg       返回报文实体
     * @param persistInfo 持久化报文实体
     * @throws Exception 异常
     */
    private boolean packMsg3rd(Msg3rdOrig msgOrig, MessageClear<MsgBody> rsMsg, PersistMsgInfo persistInfo) throws Exception {
        Flight flight = flightService.getFlight(msgOrig.getFlightDate(), msgOrig.getFlightNo(), msgOrig.getDepartureAirport(), msgOrig.getArrivalAirport(), msgOrig.getStd());
        boolean isEffect = isEffectiveFlight(flight, rsMsg, persistInfo);
        if (!isEffect) {
            return false;
        }
        fillFlightIdentInfo(flight, rsMsg);
        List<MsgBody> msgBody = getMsgBody3rd(flight, msgOrig.getContent(), msgOrig.getMsgType());
        isEffect = isEffectiveMsgBody(msgBody, rsMsg, persistInfo);
        if (!isEffect) {
            return false;
        }

        rsMsg.setStatus(MessageEnum.MessageStatus.SUCC.getKey());
        persistInfo.setMsgStep(MsgStep.REQ.getKey());
        rsMsg.setMsgTm(DateTimeUtil.getTime());
        rsMsg.addAllBody(msgBody);
        setUpInfoAndSaveTrail(persistInfo, rsMsg, msgBody);

        return true;
    }

    private void fillFlightIdentInfo(Flight flight, MessageClear<MsgBody> rsMsg) {
        rsMsg.setFlightNo(flight.getFlightNo());
        rsMsg.setFlightDt(DateTimeUtil.toStingShort(flight.getFlightDate()));
        rsMsg.setPod(flight.getDepartureAirport());
        rsMsg.setPoa(flight.getArrivalAirport());
        rsMsg.setStd(DateTimeUtil.toSting(flight.getStd()));
    }

    private void setUpInfoAndSaveTrail(PersistMsgInfo persistInfo, MessageClear<MsgBody> rsMsg, List<MsgBody> list) throws Exception {
        persistInfo.setMsgReason(MsgReasonEnum.REQ_SERVICE_SUCC.getKey());
        persistInfo.setUpSendTm(DateTimeUtil.getNowDt());
        persistInfo.setUpMsgChannel(rsMsg.getChannel());
        persistInfo.setUpMsgType(rsMsg.getMsgType());

        //String jsonString = JSON.toJSONString(rsMsg.getContentList());
        persistInfo.setUpMsgContent(genParam(list, true));
        persistInfo.setDownMsgContent(genParam(list, false));
    }

    private void persistMessage(PersistMsgInfo persistInfo) throws Exception {
        persistMsgService.saveDownLinkMsgNotice(persistInfo);
    }

    private void freetextNotify(boolean uplinkFreeText, MessageClear<MsgBody> rsMsg) throws Exception {
        if (uplinkFreeText) {
            //组装自由报
            MsgBody<UpLinkFreeText> msg = new MsgBody<>();
            msg.setErrCode(MessageEnum.MsgReasonEnum.SERVICE_REQ_SUCC.getKey());
            msg.setMsgType(MessageEnum.MessageType.FREETEXT.getKey());
            UpLinkFreeText cmd = new UpLinkFreeText();
            cmd.setText(rsMsg.getErrReasonText());
            msg.setContent(cmd);
            rsMsg.addBody(msg);
        }
    }

    /**
     * 通知报文转对应实体
     *
     * @param noticeMsg 通知报文
     * @return 通知报文实体
     */
    private NoticeMsg convertNoticeMsg(String noticeMsg) {
        return JSON.parseObject(noticeMsg, NoticeMsg.class);
    }

    /**
     * 解密
     *
     * @param message 原始报文
     * @return 解密后的报文
     */
    private String decryptMsg(String message) {
        return message;
    }

    /**
     * 将报文构建为实体，得到报文头与报文体
     *
     * @param message 解密后的报文
     * @return 原始报文实体
     */
    private MsgOrig convertMsg(String message) {
        MsgOrig msgOrig = JSON.parseObject(message, MsgOrig.class);
        logger.info("msgOrig is,{}", msgOrig.toString());
        return msgOrig;
    }


    /**
     * 调用授权服务，判断是否有权限访问服务
     *
     * @param auth  授权实体
     * @param rsMsg 返回报文
     * @return 是否授权
     */
    private boolean isAuth(Auth auth, MsgOrig orig, MessageClear<MsgBody> rsMsg, PersistMsgInfo persistInfo) {
        if (auth == null) {
            persistInfo.setMsgReason(MsgReasonEnum.ROUTE_MSG_AUTH_UNDEF.getKey());
            rsMsg.setErrReason(MsgReasonEnum.ROUTE_MSG_AUTH_UNDEF.getKey());
            rsMsg.setErrReasonText(MsgReasonEnum.ROUTE_MSG_AUTH_UNDEF.getValue2());
            return false;
        }

        if (!DateTimeUtil.isBetweenTwoDate(orig.getMsgTm(), auth.getEffectiveDt(), auth.getExpireDt())) {
            persistInfo.setMsgReason(MsgReasonEnum.ROUTE_MSG_AUTH_EXPIRE.getKey());
            rsMsg.setErrReason(MsgReasonEnum.ROUTE_MSG_AUTH_EXPIRE.getKey());
            rsMsg.setErrReasonText(MsgReasonEnum.ROUTE_MSG_AUTH_EXPIRE.getValue2());
            return false;
        }
        return true;
    }

    /**
     * 是否有效航班
     *
     * @param flight      航班信息
     * @param rsMsg       返回报文
     * @param persistInfo 持久化实体
     * @return 是否有效
     */
    private boolean isEffectiveFlight(List<Flight> flight, MessageClear<MsgBody> rsMsg, PersistMsgInfo persistInfo) {
        if (flight == null || flight.isEmpty()) {
            persistInfo.setMsgReason(MsgReasonEnum.ROUTE_MSG_FLIGHT_NOTFOUND.getKey());
            rsMsg.setErrReason(MsgReasonEnum.ROUTE_MSG_FLIGHT_NOTFOUND.getKey());
            rsMsg.setErrReasonText(MsgReasonEnum.ROUTE_MSG_FLIGHT_NOTFOUND.getValue2());
            return false;
        }
        return true;
    }

    private boolean isEffectiveFlight(Flight flight, MessageClear<MsgBody> rsMsg, PersistMsgInfo persistInfo) {
        if (flight == null) {
            persistInfo.setMsgReason(MsgReasonEnum.ROUTE_MSG_FLIGHT_NOTFOUND.getKey());
            rsMsg.setErrReason(MsgReasonEnum.ROUTE_MSG_FLIGHT_NOTFOUND.getKey());
            rsMsg.setErrReasonText(MsgReasonEnum.ROUTE_MSG_FLIGHT_NOTFOUND.getValue2());
            return false;
        }
        return true;
    }

    private boolean isEffectiveFlight(Pair<MessageEnum.MsgReasonEnum, Flight> pair, MessageClear<MsgBody> rsMsg, PersistMsgInfo persistInfo) {
        if (pair == null) {
            setMsgReason(rsMsg, persistInfo);
            return false;
        }
        MessageEnum.MsgReasonEnum err = pair.getLeft();
        persistInfo.setMsgReason(err.getKey());
        rsMsg.setErrReason(err.getKey());
        rsMsg.setErrReasonText(err.getValue2());
        Flight flight = pair.getRight();
        if (flight != null) {
            persistInfo.setFlightId(flight.getFlightId());
            persistInfo.setFlightNo(flight.getFlightNo());
            persistInfo.setFlightDt(flight.getFlightDate());
            persistInfo.setStd(flight.getStd());
            persistInfo.setDeptAirport(flight.getDepartureAirport());
            persistInfo.setArriveAirport(flight.getArrivalAirport());
        }
        return MsgReasonEnum.SERVICE_REQ_SUCC.eq(err.getKey());
    }

    /**
     * 是否有效解析报文
     *
     * @param listBody    报文列表
     * @param rsMsg       返回报文
     * @param persistInfo 持久化信息
     * @return boolean
     */
    private boolean isEffectiveMsgBody(List<MsgBody> listBody, MessageClear<MsgBody> rsMsg, PersistMsgInfo persistInfo) {
        if (listBody == null) {
            setMsgReason(rsMsg, persistInfo);
            return false;
        }

        if (listBody.size() < 1) {
            persistInfo.setMsgReason(MsgReasonEnum.REQ_SERVICE_FAIL.getKey());
            rsMsg.setErrReason(MsgReasonEnum.REQ_SERVICE_FAIL.getKey());
            rsMsg.setErrReasonText(MsgReasonEnum.REQ_SERVICE_FAIL.getValue2());
            return false;
        }

        MsgBody bd = listBody.get(0);
        int errCode = bd.getErrCode();
        persistInfo.setMsgReason(errCode);
        rsMsg.setErrReason(errCode);
        rsMsg.setErrReasonText(bd.getErrText());
        return MsgReasonEnum.SERVICE_REQ_SUCC.eq(errCode);
    }

    private void setMsgReason(MessageClear<MsgBody> rsMsg, PersistMsgInfo persistInfo) {
        persistInfo.setMsgReason(MsgReasonEnum.ROUTE_NO_SERVICE.getKey());
        rsMsg.setErrReason(MsgReasonEnum.ROUTE_NO_SERVICE.getKey());
        rsMsg.setErrReasonText(MsgReasonEnum.ROUTE_NO_SERVICE.getValue2());
    }

    private Pair<MessageEnum.MsgReasonEnum, Flight> filterFlight(List<Flight> list, String msgType) {
        ParseMsgFacade dynamicFacade = getDynamicFacade(msgType);
        if (dynamicFacade == null) {
            logger.info("this msgType:{},can not get instance.", msgType);
            return null;
        }
        return dynamicFacade.filterFlight(list);
    }


    private List<MsgBody> getMsgBody(Flight flight, String msgContext, String msgType) {
        ParseMsgFacade dynamicFacade = getDynamicFacade(msgType);
        if (dynamicFacade == null) {
            logger.info("this msgType:{},can not get instance.", msgType);
            return null;
        }
        return dynamicFacade.parseMessage(flight, msgContext);
    }

    private List<MsgBody> getMsgBody3rd(Flight flight, String msgContext, String msgType) {
        ParseMsgFacade dynamicFacade = getDynamicFacade(msgType);
        if (dynamicFacade == null) {
            logger.info("this msgType:{},can not get instance.", msgType);
            return null;
        }
        return dynamicFacade.parseMessageManual(flight, msgContext);
    }

    /**
     * 根据报文类型获取动态服务接口<br>
     * 使用静态工厂实现
     *
     * @param msgType 报文类型
     * @return 接口实现
     */
    private ParseMsgFacade getDynamicFacade(String msgType) {
        return ReferenceBeanFactory.getInstance().getFacade(msgType);
    }

    /**
     * 新建一个报文实体
     *
     * @param msgOrig 接口传入的原始报文
     * @return 创建一个明文报文
     */
    private MessageClear<MsgBody> createMsg(MsgOrig msgOrig) {
        MessageClear<MsgBody> msg = new MessageClear<>();
        msg.setUuid(UuidUtil.get32UUID());
        msg.setAcReg(msgOrig.getAcReg());
        msg.setMsgType(msgOrig.getMsgType());
        msg.setChannel(msgOrig.getChannel());
        msg.setStatus(MessageEnum.MessageStatus.FAI.getKey());
        msg.setForceChannel(MessageEnum.ChannelStatus.DEFAULT.getKey());
        return msg;
    }

    private MessageClear createMsg(NoticeMsg noticeMsg) {
        MessageClear msg = new MessageClear<>();
        msg.setUuid(noticeMsg.getUuid());
        Date msgTm = noticeMsg.getMsgTm();
        if (msgTm == null) {
            msgTm = DateTimeUtil.getNowDt();
        }
        msg.setMsgTm(DateTimeUtil.formatTime(msgTm));
        msg.setMsgType(noticeMsg.getMsgType());
        msg.setStatus(true);
        msg.setErrReason(MsgReasonEnum.REQ_SERVICE_SUCC.getKey());
        return msg;
    }


    /**
     * 新建一个持久化报文实体
     *
     * @param msgOrig 原始报文
     * @param msg     报文体
     * @return 持久化报文实体
     */
    private PersistMsgInfo createPersistInfo(MsgOrig msgOrig, MessageClear<MsgBody> msg) {
        PersistMsgInfo entity = new PersistMsgInfo();
        entity.setMsgId(msg.getUuid());
        Date nowDt = DateTimeUtil.getNowDt();
        entity.setCreateTm(nowDt);
        entity.setAcReg(msgOrig.getAcReg());
        entity.setDownMsgChannel(msgOrig.getChannel());
        entity.setDownMsgContent(msgOrig.getContent());
        entity.setDownMsgType(msgOrig.getMsgType());
        entity.setDownReceiveTm(nowDt);
        entity.setModifyTm(nowDt);
        entity.setMsgStep(MsgStep.REQ_FAIL.getKey());
        entity.setMsgReason(MsgReasonEnum.ROUTE_MSG_DOWNLINK_SUCC.getKey());
        entity.setMsgTm(nowDt);
        return entity;
    }

    /**
     * 获取输入参数、输出参数
     *
     * @param list  服务返回的报文列表
     * @param isAll true获取所有的输出参数，false 返回输入参数
     * @return 参数
     */
    private String genParam(List<MsgBody> list, boolean isAll) {
        if (!isAll) {
            return list.get(0).getInput();
        }

        StringBuilder sb = new StringBuilder(256);
        for (int i = 0, len = list.size(); i < len; i++) {
            MsgBody e = list.get(i);
            String output = e.getOutput();
            if (StringUtil.isEmpty(output)) {
                continue;
            }
            sb.append(output);
            if (i != len - 1) {
                sb.append(';');
            }
        }
        return sb.toString();
    }
}
