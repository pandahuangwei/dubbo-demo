package com.simple.core.foc.boot;

import com.simple.core.common.boot.AbstractBoot;
import com.simple.core.foc.singleton.FocScanMgr;



/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: FocBoot.java.
 * @Package com.simple.core.foc.boot.
 * @Description: 系统启动入口.
 *
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/22 14:49.
 **********************************************************************************/
public class FocBoot extends AbstractBoot {

    @Override
    protected void onBoot() {
        logger.info("启动扫描线程...");
        FocScanMgr.getInstance().startWork();
    }
}
