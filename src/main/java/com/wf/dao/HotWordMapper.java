package com.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.HotWord;

public interface HotWordMapper {

	List<Object> getHotWordList(Map map);
	List<Object> getCount(Map map);
	Integer checkWordExist(@Param("word_content") String word_content);
	Integer addWord(HotWord hotWord);
	Integer updateWord(HotWord hotWord);
	Integer deleteHotWord();
	Integer[] getHotWordByOrder(Map map); 
}
