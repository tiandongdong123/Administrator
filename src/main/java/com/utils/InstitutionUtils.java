package com.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.alibaba.citrus.util.StringUtil;
import com.wf.bean.InstitutionalUser;
import com.wf.bean.Query;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.ResourceLimitsDTO;

public class InstitutionUtils {
	
	private static Pattern pa = Pattern.compile("[^0-9a-zA-Z-_]");
	private static Pattern paName = Pattern.compile("[^0-9a-zA-Z-_\\u4e00-\\u9fa5-_（）()]");
	private static Pattern passsName = Pattern.compile("[\\u4e00-\\u9fa5]");
	public static Integer maxData=99999999;

	/**
	 * 校验机构用户查询条件
	 * @param map
	 * @param query
	 * @return
	 */
	public static boolean validateQuery(Map<String, Object> map,Query query){
		boolean flag=true;
		String userId=query.getUserId();
		String ipSegment=query.getIpSegment();
		String institution=query.getInstitution();
		String openLimit=query.getOpenLimit();
		String Organization=query.getOrganization();
		String PostCode=query.getPostCode();
		String OrderType=query.getOrderType();
		String proType=query.getProType();
		String resource=query.getResource();
		String pageNum=query.getPageNum();
		String OrderContent=query.getOrderContent();
		String pageSize=query.getPageSize();
		
		map.put("userId",userId);
		if (!StringUtil.isEmpty(ipSegment)) {
			if(ipSegment.contains("-")&&IPConvertHelper.validateDoubleIp(ipSegment)){
				map.put("ipstart", IPConvertHelper.IPToNumber(ipSegment.split("-")[0]));
				map.put("ipend", IPConvertHelper.IPToNumber(ipSegment.split("-")[1]));
			}else if(IPConvertHelper.validateOneIp(ipSegment)){
				map.put("ipstart", IPConvertHelper.IPToNumber(ipSegment));
				map.put("ipend", IPConvertHelper.IPToNumber(ipSegment));
			}else{
				map.put("ipSegment", ipSegment);
				map.put("ipError", "ipError");
				flag=false;
			}
		}
		if (!StringUtils.isEmpty(institution)) {
			map.put("institution", institution.replace("_", "\\_"));
		}
		//调用用户权限 (个人绑定未加入)
		if (AuthorityLimit.TRICAL.getName().equals(openLimit)) {
			map.put("mapping", openLimit);
		}else if (AuthorityLimit.OPENAPP.getName().equals(openLimit)) {
			map.put("mapping", openLimit);
		}else if (AuthorityLimit.OPENWECHAT.getName().equals(openLimit)) {
			map.put("mapping", openLimit);
		}else if (AuthorityLimit.PARTYADMINTIME.getName().equals(openLimit)) {
			map.put("mapping", openLimit);
		}else if(AuthorityLimit.PID.getName().equals(openLimit)){
			map.put("admin", openLimit);
		}else if(AuthorityLimit.STATISTICS.getName().equals(openLimit)){
			map.put("tongji", openLimit);
		}else if(AuthorityLimit.SUBACCOUNT.getName().equals(openLimit)){
			map.put("Subaccount", openLimit);
		}
		map.put("openLimit", openLimit);
		if (!StringUtils.isEmpty(Organization) || !StringUtils.isEmpty(PostCode)
				|| !StringUtils.isEmpty(OrderType)) {
			map.put("ginfo", "0");
		}
		map.put("Organization", Organization);
		map.put("PostCode", PostCode);
		map.put("OrderType", OrderType);
		map.put("OrderContent", OrderContent==null?"":OrderContent.trim());
		
		map.put("proType", proType);
		map.put("resource", resource);
		map.put("pageNum", (Integer.parseInt(pageNum==null?"1":pageNum)-1)*Integer.parseInt((pageSize==null?"1":pageSize)));
		map.put("pageSize", Integer.parseInt(pageSize==null?"20":pageSize));
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(ipSegment)&& StringUtils.isEmpty(institution)&&StringUtils.isEmpty(openLimit)
				&&StringUtils.isEmpty(Organization)&&StringUtils.isEmpty(PostCode)&&StringUtils.isEmpty(OrderType)&&StringUtils.isEmpty(proType)) {
			flag=false;
		}
		return flag;
	}
	
	/**
	 * 机构注册验证
	 * @param com
	 * @param list
	 * @return
	 */
	public static Map<String,String> getRegisterValidate(InstitutionalUser user){
		Map<String,String> hashmap = new HashMap<String, String>();
		List<ResourceDetailedDTO> rdlist = user.getRdlist();
		if (rdlist==null) {
			hashmap.put("flag", "fail");
			hashmap.put("fail","购买项目不能为空，请选择购买项目");
			return hashmap;
		}
		if(user.getLoginMode().equals("0") || user.getLoginMode().equals("2")){
			if(!IPConvertHelper.validateIp(user.getIpSegment())){
				hashmap.put("flag", "fail");
				hashmap.put("fail",  "账号IP段格式错误，请填写规范的IP段");
				return hashmap;
			}
		}
		if(StringUtils.equals(user.getAdminname(), user.getUserId())){
			hashmap.put("flag", "fail");
			hashmap.put("fail",  "机构管理员ID和机构用户ID重复");
			return hashmap;
		}
		if(StringUtils.equals(user.getPartyAdmin(), user.getUserId())){
			hashmap.put("flag", "fail");
			hashmap.put("fail",  "党建管理员ID和机构用户ID重复");
			return hashmap;
		}
		if (!StringUtils.isBlank(user.getAdminname())
				&& StringUtils.equals(user.getAdminname(), user.getPartyAdmin())) {
			hashmap.put("flag", "fail");
			hashmap.put("fail", "机构管理员ID和党建管理员ID重复");
			return hashmap;
		}
		// 校验app和微信app时间
		hashmap = InstitutionUtils.DateValidate(user);
		if (hashmap.size() > 0) {
			return hashmap;
		}
		List<ResourceDetailedDTO> list=new ArrayList<ResourceDetailedDTO>();
		for (ResourceDetailedDTO dto : rdlist) {
			if (!StringUtils.isEmpty(dto.getProjectname())) {
				list.add(dto);
			}
		}
		for (ResourceDetailedDTO dto : list) {
			hashmap = InstitutionUtils.getProectValidate(user,dto, true, true);
			if (hashmap.size() > 0) {
				return hashmap;
			}
		}
		user.setRdlist(list);
		return hashmap;
	}
	
	/**
	 *  注册或者修改机构用户的非空校验
	 * @param dto
	 * @param isBatch false是批量,true是非批量
	 * @return
	 */
	public static Map<String,String> getProectValidate(InstitutionalUser user,ResourceDetailedDTO dto,boolean notBatch,boolean isAdd){
		Map<String,String> hashmap=new HashMap<String,String>();
		String projectname=dto.getProjectname()==null?"":dto.getProjectname();
		if(StringUtils.isBlank(dto.getValidityStarttime())||StringUtils.isBlank(dto.getValidityEndtime())) {
			hashmap.put("flag", "fail");
			hashmap.put("fail", projectname+"时限不能为空，请填写时限");
			return hashmap;
		}
		if(DateUtil.stringToDate1(dto.getValidityStarttime())==null||DateUtil.stringToDate1(dto.getValidityEndtime())==null){
			hashmap.put("flag", "fail");
			hashmap.put("fail", projectname+"时限格式不正确，请正确填写时限");
			return hashmap;
		}
		if(DateUtil.stringToDate1(dto.getValidityStarttime()).getTime()>DateUtil.stringToDate1(dto.getValidityEndtime()).getTime()){
			hashmap.put("flag", "fail");
			hashmap.put("fail", projectname+"时限开始时间不能大于结束时间");
			return hashmap;
		}
		if(notBatch){//单个注册或修改
			if (dto.getProjectType().equals("balance")) {
				if (dto.getTotalMoney() == null) {
					hashmap.put("flag", "fail");
					hashmap.put("fail", projectname+"金额输入不正确，请正确填写金额");
					return hashmap;
				}
				if (!NumberUtils.isNumber(dto.getTotalMoney())||NumberUtils.toDouble(dto.getTotalMoney())<=0&&isAdd) {
					hashmap.put("flag", "fail");
					hashmap.put("fail", projectname+"金额输入不正确，请正确填写金额");
					return hashmap;
				}
				if(NumberUtils.toDouble(dto.getTotalMoney())<=0&&StringUtils.isEmpty(dto.getValidityStarttime2())){
					hashmap.put("flag", "fail");
					hashmap.put("fail", projectname+"金额输入不正确，请正确填写金额");
					return hashmap;
				}
			} else if (dto.getProjectType().equals("count")) {
				if (dto.getPurchaseNumber() == null) {
					hashmap.put("flag", "fail");
					hashmap.put("fail", projectname+"次数输入不正确，请正确填写次数");
					return hashmap;
				}
				if (!NumberUtils.isNumber(dto.getPurchaseNumber())||NumberUtils.toInt(dto.getPurchaseNumber())<=0&&isAdd) {
					hashmap.put("flag", "fail");
					hashmap.put("fail", projectname+"次数输入不正确，请正确填写次数");
					return hashmap;
				}
			}
		}
		if (dto.getRldto() != null) {
			boolean flag = true;// 判断是否有选中的数据库
			for (ResourceLimitsDTO rldto : dto.getRldto()) {
				if (rldto.getResourceid() != null) {
					flag = false;
					break;
				}
			}
			if (flag) {
				hashmap.put("flag", "fail");
				hashmap.put("fail", projectname + "数据库不能为空，请选择数据库");
				return hashmap;
			}
		}
		return hashmap;
	}
	
	/**
	 * 机构账号修改验证
	 * @param com
	 * @param delList
	 * @param list
	 * @return
	 */
	public static Map<String,String> getUpdateValidate(InstitutionalUser user,List<String> delList,List<ResourceDetailedDTO> list){
		Map<String,String> hashmap=new HashMap<String,String>();
		if(user.getRdlist()==null){
			hashmap.put("flag", "fail");
			hashmap.put("fail",  "购买项目不能为空");
			return hashmap;
		}
		if(user.getLoginMode().equals("0") || user.getLoginMode().equals("2")){
			if(!IPConvertHelper.validateIp(user.getIpSegment())){
				hashmap.put("flag", "fail");
				hashmap.put("fail",  "账号IP段格式错误，请填写规范的IP段");
				return hashmap;
			}
		}
		if(StringUtils.equals(user.getAdminname(), user.getUserId())){
			hashmap.put("flag", "fail");
			hashmap.put("fail",  "机构管理员ID和机构用户ID重复");
			return hashmap;
		}
		if(StringUtils.equals(user.getPartyAdmin(), user.getUserId())){
			hashmap.put("flag", "fail");
			hashmap.put("fail",  "党建管理员ID和机构用户ID重复");
			return hashmap;
		}
		if (!StringUtils.isBlank(user.getAdminname())
				&& StringUtils.equals(user.getAdminname(), user.getPartyAdmin())) {
			hashmap.put("flag", "fail");
			hashmap.put("fail", "机构管理员ID和党建管理员ID重复");
			return hashmap;
		}
		// 校验app和微信app时间
		hashmap = InstitutionUtils.DateValidate(user);
		if (hashmap.size() > 0) {
			return hashmap;
		}
		for (ResourceDetailedDTO dto : user.getRdlist()) {
			if (StringUtils.isEmpty(dto.getProjectname())) {
				delList.add(dto.getProjectid());
				continue;
			}
			hashmap = InstitutionUtils.getProectValidate(user,dto, true, false);
			if (hashmap.size() > 0) {
				return hashmap;
			}
			list.add(dto);
		}
		return hashmap;
	}
	
	public static Map<String,String> DateValidate(InstitutionalUser user){
		Map<String,String> hashmap=new HashMap<String,String>();
		if (!StringUtils.isEmpty(user.getOpenApp())) {
			if(StringUtils.isBlank(user.getAppBegintime())||StringUtils.isBlank(user.getAppEndtime())){
				hashmap.put("flag", "fail");
				hashmap.put("fail", "APP嵌入服务有效期不能为空，请正确填写有效期");
				return hashmap;
			}
			if(DateUtil.stringToDate1(user.getAppBegintime())==null||DateUtil.stringToDate1(user.getAppEndtime())==null){
				hashmap.put("flag", "fail");
				hashmap.put("fail", "APP嵌入服务有效期格式不正确，请正确填写有效期");
				return hashmap;
			}
			if(DateUtil.stringToDate1(user.getAppBegintime()).getTime()>DateUtil.stringToDate1(user.getAppEndtime()).getTime()){
				hashmap.put("flag", "fail");
				hashmap.put("fail", "APP嵌入服务有效期开始时间不能大于结束时间");
				return hashmap;
			}
		}
		if (!StringUtils.isEmpty(user.getOpenWeChat())) {
			if(StringUtils.isBlank(user.getWeChatBegintime())||StringUtils.isBlank(user.getWeChatEndtime())){
				hashmap.put("flag", "fail");
				hashmap.put("fail", "微信公众号嵌入服务有效期不能为空，请正确填写有效期");
				return hashmap;
			}
			if(DateUtil.stringToDate1(user.getWeChatBegintime())==null||DateUtil.stringToDate1(user.getWeChatEndtime())==null){
				hashmap.put("flag", "fail");
				hashmap.put("fail", "微信公众号嵌入服务有效期格式不正确，请正确填写有效期");
				return hashmap;
			}
			if(DateUtil.stringToDate1(user.getWeChatBegintime()).getTime()>DateUtil.stringToDate1(user.getWeChatEndtime()).getTime()){
				hashmap.put("flag", "fail");
				hashmap.put("fail", "微信公众号嵌入服务有效期开始时间不能大于结束时间");
				return hashmap;
			}
		}
		return hashmap;
	}

	public static Map<String,String> getBatchRegisterValidate(InstitutionalUser user,List<Map<String, Object>> userList,boolean isRegister) {
		Map<String,String> errorMap = new HashMap<String, String>();
		int maxSize = SettingUtil.getImportExcelMaxSize();
		if (userList.size() > maxSize) {
			errorMap.put("flag", "fail");
			errorMap.put("fail", isRegister?("批量注册最多可以一次注册" + maxSize + "条"):("批量修改最多可以一次注册" + maxSize + "条"));
			return errorMap;
		}
		if ("2".equals(user.getLoginMode())&&!isRegister) {
			if(!StringUtils.isEmpty(user.getOpenApp())){
				errorMap.put("flag", "fail");
				errorMap.put("fail", "开通了APP嵌入服务，登录方式不能为用户名密码+IP");
				return errorMap;
			}
			if(!StringUtils.isEmpty(user.getOpenWeChat())){
				errorMap.put("flag", "fail");
				errorMap.put("fail", "开通了微信公众号嵌入服务，登录方式不能为用户名密码+IP");
				return errorMap;
			}
		}
		List<ResourceDetailedDTO> rdList = user.getRdlist();
		if (rdList == null || rdList.size() == 0) {
			errorMap.put("flag", "fail");
			errorMap.put("fail", "购买项目不能为空，请选择购买项目");
			return errorMap;
		}
		Map<String,String> userMap=new HashMap<String,String>();
		for (int i = 0; i < userList.size(); i++) {
			Map<String, Object> map = userList.get(i);
			String userId=map.get("userId")==null?"":map.get("userId").toString();
			if(!StringUtils.isEmpty(userId)){
				if(userMap.get(userId)!=null){
					errorMap.put("flag", "fail");
					errorMap.put("fail", "批量中出现多个"+userId+",请填写不重复的机构ID");
					return errorMap;
				}
				userMap.put(userId, userId);
			}
		}
		//删除不合法的购买项目
		for (int j = 0; j < rdList.size(); j++) {
			ResourceDetailedDTO dto = rdList.get(j);
			if (StringUtils.isEmpty(dto.getProjectid())) {
				rdList.remove(j--);
			}else{
				errorMap = InstitutionUtils.getProectValidate(user,dto, false, true);
				if (errorMap.size() > 0) {
					return errorMap;
				}
			}
		}
		String ins="";
		for (int i = 0; i < userList.size(); i++) {
			Map<String, Object> map = userList.get(i);
			String userId=map.get("userId")==null?"":map.get("userId").toString();
			String institution=map.get("institution")==null?"":map.get("institution").toString();
			String password=map.get("password")==null?"":map.get("password").toString();
			if (StringUtils.isEmpty(userId)&&StringUtils.isEmpty(institution)&&StringUtils.isEmpty(password)) {
				userList.remove(i--);
				continue;
			}
			if (StringUtils.isEmpty(userId)) {
				errorMap.put("flag", "fail");
				errorMap.put("fail", "机构ID不能为空，请填写规范的机构ID");
				return errorMap;
			}
			Matcher userM = pa.matcher(userId);
			if (userM.find()) {
				errorMap.put("flag", "fail");
				errorMap.put("fail", "账号" + userId + "格式不对，请填写规范的机构ID");
				return errorMap;
			}
			if("".equals(institution)&&isRegister){
				errorMap.put("flag", "fail");
				errorMap.put("fail", "账号"+userId+"的机构名称不能为空，请填写规范的机构名称");
				return errorMap;
			}
			if(!StringUtils.isEmpty(institution)){//机构名称注册的时候不能为空，修改的时候可以为空
				Matcher insM = paName.matcher(institution);
				if (insM.find()) {
					errorMap.put("flag", "fail");
					errorMap.put("fail", "账号"+userId+"的机构名称格式不对，请填写规范的机构名称");
					return errorMap;
				}
			}
			if(StringUtils.isBlank(password)&&isRegister){//批量注册的时候必须填写密码，批量修改的时候密码可以为空
				errorMap.put("flag", "fail");
				errorMap.put("fail", "账号"+userId+("".equals(password)?"的密码不能为空，请填写正确的密码":"的密码不能有空格，请填写正确的密码"));
				return errorMap;
			}
			if(!StringUtils.isBlank(password)){
				Matcher passMatcher = passsName.matcher(password);
				if(passMatcher.find()){
					errorMap.put("flag", "fail");
					errorMap.put("fail", "账号"+userId+"的密码不能有中文，请填写正确的密码");
					return errorMap;
				}
				if(password.length()<6||password.length()>16){
					errorMap.put("flag", "fail");
					errorMap.put("fail", "账号"+userId+"的密码长度必须在6-16位之间，请填写正确的密码");
					return errorMap;
				}
			}
			
			if ("2".equals(user.getLoginMode())) {
				String ip=map.get("ip")==null?"":map.get("ip").toString();
				if(StringUtils.isEmpty(ip)&&isRegister){
					errorMap.put("flag", "fail");
					errorMap.put("fail", "账号"+userId+"的IP段不能为空，请填写规范的IP段");
					return errorMap;
				}
				if(!StringUtils.isEmpty(ip)){
					if(ip.contains(" ")){
						errorMap.put("flag", "fail");
						errorMap.put("fail", "账号"+userId+"的IP段有空格，请填写规范的IP段");
						return errorMap;
					}
					if (!IPConvertHelper.validateIp(ip)) {
						errorMap.put("flag", "fail");
						errorMap.put("fail", "账号"+userId+"的IP段不合法，请填写规范的IP段");
						return errorMap;
					}
				}
			}
			//机构管理员的校验
			String adminId=StringUtils.isEmpty(user.getAdminname())?user.getAdminOldName():user.getAdminname();
			if(userId.equals(adminId)){
				errorMap.put("flag", "fail");
				errorMap.put("fail",  "机构管理员ID和机构用户ID重复");
				return errorMap;
			}
			if("".equals(ins)){
				ins=institution;
			}
			if(!StringUtils.equals(institution, ins)&&!StringUtils.isEmpty(adminId)){
				errorMap.put("flag", "fail");
				errorMap.put("fail",  "批量中添加了机构管理员，文档中的机构名称必须是一个机构名称");
				return errorMap;
			}
			if(user.getManagerType().equals("old") && !institution.equals(user.getInstitution())){
				errorMap.put("flag", "fail");
				errorMap.put("fail", "机构管理员"+adminId+"不属于"+institution+"的机构管理员");
				return errorMap;
			}
			List<Map<String, Object>> lm =  (List<Map<String, Object>>) map.get("projectList");
			//预加载校验页面项目是否和Excel中一致
			//验证金额，次数
			for(ResourceDetailedDTO dto : rdList){
				String projectId=dto.getProjectid();
				if (projectId.contains("TimeLimit")) {
					continue;
				}
				boolean isRight=true;
				for(Map<String, Object> pro : lm) {
					if (StringUtils.equals(dto.getProjectid(),String.valueOf(pro.get("projectid")))) {
						isRight=false;
						String totalMoney = pro.get("totalMoney") == null ? "" : pro.get("totalMoney").toString();
						if (StringUtils.isEmpty(totalMoney) || !NumberUtils.isNumber(totalMoney)
								|| NumberUtils.toDouble(totalMoney) <= 0&&isRegister) {
							errorMap.put("flag", "fail");
							errorMap.put("fail", "账号" + userId + "的" + dto.getProjectname()
									+ (dto.getProjectType().equals("balance") ? "金额输入不正确，请正确填写金额"
											: "次数输入不正确，请正确填写次数"));
							return errorMap;
						}
						if ((!StringUtils.isEmpty(user.getResetCount())&&dto.getProjectType().equals("time") || !StringUtils
								.isEmpty(user.getResetMoney())&&dto.getProjectType().equals("balance"))
								&& NumberUtils.toDouble(totalMoney) <= 0) {
							errorMap.put("flag", "fail");
							errorMap.put("fail", "账号" + userId + "的" + dto.getProjectname()
									+ (dto.getProjectType().equals("balance") ? "金额输入不正确，请正确填写金额"
											: "次数输入不正确，请正确填写次数"));
							return errorMap;
						}
						if (dto.getProjectType().equals("balance")) {
							if(NumberUtils.toDouble(totalMoney)>maxData){
								errorMap.put("flag", "fail");
								errorMap.put("fail", "账号"+ userId + "的" + dto.getProjectname()
										+  "修改后金额大于最大值，请正确填写金额");
								return errorMap;
							}
						} else if (dto.getProjectType().equals("count")) {
							if(NumberUtils.toInt(totalMoney)!=NumberUtils.toDouble(totalMoney)){
								errorMap.put("flag", "fail");
								errorMap.put("fail", "账号"+ userId + "的" + dto.getProjectname()
										+  "次数输入不正确，请正确填写次数");
								return errorMap;
							}
							if(NumberUtils.toInt(totalMoney)>maxData){
								errorMap.put("flag", "fail");
								errorMap.put("fail", "账号"+ userId + "的" + dto.getProjectname()
										+  "修改后次数大于最大值，请正确填写次数");
								return errorMap;
							}
						}
					}
				}
				if(isRight){
					errorMap.put("flag", "fail");
					errorMap.put("fail", userId+"用户购买项目无法匹配，请核对正确并填写");
					return errorMap;
				}
			}
		}
		user.setRdlist(rdList);
		return errorMap;
	}

	public static void importData(InstitutionalUser user, Map<String, Object> map) {
		List<ResourceDetailedDTO> rdList=user.getRdlist();
		List<Map<String, Object>> lm =  (List<Map<String, Object>>) map.get("projectList");
		for(ResourceDetailedDTO dto : rdList){
			for(Map<String, Object> pro : lm) {
				if (StringUtils.equals(dto.getProjectid(),String.valueOf(pro.get("projectid")))) {
					if (dto.getProjectType().equals("balance")) {
						dto.setTotalMoney(pro.get("totalMoney").toString());
					} else if (dto.getProjectType().equals("count")) {
						dto.setPurchaseNumber(pro.get("totalMoney").toString());
					}
				}
			}
		}
	}
	
}
