package com.simple.core.flightimport.factory;


import com.simple.common.config.BufferConfigMgr;
import com.simple.common.config.ConfigBufferReader;
import com.simple.core.flightimport.parsecfp.CfpParser;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

/**********************************************************************************
 * Copyright(c)2015 Dcits-air.com All rights reserved.
 * @Title: CfpParserFactory.java
 * @Description: 动态创建发送Adapter工厂
 * 
 * @author connor
 * @version 1.0
 * @created 2017年2月28日 下午6:02:30
 **********************************************************************************/
public class CfpParserFactory extends AbstractBeanFactory{

	private String iataList;
	private String icaoList;

	// Singleton:禁止被构造
	private CfpParserFactory(){}
	private static CfpParserFactory instance;
	public static CfpParserFactory getInstance() {
		if (instance == null) {
			synchronized (CfpParserFactory.class) {
				if (instance == null) {
					instance = new CfpParserFactory();
				}
			}
		}
		return instance;
	}

	/**
	 * 读取配置注册
	 */
	public void initial(){
		//读取配置
		PropertiesConfiguration confMap = BufferConfigMgr.getInstance().getConfig("service");
		if (confMap != null) {
			iataList = ConfigBufferReader.getString(confMap,"Carrier.IATA","");
			icaoList = ConfigBufferReader.getString(confMap,"Carrier.ICAO","");
			String[] iataArr = iataList.split("\\|");
			for(String key : iataArr){
				String value = ConfigBufferReader.getString(confMap,key,"");
				if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) register(key,value);
			}
			String[] icaoArr = icaoList.split("\\|");
			for(String key : icaoArr){
				String value = ConfigBufferReader.getString(confMap,key,"");
				if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) register(key,value);
			}
		}
	}

	/**
	 * 航空公司代码是否配置
	 * @param carrierCode
	 * @return
     */
	public boolean isCarrierDefined(String carrierCode){
		return (null!=iataList && iataList.contains(carrierCode))
				||(null!=icaoList && icaoList.contains(carrierCode));
	}

	/**
	 * 注册
	 * @param carrierCode,航空公司代码(2或3字码)
	 * @param handlerBeanName
     * @return
     */
	public static boolean register(String carrierCode,String handlerBeanName){
		return CfpParserFactory.getInstance()
				.registerHandler(carrierCode, handlerBeanName);
	}

	/**
	 * 依据航空公司代码(2或3字码)返回对应解析类实例bean
	 * @param carrierCode
	 * @return
     */
	public static CfpParser create(String carrierCode) {
		return (CfpParser) CfpParserFactory.getInstance()
				.createHandler(carrierCode);
	}

}
