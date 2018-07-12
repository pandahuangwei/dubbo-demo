package com.simple.core.grib2import.task;

import com.simple.common.config.BufferConfigMgr;
import com.simple.common.config.ConfigBufferReader;
import com.simple.common.context.SpringContext;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.Random;

/**********************************************************************************
 * Copyright(c)2015 Dcits-air.com All rights reserved.
 * @Title: MsgTemplateBufferMgr.java 
 * @Package com.dcits.message.singleton
 * @Description: grib2扫描管理：
 * 
 * @author connor
 * @version 1.0
 * @created 2015年4月1日 下午6:02:30
 **********************************************************************************/
public class Grib2ScanMgr {
	private static final Logger logger = Logger.getLogger(Grib2ScanMgr.class);
	private ThreadScan thrScan;
	private int SecondsScanCycle=30;

	private Grid2BySkyViewDataTask grid2BySkyViewDataTask;

	// Singleton:禁止被构造
	private Grib2ScanMgr() {
		try {
			logger.info("constration start...");
			initialConfig();
			grid2BySkyViewDataTask = SpringContext.getBean("grid2BySkyViewDataTask");
			logger.info("constration done.");
		} catch (Exception e) {
			logger.error("constration error",e);
		}
	}
	private static Grib2ScanMgr instance;
	public static Grib2ScanMgr getInstance() {
		if (instance == null) {
			synchronized (Grib2ScanMgr.class) {
				if (instance == null) {
					instance = new Grib2ScanMgr();
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
			SecondsScanCycle = ConfigBufferReader.getInt(confMap,"grib2.SecondsScanCycle", 10, 24*3600,300,logger);
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
	 * 执行扫描
	 */
	private void doScanGrib2(){
		if(grid2BySkyViewDataTask.isFinishWork()) {
			grid2BySkyViewDataTask.calcAndSave();
		}
	}

	/**********************************************************************************
	 * 功能：扫描
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
					doScanGrib2();
				} catch (Exception e) {
					logger.error("doScanGrib2 error", e);
				}

				try {
					Thread.sleep(SecondsScanCycle*1000);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}











