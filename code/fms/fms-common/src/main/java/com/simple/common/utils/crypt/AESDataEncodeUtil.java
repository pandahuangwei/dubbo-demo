package com.simple.common.utils.crypt;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**********************************************************************************
 * Copyright(c)2015 Dcits-air.com All rights reserved.
 * @Title: AESDataEncodeUtil.java
 * @Package com.dcits.message.util.encode
 * @Description: AES数据加解密
 *
 * @author connor
 * @version 1.0
 * @created 2015年4月1日 下午6:02:30
 **********************************************************************************/
public class AESDataEncodeUtil {
	/**************************
	 * 生成平均分布的随机数
	 *
	 * @param min
	 * @param max
	 * @return
	 *************************/
	public static int AverageRandom(int min, int max) {
		double rand = Math.random() * Math.random();
		int randInteger = (int) (rand * 100000);

		int minInteger = (int) (min * 10000);
		int maxInteger = (int) (max * 10000);
		int diffInteger = maxInteger - minInteger;
		int resultInteger = randInteger % diffInteger + minInteger;

		return (int) (resultInteger / 10000.0);
	}

	/**************************
	 * 加密字节数组
	 *
	 * @param content
	 * @param password
	 * @return
	 *************************/
	public static byte[] encrypt(byte[] content, String password) {
		return encrypt(content, content.length, password);
	}

	/**************************
	 * 加密字节数组 :指定长度
	 *
	 * @param content
	 * @param length
	 * @param password
	 * @return
	 *************************/
	public static byte[] encrypt(byte[] content, int length, String password) {
		try {
			SecretKeySpec key = createKey(password);
			Cipher cipher = Cipher.getInstance(CipherMode);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(content, 0, length);

			byteMixup(result, result.length);

			return result;
		} catch (Exception e) {
		}
		return null;
	}

	/**************************
	 * 解密字节数组
	 *
	 * @param content
	 * @param password
	 * @return
	 *************************/
	public static byte[] decrypt(byte[] content, String password) {
		return decrypt(content, content.length, password);
	}

	/**************************
	 * 解密字节数组:指定长度
	 *
	 * @param content
	 * @param length
	 * @param password
	 * @return
	 *************************/
	public static byte[] decrypt(byte[] content, int length, String password) {
		try {
			byteReset(content, length);

			SecretKeySpec key = createKey(password);
			Cipher cipher = Cipher.getInstance(CipherMode);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(content, 0, length);

			return result;
		} catch (Exception e) {
		}
		return null;
	}

	/**************************
	 * 加密字符串(结果为16进制字符串)
	 *
	 * @param content
	 * @param password
	 * @return
	 *************************/
	public static String encrypt(String content, String password) {
		byte[] data = null;
		try {
			data = content.getBytes("UTF-8");
			return byte2hex(encrypt(data, password));
		} catch (Exception e) {
		}
		return null;
	}

	private static void byteMixup(byte[] data, int length){
		exchange(data, length, 0, 32);
		exchange(data, length, 32 * 5, 32 * 10);
		exchange(data, length, 32 * 15, 32 * 50);
		exchange(data, length, 32 * 20, 32 * 40);
		exchange(data, length, 32 * 1000, 32 * 1024);
		exchange(data, length, 32 * 1100, 32 * 1300);
		exchange(data, length, 32 * 1500, 32 * 1804);
	}

	private static void byteReset(byte[] data, int length){
		exchange(data, length, 0, 32);
		exchange(data, length, 32 * 5, 32 * 10);
		exchange(data, length, 32 * 15, 32 * 50);
		exchange(data, length, 32 * 20, 32 * 40);
		exchange(data, length, 32 * 1000, 32 * 1024);
		exchange(data, length, 32 * 1100, 32 * 1300);
		exchange(data, length, 32 * 1500, 32 * 1804);
	}

	public static void exchange(byte[] data, int length, int start, int end){
		if (null == data)
			return;
		if (start < 0 || end < 0 || end < start)
			return;
		if (length < end )
			return;

		end = end - 1;
		for (int i = 0; i < 32; i++)
		{
			byte temp = data[start + i];
			data[start + i] = (byte)(~(data[end - i]));
			data[end - i] = (byte)(~temp);
		}
	}

	/**************************
	 * 解密字符串
	 *
	 * @param content
	 * @param password
	 * @return
	 *************************/
	public static String decrypt(String content, String password) {
		byte[] data = null;
		try {
			data = hex2byte(content);
			return new String(decrypt(data, password), "UTF-8");
		} catch (Exception e) {
		}
		return null;
	}

	/**************************
	 * 字节数组转成16进制字符串
	 *
	 * @param b
	 * @return
	 *************************/
	public static String byte2hex(byte[] b) {
		try {
			StringBuffer sb = new StringBuffer(b.length * 2);
			String tmp = "";
			for (int n = 0; n < b.length; n++) {
				tmp = (Integer.toHexString(b[n] & 0xFF));
				if (tmp.length() == 1) {
					sb.append("0");
				}
				sb.append(tmp);
			}
			return sb.toString().toUpperCase();
		} catch (Exception e) {
		}

		return null;
	}

	/**************************
	 * 将hex字符串转换成字节数组
	 *
	 * @param inputString
	 * @return
	 *************************/
	public static byte[] hex2byte(String inputString) {
		try {
			if (inputString == null || inputString.length() < 2) {
				return null;
			}
			inputString = inputString.toLowerCase();
			int l = inputString.length() / 2;
			byte[] result = new byte[l];
			for (int i = 0; i < l; ++i) {
				String tmp = inputString.substring(2 * i, 2 * i + 2);
				result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
			}
			return result;
		} catch (NumberFormatException e) {
		}

		return null;
	}

	/** 算法/模式/填充 **/
	private static final String CipherMode = "AES/ECB/PKCS5Padding";

	/**************************
	 * 创建密钥
	 *
	 * @param password
	 * @return
	 *************************/
	private static SecretKeySpec createKey(String password) {
		try {
			byte[] data = null;
			if (password == null) {
				password = "";
			}
			StringBuffer sb = new StringBuffer(16);
			sb.append(password);
			while (sb.length() < 16) {
				sb.append("0");
			}
			if (sb.length() > 16) {
				sb.setLength(16);
			}
			data = sb.toString().getBytes("UTF-8");
			return new SecretKeySpec(data, "AES");
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}
}
