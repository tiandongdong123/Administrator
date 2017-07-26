package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Provider;


public interface ProviderMapper {
	
	/**
	 *	查询所有资源提供商
	 */
	List<Provider> getProviders();
	Provider getProvider(@Param("id")Integer id);
	
	boolean updateProvider(@Param("nameZh")String nameZh,@Param("providerName")String providerName,@Param("id")Integer id);//修改资源提供商
	int addProvider(Provider provider);//添加提供商
	boolean deleteProvider(@Param("id")Integer id);//删除供应商

	
	
}
