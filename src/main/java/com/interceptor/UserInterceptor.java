package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.utils.CookieUtil;

/**
 * 拦截器
 * 
 * @author vimes 2016-9-20
 * 
 */
public class UserInterceptor implements HandlerInterceptor {

	
	//完成后
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {}

	//后处理
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {}

	//预处理
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		String url = req.getRequestURI();
		//1、登录请求不验证、提醒不验证
		if (url.endsWith(CookieUtil.LOGIN_URL) || url.endsWith(CookieUtil.REMIND)) {
			return true;
		}
		//2、校验redis
		HttpSession session = req.getSession(true);
		String id=CookieUtil.getCache(session.getId());
		if (id == null) {
			res.sendRedirect(req.getContextPath() + CookieUtil.LOGIN_URL);
			return false;
		}
		//3、已经登录的每次请求都往session放入参数
		String userName=(String) session.getAttribute("userName");
		if (!url.endsWith(CookieUtil.INDEX) && userName == null) {
			String json = CookieUtil.getCache(CookieUtil.LAYOUT + id);
			if(json==null){
				res.sendRedirect(req.getContextPath() + CookieUtil.LOGIN_URL);
				return false;
			}
			JSONObject obj = JSONObject.fromObject(json);
			session.setAttribute("purviews", obj.get("purviews"));
			session.setAttribute("userName", obj.get("userName"));
			session.setAttribute("department", obj.get("department"));
		}
		String menu_url = url.split("/")[url.split("/").length - 1];
		session.setAttribute("menu_first", menu_url);
		return true;
	}
	
} 