package com.wf.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import wfks.accounting.setting.PayChannelModel;

import com.wanfangdata.rpc.bindauthority.ServiceResponse;
import com.wf.bean.BindAuthorityModel;
import com.wf.bean.GroupInfo;
import com.wf.bean.InstitutionalUser;
import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.StandardUnit;
import com.wf.bean.UserAccountRestriction;
import com.wf.bean.UserInstitution;
import com.wf.bean.UserIp;
import com.wf.bean.WarningInfo;
import com.wf.bean.WfksAccountidMapping;
import com.wf.bean.WfksUserSetting;

public interface AheadUserService {
	/** 机构账号注册 */
	boolean registerInfo(InstitutionalUser user);
	/** 机构账号修改 **/
	boolean updateinfo(InstitutionalUser user);
	/**批量机构帐号注册**/
	boolean batchRegisterInfo(InstitutionalUser user,Map<String,Object> map);
	/**批量机构帐号注册**/
	boolean batchUpdateInfo(InstitutionalUser user,Map<String,Object> map);
	
	/** 查询预警信息 */
	WarningInfo findWarning();
	
	/** 更新预警信息 */
	int updateWarning(Integer amountthreshold,Integer datethreshold,Integer remindtime,String remindemail,Integer countthreshold,Date exec_time);
	
	/** 添加预警信息 */
	int addWarning(Integer amountthreshold,Integer datethreshold,Integer remindtime,String remindemail,Integer countthreshold);

	/** 获取Excel机构用户 */
	List<Map<String, Object>> getExcelData(MultipartFile file,Map<String,String> errorMap);
	
	/** 机构账号注册 */
	int addRegisterInfo(InstitutionalUser user);
	
	/** 添加机构管理员 */
	int addRegisterAdmin(InstitutionalUser user);
	
	/** 更新机构管理员 */
	int updateRegisterAdmin(InstitutionalUser user);
	
	/** 通过用户名查询用户信息 */
	Person queryPersonInfo(String userId);

	/** 获取批量解冻/冻结的用户 */
	List<String> getExceluser(MultipartFile file);

	/**更新用户解冻/冻结状态 */
	int updateUserFreeze(String str, String redio);

    /** 查询机构账号列表信息 */
	PageList findListInfo(Map<String, Object> map) throws Exception;

	/** 添加/移除机构管理员 */
	int updatePid(Map<String, Object> map);

	/** 通过id查询用户信息 */
	Map<String,Object> findListInfoById(String userId);

	/** 通过pid查询用户信息 */
	Map<String, Object> findInfoByPid(String pid);

	/** 添加账号ip */
	void addUserIp(InstitutionalUser user);

	/** 添加管理员ip */
	void addUserAdminIp(InstitutionalUser user);
	
	/** 机构子账号处理 */
	int setAccountRestriction(InstitutionalUser user,boolean isReset);
	int setPartAccountRestriction(InstitutionalUser user);
	UserAccountRestriction getAccountRestriction(String userId);
	
	/** 添加/更新限时信息 */
	int addProjectDeadline(InstitutionalUser user, ResourceDetailedDTO dto,String adminId);

	/** 更新余额信息 */
	int chargeProjectBalance(InstitutionalUser user, ResourceDetailedDTO dto, String adminId);
	
	/** 更新系统次数信息 */
	int chargeCountLimitUser(InstitutionalUser user, ResourceDetailedDTO dto, String adminId);
	
	/** 添加项目资源信息 */
	void addProjectResources(InstitutionalUser user, ResourceDetailedDTO dto);
	
	/** 更新项目资源信息 */
	void updateProjectResources(InstitutionalUser user, ResourceDetailedDTO dto);

	/** 通过userId更新用户ip */
	void updateUserIp(InstitutionalUser user);
	
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

	/** 通过机构名称获取所属管理员  */
	List<Map<String, Object>> findInstitutionAdmin(String institution, String userId);
	/** 通过机构名称获取所属管理员 */
	PageList getSonaccount(Map<String,Object> map);

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
	void deleteResources(InstitutionalUser user, ResourceDetailedDTO dto, boolean b);
	/** 删除购买详情（权限） 
	 * @param b */
	void deleteResources(String userId,String projectId);

	/** 修改机构名称 */
	void updateInstitution(String institution, String oldins);

	/** 账号修改 */
	int updateUserInfo(InstitutionalUser user);

	/** 删除购买项目（逻辑删除） */
	int deleteAccount(InstitutionalUser user, ResourceDetailedDTO dto, String adminId) throws Exception;
	/** 删除购买项目（逻辑删除） */
	int deleteChangeAccount(InstitutionalUser user, String adminId) throws Exception;

    /** 调用接口验证老平台用户是否存在 */
	String validateOldUser(String userName);

	/** 创建党建管理员 **/
	int setPartyAdmin(InstitutionalUser user);
	/** 查询服务权限信息 */
	WfksUserSetting[] getUserSetting(String userId,String type);
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
	void addUserIns(InstitutionalUser user);


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
	/** 获取  **/
	UserInstitution getUserInstitution(String userId);
	/**判断余额和限次是否为大于等于0,是否大于最大值*/
	Double checkValue(InstitutionalUser user, ResourceDetailedDTO dto) throws Exception;
	/**获取子账号列表*/
	void updateSubaccount(InstitutionalUser user,String adminId) throws Exception;
	/** 保存机构用户权限 **/
	void addWfksAccountidMapping(InstitutionalUser user);
	/** 修改机构用户权限 **/
	void updateWfksAccountidMapping(InstitutionalUser user);
	/** 获取机构用户权限 **/
	WfksAccountidMapping[] getWfksAccountidLimit(String userId, String type);
	WfksAccountidMapping[] getWfksAccountid(String userId,String type);
	WfksAccountidMapping[]  getWfksAccountidByRelatedidKey(String relatedidKey);
	/**
	 * 查询该机构名下的所有机构管理员
	 */
	List<Person> findInstitutionAllUser(String institution);
	
	/**
	 * 新增机构用户权限
	 * @param com
	 * @return
	 */
	int addGroupInfo(InstitutionalUser user);
	
	/**
	 * 修改机构用户权限
	 * @param com
	 * @return
	 */
	int updateGroupInfo(GroupInfo info);
	
	/**
	 * 根据userId查询机构用户权限
	 * @param userId
	 * @return
	 */
	GroupInfo getGroupInfo(String userId);
}
