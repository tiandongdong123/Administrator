package com.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;  
  
import org.springframework.web.servlet.HandlerInterceptor;  
import org.springframework.web.servlet.ModelAndView;  

/**
 * 拦截器
 * 
 * @author vimes 2016-9-20
 * 
 */
public class UserInterceptor implements HandlerInterceptor {

	private static final String LOGIN_URL = "/user/toLogin.do";
	private static final String HOMEPAGE_URL = "/user/toIndex.do";
	
	//完成后
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	//后处理
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	//预处理
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {  
		HttpSession session = req.getSession(true);
        Object obj = session.getAttribute("wfAdmin");
        String url =req.getRequestURI();
        String menu_url=url.split("/")[url.split("/").length - 1];
        Object urls=session.getAttribute("urls");

        //检查cookie
		String castgc = null;
		Cookie[] cookies = req.getCookies();
		if(cookies!=null && cookies.length>0){
			for(Cookie ck : cookies){
				if(ck.getName().equals("CASTGC")){							
					castgc = ck.getValue();
					break;
				}
			}
		}
        
        if(obj == null || "".equals(obj.toString()) || castgc == null){
        	res.sendRedirect(req.getContextPath()+LOGIN_URL);
        	return false;
        }  
        return true;
	}
	
} 