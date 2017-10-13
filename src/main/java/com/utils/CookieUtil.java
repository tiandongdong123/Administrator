package com.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.redis.RedisUtil;
import com.wf.bean.Wfadmin;

public class CookieUtil {
	
	public static RedisUtil redis = new RedisUtil();
	
	public static final String LOGIN_URL = "/user/toLogin.do";
	public static final String REMIND="/user/getRemind.do";
	public static final String INDEX="/user/toIndex.do";
	public static final String LAYOUT="layout.";

	public static String getCache(String key) {
		return redis.get(key, 0);
	}

	/**
	 * 检验cookie
	 * @param req
	 * @return
	 */
	public static String getCookie(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		String castgc = null;
		if (cookies != null && cookies.length > 0) {
			for (Cookie ck : cookies) {
				if (ck.getName().equals("CASTGC")) {
					castgc = ck.getValue();
					break;
				}
			}
		}
		return castgc;
	}
	
	/**
	 * 添加cookie
	 * @param token
	 * @param response
	 */
	public static void addCookie(String token, HttpServletResponse response) {
		try {
			Cookie cookie = new Cookie("CASTGC", token);
			cookie.setMaxAge(3600);
			cookie.setDomain("wanfangdata.com.cn");
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取cookie中存的用户信息
	 * @param req
	 * @return
	 */
	public static Wfadmin getWfadmin(HttpServletRequest req){
		Wfadmin admin=null;
		Cookie[] cookies = req.getCookies();
		String json="";
		if(cookies!=null && cookies.length>0){
			for(Cookie ck : cookies){
				if(ck.getName().equals("Wfadmin")){
					try {
						json=URLDecoder.decode(ck.getValue(), "utf-8");
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(!"".equals(json)){
			JSONObject object = JSONObject.fromObject(json);
			admin= (Wfadmin)JSONObject.toBean(object,Wfadmin.class);	
		}
		return admin;
	}
	
	/**
	 * 添加cookie
	 * @param user
	 * @param response
	 * @throws Exception
	 */
	public static void addWfCookie(Wfadmin user, HttpServletResponse res) {
		try {
			Cookie cookie = new Cookie("Wfadmin", "");
			String str = URLEncoder.encode(JSONObject.fromObject(user).toString(), "utf-8");
			cookie.setValue(str);
			cookie.setMaxAge(3600);
			cookie.setDomain("wanfangdata.com.cn");
			cookie.setPath("/");
			res.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除wfcookie信息
	 * @param req
	 * @param res
	 */
	public static void removeWfadmin(HttpServletRequest req, HttpServletResponse res) {
		try {
			Cookie cookie = new Cookie("Wfadmin", "");
			cookie.setMaxAge(0);
			cookie.setDomain("wanfangdata.com.cn");
			cookie.setPath("/");
			res.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
