package com.utils;

import java.security.MessageDigest;
/**
 * MD5加密方法
 * @author ouyang
 *
 */
public class SignUtil {

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
}