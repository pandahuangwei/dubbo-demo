package com.simple.core.route.persist.service.impl;

import com.simple.common.utils.DateTimeUtil;
import com.simple.common.utils.StringUtil;
import com.simple.common.utils.UuidUtil;
import com.simple.core.common.entity.MQMsgEntity;
import com.simple.core.common.entity.NoticeMsg;
import com.simple.core.common.enums.MessageEnum.MessageType;
import com.simple.core.common.enums.MessageEnum.MsgGroup;
import com.simple.core.common.enums.MessageEnum.MsgReasonEnum;
import com.simple.core.common.enums.MessageEnum.MsgStep;
import com.simple.core.route.persist.dao.PersistDao;
import com.simple.core.route.persist.entity.PersistMsgInfo;
import com.simple.core.route.persist.entity.PersistMsgTrail;
import com.simple.core.route.persist.service.PersistMsgService;
import com.simple.core.route.util.RouteMsgAMQUtil;
import com.simple.core.route.util.RoutePropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 通知消息持久化
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.01.18 14:57.
 */
@Service("persistMsgService")
public class PersistMsgServiceImpl implements PersistMsgService {
    private static Logger logger = LoggerFactory.getLogger(PersistMsgServiceImpl.class);

    @Resource(name = "persistDao")
    private PersistDao persistDao;

    /**
     * 1持久化解析报文.
     * 1 保存报文主信息.<br>
     * 2 保存轨迹信息.<br>
     * 3 更新航班状态.
     * msgStep:SEND
     *
     * @param message 持久化报文体
     */
    @Override
    public int saveDownLinkMsgNotice(PersistMsgInfo message) throws Exception {
        logger.debug("PersistMsgInfo entity: {}", message.toString());
        PersistMsgTrail trail = buildMsgTrail(message);
        trail.setMsgGroup(MsgGroup.REQ.getKey());

        persistDao.insertMsg(message);
        persistDao.insertMsgTrail(trail);
        return updateFlightMsgState(message);
    }


    /**
     * 3持久化通知报文.<br>
     * 1 根据报文id获取报文实体.<br>
     * 2 根据不同的报文类型更新不同的报文实体状态.<br>
     * 3 将通知报文写入该报文的轨迹表.
     * msgStep:AK
     *
     * @param message 通知报文
     */
    @Override
    public int saveUpLinkMsgStateNotice(NoticeMsg message) throws Exception {
        return saveNormalMsg(message, MsgGroup.SEND.getKey());
    }

    /**
     * 4 飞机拒绝报文下行通知
     */
    @Override
    public int saveRejectMsgNotice(NoticeMsg message) throws Exception {
        return saveNormalMsg(message, MsgGroup.RJ.getKey());
    }

    /**
     * 6.报文上行状态异常通知
     */
    @Override
    public int saveUpLinkMsgStateNoticeExc(NoticeMsg message) throws Exception {
        if (message == null) {
            logger.debug("== The NoticeMsg Msg isn't Error:,abandon it.");
            return 0;
        }
        PersistMsgInfo msgInfo = getFromCacheOrDB(message.getUuid());
        if (msgInfo == null || StringUtil.isEmpty(msgInfo.getMsgId())) {
            logger.debug("== The UUID:{} ,can not find info:,abandon it.");
            return 0;
        }

        setMsgInfo(msgInfo, message);
        PersistMsgTrail msgTrail = buildMsgTrail(message, msgInfo.getMsgId());
        msgInfo.setUpMsgContent(message.getContent());
        msgInfo.setUpMsgChannel(message.getChannel());
        msgInfo.setUpMsgType(message.getMsgType());
        msgInfo.setUpSendTm(DateTimeUtil.getNowDt());
        msgTrail.setMsgGroup(MsgGroup.SEND.getKey());

        return updateHeadAndInsertDtl(msgInfo, msgTrail);
    }

    /**
     * 7.飞机拒绝报文下行异常通知（对应4的通知）
     */
    @Override
    public int saveRejectMsgNoticeExc(NoticeMsg message) throws Exception {
        if (message == null) {
            logger.debug("== The NoticeMsg Msg isn't Error:,abandon it.");
            return 0;
        }
        PersistMsgInfo msgInfo = getFromCacheOrDB(message.getUuid());
        if (msgInfo == null || StringUtil.isEmpty(msgInfo.getMsgId())) {
            logger.debug("== The UUID:{} ,can not find info:,abandon it.");
            return 0;
        }

        message.setErrReason(MsgReasonEnum.MSG_UPLINK_FMS_REJECT.getKey());
        setMsgInfo(msgInfo, message);
        PersistMsgTrail msgTrail = buildMsgTrail(message, msgInfo.getMsgId());
        msgInfo.setDownMsgContent(message.getContent());
        msgInfo.setDownMsgChannel(message.getChannel());
        msgInfo.setDownMsgType(message.getMsgType());
        msgInfo.setDownReceiveTm(DateTimeUtil.getNowDt());
        msgTrail.setMsgGroup(MsgGroup.RJ.getKey());

        return updateHeadAndInsertDtl(msgInfo, msgTrail);
    }

    /**
     * 5 报文下行异常通知
     */
    @Override
    public int saveDownLinkMsgNoticeExc(NoticeMsg message) throws Exception {
        if (message == null) {
            logger.debug("== The NoticeMsg Msg isn't Error:,abandon it.");
            return 0;
        }
        PersistMsgInfo msgInfo = getFromCacheOrDB(message.getUuid());
        if (msgInfo == null || StringUtil.isEmpty(msgInfo.getMsgId())) {
            logger.debug("== The UUID:{} ,can not find info:,abandon it.");
            return 0;
        }

        setMsgInfo(msgInfo, message);
        PersistMsgTrail msgTrail = buildMsgTrail(message, msgInfo.getMsgId());
        msgInfo.setDownMsgContent(message.getContent());
        msgInfo.setDownMsgChannel(message.getChannel());
        msgInfo.setDownMsgType(message.getMsgType());
        msgInfo.setDownReceiveTm(DateTimeUtil.getNowDt());
        msgTrail.setMsgGroup(MsgStep.SEND.getKey());

        return updateHeadAndInsertDtl(msgInfo, msgTrail);
    }

    @Override
    public int answerMsgNotice(NoticeMsg message) throws Exception {
        if (message == null) {
            logger.debug("== The NoticeMsg Msg isn't Error:,abandon it.");
            return 0;
        }
        PersistMsgInfo msgInfo = getFromCacheOrDB(message.getUuid());
        if (msgInfo == null || StringUtil.isEmpty(msgInfo.getMsgId())) {
            logger.debug("== The UUID:{} ,can not find info:,abandon it.");
            return 0;
        }

        //直接从数据库取.TODO 加缓存
        PersistMsgTrail msgTrail = buildMsgTrail(message, msgInfo.getMsgId());
        msgInfo.setMsgTm(new Date());
        msgTrail.setMsgGroup(message.getMsgStep());
        //构建报文原因
        if (MsgStep.isNeedBuildReason(message.getMsgStep())) {
            msgTrail.setMsgReason(MsgReasonEnum.getReason(message.getMsgStep()));
        }

        updateHeadAndInsertDtl(msgInfo, msgTrail);

        msgInfo.setMsgStep(message.getMsgStep());
        msgInfo.setMsgType(message.getMsgType());

        //计算报文类型用于更新航班中的报文状态:如果是能确认的类型，则直接去，否则需要去数据库获取
        String msgType = StringUtil.getMsgType(msgInfo.getMsgType());
        if (MessageType.isConfirmType(msgType)) {
            msgInfo.setMsgType(MessageType.getMapMsgType(msgInfo.getMsgType()));
        } else {
            msgInfo.setMsgType(msgInfo.getDownMsgType());
        }

        msgInfo.setMsgType(msgType);
        return updateFlightMsgState(msgInfo);
    }

    /**
     * 正常报文通知.
     * 1 根据报文id获取报文实体.<br>
     * 2 根据不同的报文类型更新不同的报文实体状态.<br>
     * 3 将通知报文写入该报文的轨迹表.
     * 4 更新航班状态
     *
     * @param message 通知报文
     * @return int
     */
    private int saveNormalMsg(NoticeMsg message, int msgGroup) {
        if (message == null) {
            logger.debug("== The NoticeMsg Msg isn't Error:,abandon it.");
            return 0;
        }
        PersistMsgInfo msgInfo = getFromCacheOrDB(message.getUuid());
        if (msgInfo == null || StringUtil.isEmpty(msgInfo.getMsgId())) {
            logger.debug("== The UUID:{} ,can not find info:,abandon it.");
            return 0;
        }

        msgInfo.setModifyTm(DateTimeUtil.getNowDt());
        msgInfo.setMsgStep(message.getMsgStep());
        msgInfo.setMsgType(message.getMsgType());
        msgInfo.setMsgTm(message.getMsgTm());

        PersistMsgTrail trail = buildMsgTrail(message, msgInfo.getMsgId());
        trail.setMsgGroup(msgGroup);

        persistDao.updateMsgState(msgInfo);
        persistDao.insertMsgTrail(trail);

        saveMsgSize(msgInfo);
        return updateFlightMsgState(msgInfo);
    }

    /**
     * 预留方法。数据量大加缓存，然后从缓存中获取.
     * TODO
     *
     * @param uuid uuid
     * @return 持久化信息实体
     */
    private PersistMsgInfo getFromCacheOrDB(String uuid) {
        if (StringUtil.isEmpty(uuid)) {
            logger.debug("== The NoticeMsg Msg isn't Error:,abandon it.");
            return null;
        }

        PersistMsgInfo msgInfo = persistDao.getMsgInfo(uuid);
        if (msgInfo == null || StringUtil.isEmpty(msgInfo.getMsgId())) {
            logger.debug("== Use Uuid can't get Message,The Msg isn't allow:{},abandon it.", uuid);
            return null;
        }
        logger.info("== Get the PersistMsgInfo is :{}", msgInfo.toString());
        return msgInfo;
    }

    private int updateHeadAndInsertDtl(PersistMsgInfo msgInfo, PersistMsgTrail msgTrail) {
        persistDao.updateMsgState(msgInfo);
        return persistDao.insertMsgTrail(msgTrail);
    }

    private int saveMsgSize(PersistMsgInfo msgInfo) {
        if (!MsgStep.SEND.eq(msgInfo.getMsgStep())) {
            return -1;
        }
        return persistDao.insertMsgSize(msgInfo);
    }

    private int updateFlightMsgState(PersistMsgInfo message) {
        if (message == null || StringUtil.isEmpty(message.getFlightId())) {
            return 0;
        }
        if (message.getMsgTm() == null) {
            message.setMsgTm(new Date());
        }
        if (StringUtil.isEmpty(message.getMsgType())) {
            message.setMsgType(message.getDownMsgType());
        }

        write2Mq(message);

        return persistDao.updateFlightMsgState(message);
    }

    /**
     * 将报文状态字段写入Mq
     *
     * @param message 报文信息
     */
    private void write2Mq(PersistMsgInfo message) {
        if (!RoutePropertyUtil.getInstance().isMsgStateToMqTurnOn()) {
            logger.info("The Write to mq param:msgStateToMqTurnOn = false,do not write to mq...... ");
            return;
        }

        try {
            MQMsgEntity entity = createMQEntity(message);
            logger.info("Write to mq...:{} ", entity.toString());
            RouteMsgAMQUtil.getInstance().sendObject(entity);
        } catch (Exception e) {
            logger.info("The Write to mq is Err , please check it...... ", e);
        }
    }

    private MQMsgEntity createMQEntity(PersistMsgInfo message) {
        MQMsgEntity entity = new MQMsgEntity();
        entity.setAcReg(message.getAcReg());
        entity.setFlightId(message.getFlightId());
        entity.setFlightDt(message.getFlightDt());
        entity.setFlightNo(message.getFlightNo());
        entity.setDeptAirport(message.getDeptAirport());
        entity.setArriveAirport(message.getArriveAirport());
        entity.setMsgType(message.getMsgType());
        entity.setMsgStep(message.getMsgStep());
        return entity;
    }

    private void setMsgInfo(PersistMsgInfo msgInfo, NoticeMsg message) {
        msgInfo.setMsgId(message.getUuid());
        msgInfo.setModifyTm(DateTimeUtil.getNowDt());
        msgInfo.setMsgStep(message.getMsgStep());
        msgInfo.setMsgType(message.getMsgType());
        msgInfo.setMsgTm(message.getMsgTm());
        msgInfo.setAcReg(message.getAcReg());
    }

    /**
     * 构建报文轨迹实体
     *
     * @param message 通知报文
     * @param msgId   报文uuid
     * @return 轨迹实体
     */
    private PersistMsgTrail buildMsgTrail(NoticeMsg message, String msgId) {
        PersistMsgTrail entity = new PersistMsgTrail();
        entity.setTrailId(UuidUtil.get32UUID());
        entity.setMsgId(msgId);
        Date nowDt = DateTimeUtil.getNowDt();
        entity.setCreateTm(nowDt);
        entity.setModifyTm(nowDt);
        entity.setMsgType(message.getMsgType());
        entity.setMsgState(message.getMsgStep());
        entity.setMsgReason(message.getErrReason());
        entity.setMsgChannel(message.getChannel());
        entity.setReplyContent(message.getContent());

        entity.setMsgGroup(getMsgGroup(message.getMsgStep()));
        return entity;
    }

    private int getMsgGroup(int msgStep) {
        if (MsgStep.REQ.eq(msgStep) || MsgStep.REQ_FAIL.eq(msgStep)) {
            return MsgGroup.REQ.getKey();
        }

        if (MsgStep.SEND.eq(msgStep) || MsgStep.SEND_FAIL.eq(msgStep)) {
            return MsgGroup.SEND.getKey();
        }
        if (MsgStep.AK.eq(msgStep)) {
            return MsgGroup.AK.getKey();
        }

        if (MsgStep.AC.eq(msgStep)) {
            return MsgGroup.AC.getKey();
        }

        return MsgGroup.RJ.getKey();
    }

    /**
     * 构建报文轨迹实体
     *
     * @param message 报文体
     * @return 轨迹实体
     */
    private PersistMsgTrail buildMsgTrail(PersistMsgInfo message) {
        PersistMsgTrail entity = new PersistMsgTrail();
        entity.setTrailId(UuidUtil.get32UUID());
        entity.setMsgId(message.getMsgId());
        Date nowDt = DateTimeUtil.getNowDt();
        entity.setCreateTm(nowDt);
        entity.setModifyTm(nowDt);
        entity.setMsgType(message.getMsgType());
        entity.setMsgState(message.getMsgStep());
        entity.setMsgReason(message.getMsgReason());
        entity.setMsgChannel(message.getUpMsgChannel());
        entity.setReplyContent(message.getDownMsgContent());

        return entity;
    }

}
