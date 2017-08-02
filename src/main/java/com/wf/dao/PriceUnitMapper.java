package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.PriceUnit;

public interface PriceUnitMapper {
	
	List<Object> getUnit(@Param("pagenum")Integer pagenum,@Param("pagesize")Integer pagesize);
	
	int getUnitNum();
	
	int deleteUnit(@Param("ids") String[] ids);
	
	List<Object> checkUnit(@Param("unitname") String unitname,@Param("unitcode") String unitcode);
	
	int doAddUnit(@Param("unitname") String unitname,@Param("unitcode") String unitcode);
	
	int doUpdateUnit(@Param("unitname") String unitname,@Param("unitcode") String unitcode,@Param("id") Integer id);
	
	List<Object> getRunitName();
	
	PriceUnit getOneUnit(@Param("unitcode") String unitcode);
	
}