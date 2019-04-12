package com.redis;


import com.utils.GetDetails;

import net.sf.json.JSONArray;

public class getClcFromRedis {
	
public String  getClcName(String clc) {
		
		String clcdic=GetDetails.CLC_DIC==null?GetDetails.getCLCDic():GetDetails.CLC_DIC;
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
