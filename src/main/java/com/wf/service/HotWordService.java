package com.wf.service;

import java.util.Map;

import com.wf.bean.HotWord;
import com.wf.bean.PageList;

public interface HotWordService {
	
	PageList getHotWord(Map map);
	Integer checkWordExist(String word_content);
	Integer addWord(HotWord hotWord);
	Integer updateWord(HotWord hotWord);
	Integer updateWordIssue(HotWord hotWord);
	Integer checkRedisCount();
	boolean publishToRedis();
	Integer deleteHotWord();
}
