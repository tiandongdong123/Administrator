package com.utils; 

/**
 * webservice接口调用返回值
 * 
 * @author ouyang
 * 
 */
public class WebServiceConsts {

	public static final int Succed = 1000; /* 成功 */
	public static final int ParamError = 1001; /* 用户参数错误 */
	public static final int IdError = 1002; /* 用户ID为空 */
	public static final int nameError = 1003; /* 用户名为空 */
	public static final int pwdError = 1004; /* 用户密码为空 */
	public static final int IdHaved = 1005; /* 已含有此ID用户 */
	public static final int PhoneError = 1006; /* 手机号错误 */
	public static final int UserError = 1007; /* 未查找到用户 */
	public static final int IplimitIsNull = 1008; /* IP地址列表为空 */
	public static final int LoginTypeError = 1009; /* 用户登录类型LoginType参数错误 */
	public static final int InwardError = 1101; /* 服务器内部错误 */
	public static final int AddUserError = 1102; /* 插入用户数据失败 */
	public static final int TransferInError = 1020; /* 资源权限TransferIn字段错误 */
	public static final int TransferOutError = 1021; /* 资源权限TransferOut字段错误 */
	public static final int iplimitIdError = 1022; /* IP列表ID错误与用户ID不相同 */
	public static final int iplimitInsertError = 1023; /* IP列表ID插入错误 */
	public static final int AccountIdError = 1030; /* 角色ID字段错误 */
	public static final int AccountRelatedIdError = 1031; /* 角色RelatedId字段错误 */
	public static final int PropertyNameError = 1032; /* UserSetting的PropertName为空 */
	public static final int UserSettingUserId = 1041; /* UserSetting的UserId为空 */

}
