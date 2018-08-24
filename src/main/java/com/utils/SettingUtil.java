package com.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.wanfangdata.setting.Setting;
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
	private static JSONArray arrayArea = new JSONArray();

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
			String solrPowerId = Setting.get("/Conf/AdminManager/Config", up);
			param = JSONObject.fromObject(solrPowerId);
			return String.valueOf(param.get(key));
		} catch (Exception e) {
			log.error("更新获取配置文件失败", e);
		}
		return "";
	}
	
	/**
	 * 获取区域编码
	 * @return
	 */
	public static JSONArray getRegionCode() {
		UpdateHandler up = new UpdateHandler() {
			@Override
			public void Handle(String s, String s1) {
				arrayArea = getData(s1);
			}
		};
		try {
			String xml = Setting.get("/Conf/AdminManager/RegionCode.xml",up);
			return getData(xml);
		} catch (Exception e) {
			log.error("更新获取配置文件失败", e);
		}
		return arrayArea;
	}
	
	/**
	 * 解析xml
	 * @param xml
	 * @return
	 */
	private static JSONArray getData(String xml){
		 Document document = null;
        JSONArray array = new JSONArray();
        try {
            document = DocumentHelper.parseText(xml);
    		Element root = document.getRootElement();
    		List<Element> list = root.elements();
    		for (Element e : list) {
    			List<Element> list1 = e.elements();
    			for (Element e1 : list1) {
    				JSONObject obj = new JSONObject();
    				obj.put("name", e1.attribute("Name").getValue());
    				obj.put("id", e1.attribute("Value").getValue());
    				array.add(obj);
    			}
    		}
        } catch (DocumentException e) {
            log.error("String转xml异常", e);
        }
		return array;
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
	
	
	/**
	 * 获取区域编码
	 * 
	 * @return
	 */
	public static String getCollection(String key) {
		try {
			UpdateHandler up = new UpdateHandler() {
				@Override
				public void Handle(String s, String s1) {}
			};
			String xml = Setting.get("/PqConfigs/SolrUrl.xml", up);
			if(StringUtils.isEmpty(xml)){
				return "";
			}
			Document document = null;
			try {
				document = DocumentHelper.parseText(xml);
				Element root = document.getRootElement();
				List<Element> list = root.elements();
				for (Element e : list) {
					String core = e.attribute("key").getValue();
					if (StringUtils.equals(key, core)) {
						return e.attribute("value").getValue();
					}
				}
			} catch (DocumentException e) {
				log.error("String转xml异常", e);
			}
		} catch (Exception e) {
			log.error("获取配置文件SolrUrl.xml失败", e);
		}
		return "";
	}
	
}
