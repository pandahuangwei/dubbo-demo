package com.simple.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: SpringContext.java.
 * @Package com.simple.common.context.
 * @Description: 通过Spring获取类示例.
 * 
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/1 16:36.
 **********************************************************************************/
@Service
public class SpringContext implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContext.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static <T> T getBean(String name) throws BeansException {
		return (T) applicationContext.getBean(name);
	}
}