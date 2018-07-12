package com.simple.core.charging.job;

import com.simple.common.utils.StringUtil;
import com.simple.core.charging.service.AuthService;
import com.simple.core.charging.service.MsgSyncService;
import com.simple.core.charging.util.ChargingPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 同步数据定时任务入口
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.06 8:59.
 */
public class SyncDataApplication implements BeanFactoryAware {
    private static Logger logger = LoggerFactory.getLogger(SyncDataApplication.class);

    private BeanFactory beanFactory;
    private static long authSyncCount;
    private static long msgSizeSyncCount;

    /**
     * 同步权限数据
     */
    public void syncAuth() {
        if (!ChargingPropertyUtil.getInstance().isAuthGetTurnOn()) {
            logger.info("The syncAuth authGetTurnOn is false,task not to run...");
            return;
        }
        long startTm = System.nanoTime();
        authSyncCount++;
        logger.info("The syncAuth which was called by timer for {},time started...", authSyncCount);
        ((AuthService) beanFactory.getBean("authService")).saveAuthFromMcp();
        logger.info("The syncAuth which was called by timer for {},time was ended,{} ", authSyncCount, StringUtil.getSpentTm(startTm));
    }

    /**
     * 同步报文大小数据
     */
    public void syncMsgSize() {
        if (!ChargingPropertyUtil.getInstance().isMsgSizeReSyncTurnOn()) {
            logger.info("The syncMsgSize msgSizeReSyncTurnOn is false,task not to run...");
            return;
        }
        long startTm = System.nanoTime();
        msgSizeSyncCount++;
        logger.info("The syncMsgSize which was called by timer for {},time started...", msgSizeSyncCount);
        ((MsgSyncService) beanFactory.getBean("msgSyncService")).syncMsgSize();
        logger.info("The syncMsgSize which was called by timer for {},time was ended,{} ", msgSizeSyncCount, StringUtil.getSpentTm(startTm));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
