package com.utils;

import java.io.IOException;
import java.util.Properties;



/**
 * 读取配置文件类
 * @author vimesly
 *
 */
public class SettingUtil {

	/**
	 * 根据properties文件的key获取value
	 * 
	 * @param filePath
	 *            properties文件路径
	 * @param key
	 *            属性key
	 * @return 属性value
	 */
		private static Properties pros = new Properties();
		public static Properties getPros(String properties) {
			try {
				pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(properties));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pros;
		}
	
}
