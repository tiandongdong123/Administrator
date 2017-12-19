package com.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
