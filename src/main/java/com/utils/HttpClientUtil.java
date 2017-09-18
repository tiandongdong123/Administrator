package com.utils;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.xxl.conf.core.XxlConfClient;
/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpClientUtil {
	
	private static String INIT = XxlConfClient.get("wf-public.user.init", null);
	private static String UPDATE = XxlConfClient.get("wf-public.user.update", null);
	
    /** 
     * 调用接口验证老平台用户是否存在 
     */
	public static void updateUserData(String user_id,boolean isInit) {
		HttpPost httpPost = null;
		try {
			System.out.println("=="+UPDATE);
			String[] urls = UPDATE.split(";");
			for (String url : urls) {
				HttpClient httpclient = HttpClients.createDefault();
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				httpPost = new HttpPost(url);
				nvps.add(new BasicNameValuePair("key", "123456789"));
				if (isInit) {
					nvps.add(new BasicNameValuePair("user_id", user_id));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
				httpclient.execute(httpPost);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
	}
	
	public static void test(){
		System.out.println(INIT);
	}
}