package com.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.utils.CookieUtil;
import com.utils.MenuXml;
import com.wf.bean.Wfadmin;
import com.wf.dao.RoleMapper;

/**
 * 拦截器
 * 
 * @author vimes 2016-9-20
 * 
 */
public class UserInterceptor implements HandlerInterceptor {

	@Autowired
	private RoleMapper role;
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
		// 2、校验cookie
		String cookie = CookieUtil.getCookie(req);
		if (cookie == null) {
			res.sendRedirect(req.getContextPath() + CookieUtil.LOGIN_URL);
			return false;
		}
		// 3、获取cookie中的wfadmin
		Wfadmin admin = CookieUtil.getWfadmin(req);
		if (admin == null) {
			res.sendRedirect(req.getContextPath() + CookieUtil.LOGIN_URL);
			return false;
		}
		// 4、校验redis
		String adminId = admin.getWangfang_admin_id();
		HttpSession session = req.getSession(false);
		if (session==null || !CookieUtil.exists(cookie, adminId)) {
			res.sendRedirect(req.getContextPath() + CookieUtil.LOGIN_URL);
			return false;
		}
		if (!url.endsWith(CookieUtil.INDEX)) {
			if (session.getAttribute("userName") == null) {
				String json = CookieUtil.getCache(CookieUtil.LAYOUT + adminId);
				if (json == null) {
					res.sendRedirect(req.getContextPath() + CookieUtil.LOGIN_URL);
					return false;
				}
				JSONObject obj = JSONObject.fromObject(json);
				session.setAttribute("purviews", obj.get("purviews"));
				session.setAttribute("userName", obj.get("userName"));
				session.setAttribute("department", obj.get("department"));
			}
		}
		//获取所有需要权限判断的url
		List<String> menuAllUrl=MenuXml.MENU_ALL_URL;
		//判断当前访问url是否需要进行权限判断
		if(menuAllUrl.contains(url)){
			//进行权限判断 查看当前用户是否有该权限
			String s=role.getRoleById(admin.getRole_id()).getPurview();
			List<String> list=Arrays.asList(s.split(","));
			List<String> adminUrl=MenuXml.getPurviewsListUrl(list);
			if(!adminUrl.contains(url)){
				res.sendRedirect("/user/topurview.do");
				return false;
			}
		}
		session.setAttribute("menu_first", url);
		return true;
	}	
} 