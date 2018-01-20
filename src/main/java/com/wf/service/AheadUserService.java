package com.wf.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wanfangdata.rpc.bindauthority.ServiceResponse;
import com.wf.bean.*;
import net.sf.json.JSONObject;

import org.springframework.web.multipart.MultipartFile;

import wfks.accounting.setting.PayChannelModel;

public interface AheadUserService {
	
	/** 查询预警信息 */
	WarningInfo findWarning();
	
	/** 更新预警信息 */
	int updateWarning(Integer amountthreshold,Integer datethreshold,Integer remindtime,String remindemail,Integer countthreshold);
	
	/** 添加预警信息 */
	int addWarning(Integer amountthreshold,Integer datethreshold,Integer remindtime,String remindemail,Integer countthreshold);

	/** 获取Excel机构用户 */
	List<Map<String, Object>> getExcelData(MultipartFile file);
	
	/** 查询所有项目 */
	List<Project> findProject();
	
	/** 机构账号注册 */
	int addRegisterInfo(CommonEntity com);
	
	/** 添加机构管理员 */
	int addRegisterAdmin(CommonEntity com);
	
	/** 批量更新机构账号<批量修改>(当前账号无管理员添加新的，已有管理员不做任何操作) */
	int updateRegisterInfo(CommonEntity com,String pid,String adminId);
	
	/** 更新机构管理员 */
	int updateRegisterAdmin(String institution,JSONObject json) throws Exception;
	
	/** 通过用户名查询用户信息 */
	Person queryPersonInfo(String userId);

	/** 获取批量解冻/冻结的用户 */
	List<String> getExceluser(MultipartFile file);

	/**更新用户解冻/冻结状态 */
	int updateUserFreeze(String str, String redio);
	
	/** 查询所有数据库 */
    //List<Map<String, Object>> selectList();

    /** 查询机构账号列表信息 */
	PageList findListInfo(Map<String, Object> map);

	/** 添加/移除机构管理员 */
	int updatePid(Map<String, Object> map);

	/** 通过id查询用户信息 */
	Map<String,Object> findListInfoById(String userId);

	/** 通过pid查询用户信息 */
	Map<String, Object> findInfoByPid(String pid);

	/** 添加账号ip */
	void addUserIp(CommonEntity com);

	/** 添加管理员ip */
	void addUserAdminIp(CommonEntity com);
	
	/** 添加账号限制信息 */
	int addAccountRestriction(CommonEntity com);

	/** 添加余额信息 */
	int addProjectBalance(CommonEntity com, ResourceDetailedDTO dto, String adminId);

	/** 添加/更新限时信息 */
	int addProjectDeadline(CommonEntity com, ResourceDetailedDTO dto,String adminId);

	/** 添加系统次数信息 */
	int addProjectNumber(CommonEntity com, ResourceDetailedDTO dto,String adminId);

	/** 更新余额信息 */
	int chargeProjectBalance(CommonEntity com, ResourceDetailedDTO dto, String adminId);
	
	/** 更新系统次数信息 */
	int chargeCountLimitUser(CommonEntity com, ResourceDetailedDTO dto, String adminId);
	
	/** 添加项目资源信息 */
	void addProjectResources(CommonEntity com, ResourceDetailedDTO dto);
	
	/** 更新项目资源信息 */
	void updateProjectResources(CommonEntity com, ResourceDetailedDTO dto);

	/** 通过userId更新用户ip */
	void updateUserIp(CommonEntity com);

	/** 通过userId更新用户限制 */
	int updateAccountRestriction(CommonEntity com);
	
	/** 通过userId获取购买项目信息 */
	Map<String, Object> getprojectinfo(String userId, Map<String, Object> map);

	/** 更新所有机构账号的管理员 */
	int updateAllPid(String pid,String old_pid);

	/** 查询相似机构名称 */
	List<String> queryInstitution(String institution);

	/** 查询相似机构管理员 */
	List<String> queryAdminName(String adminname);
	
	/** 查询余额信息 */
	Map<String, Object> selectBalanceById(String userId);

	/** 验证ip是否有交集  */
	List<UserIp> validateIp(String userId, long l, long m);

	/** 通过机构名称获取所属管理员 
	 * @param pid */
	List<Map<String, Object>> findInstitutionAdmin(String institution, String userId);

	/** 通过机构账号查询机构子账号列表 
	 * @param sonId */
	List<Map<String, Object>> sonAccountNumber(String userId, String sonId, String start_time, String end_time);

	/** 获取购买项目 */
	List<PayChannelModel> purchaseProject();

	/** 通过产品id反查资源库 */
	List<Map<String, Object>> selectListByRid(String proid);

    /** 
     * 个人账号充值（修改） 
     * @return 
     */
	int personCharge(String userId, String turnover, String reason, String authToken, HttpServletResponse httpResponse) throws Exception;

	/** 删除用户 */
	int deleteUser(String userId);

	/** 删除用户IP */
	int deleteUserIp(String userId);

	/** 删除购买详情（权限） 
	 * @param b */
	void deleteResources(CommonEntity com, ResourceDetailedDTO dto, boolean b);

	/** 修改机构名称 */
	void updateInstitution(String institution, String oldins);

	/** 账号修改 */
	int updateUserInfo(CommonEntity com, String adminId);

	/** 删除购买项目（逻辑删除） */
	int deleteAccount(CommonEntity com, ResourceDetailedDTO dto, String adminId);

    /** 调用接口验证老平台用户是否存在 */
	String validateOldUser(String userName);

	/** 设置服务权限 */
	int setAddauthority(Authority authority,Person person);

	/** 查询服务权限信息 */
	WfksAccountidMapping getAddauthority(String userId, String msg);

	WfksUserSetting getUserSetting(String userId, String msg);

	WfksAccountidMapping[] getAddauthorityByUserId(String userId);

	WfksUserSetting[] getUserSettingByUserId(String userId);
	
	/**根据用户id数组查询用户信息*/
	List<Person> queryPersonInId(List<String> userIds);
	/**获取权限列表*/
	List<AuthoritySetting> getAuthoritySettingList();
	
	/**
	 * 根据条件查询标准机构
	 * @param orgCode
	 * @param companySimp
	 * @return
	 */
	List<StandardUnit> findStandardUnit(String orgCode, String companySimp);
	
	/**
	 * 添加用户机构权限
	 * @param com
	 */
	void addUserIns(CommonEntity com);


	/**
	 * 开通个人绑定机构权限
	 * @param bindAuthority
	 */
	void openBindAuthority(BindAuthority bindAuthority);

	/**
	 * 查询是否开通个人绑定机构权限
	 * @param userId
	 * @return
	 */
	BindAuthority getBindAuthority(String userId);

	/**
	 * 根据id查询绑定数量
	 * @param userId
	 * @return
	 */
	int getBindAuthorityCount(String userId);

	/**
	 * 修改个人绑定机构权限
	 * @param bindAuthority
	 */
	void editBindAuthority(BindAuthority bindAuthority);

	void closeBindAuthority(BindAuthority bindAuthority);

	/**
	 * 获取
	 * @param userId
	 * @return
	 */
	UserInstitution getUserInstitution(String userId);
	
}
