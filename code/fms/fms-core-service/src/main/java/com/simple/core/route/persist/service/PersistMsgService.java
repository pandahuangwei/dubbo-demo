package com.simple.core.route.persist.service;

import com.simple.core.route.persist.entity.PersistMsgInfo;
import com.simple.core.common.entity.NoticeMsg;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.01.18 14:56.
 */
public interface PersistMsgService {

    /**
     * 1持久化报文下行通知
     *
     * @param message 持久化报文体
     */
    int saveDownLinkMsgNotice(PersistMsgInfo message) throws Exception;

    /**
     * 3报文上行通知接口
     */
    int saveUpLinkMsgStateNotice(NoticeMsg message) throws Exception;


    /**
     * 5.报文下行异常通知(1.报文超时；2.接口异常（对应1）)
     */
    int saveDownLinkMsgNoticeExc(NoticeMsg message) throws Exception;

    /**
     * 6.报文上行状态异常通知
     */
    int saveUpLinkMsgStateNoticeExc(NoticeMsg message) throws Exception;


    /**
     * 4.飞机拒绝报文下行通知
     */
    int saveRejectMsgNotice(NoticeMsg message) throws Exception;

    /**
     * 7.飞机拒绝报文下行异常通知（对应4的通知）
     */
    int saveRejectMsgNoticeExc(NoticeMsg message) throws Exception;

    int answerMsgNotice(NoticeMsg message) throws Exception;
}