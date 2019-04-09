package com.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
		// 1. 得到用户想访问的资源
        String uri = request.getRequestURI();
		System.out.println("111111222:"+uri);
		if(uri.equals("/group/batchregister.do")){
			chain.doFilter(request, response);
		}
		if(uri.equals("/group/batchregister.do")){
			chain.doFilter(request, response);
		}
		return;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
