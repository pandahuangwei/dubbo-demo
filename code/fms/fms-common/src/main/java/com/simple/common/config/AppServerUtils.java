package com.simple.common.config;

import org.springframework.util.StringUtils;

/**********************************************************************************
 * Copyright(c)2015 Simple-air.com All rights reserved.
 * @Title: AppServerUtils.java 
 * @Description: 应用服务器工具类
 * 
 * @author connor
 * @version 1.0
 * @created 2015年4月1日 下午6:02:30
 **********************************************************************************/
public class AppServerUtils {
	public static String confDir = null;

	public static boolean isOracleAppServer() {
		return (!(StringUtils.isEmpty(System.getProperty("oracle.j2ee.home"))));
	}

	public static boolean isWeblogicAppServer() {
		return (!(StringUtils.isEmpty(System.getProperty("weblogic.Name"))));
	}

	public static boolean isTomcatServer() {
		return (!(StringUtils.isEmpty(System.getProperty("catalina.home"))));
	}

	public static boolean isJbossAppServer() {
		return (!(StringUtils.isEmpty(System.getProperty("jboss.server.name"))));
	}

	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	/**
	 * 应用服务器类型
	 * @return
	 */
	public static String getAppServerType() {
		if (isOracleAppServer())
			return "oracle";
		if (isJbossAppServer()) {
			return "jboss";
		}
		if (isTomcatServer())
			return "tomcat";
		if (isWeblogicAppServer()) {
			return "weblogic";
		}
		return "null";
	}

	/**
	 * 应用服务器路径 
	 * @return
	 */
	public static String getAppServerDir() {
		if (!StringUtils.isEmpty(confDir)) {
			return confDir;
		} else if (isOracleAppServer()) {
			confDir = System.getProperty("oracle.j2ee.home");
		} else if (isJbossAppServer()) {
			confDir = System.getProperty("jboss.home.dir");
		} else if (isTomcatServer()) {
			confDir = System.getProperty("catalina.home");
		} else {
			//confDir = System.getProperty("user.dir");
			//路径问题后续改进
			confDir = ClassLoader.getSystemResource("service.properties").getPath();
		}
		return confDir;
	}
	
	/**
	 * 应用服务器路径  bin/config 
	 * @return
	 */
	public static String getAppServerConfigDir() {
		String path = getAppServerDir();
		if(path.endsWith("bin")) return path+"/config";
		else return path+"/bin/config";
	}
}