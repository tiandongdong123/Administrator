package com.wf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.RedisUtil;
import com.wf.bean.HotWord;
import com.wf.bean.PageList;
import com.wf.dao.HotWordMapper;
import com.wf.service.HotWordService;

@Service
public class HotWordServiceImpl implements HotWordService{

	@Autowired
	private HotWordMapper hotWordMapper;
	
	@Override
	public PageList getHotWord(Map map) {
		
		PageList pagelist=new PageList();
		
		List<Object> list=hotWordMapper.getHotWordList(map);
		List<Object> count=hotWordMapper.getCount(map);
		
		pagelist.setPageSize(Integer.valueOf(map.get("pageSize").toString()));
		pagelist.setPageNum(Integer.valueOf(map.get("pageNum").toString()));
		pagelist.setPageRow(list);
		pagelist.setTotalRow(count.size());
		
		return pagelist;
	}

	@Override
	public Integer checkWordExist(String word_content) {
		return hotWordMapper.checkWordExist(word_content);
	}

	@Override
	public Integer addWord(HotWord hotWord) {
		return hotWordMapper.addWord(hotWord);
	}

	@Override
	public Integer updateWord(HotWord hotWord) {
		return hotWordMapper.updateWord(hotWord);
	}

	
	@Override
	public Integer updateWordIssue(HotWord hotWord) {
		return hotWordMapper.updateWord(hotWord);
	} 

	@Override
	public boolean checkRedisCount() {
		RedisUtil redis=new RedisUtil();
		String content=redis.get("theme",11);
		JSONArray array=JSONArray.fromObject(content);
		return array.size()>=20; 
	}

	@Override
	public boolean publishToRedis() {
		boolean success=true;
		try {
			RedisUtil redis=new RedisUtil();
			//redis.del(11,"theme");
			Map map=new HashMap();
			map.put("status",1);
			List<Object> list=hotWordMapper.getCount(map);
			JSONArray array=new JSONArray();
			for (Object object : list) {
				JSONObject json=JSONObject.fromObject(object);
				json.put("theme", json.get("word"));
				json.put("frequency",json.get("search_count"));
				json.put("field","");
				json.put("result","");
				json.put("tableName","");
				json.put("themeState","");
				json.put("type","");
				json.put("url","");
				json.put("userId","");
				json.put("userType","");
				json.put("checkTime","");
				json.put("createTime","");
				array.add(json);
			}
			redis.set("theme",array.toString(),11);
		} catch (Exception e) {
			success=false;
		}
		return success;

	}

	@Override
	public Integer deleteHotWord() {
		return hotWordMapper.deleteHotWord();
	}

}
