package com.simple.core.route.service;

import com.simple.core.common.entity.Message;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.22 11:21.
 */
public interface RouteMsgService {

    /**
     * 1.报文下行通知
     *
     * @param message 报文
     * @return 报文
     */
    Message downLinkMsgNotice(String message) throws Exception;

    /**
     * 5.报文下行异常通知(1.报文超时；2.接口异常（对应1）)
     *
     * @param message 报文
     * @return 报文
     */
    Message downLinkMsgNoticeExc(String message) throws Exception;

    /**
     * 3.报文上行状态通知接口
     *
     * @param message 报文
     * @return 报文
     */
    Message upLinkMsgStateNotice(String message) throws Exception;

    /**
     * 6.报文上行状态异常通知
     *
     * @param message 报文
     * @return 报文
     */
    Message upLinkMsgStateNoticeExc(String message) throws Exception;


    /**
     * 4.飞机拒绝报文下行通知
     *
     * @param message 报文
     * @return 报文
     */
    Message rejectMsgNotice(String message) throws Exception;

    /**
     * 7.飞机拒绝报文下行异常通知（对应4的通知）
     *
     * @param message 报文
     * @return 报文
     */
    Message rejectMsgNoticeExc(String message) throws Exception;


    /**
     * 第三方主动发起的上行报文
     *
     * @param message 报文
     * @return 报文
     */
    Message upLinkMsgInitiative(String message) throws Exception;

    /**
     * 飞机应答通知接口
     *
     * @param message 报文
     * @return 报文
     */
    Message answerMsgNotice(String message) throws Exception;
}
