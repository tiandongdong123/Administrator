package com.wf.dao;


import java.util.List;
import java.util.Map;

import com.wf.bean.userStatistics.UserStatistics;
import org.apache.ibatis.annotations.Param;

import com.wf.bean.Person;

/**
 *	用户管理 Mapper 
 */
public interface PersonMapper {
    
	/**机构账号注册*/
	int addRegisterInfo(Person p);

	/**机构管理员注册*/
	int addRegisterAdmin(Person per);

	/**更新机构账号*/
	int updateRegisterInfo(Person person);

	/**更新管理员账号*/
	int updateRegisterAdmin(Person per);

	/**通过用户名查询用户信息*/
	Person queryPersonInfo(String userId);

	/** 更新用户解冻/冻结状态 */
	int updateUserFreeze(Map<String, String> map);

	/** 查询用户列表信息 */
	List<Object> findListInfo(Map<String, Object> map);
	
	/** 查询用户列表信息(优化) */
	List<Object> findListInfoSimp(Map<String, Object> map);

	/** 查询用户列表总数 */
	int findListCount(Map<String, Object> map);
	
	/** 查询用户列表总数(优化) */
	int findListCountSimp(Map<String, Object> map);
	
	/** 查询多个用户 */
	Map<String, Object> findUserById(String userId);
	
	/** 添加/移除机构管理员 */
	int updatePid(Map<String, Object> map);

	/** 通过id查询用户信息 */
	Map<String, Object> findListInfoById(String userId);

	/** 通过pid查询用户信息 */
	Map<String, Object> findInfoByPid(String pid);
	
	/** 更新所有机构账号的管理员 */
	int updateAllPid(Map<String, Object> map);
	
	/** 查询相似机构名称 */
	List<String> queryInstitution(String institution);
	
	/** 查询相似机构管理员 */
	List<String> queryAdminName(String userId);

	/** @Description: TODO(更新用户表) */
	boolean doUpdatePerson(Person ps);

	/** 通过机构名称获取所属管理员 
	 * @param pid */
	List<Map<String, Object>> findInstitutionAdmin(@Param("institution")String institution, @Param("userId")String userId);

	/** 通过机构账号查询机构子账号列表 */
	List<Map<String, Object>> sonAccountNumber(Map<String, Object> map);
	
	/** 更新机构名称 */
	void updateAllInstitution(Map<String, Object> map);

	/** 通过用户id查询学历信息 */
	String getEducations(String userId);
	
	/** 查询所有机构用户 */
	List<Object> findListIns();
	
	/**
	 * 根据机构名称所有机构用户ID
	 * @param institution 机构名称
	 * @return
	 */
	List<Map<String, Object>> getAllInstitutional(@Param("institution")String institution);
	
	/**
	 * 根据用户ID查询用户类型
	 * @param institution 用户ID
	 * @return
	 */
	String getUserTypeByUserId(@Param("userId")String userId);
	
	/**
	 * 获取平台总用户数
	 * @return 总用户数
	 */
	Integer countUser();
	
	/**
	 * 根据用户id集合查询用户信息
	 * @param userIds
	 * @return
	 */
    List<Person> queryPersonInId(@Param("userIds")List<String> userIds);
    
    
    /**
	 * 获取所有机构名称
	 * @param institution 机构名称
	 * @return
	 */
	List<String> getAllInstitution(@Param("institution")String institution);
	/**
	 *获取机构下所有账号
	 */
	List<String> getInstitutionUser(@Param("institutionName")String institutionName);

	/**
	 * 根据用户id查询机构名称
	 * @param userId
	 * @return
	 */
	String getInstitutionByUserId(@Param("userId") String userId);

	/**
	 * 根据用户id查询用户手机号
	 * @param userId
	 * @return
	 */
	String getPhoneNumberByUserId(@Param("userId") String userId);
	
	/**
	 * 获取子账号列表
	 * @param pid
	 * @return
	 */
	List<String> getSubaccount(@Param("pid") String pid);

	/**
	 * 用户统计数据/天
	 * @param dateTime
	 * @return
	 */
	UserStatistics selectStatisticsByDate(String dateTime);

	/**
	 * 当天机构数量
	 */
	Integer getInstitutionCount(String dateTime);

	/**
	 * 当天新增机构账号机构名称已存在数量
	 * @param dateTime
	 * @return
	 */
	Integer getExistedInstitutionCountByDate(String dateTime);

	/**
	 * 所有未冻结的机构账号
	 */
	List<String> selectunFreezeInstitutionAccount();
}