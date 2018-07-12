package com.simple.common.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**********************************************************************************
 * Copyright(c)2015 Simple-air.com All rights reserved.
 * @Title: BufferConfigMgr.java
 * @Package com.aerox.common.config
 * @Description: 配置缓存管理：单例
 *
 * @author connor
 * @version 1.0
 * @created 2015年4月1日 下午6:02:30
 **********************************************************************************/
public class BufferConfigMgr {
	private static final Logger logger = Logger.getLogger(BufferConfigMgr.class);
	private ConfigBufferReader reader = new ConfigBufferReader();

	// Singleton:禁止被构造
	private BufferConfigMgr() {
	}
	private static BufferConfigMgr instance;
	public synchronized static BufferConfigMgr getInstance() {
		if (instance == null) {
			instance = new BufferConfigMgr();
		}
		return instance;
	}
	
	public boolean initial(String rootPath,boolean bIncludeSubDir) {
		return initial(rootPath, ".properties",bIncludeSubDir);
	}

	public boolean initial(String rootPath,String exts,boolean bIncludeSubDir) {
		try {
			logger.info("initial:"+rootPath);
			return reader.initial(rootPath, ".properties",true);
		} catch (ConfigurationException e) {
			logger.error("initial error",e);
		}
		return false;
	}

	//common
	public PropertiesConfiguration getConfig(String... names){
		return reader.getConfig(names);
	}
}











