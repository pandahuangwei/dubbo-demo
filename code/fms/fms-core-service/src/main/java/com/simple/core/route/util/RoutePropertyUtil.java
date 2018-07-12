package com.simple.core.route.util;

import org.springframework.beans.factory.InitializingBean;

/**
 * 对应route节点下的service.properties，key相等将值读入.
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.30 17:29.
 */
public class RoutePropertyUtil implements InitializingBean {
    private static RoutePropertyUtil instance = new RoutePropertyUtil();

    /**
     * 报文是否加密
     */
    private boolean msgEncrypt = false;
    /**
     * 报文信息是否写入Mq
     */
    private boolean msgStateToMqTurnOn = false;
    /**
     * 访问activeMq的URL地址
     */
    private String msgStateMqURL;
    /**
     * 访问mq的用户名
     */
    private String mqUserName;
    /**
     * 访问mq的密码
     */
    private String mqPassword;
    /**
     * mq的队列
     */
    private String mqQueueName;

    public static RoutePropertyUtil getInstance() {
        return instance;
    }

    public boolean isMsgEncrypt() {
        return msgEncrypt;
    }

    public void setMsgEncrypt(boolean msgEncrypt) {
        this.msgEncrypt = msgEncrypt;
    }

    public boolean isMsgStateToMqTurnOn() {
        return msgStateToMqTurnOn;
    }

    public void setMsgStateToMqTurnOn(boolean msgStateToMqTurnOn) {
        this.msgStateToMqTurnOn = msgStateToMqTurnOn;
    }

    public String getMsgStateMqURL() {
        return msgStateMqURL;
    }

    public void setMsgStateMqURL(String msgStateMqURL) {
        this.msgStateMqURL = msgStateMqURL;
    }

    public String getMqUserName() {
        return mqUserName;
    }

    public void setMqUserName(String mqUserName) {
        this.mqUserName = mqUserName;
    }

    public String getMqPassword() {
        return mqPassword;
    }

    public void setMqPassword(String mqPassword) {
        this.mqPassword = mqPassword;
    }

    public String getMqQueueName() {
        return mqQueueName;
    }

    public void setMqQueueName(String mqQueueName) {
        this.mqQueueName = mqQueueName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }
}
