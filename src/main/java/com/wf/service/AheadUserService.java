package com.wf.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import wfks.accounting.setting.PayChannelModel;

import com.wanfangdata.rpc.bindauthority.ServiceResponse;
import com.wf.bean.BindAuthorityModel;
import com.wf.bean.CommonEntity;
import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.StandardUnit;
import com.wf.bean.UserBoughtItems;
import com.wf.bean.UserInstitution;
import com.wf.bean.UserIp;
import com.wf.bean.WarningInfo;
import com.wf.bean.WfksAccountidMapping;
import com.wf.bean.WfksUserSetting;

public interface AheadUserService {
	
	/** 查询预警信息 */
	WarningInfo findWarning();
	
	/** 更新预警信息 */
	int updateWarning(Integer amountthreshold,Integer datethreshold,Integer remindtime,String remindemail,Integer countthreshold,Date exec_time);
	
	/** 添加预警信息 */
	int addWarning(Integer amountthreshold,Integer datethreshold,Integer remindtime,String remindemail,Integer countthreshold);

	/** 获取Excel机构用户 */
	List<Map<String, Object>> getExcelData(MultipartFile file);
	
	/** 机构账号注册 */
	int addRegisterInfo(CommonEntity com);
	
	/** 添加机构管理员 */
	int addRegisterAdmin(CommonEntity com);
	
	/** 批量更新机构账号<批量修改>(当前账号无管理员添加新的，已有管理员不做任何操作) */
	int updateRegisterInfo(CommonEntity com,String pid,String adminId);
	
	/** 更新机构管理员 */
	int updateRegisterAdmin(CommonEntity com);
	
	/** 通过用户名查询用户信息 */
	Person queryPersonInfo(String userId);

	/** 获取批量解冻/冻结的用户 */
	List<String> getExceluser(MultipartFile file);

	/**更新用户解冻/冻结状态 */
	int updateUserFreeze(String str, String redio);

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
	int addProjectBalance(CommonEntity com, ResourceDetailedDTO dto, String adminId) throws Exception;

	/** 添加/更新限时信息 */
	int addProjectDeadline(CommonEntity com, ResourceDetailedDTO dto,String adminId) throws Exception;

	/** 添加系统次数信息 */
	int addProjectNumber(CommonEntity com, ResourceDetailedDTO dto,String adminId) throws Exception;

	/** 更新余额信息 */
	int chargeProjectBalance(CommonEntity com, ResourceDetailedDTO dto, String adminId) throws Exception;
	
	/** 更新系统次数信息 */
	int chargeCountLimitUser(CommonEntity com, ResourceDetailedDTO dto, String adminId) throws Exception;
	
	/** 添加项目资源信息 */
	void addProjectResources(CommonEntity com, ResourceDetailedDTO dto);
	
	/** 更新项目资源信息 */
	void updateProjectResources(CommonEntity com, ResourceDetailedDTO dto);

	/** 通过userId更新用户ip */
	void updateUserIp(CommonEntity com);

	/** 通过userId更新用户限制 */
	int updateAccountRestriction(CommonEntity com);
	
	/** 通过userId获取购买项目信息 */
	List<Map<String, Object>> getProjectInfo(String userId);

	/** 更新所有机构账号的管理员 */
	int updateAllPid(String pid,String old_pid);

	/** 查询相似机构名称 */
	List<String> queryInstitution(String institution);

	/** 查询相似机构管理员 */
	List<String> queryAdminName(String adminname);
	
	/** 查询余额信息 */
	Map<String, Object> selectBalanceById(String userId);

	/** 验证ip是否有交集  */
	List<Map<String,Object>> validateIp(List<UserIp> list);
	/** 根据user_id查询ip信息  */
	List<Map<String, Object>> listIpByUserId(String userId);

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
	int deleteAccount(CommonEntity com, ResourceDetailedDTO dto, String adminId) throws Exception;

    /** 调用接口验证老平台用户是否存在 */
	String validateOldUser(String userName);

	/** 创建党建管理员 **/
	int setPartyAdmin(CommonEntity com);

	/** 查询服务权限信息 */
	WfksAccountidMapping getAddauthority(String userId, String msg);

	WfksUserSetting getUserSetting(String userId, String msg);
	
	WfksUserSetting[] getUserSetting2(String userId,String type);

	WfksAccountidMapping[] getAddauthorityByUserId(String userId);

	WfksUserSetting[] getUserSettingByUserId(String userId);
	
	/**根据用户id数组查询用户信息*/
	List<Person> queryPersonInId(List<String> userIds);
	
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
	 * @param bindAuthorityModel
	 */
	void openBindAuthority(BindAuthorityModel bindAuthorityModel);

	/**
	 * 查询是否开通个人绑定机构权限
	 * @param userId
	 * @return
	 */
	BindAuthorityModel getBindAuthority(String userId);

	/**
	 * 根据id查询绑定数量
	 * @param userId
	 * @return
	 */
	int getBindAuthorityCount(String userId);

	/**
	 * 修改个人绑定机构权限
	 * @param bindAuthorityModel
	 */
	ServiceResponse editBindAuthority(BindAuthorityModel bindAuthorityModel);

	/**
	 * 关闭个人绑定机构权限
	 * @param bindAuthorityModel
	 */
	void closeBindAuthority(BindAuthorityModel bindAuthorityModel);

	/**
	 * 检验个人绑定数量是否大于修改后的数值（大于则返回此账号id）
	 * @return
	 */
	List<String> checkBindLimit(List<Map<String, Object>> listMap,Integer bindLimit);

	/**
	 * 获取
	 * @param userId
	 * @return
	 */
	UserInstitution getUserInstitution(String userId);
	/**判断余额和限次是否为大于等于0*/
	boolean checkLimit(CommonEntity com, ResourceDetailedDTO dto) throws Exception;
	/**添加机构用户购买项目表*/
	void addUserBoughtItems(CommonEntity com);
	/**修改机构用户购买项目表*/
	void updateUserBoughtItems(CommonEntity com);
	/**根据机构用户ID获取购买项目信息*/
	List<UserBoughtItems> getUserBoughtItems(String userId);
	/**获取子账号列表*/
	void updateSubaccount(CommonEntity com,String adminId) throws Exception;
	/** 保存机构用户权限 **/
	void addWfksAccountidMapping(CommonEntity com);
}
