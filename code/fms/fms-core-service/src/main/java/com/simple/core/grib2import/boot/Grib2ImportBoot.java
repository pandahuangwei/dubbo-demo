package com.simple.core.grib2import.boot;

import com.simple.core.common.boot.AbstractBoot;
import com.simple.core.grib2import.task.Grib2ScanMgr;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: Grib2ImportBoot.java.
 * @Package com.simple.core.grib2import.boot.
 * @Description: 系统启动入口.
 * 
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/22 14:53.
 **********************************************************************************/
public class Grib2ImportBoot extends AbstractBoot {

    @Override
    protected void onBoot() {
        logger.info("启动扫描线程...");
        Grib2ScanMgr.getInstance().startWork();
    }

}
