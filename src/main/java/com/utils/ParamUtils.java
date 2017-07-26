package com.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author ninemax
 * 检索参数工具类
 * 2016-09-24
 */
public class ParamUtils {
	public static String getParam(String para){
		para = para.replace("\\", "");
		String res = "";
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\"]";
		if(!StringUtils.isEmpty(para)){
			para = para.trim();
			String paras[] = para.split(" ");//按空格分割
			for(int i = 0;i<paras.length;i++){
				
				String keyWord = paras[i];
				//替换特殊字符
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(keyWord);
				keyWord = m.replaceAll("").trim();
				if(!StringUtils.isEmpty(keyWord)){
					res += "*$title:"+keyWord;
				
				}
			}
			if(!StringUtils.isEmpty(res))
				res = res.substring(1);
		}
		
		return res;
	}
	
}