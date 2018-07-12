package com.simple.core.charging.util;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.05 18:24.
 */
public class ChargingPropertyUtil implements InitializingBean {

    private static ChargingPropertyUtil instance = new ChargingPropertyUtil();

    /**
     * 访问远程授权信息是否打开
     */
    private boolean authGetTurnOn = false;
    /**
     * 获取授权的公司二字码
     */
    private String authCarrierIata;
    /**
     * 获取授权信息间隔时间
     */
    private int authReGetMin = 120;

    /**
     * 远程计费系统ip及端口
     */
    private String chargingIpPort;

    /**
     * 接口数据是否加密
     */
    private boolean facadeEncrypt = false;

    private String authSyncMethod;

    /**
     * 同步报文大小是否打开
     */
    private boolean msgSizeReSyncTurnOn = false;

    /**
     * 报文大小同步时间
     */
    private int msgSizeReSyncMin = 120;
    /**
     * 报文大小同步状态方法名
     */
    private String msgSizeSyncStatusMethod;
    /**
     * 报文大小同步方法
     */
    private String msgSizeSyncMethod;

    /**
     * 获取实例
     *
     * @return
     */
    public static ChargingPropertyUtil getInstance() {
        return instance;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }

    public boolean isAuthGetTurnOn() {
        return authGetTurnOn;
    }

    public void setAuthGetTurnOn(boolean authGetTurnOn) {
        this.authGetTurnOn = authGetTurnOn;
    }

    public String getAuthCarrierIata() {
        return authCarrierIata;
    }

    public void setAuthCarrierIata(String authCarrierIata) {
        this.authCarrierIata = authCarrierIata;
    }

    public int getAuthReGetMin() {
        return authReGetMin;
    }

    public void setAuthReGetMin(int authReGetMin) {
        this.authReGetMin = authReGetMin;
    }

    public String getChargingIpPort() {
        return chargingIpPort;
    }

    public void setChargingIpPort(String chargingIpPort) {
        this.chargingIpPort = chargingIpPort;
    }

    public boolean isFacadeEncrypt() {
        return facadeEncrypt;
    }

    public void setFacadeEncrypt(boolean facadeEncrypt) {
        this.facadeEncrypt = facadeEncrypt;
    }

    public String getAuthSyncMethod() {
        return authSyncMethod;
    }

    public void setAuthSyncMethod(String authSyncMethod) {
        this.authSyncMethod = authSyncMethod;
    }

    public String getMsgSizeSyncStatusMethod() {
        return msgSizeSyncStatusMethod;
    }

    public void setMsgSizeSyncStatusMethod(String msgSizeSyncStatusMethod) {
        this.msgSizeSyncStatusMethod = msgSizeSyncStatusMethod;
    }

    public String getMsgSizeSyncMethod() {
        return msgSizeSyncMethod;
    }

    public void setMsgSizeSyncMethod(String msgSizeSyncMethod) {
        this.msgSizeSyncMethod = msgSizeSyncMethod;
    }

    public boolean isMsgSizeReSyncTurnOn() {
        return msgSizeReSyncTurnOn;
    }

    public void setMsgSizeReSyncTurnOn(boolean msgSizeReSyncTurnOn) {
        this.msgSizeReSyncTurnOn = msgSizeReSyncTurnOn;
    }

    public int getMsgSizeReSyncMin() {
        return msgSizeReSyncMin;
    }

    public void setMsgSizeReSyncMin(int msgSizeReSyncMin) {
        this.msgSizeReSyncMin = msgSizeReSyncMin;
    }
}