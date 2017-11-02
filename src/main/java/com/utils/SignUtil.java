package com.utils;

import java.security.DigestException;
import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;

import sun.misc.BASE64Encoder;
/**
 * MD5加密方法
 * @author ouyang
 *
 */
public class SignUtil {
	
	/**
	 * MD5加密算法
	 * @param secret
	 * @return
	 */
	public static String buildSign(String secret) {
		StringBuilder sign = new StringBuilder();
		MessageDigest md5;
		byte[] bytes = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			bytes = md5.digest(secret.getBytes("UTF-8"));// md5加密
			// 将MD5输出的二进制结果转换为小写的十六进制
			for (byte b : bytes) {
				int bt = b & 0xff;
				if (bt < 16) {
					sign.append(0);
				}
				sign.append(Integer.toHexString(bt));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign.toString().toUpperCase();
	}
	
	/**
	 * SHA1 安全加密算法
	 * @param maps 参数key-value map集合
	 * @return
	 * @throws DigestException 
	 */
	public static String SHA1(String decrypt) {
		if (StringUtils.isEmpty(decrypt)) {
			return null;
		}
		StringBuffer sha = new StringBuffer();
		try {
			// 指定sha1算法
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(decrypt.getBytes("UTF-8"));
			// 获取字节数组
			byte messageDigest[] = digest.digest();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					sha.append(0);
				}
				sha.append(shaHex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sha.toString().toUpperCase();
	}
	
	/**
	 * 转化为base64
	 * 
	 * @param str
	 * @return
	 */
	public static String toBase64(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		BASE64Encoder encode = new BASE64Encoder();
		return encode.encode(str.getBytes());
	}
	
}