package com.simple.common.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**********************************************************************************
 * Copyright(c)2015 Simple-air.com All rights reserved.
 * @Title: ConfigBufferMgr.java 
 * @Package com.dcits.message.util 
 * @Description: 读取配置到缓存<br>
 *  文件相对路径(不含后缀名)-Configuration;
 *  如：test.123 = Configuration
 *  exts:.cfg|xx.xx|
 *  <br>
 *  如果需要自动刷新缓存，请在配置文件中添加属性 <br>
 *      AutoReload=true<br>
 *      AutoReloadDelayInSeconds=60<br>
 *      
 * @author connor
 * @version 1.0
 * @created 2015年4月1日 下午6:02:30
 **********************************************************************************/
public class ConfigBufferReader{
	private Map<String,PropertiesConfiguration> mapConfs = new HashMap<String,PropertiesConfiguration>(); 
	private String rootPath;
	private String fileExts;//默认“.properties”

	public String getRootPath() {
		return rootPath;
	}
	public String getFileExts() {
		return fileExts;
	}

	/***
	 * 初始化配置缓存
	 * @param rootPath 根路径
	 * @param exts 包含后缀名，如 .cfg|.properties|.tmpl
	 * @param bIncludeSubDir,是否扫描子目录
	 * @throws ConfigurationException 
	 */
	public boolean initial(String rootPath,String exts,boolean bIncludeSubDir) throws ConfigurationException {
		this.rootPath=rootPath;
		if(null==exts||exts.isEmpty())
			exts = ".properties";
		this.fileExts=exts;

		Map<String,PropertiesConfiguration> mapConfsNew = new HashMap<String,PropertiesConfiguration>();
		File dir = new File(rootPath);
		if(dir.isFile()){
			String filename = dir.getName().toLowerCase();
			String[] temps = filename.split("\\.");
			if(temps.length>=2){
				String ext = "."+temps[temps.length-1];
				String name = filename.replace(ext, "");
				if(exts.contains(ext)){
					PropertiesConfiguration propConfig = ConfigBufferReader.readConfigFile(rootPath);
					mapConfsNew.put(name, propConfig);
					mapConfs = mapConfsNew;
					return !mapConfs.isEmpty();
				}
			}
			return false;
		}

		Map<String,String> fileMap = ConfigBufferReader.getDirFileMap(rootPath,exts,bIncludeSubDir);
		for(String name:fileMap.keySet()){		
			PropertiesConfiguration propConfig = ConfigBufferReader.readConfigFile(fileMap.get(name));
			mapConfsNew.put(name, propConfig);
			
			//自动重新加载 :默认1min
			boolean bAutoReload = false;
			try {
				bAutoReload = propConfig.getBoolean("AutoReload");
			} catch (Exception e) {
				bAutoReload = false;
			}
			if(bAutoReload){
				int nAutoReloadDelay = 0;
				try {
					nAutoReloadDelay = 1000*propConfig.getInt("AutoReloadDelayInSeconds");
				} catch (Exception e) {
					nAutoReloadDelay = 60000;
				}
				// [5000~无穷大]s
				if(nAutoReloadDelay<5000) nAutoReloadDelay=5000;
				FileChangedReloadingStrategy change= new FileChangedReloadingStrategy();
				change.setRefreshDelay(nAutoReloadDelay);
				propConfig.setReloadingStrategy(change);
			}
		}
		mapConfs = mapConfsNew;
		return !mapConfs.isEmpty();
	}
	
	public static PropertiesConfiguration readConfigFile(String filePath) throws ConfigurationException{
		PropertiesConfiguration propConfig = new PropertiesConfiguration();
		propConfig.setEncoding("utf8");  
		propConfig.load(filePath); 
		return propConfig;
	}

	/***
	 * 通过names获取对应的配置缓存
	 * @param names，目录.文件名（不含后缀名）
	 */
	public PropertiesConfiguration getConfig(String... names){
		String newName = "";
		int count=0;
		for(String name:names){
			newName +=name;
			count++;
			if(names.length>1 && count!=names.length) newName +=".";
		}
		
		return mapConfs.get(newName.toLowerCase());
	}

	public static String getString(PropertiesConfiguration config,String key,String error){
		try {
			return config.getString(key);
		} catch (Exception e) {
		}
		return error;
	}
	public static boolean getBoolean(PropertiesConfiguration config,String key,boolean error){
		try {
			return config.getBoolean(key);
		} catch (Exception e) {					
		}	
		return error;
	}
	public static int getInt(PropertiesConfiguration config,String key,int error){
		try {
			return config.getInt(key);
		} catch (Exception e) {					
		}	
		return error;
	}
	public static double getDouble(PropertiesConfiguration config,String key,double error){
		try {
			return config.getDouble(key);
		} catch (Exception e) {					
		}	
		return error;
	}

	public static String getString(Configuration config, String key, String error, Logger logger){
		try {
			String value = config.getString(key);
			logger.info("Read config:"+key+"="+value);
			return value;
		} catch (Exception e) {
			logger.error("Read config error:key="+key+",error="+e.getMessage());
		}
		return error;
	}
	public static boolean getBoolean(Configuration config,String key,boolean error,Logger logger){
		try {
			boolean value = config.getBoolean(key);
			logger.info("Read config:"+key+"="+value);
			return value;
		} catch (Exception e) {
			logger.error("Read config error:key="+key+",error="+e.getMessage());
		}
		return error;
	}
	public static int getInt(Configuration config,String key,int min,int max,int error,Logger logger){
		try {
			int value = config.getInt(key);
			if(value>max) value = max;
			if(value<min) value = min;
			logger.info("Read config:"+key+"="+value);
			return value;
		} catch (Exception e) {
			logger.error("Read config error:key="+key+",error="+e.getMessage());
		}
		return error;
	}
	public static double getDouble(Configuration config,String key,int min,int max,double error,Logger logger){
		try {
			double value = config.getDouble(key);
			if(value>max) value = max;
			if(value<min) value = min;
			logger.info("Read config:"+key+"="+value);
			return value;
		} catch (Exception e) {
			logger.error("Read config error:key="+key+",error="+e.getMessage());
		}
		return error;
	}
	
	/***
	 * 读取配置中的路径，根目录为当前文件路径
	 * @param config
	 * @param key
	 * @throws IOException 
	 */
	public static String getFilePath(PropertiesConfiguration config,String key) throws IOException{
		String path = config.getString(key);
		if(path.contains(":"))
			return path;
		return correctFilePath(getFileDirFromPath(config.getFileName())+path);
	}
	
	/***
	 * 遍历文件夹下所有文件：相对路径名称-绝对路径 ,字母小写
	 * 如：test.123 = c:\xxx\test\123.cfg
	 * @param dirPath,exts,bIncludeSubDir
	 * @return
	 */
	private static Map<String,String> getDirFileMap(String dirPath,String exts,boolean bIncludeSubDir){
		Map<String,String> map = new HashMap<String,String>();
		List<String> extList = new ArrayList<String>();
		String[] arext = exts.split("\\|");
		for(String ext:arext){
			String t = ext.trim().toLowerCase();
			if(t.length()>0) extList.add(t);
		}
		p_getDirFileMap(map,null,dirPath,extList,bIncludeSubDir);
		return map;
	}
	private static void p_getDirFileMap(Map<String,String> outmap,
			String nameParent,String dirPath,List<String> extList,boolean bIncludeSubDir){
		if(null==outmap) return;

		File dir = new File(dirPath);
		File[] fs = dir.listFiles();
		if(null==fs) return;
		
		for(int i=0; i<fs.length; i++){
		    String filename = fs[i].getName().toLowerCase();
		    if(fs[i].isFile()){
		    	String[] temps = filename.split("\\.");
		    	if(temps.length>=2){
		    		String ext = "."+temps[temps.length-1];
		    		if(extList.contains(ext)){
		    			String name = filename.replace(ext, "");
		    			if(null!=nameParent&&!nameParent.isEmpty())
				    		name = nameParent+"."+name;
				    	outmap.put(name, fs[i].getAbsolutePath());
		    		}
		    	}
		    }
		    else if(bIncludeSubDir){
		    	if(null!=nameParent&&!nameParent.isEmpty())
		    		filename = nameParent+"."+filename;
		    	p_getDirFileMap(outmap,filename,fs[i].getAbsolutePath(),extList,true);
		    }
		}
	}
	/***
	 * 根据路径获取目录
	 * @param filePath
	 * @return
	 */
	private static String getFileDirFromPath(String filePath){
		File f = new File(filePath);
		return f.getParent();
	}
	/***
	 * 修正文件路径:绝对路径名,移除多余的名称（比如 "." 和 ".."）、
	 *   分析符号连接（对于 UNIX 平台），以及将驱动器名转换成标准大小写形式（对于 Microsoft Windows 平台）
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	private static String correctFilePath(String filePath) throws IOException{
		String path = filePath.replace("/", File.separator)
				.replace("\\\\", File.separator).replace("\\", File.separator);
		File f = new File(path);
		return f.getCanonicalPath();
	}
}











