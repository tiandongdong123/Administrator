package com.interceptor;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class XSSFilterUtils {
	
	private static HashMap<String, String> xssMap = new HashMap<String, String>();
	
	static {
		// TODO Auto-generated method stub
		// 含有脚本： script
        xssMap.put("[s|S][c|C][r|R][i|C][p|P][t|T]", "");
        // 含有脚本 javascript
        xssMap.put("[\\\"\\\'][\\s]*[j|J][a|A][v|V][a|A][s|S][c|C][r|R][i|I][p|P][t|T]:(.*)[\\\"\\\']", "\"\"");
        // 含有函数： eval
        xssMap.put("[e|E][v|V][a|A][l|L]\\((.*)\\)", "");
        // 含有符号 <
        xssMap.put("<", "");
        // 含有符号 >
        xssMap.put(">", "");
        // 含有符号 (
        xssMap.put("\\(", "(");
        // 含有符号 )
        xssMap.put("\\)", ")");
        // 含有符号 '
        xssMap.put("'", "");
        // 含有符号 "
        xssMap.put("\"", "");
	}
	public static HttpRequestWrapper checkXss(HttpServletRequest httpReq){
		 return new HttpRequestWrapper(httpReq,xssMap);
	}

}
