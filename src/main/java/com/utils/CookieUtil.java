package com.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.redis.RedisUtil;
import com.redis.UserRedisUtil;
import com.wf.bean.Wfadmin;

public class CookieUtil {
	
	
	public static final String LOGIN_URL = "/user/toLogin.do";
	public static final String REMIND="/user/getRemind.do";
	public static final String INDEX="/user/toIndex.do";
	public static final String LAYOUT="layout.";
	
	private static String domain=Getproperties.getPros("validateOldUser.properties").getProperty("domain");

	public static String getCache(String key) {
		return UserRedisUtil.get(key, 0);
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
	 * @param token
	 * @param response
	 */
	public static void addCookie(String token, HttpServletResponse response) {
		try {
			Cookie cookie = new Cookie("CASTGC", token);
			cookie.setMaxAge(3600);
			cookie.setDomain(domain);
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			cookie.setDomain(domain);
			cookie.setPath("/");
			res.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public static boolean exists(String key, String userId) {
		String value = UserRedisUtil.hget(key, "Admin." + userId, 0);
		if (value != null && !"".equals(value)) {
			return true;
		}
		return false;
	}
	
}
