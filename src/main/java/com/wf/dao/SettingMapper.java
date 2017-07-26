package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Setting;


public interface SettingMapper {
	
	/**列表+分页+模糊搜索【条件：名称(settingName)、英文标识(settingKey)、备注(remark)】*/
	List<Object> getSetting(@Param("setting")Setting setting,@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize);
	/**查询符合条件的个数【条件仅支持：名称(settingName)、英文标识(settingKey)、备注(remark)】*/
	int getSettingNum(@Param("setting")Setting setting);
	/**查询当前Setting*/
	Setting getSettingById(@Param("setting")Setting setting);
	/**添加Setting表*/
	int addSetting(Setting setting);
	/**更新Setting表*/
	int updateSetting(Setting setting);
	/**删除Setting*/
	int deleteSetting(String id);
	
}
