package com.utils;
import javax.servlet.http.HttpServletResponse;

public class JsonUtil {
	/**
	 * 固定格式输出json
	 * @param str
	 * @return
	 * @throws Exception 
	 */
	public static void toJsonHtml(HttpServletResponse response,boolean str) throws Exception{
		String json="";
		json="{\"flag\":\"" + str + "\"}";
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
	/**
	 * 输出json
	 * @param json
	 * @throws Exception
	 */
	public static void toJsonHtml(HttpServletResponse response,String json) throws Exception{
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
}
