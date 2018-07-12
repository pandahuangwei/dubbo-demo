package com.simple.core.flightimport.factory;

import com.simple.common.context.SpringContext;
import java.util.HashMap;
import java.util.Map;

/**********************************************************************************
 * Copyright(c)2015 Dcits-air.com All rights reserved.
 * @Title: AbstractBeanFactory.java 
 * @Package com.dcits.message.factory 
 * @Description: 动态创建消息组装Handler工厂
 * 
 * @author connor
 * @version 1.0
 * @created 2015年4月1日 下午6:02:30
 **********************************************************************************/
public abstract class AbstractBeanFactory {
	private Map<String,String> mapHandler = new HashMap<String,String>();

	/**
	 * 注册Handler
	 * @param name
	 * @param handlerBeanName
     * @return
     */
	public boolean registerHandler(String name,String handlerBeanName){
		if( !mapHandler.containsKey(name) ){
			mapHandler.put(name, handlerBeanName);
			return true;
		}
		return false;
	}
	
	/**
	 * 依据注册name返回对应Handler实例bean
	 *         
	 * @param name
	 * @return
	 */
	public Object createHandler(String name) {
		if(null==name) return null;
		String className = mapHandler.get(name);
		if(null==className) return null;
		return SpringContext.getBean(className);
	}


}
