package com.wf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.DateUtil;
import com.utils.IPConvertHelper;
import com.utils.SolrService;
import com.wf.bean.GroupInfo;
import com.wf.bean.Person;
import com.wf.bean.UserAccountRestriction;
import com.wf.bean.UserInstitution;
import com.wf.bean.WfksAccountidMapping;
import com.wf.bean.WfksPayChannelResources;
import com.wf.bean.WfksUserSetting;
import com.wf.bean.WfksUserSettingKey;
import com.wf.dao.AheadUserMapper;
import com.wf.dao.GroupInfoMapper;
import com.wf.dao.PersonMapper;
import com.wf.dao.UserAccountRestrictionMapper;
import com.wf.dao.UserInstitutionMapper;
import com.wf.dao.UserIpMapper;
import com.wf.dao.WfksAccountidMappingMapper;
import com.wf.dao.WfksPayChannelResourcesMapper;
import com.wf.dao.WfksUserSettingMapper;
import com.wf.service.AheadUserService;
import com.wf.service.InstitutionService;
import com.xxl.conf.core.XxlConfClient;
@Service
public class InstitutionServiceImpl  implements InstitutionService {
	
	@Autowired
	private AheadUserMapper aheadUserMapper;
	@Autowired
	private PersonMapper personMapper;
	@Autowired
	private UserIpMapper userIpMapper;
	@Autowired
	private WfksPayChannelResourcesMapper wfksMapper;
	@Autowired
	private GroupInfoMapper groupInfoMapper;
	@Autowired
	private UserAccountRestrictionMapper userAccountRestrictionMapper;
	@Autowired
	private UserInstitutionMapper userInstitutionMapper;
	@Autowired
	private WfksAccountidMappingMapper wfksAccountidMappingMapper;
	@Autowired
	private WfksUserSettingMapper wfksUserSettingMapper;
	@Autowired
	private AheadUserService aheadUserService;
	
	private static Logger log = Logger.getLogger(InstitutionServiceImpl.class);
	private String hosts=XxlConfClient.get("wf-public.solr.url", null);

	@Override
	public Map<String,String> setSolrData() {
		Map<String,String> messageMap=new HashMap<>();
		messageMap.put("flag", "fail");
		messageMap.put("fail", "机构用户信息发送solr失败");
		try{
			log.info("开始初始化solr数据");
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("userType", "23");
			map.put("pageNum", 0);
			map.put("pageSize", 1000000);
			List<Object> userList = personMapper.findListInfoSimp(map);
			log.info("查询到的机构和机构子账号数是"+userList.size());
			List<Map<String, Object>> solrList=new ArrayList<>();
			Date date=DateUtil.stringToDate(DateUtil.dateToString(new Date()));
			String time=DateUtil.dateToString(date);
			time=time.replace(" ", "T")+"Z";
			for(Object object : userList){
				Map<String,Object> solrMap=new LinkedHashMap<>();
				//user
				Map<String, Object> userMap = (Map<String,Object>) object;
				this.addUser(userMap,solrMap);//添加用户信息
				this.addIp(userMap,solrMap);//添加Ip
				this.addLimit(userMap,solrMap);//查询权限信息
				this.getUserAccountidMapping(userMap,solrMap);//查询角色信息
				solrMap.put("CreateTime", date);
				solrMap.put("UpdateTime", date);
				solrList.add(solrMap);
			}
			//发送solr
			SolrService.getInstance(hosts+"/GroupInfo");
			SolrService.createList(solrList);
			String query="CreateTime:[ * TO "+time+")";
			SolrService.deleteByQuery("GroupInfo", query);
			messageMap.put("flag", "success");
			messageMap.put("success", "机构用户信息发送solr成功");
			log.info("结束初始化solr数据");
		}catch(Exception e){
			log.error("初始化solr数据异常",e);
		}
		return messageMap;
	}
	
	//添加权限
	private void addLimit(Map<String, Object> userMap, Map<String, Object> solrMap) {
		String userId = userMap.get("userId").toString();
		//wfks_pay_channel_resources
		List<String> payChannelIdList=new ArrayList<>();
		List<WfksPayChannelResources> listWfks = wfksMapper.selectByUserId(userId);
		for(WfksPayChannelResources res:listWfks){
			payChannelIdList.add(res.getPayChannelid());
		}
		solrMap.put("PayChannelId", payChannelIdList);
		//user_account_restriction
		UserAccountRestriction account=userAccountRestrictionMapper.getAccountRestriction(userId);
		solrMap.put("HasChildGroup", account==null?false:true);
		if(account!=null){
			solrMap.put("ChildGroupLimit",account.getUpperlimit());
			solrMap.put("ChildGroupConcurrent", account.getsConcurrentnumber());
			solrMap.put("GroupConcurrent", account.getpConcurrentnumber());
			solrMap.put("ChildGroupDownloadLimit", account.getDownloadupperlimit());
			solrMap.put("ChildGroupPayment", account.getChargebacks());
		}
		
		//user_institution
		UserInstitution ins=userInstitutionMapper.getUserIns(userId);
		if(ins!=null){
			String json =ins.getStatisticalAnalysis();
			JSONObject obj = JSONObject.fromObject(json);
			List<String> tongjiList=new ArrayList<>();
			if (obj.getInt("database_statistics") == 1) {
				tongjiList.add("database_statistics");
			}
			if (obj.getInt("resource_type_statistics") == 1) {
				tongjiList.add("resource_type_statistics");
			}
			solrMap.put("StatisticalAnalysis", tongjiList);
		}
	}

	//添加IP
	private void addIp(Map<String, Object> userMap, Map<String, Object> solrMap) throws Exception{
		//user_ip
		String userId=userMap.get("userId").toString();
		List<Map<String,Object>> list_ip = userIpMapper.findIpByUserId(userId);
		if(userMap.get("loginMode")!=null&&!userMap.get("loginMode").toString().equals("1")){
			long StartIP=Long.MAX_VALUE;
			long EndIP=0;
			List<String> IPList=new ArrayList<>();
			for(Map<String, Object> userIp : list_ip){
				Long beginIpAddressNumber=(long) userIp.get("beginIpAddressNumber");
				Long endIpAddressNumber=(long) userIp.get("endIpAddressNumber");
				StartIP=StartIP<beginIpAddressNumber?StartIP:beginIpAddressNumber;
				EndIP=EndIP>endIpAddressNumber?EndIP:endIpAddressNumber;
				IPList.add(IPConvertHelper.NumberToIP(beginIpAddressNumber)+"-"+IPConvertHelper.NumberToIP(endIpAddressNumber));
			}
			solrMap.put("StartIP", StartIP);
			solrMap.put("EndIP", EndIP);
			solrMap.put("OpenIP", IPList);
		}
	}

	//添加用户信息
	private void addUser(Map<String, Object> userMap, Map<String, Object> solrMap) throws Exception{
		String userId=userMap.get("userId").toString();
		solrMap.put("Id", userId);
		solrMap.put("Password", userMap.get("password"));
		solrMap.put("Institution", userMap.get("institution"));
		solrMap.put("UserType", userMap.get("usertype"));
		solrMap.put("ParentId", userMap.get("pid"));
		solrMap.put("LoginMode", userMap.get("loginMode"));
		solrMap.put("IsFreeze", "1".equals(userMap.get("isFreeze"))?true:false);
		//查询机构管理员
		String pid=userMap.get("pid")==null?"":userMap.get("pid").toString();
		if(!"".equals(pid)){
			Map<String, Object> adminMap=aheadUserService.findInfoByPid(pid);
			solrMap.put("AdministratorId", adminMap.get("userId"));
			solrMap.put("AdministratorEmail", adminMap.get("adminEmail"));
			solrMap.put("AdministratorPassword", adminMap.get("password"));
			List<Map<String,Object>> list_ip=(List<Map<String, Object>>) adminMap.get("adminIP");
			if(list_ip!=null&&list_ip.size()>0){
				List<String> adminIp=new ArrayList<>();
				for(Map<String, Object> userIp : list_ip){
					adminIp.add(userIp.get("beginIpAddressNumber")+"-"+userIp.get("endIpAddressNumber"));
				}
				solrMap.put("AdministratorOpenIP", adminIp);
			}
		}
		//GroupInfo
		GroupInfo info=groupInfoMapper.getGroupInfo(userId);
		if(info!=null){
			solrMap.put("OrderType",info.getOrderType());
			solrMap.put("OrderContent", info.getOrderContent());
			solrMap.put("CountryRegion",info.getCountryRegion());
			solrMap.put("PostCode", info.getPostCode());
			solrMap.put("Organization",info.getOrganization());
		}
	}

	//获取权限信息
	private void getUserAccountidMapping(Map<String, Object> userMap,Map<String, Object> solrMap) throws Exception{
		String userId=userMap.get("userId").toString();
		List<String> IsTrialList=new ArrayList<>();
		WfksAccountidMapping[] mapping = wfksAccountidMappingMapper.getWfksAccountidByIdKey(userId);
		for (WfksAccountidMapping wm : mapping) {
			if ("trical".equals(wm.getRelatedidAccounttype())) {
				IsTrialList.add(wm.getRelatedidKey());
			}
			if("openApp".equals(wm.getRelatedidAccounttype())){
				solrMap.put("AppStartTime", DateUtil.DateToFromatStr(wm.getBegintime()));
				solrMap.put("AppEndTime", DateUtil.DateToFromatStr(wm.getEndtime()));
			}
			if("openWeChat".equals(wm.getRelatedidAccounttype())){
				solrMap.put("WeChatStartTime", DateUtil.DateToFromatStr(wm.getBegintime()));
				solrMap.put("WeChatEndTime", DateUtil.DateToFromatStr(wm.getEndtime()));
				WfksUserSettingKey key=new WfksUserSettingKey();
				key.setUserId(userId);
				key.setUserType("WeChat");
				key.setPropertyName("email");
				WfksUserSetting[] setting=wfksUserSettingMapper.selectByUserId(key);
				if(setting.length>0){
					solrMap.put("Email4WeChat", setting[0].getPropertyValue());
				}
			}
			if("PartyAdminTime".equals(wm.getRelatedidAccounttype())){
				solrMap.put("PartyAdminStartTIme", DateUtil.DateToFromatStr(wm.getBegintime()));
				solrMap.put("PartyAdminEndTIme", DateUtil.DateToFromatStr(wm.getBegintime()));
				Person per = personMapper.queryPersonInfo(wm.getRelatedidKey());
				if(per!=null){
					solrMap.put("PartyAdminId", per.getUserId());
					solrMap.put("PartyAdminPassword", per.getPassword());
					String json = String.valueOf(per.getExtend());
					if(!StringUtils.isEmpty(json)){
						JSONObject obj = JSONObject.fromObject(json);
						solrMap.put("PartyAdminTrial", String.valueOf(obj.getBoolean("IsTrialPartyAdminTime")));
					}
				}
			}
		}
		if(IsTrialList.size()>0){
			solrMap.put("IsTrial", IsTrialList);
		}
	}
	
}
