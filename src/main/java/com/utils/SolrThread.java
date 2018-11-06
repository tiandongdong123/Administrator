package com.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.wanfangdata.encrypt.PasswordHelper;
import com.wf.bean.InstitutionalUser;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.importSolr.ImportSolrRequest;
import com.xxl.conf.core.XxlConfClient;

public class SolrThread implements Runnable {
	
	private String hosts=XxlConfClient.get("wf-public.solr.url", null);
	private static Logger log = Logger.getLogger(SolrThread.class);
	private Thread t;
	private List<SolrInputDocument> solrList=null;
	private List<Map<String, Object>> createList=null;
	private List<Map<String, Object>> updateList=null;

	public SolrThread(List<SolrInputDocument> solrList,List<Map<String, Object>> createList,List<Map<String, Object>> updateList) {
		this.solrList = solrList;
		this.createList=createList;
		this.updateList=updateList;
	}
	
	/**
	 * 执行方法
	 */
	public void run() {
		try {
			//发送solr
			log.info("solr开始更新或新增");
			SolrService.getInstance(hosts+"/GroupInfo");
			if(solrList!=null){
				SolrService.addIndexList(solrList);
			}
			if(createList!=null){
				SolrService.createList(createList);
			}
			if(updateList!=null){
				SolrService.updateList(updateList);
			}
			log.info("solr完成更新或新增");
		} catch (Exception e) {
			SendMail2 util=new SendMail2();
			util.sendSolrEmail();
			log.error("发送solr异常",e);
		}
	}
	
	/**
	 * 开始执行
	 */
	public void start() {
		if (t == null) {
			t = new Thread(this, "");
			t.start();
		}
	}
	/**
	 * 冻结解冻
	 * @param aid
	 * @param flag
	 */
	public static void setFreeze(String aid,String flag){
		List<SolrInputDocument> list=new ArrayList<SolrInputDocument>();
		SolrInputDocument doc=new SolrInputDocument();
		doc.setField("Id", aid);
		Map<String, Object> operation = new HashMap<>();
		operation.put("set", "1".equals(flag)?true:false);
		doc.addField("IsFreeze", operation);
		doc.addField("UpdateTime", SolrThread.getDate());//修改时间
		list.add(doc);
		SolrThread mt = new SolrThread(list,null,null);
        Thread t1 = new Thread(mt,"冻结解冻");
        t1.start();
	}
	
	/**
	 * 移除管理员
	 * @param userId
	 * @param com
	 */
	public static void removeAdmin(String userId, InstitutionalUser com) {
		
		Map<String,Object> solrMap=new HashMap<String,Object>();
		solrMap.put("ParentId", null);
		solrMap.put("ChildGroupLimit", null);
		solrMap.put("ChildGroupConcurrent", null);
		solrMap.put("ChildGroupDownloadLimit", null);
		solrMap.put("ChildGroupPayment", null);
		solrMap.put("StatisticalAnalysis", null);
		solrMap.put("UpdateTime", SolrThread.getDate());//修改时间
		solrMap.put("AdministratorId", null);
		solrMap.put("AdministratorEmail", null);
		solrMap.put("AdministratorPassword", null);
		solrMap.put("AdministratorOpenIP", null);
		solrMap.put("HasChildGroup", false);
		List<SolrInputDocument> list=new ArrayList<SolrInputDocument>();
		SolrInputDocument doc=new SolrInputDocument();
		doc.setField("Id", userId);
		for (Map.Entry<String, Object> m : solrMap.entrySet()) {
			Map<String, Object> oper = new HashMap<>();
			oper.put("set", m.getValue());
			doc.addField(m.getKey(),oper);
		}
		list.add(doc);
		SolrThread mt = new SolrThread(list,null,null);
        Thread t1 = new Thread(mt,"solr线程");
        t1.start();
	}
	
	/**
	 * 添加管理员
	 * @param userId
	 * @param com
	 */
	public static void addAdmin(String userId,String pid, InstitutionalUser com) {
		Map<String,Object> solrMap=new HashMap<String,Object>();
		//机构子账号表
		solrMap.put("ParentId", pid);
		if(com.getUpperlimit()!=null){
			solrMap.put("ChildGroupLimit", com.getUpperlimit()==null?"":com.getUpperlimit());
			solrMap.put("ChildGroupConcurrent", com.getsConcurrentnumber()==null?"":com.getsConcurrentnumber());
			if(com.getpConcurrentnumber()!=null){
				solrMap.put("GroupConcurrent", com.getpConcurrentnumber());
			}
			solrMap.put("ChildGroupDownloadLimit", com.getDownloadupperlimit()==null?"":com.getDownloadupperlimit());
			solrMap.put("ChildGroupPayment", com.getChargebacks()==null?"":com.getChargebacks());
			solrMap.put("HasChildGroup", true);
		}else{
			solrMap.put("HasChildGroup", false);
		}
		
		//统计分析
		String tongji = com.getTongji()==null?"":com.getTongji();
		if(!StringUtils.isEmpty(tongji)){
			Map<String, Object> operation = new HashMap<>();
			List<String> ls=new ArrayList<>();
			if(tongji.contains("database_statistics")){
				ls.add("database_statistics");
			}
			if(tongji.contains("resource_type_statistics")){
				ls.add("resource_type_statistics");
			}
			operation.put("set", ls);
			solrMap.put("StatisticalAnalysis", operation);
		}

		//管理员
		solrMap.put("AdministratorId", pid);
		solrMap.put("AdministratorEmail", com.getAdminEmail());
		try {
			solrMap.put("AdministratorPassword", PasswordHelper.encryptPassword(com.getAdminpassword()));
		} catch (Exception e) {
			
		}
		solrMap.put("AdministratorOpenIP", "".equals(com.getAdminIP())?null:com.getAdminIP());
		
		List<SolrInputDocument> list=new ArrayList<SolrInputDocument>();
		SolrInputDocument doc=new SolrInputDocument();
		doc.setField("Id", userId);
		for (Map.Entry<String, Object> m : solrMap.entrySet()) {
			if(!m.getKey().equals("StatisticalAnalysis")){
				Map<String, Object> oper = new HashMap<>();
				oper.put("set", m.getValue());
				doc.addField(m.getKey(),oper);
			}else{
				doc.addField(m.getKey(),m.getValue());
			}
		}
		doc.addField("UpdateTime", SolrThread.getDate());//修改时间
		list.add(doc);
		System.out.println(list.toString());
		SolrThread mt = new SolrThread(list,null,null);
        Thread t1 = new Thread(mt,"solr线程");
        t1.start();
	}
	
	/**
	 * 修改机构名称
	 * @param institution
	 * @param oldins
	 */
	public static void updateInstitution(String institution,String oldins) {
		SolrQuery sq=new SolrQuery();
		sq.set("collection", "GroupInfo");
		sq.setQuery("Institution:"+oldins);
		sq.set("fl", "Id");
		sq.setRows(10000);
		SolrDocumentList list=SolrService.getSolrList(sq);
		List<SolrInputDocument> sdList=new ArrayList<SolrInputDocument>();
		for(SolrDocument sd:list){
			SolrInputDocument input=new SolrInputDocument();
			input.addField("Id", sd.getFirstValue("Id"));
			Map<String, Object> oper = new HashMap<>();
			oper.put("set", institution);
			input.addField("Institution",oper);
			input.addField("UpdateTime", SolrThread.getDate());//修改时间
			sdList.add(input);
		}
		SolrThread mt = new SolrThread(sdList,null,null);
        Thread t1 = new Thread(mt,"solr线程");
        t1.start();
	}
	
	//整体更新solr数据
	public static void registerInfo(InstitutionalUser user,boolean isAdd) throws Exception{
		Map<String,Object> solrMap=new LinkedHashMap<>();
		// 全库更新机构管理员 
		updateAllAdministrator(user);
		addUser(solrMap,user,isAdd);
		addIp(solrMap,user);
		addLimit(solrMap,user);
		addRole(solrMap,user);
		Date date=SolrThread.getDate();
		solrMap.put("CreateTime", date);
		solrMap.put("UpdateTime", date);
		List<Map<String, Object>> solrList=new ArrayList<>();
		solrList.add(solrMap);
		SolrThread mt = new SolrThread(null,solrList,null);
        Thread t1 = new Thread(mt,"solr线程");
        t1.start();
	}
	/*
	 * 全库更新机构管理员
	 */
	private static void updateAllAdministrator(InstitutionalUser user) {
		log.info("全库更新机构管理员信息");
		Map<String,Object> solrMap=new LinkedHashMap<>();
		//查找同一个机构管理员的所有机构信息
		SolrQuery solrquery=new SolrQuery();
		solrquery.setQuery("AdministratorId:("+user.getAdminOldName()+")");
		try {
			SolrDocumentList result=SolrService.getDataList(solrquery);
			log.info("该机构管理员信息需要更新"+result.size()+"条信息");
			List<SolrInputDocument> sdList=new ArrayList<SolrInputDocument>();
			//更新机构用户中的机构管理员信息
			for (SolrDocument solrDocument : result) {
				SolrInputDocument input=new SolrInputDocument();
				input.addField("Id", solrDocument.get("Id"));				
				//局部更新机构管理员密码
				Map<String, String> partialUpdatePS = new HashMap<String, String>();
				partialUpdatePS.put("set", PasswordHelper.encryptPassword(user.getAdminpassword()));
				input.addField("AdministratorPassword", partialUpdatePS);		
				//局部更新机构管理员Email
				Map<String, String> partialUpdateEM = new HashMap<String, String>();
				partialUpdateEM.put("set", user.getAdminEmail());
				input.addField("AdministratorEmail", partialUpdateEM);
				//局部更新机构管理员IP
				Map<String, String> partialUpdateIP = new HashMap<String, String>();
				partialUpdateIP.put("set", user.getAdminIP());
				input.addField("AdministratorOpenIP", partialUpdateIP);
				sdList.add(input);
			}
			SolrThread mt = new SolrThread(sdList,null,null);
	        Thread t1 = new Thread(mt,"solr线程");
	        t1.start();
		} catch (Exception e) {
			log.info("全库更新机构管理员信息出错",e);
		}
	}
	//将用户提交数据放入ImportSolrRequest
	private static void swapParam(InstitutionalUser user, ImportSolrRequest request) throws Exception{
		swapUser(user,request);
		swapIp(user,request);
		swpLimit(user,request);
	}

	//交换用户信息
	private static void swapUser(InstitutionalUser user, ImportSolrRequest request) throws Exception{
		request.getUser().setId(user.getUserId());
		String password="";
		if (StringUtils.isNotBlank(user.getPassword())) {
			password = PasswordHelper.encryptPassword(user.getPassword());
		} else {
			password = PasswordHelper.encryptPassword("666666");
		}
		request.getUser().setPassword(password);
		request.getUser().setInstitution(user.getInstitution());
		request.getUser().setUserType("2");
		
		String pid =null;
		if (StringUtils.isNotBlank(user.getAdminname())) {
			pid = user.getAdminname();
		} else if (StringUtils.isNotBlank(user.getAdminOldName())) {
			pid = user.getAdminOldName();
		}
		request.getUser().setParentId(pid);
		request.getUser().setLoginMode(user.getLoginMode());
		request.getUser().setIsFreeze(false);
		if(!"".equals(pid)){
			request.getAdmin().setAdministratorId(pid);
			request.getAdmin().setAdministratorEmail(user.getAdminEmail());
			request.getAdmin().setAdministratorPassword(PasswordHelper.encryptPassword(user.getAdminpassword()));
			request.getAdmin().setAdministratorOpenIP(user.getAdminIP());
		}else{
			request.getAdmin().setAdministratorId(null);
			request.getAdmin().setAdministratorEmail(null);
			request.getAdmin().setAdministratorPassword(null);
			request.getAdmin().setAdministratorOpenIP(null);
		}
		//GroupInfo
		request.getUser().setOrderType(user.getOrderType());
		request.getUser().setOrderContent(user.getOrderContent());
		request.getUser().setCountryRegion(user.getCountryRegion());
		request.getUser().setPostCode(user.getPostCode());
		request.getUser().setOrganization(user.getOrganization());
	}
	
	//交换Ip信息
	private static void swapIp(InstitutionalUser user, ImportSolrRequest request) throws Exception{
		//ip
		if (user.getLoginMode().equals("0") || user.getLoginMode().equals("2")) {
			String[] arr_ip = user.getIpSegment().split("\r\n");
			Long StartIP=Long.MAX_VALUE;
			Long EndIP=0L;
			List<String> IPList=new ArrayList<>();
			for(String ip : arr_ip){
				if(ip.contains("-")){				
					long begin=IPConvertHelper.IPToNumber(ip.substring(0, ip.indexOf("-")));
					StartIP=StartIP>begin?begin:StartIP;
					long end=IPConvertHelper.IPToNumber(ip.substring(ip.indexOf("-")+1, ip.length()));
					EndIP=EndIP>end?EndIP:end;
					IPList.add(ip);
				}
			}
			request.getUser().setStartIP(StartIP);
			request.getUser().setEndIP(EndIP);
			request.getUser().setOpenIP(IPList);
		}else{
			request.getUser().setStartIP(null);
			request.getUser().setEndIP(null);
			request.getUser().setOpenIP(null);
		}
	}
	
	//交换权限
	private static void swpLimit(InstitutionalUser user, ImportSolrRequest request) throws Exception{

		//PayChannelId
		List<ResourceDetailedDTO> rdList=user.getRdlist();
		Map<String,String> ProjectMap=new HashMap<String,String>();
		Map<String,String> IsTrialMap=new HashMap<String,String>();
		for(ResourceDetailedDTO dto:rdList){
			ProjectMap.put(dto.getProjectid(), dto.getProjectid());
			if(StringUtils.equals(dto.getMode(), "trical")){
				IsTrialMap.put(dto.getProjectid(), dto.getProjectid());
			}
		}
		request.getUser().setPayChannelId(ProjectMap.values());
		request.getUser().setIsTrial(IsTrialMap.values());
		//子账号权限
		request.getUser().setHasChildGroup(user.getUpperlimit()==null?false:true);
		request.getUserRole().setChildGroupLimit(user.getUpperlimit());
		request.getUserRole().setChildGroupConcurrent(user.getsConcurrentnumber());
		request.getUserRole().setChildGroupDownloadLimit(user.getDownloadupperlimit());
		request.getUserRole().setChildGroupPayment( user.getChargebacks());
		request.getUserRole().setGroupConcurrent(user.getpConcurrentnumber());
		
		//统计分析
		String tongji = user.getTongji()==null?"":user.getTongji();
		List<String> ls=new ArrayList<>();
		if(tongji.contains("database_statistics")){
			ls.add("database_statistics");
		}
		if(tongji.contains("resource_type_statistics")){
			ls.add("resource_type_statistics");
		}
		request.getUser().setStatisticalAnalysis(ls);
	}

	//添加用户信息
	private static void addUser(Map<String,Object> solrMap,InstitutionalUser user,boolean isAdd) throws Exception{
		solrMap.put("Id", user.getUserId());
		String password = "";
		if (StringUtils.isNotBlank(user.getPassword())) {
			password = PasswordHelper.encryptPassword(user.getPassword());
		} else {
			password = PasswordHelper.encryptPassword("666666");
		}
		solrMap.put("Password", password);
		solrMap.put("Institution", user.getInstitution());
		solrMap.put("UserType", "2");
		String pid = "";
		if (StringUtils.isNotBlank(user.getAdminname())) {
			pid = user.getAdminname();
		} else if (StringUtils.isNotBlank(user.getAdminOldName())) {
			pid = user.getAdminOldName();
		}
		solrMap.put("ParentId", pid);
		solrMap.put("LoginMode", user.getLoginMode());
		if(isAdd){
			solrMap.put("IsFreeze", false);
		}
		if(!"".equals(pid)){
			solrMap.put("AdministratorId", pid);
			solrMap.put("AdministratorEmail", user.getAdminEmail());
			solrMap.put("AdministratorPassword", PasswordHelper.encryptPassword(user.getAdminpassword()));
			solrMap.put("AdministratorOpenIP", user.getAdminIP());
		}else{
			solrMap.put("AdministratorId", null);
			solrMap.put("AdministratorEmail", null);
			solrMap.put("AdministratorPassword", null);
			solrMap.put("AdministratorOpenIP", null);
		}
		//GroupInfo
		solrMap.put("OrderType",user.getOrderType());
		solrMap.put("OrderContent", user.getOrderContent());
		solrMap.put("CountryRegion",user.getCountryRegion());
		solrMap.put("PostCode", user.getPostCode());
		solrMap.put("Organization",user.getOrganization());
	}
	//添加Ip
	private static void addIp(Map<String,Object> solrMap,InstitutionalUser user) throws Exception{
		//ip
		if (user.getLoginMode().equals("0") || user.getLoginMode().equals("2")) {
			String[] arr_ip = user.getIpSegment().split("\r\n");
			long StartIP=Long.MAX_VALUE;
			long EndIP=0;
			List<String> IPList=new ArrayList<>();
			for(String ip : arr_ip){
				if(ip.contains("-")){				
					long begin=IPConvertHelper.IPToNumber(ip.substring(0, ip.indexOf("-")));
					StartIP=StartIP>begin?begin:StartIP;
					long end=IPConvertHelper.IPToNumber(ip.substring(ip.indexOf("-")+1, ip.length()));
					EndIP=EndIP>end?EndIP:end;
					IPList.add(ip);
				}
			}
			if(EndIP>0){
				solrMap.put("StartIP", StartIP);
				solrMap.put("EndIP", EndIP);
				solrMap.put("OpenIP", IPList);
			}
		}else{
			solrMap.put("StartIP", null);
			solrMap.put("EndIP", null);
			solrMap.put("OpenIP", null);
		}
	}
	//添加权限
	private static void addLimit(Map<String,Object> solrMap,InstitutionalUser user) throws Exception{
		//PayChannelId
		List<ResourceDetailedDTO> rdList=user.getRdlist();
		Map<String,String> ProjectMap=new HashMap<String,String>();
		Map<String,String> IsTrialMap=new HashMap<String,String>();
		for(ResourceDetailedDTO dto:rdList){
			ProjectMap.put(dto.getProjectid(), dto.getProjectid());
			if(StringUtils.equals(dto.getMode(), "trical")){
				IsTrialMap.put(dto.getProjectid(), dto.getProjectid());
			}
		}
		solrMap.put("PayChannelId", ProjectMap.values());
		solrMap.put("IsTrial", IsTrialMap.values());
		//子账号权限
		boolean HasChildGroup=user.getUpperlimit()==null?false:true;
		solrMap.put("HasChildGroup", HasChildGroup);
		solrMap.put("ChildGroupLimit",user.getUpperlimit());
		solrMap.put("ChildGroupConcurrent", user.getsConcurrentnumber());
		solrMap.put("ChildGroupDownloadLimit", user.getDownloadupperlimit());
		solrMap.put("ChildGroupPayment", HasChildGroup?user.getChargebacks():null);
		solrMap.put("GroupConcurrent", user.getpConcurrentnumber());
		
		//统计分析
		String tongji = user.getTongji()==null?"":user.getTongji();
		Map<String, Object> operation = new HashMap<>();
		List<String> ls=new ArrayList<>();
		if(tongji.contains("database_statistics")){
			ls.add("database_statistics");
		}
		if(tongji.contains("resource_type_statistics")){
			ls.add("resource_type_statistics");
		}
		operation.put("set", ls);
		solrMap.put("StatisticalAnalysis",operation);
	}
	//添加角色
	private static void addRole(Map<String,Object> solrMap,InstitutionalUser user) throws Exception{
		//openapp
		if(!StringUtils.isEmpty(user.getOpenApp())){
			solrMap.put("AppStartTime", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getAppBegintime())));
			solrMap.put("AppEndTime", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getAppEndtime())));
		}else{
			solrMap.put("AppStartTime", null);
			solrMap.put("AppEndTime", null);
		}
		//WeChat
		if(!StringUtils.isEmpty(user.getOpenWeChat())){
			solrMap.put("WeChatStartTime", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getWeChatBegintime())));
			solrMap.put("WeChatEndTime", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getWeChatEndtime())));
			solrMap.put("Email4WeChat", user.getWeChatEamil());
		}else{
			solrMap.put("WeChatStartTime", null);
			solrMap.put("WeChatEndTime", null);
			solrMap.put("Email4WeChat", null);
		}
		//PartyAdmin
		if(!StringUtils.isEmpty(user.getPartyLimit())){
			solrMap.put("PartyAdminStartTIme", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getPartyBegintime())));
			solrMap.put("PartyAdminEndTIme", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getPartyEndtime())));
			solrMap.put("PartyAdminId", user.getPartyAdmin());
			solrMap.put("PartyAdminPassword", PasswordHelper.encryptPassword(user.getPartyPassword()));
			solrMap.put("PartyAdminTrial", "isTrial".equals(user.getIsTrial())?true:false);
		}else{
			solrMap.put("PartyAdminStartTIme", null);
			solrMap.put("PartyAdminEndTIme", null);
			solrMap.put("PartyAdminId", null);
			solrMap.put("PartyAdminPassword", null);
			solrMap.put("PartyAdminTrial", null);
		}
	}
	
	public static void updateInfo(InstitutionalUser user) throws Exception{
		Map<String,Object> solrMap=new LinkedHashMap<>();
		updateAllAdministrator(user);
		updateUser(solrMap,user);
		updateIp(solrMap,user);
		updateLimit(solrMap,user);
		updateRole(solrMap,user);
		Date date=SolrThread.getDate();
		solrMap.put("UpdateTime", date);
		List<Map<String, Object>> solrList=new ArrayList<>();
		solrList.add(solrMap);
		SolrThread mt = new SolrThread(null,null,solrList);
        Thread t1 = new Thread(mt,"solr线程");
        t1.start();
	}
	
	//设置user
	private static void updateUser(Map<String,Object> solrMap,InstitutionalUser user) throws Exception{
		solrMap.put("Id", user.getUserId());
		if (StringUtils.isNotBlank(user.getPassword())) {
			String password = PasswordHelper.encryptPassword(user.getPassword());
			solrMap.put("Password", password);
		}
		solrMap.put("Institution", user.getInstitution());
		solrMap.put("UserType", "2");
		String pid="";
		if (StringUtils.isNotBlank(user.getAdminname())) {
			pid=user.getAdminname();
		} else if (StringUtils.isNotBlank(user.getAdminOldName())) {
			pid=user.getAdminOldName();
		}
		if(!StringUtils.isEmpty(pid)){
			solrMap.put("ParentId", pid);
		}
		
		//GroupInfo
		if(!StringUtils.isEmpty(user.getOrderType())){
			solrMap.put("OrderType",user.getOrderType());
			solrMap.put("OrderContent", user.getOrderContent());
		}
		if(!StringUtil.isEmpty(user.getPostCode())){
			solrMap.put("CountryRegion",user.getCountryRegion());
			solrMap.put("PostCode", user.getPostCode());
		}
		if(!StringUtils.isEmpty(user.getOrganization())){
			solrMap.put("Organization",user.getOrganization());
		}
		
		solrMap.put("LoginMode", user.getLoginMode());
		if(StringUtils.isEmpty(pid)){
			solrMap.put("AdministratorId", "");
			solrMap.put("AdministratorEmail", "");
			solrMap.put("AdministratorPassword", "");
			solrMap.put("AdministratorOpenIP", "");
		}		
		if(!StringUtils.isEmpty(pid)){
			solrMap.put("AdministratorId", pid);
			if(!StringUtils.isEmpty(user.getAdminEmail())){
			solrMap.put("AdministratorEmail", user.getAdminEmail());
			}
			if(!StringUtils.isEmpty(user.getAdminpassword())){
				solrMap.put("AdministratorPassword", PasswordHelper.encryptPassword(user.getAdminpassword()));
			}
			log.info("88888888888888888888888user.getAdminIp="+user.getAdminIP());
			solrMap.put("AdministratorOpenIP", user.getAdminIP());
		}
	}
	
	//设置IP
	private static void updateIp(Map<String,Object> solrMap,InstitutionalUser user) throws Exception{
		if (user.getLoginMode().equals("0") || user.getLoginMode().equals("2")) {
			String[] arr_ip = user.getIpSegment().split("\r\n");
			long StartIP=Long.MAX_VALUE;
			long EndIP=0;
			List<String> IPList=new ArrayList<>();
			for(String ip : arr_ip){
				if(ip.contains("-")){				
					long begin=IPConvertHelper.IPToNumber(ip.substring(0, ip.indexOf("-")));
					StartIP=StartIP>begin?begin:StartIP;
					long end=IPConvertHelper.IPToNumber(ip.substring(ip.indexOf("-")+1, ip.length()));
					EndIP=EndIP>end?EndIP:end;
					IPList.add(ip);
				}
			}
			solrMap.put("StartIP", StartIP);
			solrMap.put("EndIP", EndIP);
			solrMap.put("OpenIP", IPList);
		}
	}
	
	//设置权限
	private static void updateLimit(Map<String,Object> solrMap,InstitutionalUser user) throws Exception{
		//PayChannelId
		List<ResourceDetailedDTO> rdList=user.getRdlist();
		Map<String,String> ProjectMap=new HashMap<String,String>();
		Map<String,String> IsTrialMap=new HashMap<String,String>();
		for(ResourceDetailedDTO dto:rdList){
			ProjectMap.put(dto.getProjectid(), dto.getProjectid());
			if(StringUtils.equals(dto.getMode(), "trical")){
				IsTrialMap.put(dto.getProjectid(), dto.getProjectid());
			}
		}
		solrMap.put("PayChannelId", ProjectMap.values());
		solrMap.put("IsTrial", IsTrialMap.values());
		//子账号权限
		boolean limit=user.getUpperlimit()==null?false:true;
		solrMap.put("HasChildGroup",limit);
		if(limit){
			solrMap.put("ChildGroupLimit",user.getUpperlimit());
			solrMap.put("ChildGroupConcurrent", user.getsConcurrentnumber());
			solrMap.put("ChildGroupDownloadLimit", user.getDownloadupperlimit());
			solrMap.put("ChildGroupPayment", user.getChargebacks());
		}
		
		if(user.getpConcurrentnumber()!=null){
			solrMap.put("GroupConcurrent", user.getpConcurrentnumber());
		}
		//统计分析
		String tongji = user.getTongji()==null?"":user.getTongji();
		List<String> ls=new ArrayList<>();
		if(tongji.contains("database_statistics")){
			ls.add("database_statistics");
		}
		if(tongji.contains("resource_type_statistics")){
			ls.add("resource_type_statistics");
		}
		if(ls.size()>0){
			solrMap.put("StatisticalAnalysis",ls);
		}
	}
	
	//设置角色
	private static void updateRole(Map<String,Object> solrMap,InstitutionalUser user) throws Exception{
		//openapp
		if(!StringUtils.isEmpty(user.getOpenApp())){
			solrMap.put("AppStartTime", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getAppBegintime())));
			solrMap.put("AppEndTime", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getAppEndtime())));
		}
		//WeChat
		if(!StringUtils.isEmpty(user.getOpenWeChat())){
			solrMap.put("WeChatStartTime", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getWeChatBegintime())));
			solrMap.put("WeChatEndTime", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getWeChatEndtime())));
			solrMap.put("Email4WeChat", user.getWeChatEamil());
		}
		//PartyAdmin
		if(!StringUtils.isEmpty(user.getPartyLimit())){
			solrMap.put("PartyAdminStartTIme", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getPartyBegintime())));
			solrMap.put("PartyAdminEndTIme", DateUtil.DateToFromatStr(DateUtil.stringToDate1(user.getPartyEndtime())));
			solrMap.put("PartyAdminId", user.getPartyAdmin());
			solrMap.put("PartyAdminPassword", PasswordHelper.encryptPassword(user.getPartyPassword()));
			solrMap.put("PartyAdminTrial", "isTrial".equals(user.getIsTrial())?true:false);
		}
	}
	
	private static Date getDate(){
		return DateUtil.stringToDate(DateUtil.dateToString(new Date()));
	}
}