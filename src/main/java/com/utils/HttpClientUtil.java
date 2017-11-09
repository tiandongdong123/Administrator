package com.utils;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.wf.bean.StandardUnit;
import com.xxl.conf.core.XxlConfClient;
/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpClientUtil {
	
	private static Logger log = Logger.getLogger(HttpClientUtil.class);
	
	private static String UPDATE = XxlConfClient.get("wf-public.user.update", null);
	private static String REG_URL=XxlConfClient.get("wf-admin.bzcbsorgreg", null);
	private static String EDIT_URL=XxlConfClient.get("wf-admin.bzcbsorgedit",null);
    private static String SALEAGTID=XxlConfClient.get("wf-admin.saleagtid",null);
    private static String SALEPUBKEY=XxlConfClient.get("wf-admin.salepubkey",null);
	private static String CHARSET="UTF-8";
	
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

	/**
	 * 标准模块的注册
	 * 
	 * @param user_id
	 * @param isInit
	 * @return
	 */
	public static String orgReg(StandardUnit unit) {
		return getHttpResponse(unit, REG_URL);
	}

	/**
	 * 标准模块的更新
	 * 
	 * @param user_id
	 * @param isInit
	 * @return
	 */
	public static String orgEdit(StandardUnit unit) {
		return getHttpResponse(unit, EDIT_URL);
	}

	/**
	 * 标准调用接口方法
	 * 
	 * @param obj
	 * @param URL
	 * @return
	 */
	private static String getHttpResponse(StandardUnit unit, String URL) {
		long time=System.currentTimeMillis();
		log.info("接口入参：" + JSONObject.fromObject(unit).toString());
		String result = null;
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(URL);
			// 设置参数
			List<NameValuePair> list = getPari(unit);
			if (list.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, CHARSET);
				httpPost.setEntity(entity);
			}
			HttpResponse response = httpclient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, CHARSET);
				}
			}
			log.info("接口出参：" + result+",耗时："+(System.currentTimeMillis()-time)+"ms");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 组装post接口参数
	 * 
	 * @param obj
	 * @return
	 */
	private static List<NameValuePair> getPari(StandardUnit unit) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Map<String, String> map = new HashMap<String, String>();
		String saleagtid = SALEAGTID;
		String orgCode = unit.getOrgCode();
		String orgName = saleagtid+"_"+unit.getOrgName();
		String company = unit.getCompany();
		String companysimp = unit.getCompanySimp();
		String ipsection = unit.getiPLimits();
		map.put("saleagtid", saleagtid);
		map.put("orgcode", orgCode);
		map.put("orgname", orgName);
		map.put("company", company);
		map.put("companysimp", companysimp);
		map.put("ipsection", ipsection);
		StringBuilder builder = new StringBuilder();
		builder.append(saleagtid);
		builder.append(orgCode);
		builder.append(orgName);
		builder.append(company);
		builder.append(companysimp);
		builder.append(ipsection);
		String publicKey = SALEPUBKEY.substring(0, 16);
		String encstrTmp = SignUtil.buildSign(builder.toString());
		String encstr = SignUtil.buildSign(publicKey + encstrTmp);
		map.put("encstr", encstr);
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> elem = iterator.next();
			list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
		}
		return list;
	}
}