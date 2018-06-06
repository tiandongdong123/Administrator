package com.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import com.wanfangdata.setting.UpdateHandler;

/**
 * 读取配置文件类
 * 
 * @author vimesly
 * 
 */
public class SettingUtil {
	private static Logger log = Logger.getLogger(SettingUtil.class);
	private static JSONObject param=null;

	private static Properties pros = new Properties();

	public static Properties getPros(String properties) {
		try {
			pros.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(properties));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pros;
	}

	public static String getSetting(String key) {
		UpdateHandler up = new UpdateHandler() {
			@Override
			public void Handle(String s, String s1) {
				param = JSONObject.fromObject(s1);
			}
		};
		try {
			String solrPowerId = com.wanfangdata.setting.Setting.get("/AdminManagerConfig", up);
			param = JSONObject.fromObject(solrPowerId);
			return String.valueOf(param.get(key));
		} catch (Exception e) {
			log.error("更新获取配置文件失败", e);
		}
		return "";
	}
	
	/**
	 * 批量导入，批量注册单次最大条数
	 * @return
	 */
	public static Integer getImportExcelMaxSize() {
		String maxSize = SettingUtil.getSetting("importExcelMaxSize");
		if ("".equals(maxSize)) {
			return 0;
		}
		return NumberUtils.toInt(maxSize);
	}
	
	/**
	 * 获取四种资源ID
	 * @return
	 */
	public static Map<String,String> getResouceLimit() {
		String resouceLimit = SettingUtil.getSetting("resouceLimit");
		String[] strs=resouceLimit.split(",");
		Map<String,String> map=new HashMap<String,String>();
		for(String str:strs){
			map.put(str, str);
		}
		return map;
	}
}
