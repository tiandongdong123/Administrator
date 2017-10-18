package com.utils;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;

import com.xxl.conf.core.XxlConfClient;
/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpClientUtil {
	
	private static Logger log = Logger.getLogger(HttpClientUtil.class);
	
	//private static String INIT = XxlConfClient.get("wf-public.user.init", null);
	private static String UPDATE = XxlConfClient.get("wf-public.user.update", null);
	
    /** 
     * 调用接口验证老平台用户是否存在 
     */
	public static void updateUserData(String user_id,boolean isInit) {
		HttpPost httpPost = null;
		try {
			long time=System.currentTimeMillis();
			String[] urls = UPDATE.split(";");
			for (String u : urls) {
				String url = u + "?key=123456789&user_id=" + user_id;
				URL httpurl = new URL(url);
				HttpURLConnection con = (HttpURLConnection) httpurl.openConnection();
				log.info(url+",status:" + con.getResponseCode()+",耗时："+(System.currentTimeMillis()-time));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
	}
}