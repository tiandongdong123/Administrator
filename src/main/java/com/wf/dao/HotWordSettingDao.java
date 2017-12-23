package com.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.HotWordSetting;

public interface HotWordSettingDao {
	Integer addWordSetting(HotWordSetting hotWordSetting);
	List<Object> getHotWordSetting(Map map);
	List<Object> getHotWordSettingCount(Map map);
	HotWordSetting getOneHotWordSetting(Integer id);
	Integer updateHotWordSetting(HotWordSetting hotWordSetting);
	HotWordSetting getHotWordSettingTask();
	List<Map<String,Object>> getHotWordTongJi(@Param("hotword") String hotword);
	Integer updateAllSetting();
}
