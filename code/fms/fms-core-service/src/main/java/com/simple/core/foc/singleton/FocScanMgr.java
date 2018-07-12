package com.simple.core.foc.singleton;

import com.simple.common.config.BufferConfigMgr;
import com.simple.common.config.ConfigBufferReader;
import com.simple.common.context.SpringContext;
import com.simple.common.utils.DateTimeUtil;
import com.simple.core.foc.service.FocFlightService;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Random;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: FocScanMgr.java.
 * @Package com.simple.core.foc.singleton.
 * @Description: foc扫描管理.
 *
 * @author guowei.
 * @version 1.0.
 * @created 2017/4/6 11:24.
 **********************************************************************************/
public class FocScanMgr {
	private static final Logger logger = Logger.getLogger(FocScanMgr.class);
	private ThreadScan thrScan;
	private int SecondsScanCycle=300;
	private int FocScanRangeStart=3;
	private int FocScanRangeEnd=3;
	private String FocCarrierIATA="";
	private boolean GetFocFligthByUpdateTime=true;
	private boolean GetFocCfpByUpdateTime=true;
	private int UpdateTimeDefault=12;
	private boolean HasDispatchTime=true;
	private boolean HasCrewArrivalTime=true;
	private int FocFligthByUpdateTimeOffset=30;
	private int FocCfpByUpdateTimeOffset=30;

	private FocFlightService focFlightService;

	// Singleton:禁止被构造
	private FocScanMgr() {
		try {
			logger.info("constration start...");
			initialConfig();
			focFlightService = SpringContext.getBean("focFlightService");
			focFlightService.setCarrierIATA(FocCarrierIATA);
			logger.info("constration done.");
		} catch (Exception e) {
			logger.error("constration error",e);
		}
	}
	private static FocScanMgr instance;
	public static FocScanMgr getInstance() {
		if (instance == null) {
			synchronized (FocScanMgr.class) {
				if (instance == null) {
					instance = new FocScanMgr();
				}
			}
		}
		return instance;
	}
	
	private void initialConfig(){
		//读取配置
		logger.info("initial config (service.properties) start...");
		Configuration confMap = BufferConfigMgr.getInstance().getConfig("service");
		if (confMap != null) {
			SecondsScanCycle = ConfigBufferReader.getInt(confMap,"foc.SecondsScanCycle", 10, 24*3600,300,logger);
			FocScanRangeStart = ConfigBufferReader.getInt(confMap,"foc.ScanRange.Start",0,24,3,logger);
			FocScanRangeEnd = ConfigBufferReader.getInt(confMap,"foc.ScanRange.End",0,24,3,logger);
			FocCarrierIATA = ConfigBufferReader.getString(confMap,"foc.CarrierIATA","",logger);
			GetFocFligthByUpdateTime = ConfigBufferReader.getBoolean(confMap,"foc.GetFocFligthByUpdateTime",true,logger);
			GetFocCfpByUpdateTime = ConfigBufferReader.getBoolean(confMap,"foc.GetFocCfpByUpdateTime",true,logger);
			UpdateTimeDefault = ConfigBufferReader.getInt(confMap,"foc.UpdateTimeDefault",0,24,12,logger);
			HasDispatchTime = ConfigBufferReader.getBoolean(confMap,"foc.HasDispatchTime",true,logger);
			HasCrewArrivalTime = ConfigBufferReader.getBoolean(confMap,"foc.HasCrewArrivalTime",true,logger);

			FocFligthByUpdateTimeOffset = ConfigBufferReader.getInt(confMap,"foc.FocFligthByUpdateTimeOffset",0,240,30,logger);
			FocCfpByUpdateTimeOffset= ConfigBufferReader.getInt(confMap,"foc.FocCfpByUpdateTimeOffset",0,240,30,logger);
		}else{
			logger.error("miss config file:service.properties");
		}
		logger.info("initial config done.");
	}

	public void startWork(){
		logger.info("FocScanMgr startWork...");
		if(null==thrScan||!thrScan.isAlive()){
			thrScan=new ThreadScan();
			thrScan.start();
		}
	}
	public void stopWork(){
		logger.info("FocScanMgr stopWork...");
		if(null!=thrScan){
			thrScan.exit();
		}
	}

	/**
	 * 执行扫描flight
	 */
	private void doScanFlight(){
		Date dtNow = new Date();
		Date dtStart = DateTimeUtil.addHours(dtNow,-FocScanRangeStart);
		Date dtEnd = DateTimeUtil.addHours(dtNow,FocScanRangeEnd);
		Date lastUpdateTime = focFlightService.getMaxFlightModifyTimeFromImporter(UpdateTimeDefault);
		//最大时间往前减30分钟
		Date lastUpdateTime2=DateTimeUtil.subMinutes(lastUpdateTime,FocFligthByUpdateTimeOffset);
		if(GetFocFligthByUpdateTime){
			logger.info("doScanFlight by update time:"+DateTimeUtil.toSting(lastUpdateTime2));
			focFlightService.sendFocFlightToImporter(lastUpdateTime2,HasDispatchTime,HasCrewArrivalTime);
		}
		else {
			logger.info("doScanFlight by star/end time:"+DateTimeUtil.toSting(dtStart)
				+", "+DateTimeUtil.toSting(dtEnd));
			focFlightService.sendFocFlightToImporter(dtStart, dtEnd, HasDispatchTime, HasCrewArrivalTime);
		}
	}

	/**
	 * 执行扫描cfp
	 */
	private void doScanCfp(){
		Date dtNow = new Date();
		Date dtStart = DateTimeUtil.addHours(dtNow,-FocScanRangeStart);
		Date dtEnd = DateTimeUtil.addHours(dtNow,FocScanRangeEnd);
 		Date lastUpdateTime = focFlightService.getMaxCfpModifyTimeFromImporter(UpdateTimeDefault);
		//最大时间往前减30分钟
		Date lastUpdateTime2=DateTimeUtil.subMinutes(lastUpdateTime,FocCfpByUpdateTimeOffset);
		if(GetFocCfpByUpdateTime) {
			logger.info("doScanCfp by update time:"+DateTimeUtil.toSting(lastUpdateTime2));
			focFlightService.sendFocCfpToImporter(lastUpdateTime2);
		}
		else {
			logger.info("doScanCfp by star/end time:"+DateTimeUtil.toSting(dtStart)
					+", "+DateTimeUtil.toSting(dtEnd));
			focFlightService.sendFocCfpToImporter(dtStart, dtEnd);
		}
	}

	/**
	 * 同步FOC中删除的航班
	 */
	private void doSyncDeletedFlights(){
		logger.info("doSyncDeletedFlights start...");
		focFlightService.syncDeletedFlights();
	}

	/**********************************************************************************
	 * 功能：扫描FOC航班动态
	 *
	 **********************************************************************************/
	class ThreadScan extends Thread {
		private boolean bExit=false;
		public void exit(){
			bExit = true;
		}
		public void run() {
			logger.info("ThreadScan running...");
			try {
				Random r = new Random();
				Thread.sleep(20000+r.nextInt(10000));
			} catch (InterruptedException e) {
			}

			while(!bExit){
				try {
					doScanFlight();
				} catch (Exception e) {
					logger.error("doScanFlight error", e);
				}

				try {
					doScanCfp();
				} catch (Exception e) {
					logger.error("doScanCfp error", e);
				}

				try {
					doSyncDeletedFlights();
				} catch (Exception e) {
					logger.error("doSyncDeletedFlights error", e);
				}

				try {
					Thread.sleep(SecondsScanCycle*1000);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}











