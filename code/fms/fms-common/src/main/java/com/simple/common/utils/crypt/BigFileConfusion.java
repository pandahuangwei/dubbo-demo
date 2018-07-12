package com.simple.common.utils.crypt;

import java.io.File;
import java.io.RandomAccessFile;

/**********************************************************************************
 * Copyright(c)2015 Dcits-air.com All rights reserved.
 * @Title: BinFileParser.java 
 * @Package com.dcits.message.util.encode 
 * @Description: 大文件数据混淆
 * 
 * @author connor
 * @version 1.0
 * @created 2015年4月1日 下午6:02:30
 **********************************************************************************/
public class BigFileConfusion {
	private final static int LEN = 128;
	
	public static boolean fileMixed(String filepath){
		if (filepath.toLowerCase().endsWith(FileEncodeUtil.sExt_Big))
			return false;
			
		File file = new File(filepath);
		if (file.exists()) {
			if( filedataExchange(filepath)){
				String newfilePath = filepath + FileEncodeUtil.sExt_Big;
				return renameFile(filepath,newfilePath);
			}
		}
		
		return false;
	}
	
	public static boolean fileReset(String filepath){
		if (filepath.toLowerCase().endsWith(FileEncodeUtil.sExt_Big)){
			if( filedataExchange(filepath)){
				String newpath = filepath.substring(0,filepath.length()-FileEncodeUtil.sExt_Big.length());
				return renameFile(filepath,newpath);
			}
		}else{
			File file = new File(filepath);
			if (file.exists()) {
				return true;
			} else {
				filepath = filepath+FileEncodeUtil.sExt_Big;
				if( filedataExchange(filepath)){
					String newpath = filepath.substring(0,filepath.length()-FileEncodeUtil.sExt_Big.length());
					return renameFile(filepath,newpath);
				}
			}
		}
		return false;
	}
	
	private static boolean filedataExchange(String filepath){
		RandomAccessFile randomFile = null;
		
		try {
			byte[] buff = new byte[LEN];
			randomFile = new RandomAccessFile(filepath, "rw");
			
			randomFile.seek(0);
			int readLen = randomFile.read(buff);
			if( readLen == LEN ){
				AESDataEncodeUtil.exchange(buff, LEN, 0, LEN);
				randomFile.seek(0);
				randomFile.write(buff);	
			}
			
			randomFile.seek(1024);
			readLen = randomFile.read(buff);
			if( readLen == LEN ){
				AESDataEncodeUtil.exchange(buff, LEN, 0, LEN);
				randomFile.seek(1024);
				randomFile.write(buff);	
			}
			
			randomFile.seek(1024*32);
			readLen = randomFile.read(buff);
			if( readLen == LEN ){
				AESDataEncodeUtil.exchange(buff, LEN, 0, LEN);
				randomFile.seek(1024*32);
				randomFile.write(buff);	
			}
			
			randomFile.seek(1024*1024);
			readLen = randomFile.read(buff);
			if( readLen == LEN ){
				AESDataEncodeUtil.exchange(buff, LEN, 0, LEN);
				randomFile.seek(1024*1024);
				randomFile.write(buff);	
			}

			return true;
		} catch (Exception e) {
		}finally {
			try {
				randomFile.close();
			} catch (Exception e) {
			}
		}
		return false;
	}
	
    private static boolean renameFile(String src,String dst){
    	
    	File file = new File(src); 
    	return file.renameTo(new File(dst));
    }
}
