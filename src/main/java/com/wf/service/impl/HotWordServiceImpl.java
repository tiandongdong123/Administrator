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
		pagelist.setPageTotal(count.size()%pagelist.getPageSize()==0?count.size()/pagelist.getPageSize():count.size()/pagelist.getPageSize()+1);
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
	public Integer checkRedisCount() {
		String content=RedisUtil.get("theme",11);
		JSONArray array=JSONArray.fromObject(content);
		return array.size(); 
	}

	@Override
	public boolean publishToRedis() {
		boolean success=true;
		try {
			//redis.del(11,"theme");
			Map map=new HashMap();
			map.put("status",1);
			List<Object> list=hotWordMapper.getCount(map);
			JSONArray array=new JSONArray();
			for (Object object : list) {
				JSONObject json=JSONObject.fromObject(object);
				json.put("theme", json.get("word"));
				json.put("frequency",json.get("searchCount"));
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
			RedisUtil.set("theme",array.toString(),11);
			System.out.println("发布到ridis的数据:  "+array.toString());
		} catch (Exception e) {
			success=false;
		}
		return success;

	}

	@Override
	public Integer deleteHotWord() {
		return hotWordMapper.deleteHotWord();
	}

	@Override
	public Integer[] getHotWordByOrder(Map map) {
		return hotWordMapper.getHotWordByOrder(map);
	}

}
