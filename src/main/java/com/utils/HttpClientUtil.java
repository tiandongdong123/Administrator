package com.utils;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpClientUtil {
	
	//private static String INIT = "http://my.wanfangdata.com.cn/auth/user/userRedis.do";
	private static String UPDATE = "http://my.wanfangdata.com.cn/auth/user/updateUser.do";
	
    /** 
     * 调用接口验证老平台用户是否存在 
     */
	public static void updateUserData(String user_id,boolean isInit) {
		HttpPost httpPost = null;
		try {
			String[] urls = UPDATE.split(";");
			HttpClient httpclient = HttpClients.createDefault();
			for (String url : urls) {
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
}