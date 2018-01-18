package com.redis;


import net.sf.json.JSONArray;

public class getClcFromRedis {
	
public String  getClcName(String clc) {
		
		RedisUtil redis=new RedisUtil();
		String clcdic=redis.get("CLCDic", 0);
		JSONArray json=JSONArray.fromObject(clcdic);
		String name=null;
		for(int i=0;i<json.size();i++){
			if(json.getJSONObject(i).getString("value").equals(clc)){
				name=json.getJSONObject(i).getString("name");
			}
		}
		return name;
	}
}