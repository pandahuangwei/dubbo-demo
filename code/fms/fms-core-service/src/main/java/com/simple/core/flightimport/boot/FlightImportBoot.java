package com.simple.core.flightimport.boot;

import com.simple.core.common.boot.AbstractBoot;
import com.simple.core.flightimport.factory.CfpParserFactory;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: FlightImportBoot.java.
 * @Package com.simple.core.flightimport.boot.
 * @Description: 系统启动入口.
 *
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/22 14:49.
 **********************************************************************************/
public class FlightImportBoot extends AbstractBoot {

    @Override
    protected void onBoot() {
        logger.info("初始化CFP解析工厂...");
        CfpParserFactory.getInstance().initial();
    }
}
