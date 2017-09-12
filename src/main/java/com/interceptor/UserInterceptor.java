package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.utils.CookieUtil;
import com.wf.bean.Wfadmin;

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
		//2、非登录请求要验证是否登录
		Wfadmin admin=CookieUtil.getWfadmin(req);
		if (admin==null) {
			res.sendRedirect(req.getContextPath() + CookieUtil.LOGIN_URL);
			return false;
		}
		//3、已经登录的每次请求都往session放入参数
		String menu_url = url.split("/")[url.split("/").length - 1];
		req.getSession().setAttribute("menu_first", menu_url);
		if (!url.endsWith(CookieUtil.INDEX)) {
			String userName = (String) req.getSession().getAttribute("userName");
			if (userName == null) {
				userName = CookieUtil.LAYOUT + admin.getWangfang_admin_id();
				String json = CookieUtil.getCache(userName);
				if(json==null){
					res.sendRedirect(req.getContextPath() + CookieUtil.INDEX);
					return false;
				}
				JSONObject obj = JSONObject.fromObject(json);
				req.getSession().setAttribute("purviews", obj.get("purviews"));
				req.getSession().setAttribute("userName", obj.get("userName"));
				req.getSession().setAttribute("department", obj.get("department"));
			}
		}
		return true;
	}
	
} 