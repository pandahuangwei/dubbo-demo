package com.simple.common.utils.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**********************************************************************************
 * Copyright(c)2015 Dcits-air.com All rights reserved.
 * @Title: FileEncodeUtil.java
 * @Package com.dcits.message.util.encode
 * @Description:  基于AES加密算法的文件加解密
 *
 * @author connor
 * @version 1.0
 * @created 2015年4月1日 下午6:02:30
 **********************************************************************************/
public class FileEncodeUtil {
	public static final String sExt_Tiny = ".ty";
	public static final String sExt_Big = ".bg";

	// 密钥及文件头定义
	private static String _passwd = "i5tah*gw37JOHT";
	private static String _encode_flag = "The file is encoded by simple a-tech!\n";
	private static int HEAD_LENGTH = 128;

	/**************************
	 * 判断文件是否已经加密
	 *
	 * @param filepath
	 * @return
	 *************************/
	public static boolean IsFileEncoded(String filepath) {
		FileInputStream reader = null;

		try {
			reader = new FileInputStream(filepath);

			byte[] head = new byte[HEAD_LENGTH];
			int readLen = reader.read(head, 0, HEAD_LENGTH);
			if (CheckHead(head, readLen)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}

		return false;
	}

	/**************************
	 * 加密字符串到文件
	 *
	 * @param text
	 * @param filepath
	 * @return
	 *************************/
	public static boolean EncodeStringToFile(String text, String filepath) {
		FileOutputStream writer = null;

		try {
			writer = new FileOutputStream(filepath);
			// 写入校验头
			byte[] inbuf = text.getBytes("utf-8");
			int len = inbuf.length;

			byte[] head = new byte[HEAD_LENGTH];
			if (MakeHead(head, HEAD_LENGTH, GetHashMd5(inbuf, len))) {
				writer.write(head, 0, HEAD_LENGTH);

				// 加密并写入
				byte[] outbuf = AESDataEncodeUtil.encrypt(inbuf, len, _passwd);
				writer.write(outbuf, 0, outbuf.length);

				writer.flush();
				writer.close();

				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}

		return false;
	}

	/**************************
	 * 解密字符串文件
	 *
	 * @param filepath
	 * @return
	 *************************/
	public static String DecodeFileToString(String filepath) {
		FileInputStream reader = null;

		try {
			reader = new FileInputStream(filepath);

			byte[] head = new byte[HEAD_LENGTH];
			int readLen = reader.read(head, 0, HEAD_LENGTH);
			if (CheckHead(head, readLen)) {
				int len = (int) (new File(filepath).length());
				len -= readLen;
				byte[] inbuf = new byte[len];// 缓存
				readLen = reader.read(inbuf, 0, len);
				byte[] outbuf = AESDataEncodeUtil.decrypt(inbuf, readLen, _passwd);
				reader.close();

				String str = "";
				if (CheckData(outbuf, head)) {
					// utf8编码的文件头：EF BB BF
					if (outbuf[0] == -17 && outbuf[1] == -69
							&& outbuf[2] == -65)
						str = new String(outbuf, 3, outbuf.length - 3, "utf-8");
					else
						str = new String(outbuf, "utf-8");
				}

				return str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}

		return null;
	}

	/**************************
	 * 加密字符串
	 *
	 * @param srcStr
	 * @return
	 *************************/
	public static String EncodeString(String srcStr) {
		return AESDataEncodeUtil.encrypt(srcStr, _passwd);
	}

	/**************************
	 * 解密字符串
	 *
	 * @param encodeStr
	 * @return
	 *************************/
	public static String DecodeString(String encodeStr) {
		return AESDataEncodeUtil.decrypt(encodeStr, _passwd);
	}

	/**************************
	 * 加密文件（二进制方式）
	 *
	 * @param srcFilepath
	 * @param dstFilepath
	 * @return
	 *************************/
	public static boolean EncodeFileBin(String srcFilepath, String dstFilepath) {
		FileInputStream reader = null;
		FileOutputStream writer = null;

		try {
			// 读取数据
			reader = new FileInputStream(srcFilepath);
			int len = (int) (new File(srcFilepath).length());
			byte[] inbuf = new byte[len];
			int readLen = reader.read(inbuf, 0, len);
			reader.close();

			// 写入校验头
			writer = new FileOutputStream(dstFilepath);
			byte[] head = new byte[HEAD_LENGTH];
			if (MakeHead(head, HEAD_LENGTH, GetHashMd5(inbuf, readLen))) {
				writer.write(head, 0, HEAD_LENGTH);

				// 加密并写入
				byte[] outbuf = AESDataEncodeUtil.encrypt(inbuf, readLen, _passwd);
				writer.write(outbuf, 0, outbuf.length);

				writer.flush();
				writer.close();

				return true;
			}
		} catch (Exception e) {
		} finally {
			try {
				writer.close();
				reader.close();
			} catch (Exception e) {
			}
		}

		return false;
	}

	/**************************
	 * 解密文件（二进制方式）
	 *
	 * @param srcFilepath
	 * @param dstFilepath
	 * @return
	 *************************/
	public static boolean DecodeFileBin(String srcFilepath, String dstFilepath) {
		FileInputStream reader = null;
		FileOutputStream writer = null;

		try {
			reader = new FileInputStream(srcFilepath);

			byte[] head = new byte[HEAD_LENGTH];
			int readLen = reader.read(head, 0, HEAD_LENGTH);
			if (CheckHead(head, readLen)) {
				int len = (int) (new File(srcFilepath).length());
				len -= readLen;
				byte[] inbuf = new byte[len];
				readLen = reader.read(inbuf, 0, len);
				reader.close();

				byte[] outbuf = AESDataEncodeUtil.decrypt(inbuf, readLen, _passwd);
				boolean bRet = false;
				if (CheckData(outbuf, head)) {
					writer = new FileOutputStream(dstFilepath);

					writer.write(outbuf, 0, outbuf.length);

					writer.flush();
					writer.close();
					bRet = true;
				}
				return bRet;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				reader.close();
			} catch (Exception e) {
			}
		}

		return false;
	}

	/**************************
	 * 计算数据校验码：MD5
	 *
	 * @param bytearray
	 * @param len
	 * @return
	 *************************/
	public static String GetHashMd5(byte[] bytearray, int len) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(bytearray, 0, len<1?bytearray.length:len);
			return AESDataEncodeUtil.byte2hex(md.digest());
		} catch (Exception e) {
		}

		return null;
	}

	/**************************
	 * 计算数据校验码：Sha1
	 *
	 * @param bytearray
	 * @param len
	 * @return
	 *************************/
	public static String GetHashSha1(byte[] bytearray, int len) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA1");
			md.update(bytearray, 0, len);
			return AESDataEncodeUtil.byte2hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
		}

		return null;
	}

	/**************************
	 * 创建加密文件头：64flag|64校验
	 *
	 * @param head
	 * @param len
	 * @param filehash
	 * @return
	 *************************/
	private static boolean MakeHead(byte[] head, int len, String filehash) {
		if (null == head || len < 64)
			return false;

		try {
			for (int i = 0; i < len; i++) {
				head[i] = (byte) AESDataEncodeUtil.AverageRandom(-127, 128);
			}

			// 前64字节
			byte[] head_utf8 = _encode_flag.getBytes("utf-8");
			for (int i = 0; i < Math.min(head_utf8.length, 64); i++) {
				head[i] = head_utf8[i];
			}

			// 后64字节
			byte[] hash_utf8 = filehash.getBytes("utf-8");
			for (int i = 0; i < Math.min(hash_utf8.length, 64); i++) {
				head[i + 64] = hash_utf8[i];
			}

			return true;
		} catch (Exception e) {
		}

		return false;
	}

	/**************************
	 * 验证加密文件头
	 *
	 * @param head
	 * @param len
	 * @return
	 *************************/
	private static boolean CheckHead(byte[] head, int len) {
		if (null == head)
			return false;

		try {
			byte[] utf8 = _encode_flag.getBytes("utf-8");
			if (utf8.length > len)
				return false;

			for (int i = 0; i < Math.min(utf8.length, 64); i++) {
				if (head[i] != utf8[i])
					return false;
			}

			return true;

		} catch (Exception e) {
		}

		return false;
	}

	/**************************
	 * 验证加密文件头
	 *
	 * @param data
	 * @param head
	 * @return
	 *************************/
	private static boolean CheckData(byte[] data, byte[] head) {
		if (null == data || null == head)
			return false;

		try {
			String str = GetHashMd5(data, data.length);
			byte[] utf8 = str.getBytes("utf-8");
			if (head.length - 64 < Math.min(utf8.length, 64))
				return false;

			for (int i = 0; i < Math.min(utf8.length, 64); i++) {
				if (head[64 + i] != utf8[i])
					return false;
			}

			return true;
		} catch (Exception e) {
		}

		return false;
	}
}
