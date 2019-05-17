package com.wf.controller;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.citrus.util.StringUtil;
import com.sun.jndi.toolkit.url.Uri;
import com.utils.AuthorityLimit;
import com.utils.DateUtil;
import com.utils.InstitutionUtils;
import com.utils.Organization;
import com.utils.SettingUtil;
import com.wanfangdata.encrypt.PasswordHelper;
import com.wf.bean.Authority;
import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.bean.Query;
import com.wf.bean.UserInstitution;
import com.wf.bean.WarningInfo;
import com.wf.bean.WfksAccountidMapping;
import com.wf.bean.WfksUserSetting;
import com.wf.service.AheadUserService;
import com.wf.service.OpreationLogsService;
import com.wf.service.PersonService;
/**
 * 
 * @author wangguosheng
 * @模块描述: 机构用户Controller
 *
 */
@Controller
@RequestMapping("group")
public class GroupController {
	@Autowired
	private AheadUserService aheadUserService;
	@Autowired
	private PersonService personservice;
	@Autowired
	private OpreationLogsService opreationLogs;
	
	private static Logger log = Logger.getLogger(GroupController.class);
	//二维码邮箱是否勾选
	private static String DEFAULT = "default";
	private static String UNSELECT = "unselect";
	private String[] channelid=new String[]{"GBalanceLimit","GTimeLimit"};
	private static Properties pro;
	static{
		try {
			if(pro==null){
				pro=new Properties();
				InputStream in = null;
		    	String path = Thread.currentThread().getContextClassLoader().getResource("").getFile() + "enterpriseUrl.properties";
		    	path = java.net.URLDecoder.decode(path, "UTF-8");
				in = new FileInputStream(path);
				pro.load(in);
				}
		} catch (Exception e) {
			log.error("无法加载配置文件enterpriseUrl.properties", e);
    		throw new IllegalStateException("无法加载配置文件enterpriseUrl.properties");
		}
	}
	
	/**
	 *	机构用户信息管理查询
	 */
	@RequestMapping("index")
	public ModelAndView toInformation(String userId,String ipSegment,String institution,String adminname,
			String adminIP,String pageNum,String pageSize,String start_time,String end_time){
		ModelAndView view = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("ipSegment", ipSegment);
		map.put("institution", institution);
		map.put("adminname", adminname);
		map.put("adminIP", adminIP);
		map.put("pageNum", (Integer.parseInt(pageNum==null?"1":pageNum)-1)*Integer.parseInt((pageSize==null?"1":pageSize)));
		map.put("pageSize", Integer.parseInt(pageSize==null?"10":pageSize));
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		map.put("isSimple", "0");
		if(ipSegment!=null&&!ipSegment.equals("")){			
			map.put("ipSegment", ipSegment);
		}
		if(adminIP!=null&&!adminIP.equals("")){			
			map.put("adminIP", adminIP);
		}
		view.addObject("map", map);
		view.addObject("arrayArea", SettingUtil.getRegionCode());//地区地区
		view.addObject("org", Organization.values());//机构账户类型
		view.addObject("Authority", AuthorityLimit.values());//权限
		view.addObject("msg", "0");	
		view.setViewName("/page/usermanager/ins_information");
		return view;
	}
	/**
	 *	机构用户信息管理列表
	 */
	@RequestMapping("list")
	public ModelAndView information(Query query,HttpServletRequest request){
		long time=System.currentTimeMillis();
		ModelAndView view = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			view.addObject("arrayArea", SettingUtil.getRegionCode());//地区地区
			view.addObject("org", Organization.values());//机构账户类型
			view.addObject("Authority", AuthorityLimit.values());//权限
			if(!InstitutionUtils.validateQuery(map,query)){
				view.setViewName("/page/usermanager/ins_information");
				view.addObject("map", map);
				return view;
			}
			String userId=map.get("userId")==null?"":map.get("userId").toString();
			if(!StringUtils.isEmpty(userId)){
				Person person=aheadUserService.queryPersonInfo(userId);
				if (person == null || person.getUsertype() == 0 || person.getUsertype() == 3) {
					view.setViewName("/page/usermanager/ins_information");
					map.put("ipSegment", query.getIpSegment());
					view.addObject("map", map);
					return view;
				}
				if(person.getUsertype()==1){
					map.put("pid", userId);
					map.put("userId", "");
				}else if(person.getUsertype()==4){
					String json=person.getExtend();
					if(!StringUtils.isEmpty(json)){
						JSONObject obj = JSONObject.fromObject(json);
						map.put("userId", String.valueOf(obj.get("RelatedGroupId")));
					}
				}
			}
			PageList pageList = aheadUserService.findListInfo(map);
			pageList.setPageNum(Integer.parseInt(map.get("pageNum").toString())+1);//当前页
			pageList.setPageSize(Integer.parseInt(map.get("pageSize").toString()));//每页显示的数量
			if(!StringUtil.isEmpty(query.getIpSegment())){
				map.put("ipSegment", query.getIpSegment());
			}
			if (!StringUtils.isEmpty(query.getInstitution())) {
				map.put("institution", query.getInstitution());
			}
			map.put("userId", userId);
			map.put("pageList", pageList);
		}catch(Exception e){
			log.error("机构用户查询异常：", e);
		}
		view.addObject("map", map);
		view.addObject("timelimit",DateUtil.getTimeLimit());
		log.info("本地查询机构用户信息耗时："+(System.currentTimeMillis()-time)+"ms");
		view.setViewName("/page/usermanager/ins_information");
		return view;
	}
	/**
	 *	机构用户注册跳转
	 */
	@RequestMapping("register")
	public ModelAndView register(HttpServletResponse httpResponse){
		ModelAndView view = new ModelAndView();
		view.addObject("arrayArea", SettingUtil.getRegionCode());
		view.addObject("org", Organization.values());//机构账户类型
		view.setViewName("/page/usermanager/ins_register");
		return view;
	}
	
	/**
	 *	账号修改页面返显
	 */
	@RequestMapping("modify")
	public ModelAndView numUpdate(String userId) throws Exception{
		ModelAndView view = new ModelAndView();
		Map<String, Object> map = aheadUserService.findListInfoById(userId);
		if(map.get("pid")!=null&&StringUtils.isNoneBlank(map.get("pid").toString())){  
			Map<String, Object> m = aheadUserService.findInfoByPid(map.get("pid").toString());
			map.put("adminname", m.get("userId"));
			map.put("adminpassword", m.get("password"));
			map.put("adminIP", m.get("adminIP"));
			map.put("adminEmail", m.get("adminEmail"));
			List<Map<String, Object>> admin=personservice.getAllInstitutional(String.valueOf(map.get("institution")));
			view.addObject("admin", admin);
		}
		//用户权限
		getWfksAccountidLimit(userId,view);
		//获取党建管理员
		getPartyAdmin(userId,view);
		//数据分析权限
		UserInstitution ins = aheadUserService.getUserInstitution(userId);
		String tongji="";
		if(ins!=null){
			String json = ins.getStatisticalAnalysis();
			JSONObject obj = JSONObject.fromObject(json);
			if (obj.getInt("database_statistics") == 1) {
				tongji += "database_statistics";
			}
			if (obj.getInt("resource_type_statistics") == 1) {
				if (tongji.length() > 0) {
					tongji += ",";
				}
				tongji += "resource_type_statistics";
			}
		}
		view.addObject("tongji",tongji);
		//个人绑定机构权限
		view.addObject("bindInformation",aheadUserService.getBindAuthority(userId));
		List<Map<String, Object>> proList=aheadUserService.getProjectInfo(userId);
		map.put("proList", proList);
		map.put(DEFAULT,UNSELECT);
		//余额转限时，限时转余额
		limitChange(proList,view);
		view.addObject("timelimit",DateUtil.getTimeLimit());
		view.addObject("arrayArea", SettingUtil.getRegionCode());
		view.addObject("org", Organization.values());
		view.addObject("groupInfo", aheadUserService.getGroupInfo(userId));
		view.addObject("map",map);
		view.setViewName("/page/usermanager/ins_numupdate");
		//日志打印机构用户修改前的购买资源信息
		Map<String,Object> jgmap=(Map<String, Object>) view.getModel().get("map");	
		List<Map<String, Object>> gmxmlist=(List<Map<String, Object>>) jgmap.get("proList");
		for (Map<String, Object> entry : gmxmlist) {
			log.info(jgmap.get("userId")+" - '"+entry.get("name")+"'的余额为:"+entry.get("balance"));	
		}
		return view;
	}
	//获取机构用户权限表1
		private void getWfksAccountidLimit(String userId,ModelAndView view){
			WfksAccountidMapping[] mapping = aheadUserService.getWfksAccountidLimit(userId, "Limit");
			Map<String, WfksAccountidMapping> map = new HashMap<String, WfksAccountidMapping>();
			for (WfksAccountidMapping wfks : mapping) {
				if (!"Limit".equals(wfks.getIdAccounttype())) {
					continue;
				}
				if ("trical".equals(wfks.getRelatedidAccounttype())) {
					continue;
				}
				if (wfks.getBegintime() != null) {
					wfks.setBegin(DateUtil.dateToString2(wfks.getBegintime()));
				}
				if (wfks.getEndtime() != null) {
					wfks.setEnd(DateUtil.dateToString2(wfks.getEndtime()));
				}
				if ("openWeChat".equals(wfks.getRelatedidAccounttype())) {
					WfksUserSetting[] setting = aheadUserService.getUserSetting(userId, "WeChat");
					for (WfksUserSetting wf : setting) {
						view.addObject(wf.getPropertyName(), wf.getPropertyValue());
						break;
					}
				}
				map.put(wfks.getRelatedidAccounttype(), wfks);
			}
			view.addObject("limit", map);
		}
		//获得党建信息
		private void getPartyAdmin(String userId, ModelAndView view) {
			WfksAccountidMapping[] mapping = aheadUserService.getWfksAccountid(userId, "PartyAdminTime");
			if(mapping==null||mapping.length==0){
				return;
			}
			Authority author=new Authority();
			Person per = personservice.findById(mapping[0].getRelatedidKey());
			if(per==null){
				return;
			}
			author.setPartyAdmin(per.getUserId());
			author.setBegintime(DateUtil.dateToString2(mapping[0].getBegintime()));
			author.setEndtime(DateUtil.dateToString2(mapping[0].getEndtime()));
			try {
				author.setPassword(PasswordHelper.decryptPassword(per.getPassword()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			String json = String.valueOf(per.getExtend());
			if(!StringUtils.isEmpty(json)){
				JSONObject obj = JSONObject.fromObject(json);
				String id=String.valueOf(obj.get("RelatedGroupId"));
				author.setUserId(id);
				author.setTrial(obj.getBoolean("IsTrialPartyAdminTime"));
			}
			view.addObject("party",author);
		
		}
		//余额转限时，限时转余额
		private void limitChange(List<Map<String, Object>> proList,ModelAndView view){
			Map<String,String> changeMap=new HashMap<String,String>();
			changeMap.put(channelid[0], channelid[0]);
			changeMap.put(channelid[1], channelid[1]);
			for(Map<String, Object> map:proList){
				String payChannelid=String.valueOf(map.get("payChannelid"));
				for(String str:channelid){
					if(str.equals(payChannelid)){
						changeMap.remove(payChannelid);
					}
				}
			}
			view.addObject("changeMap",changeMap);
		}
		/**
		 *	子账号列表页跳转
		 */
		@RequestMapping("childquery")
		public ModelAndView tosonaccount(HttpServletRequest req, String userId, String institution,
				String start_time, String end_time, String pageNum, String pageSize,String isBack,String goPage,String pid) {
			ModelAndView view = new ModelAndView();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("start_time",start_time);
			map.put("end_time",end_time);
			map.put("institution",institution);
			if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(institution)
					&& StringUtil.isEmpty(start_time) && StringUtil.isEmpty(end_time)) {
				view.addObject("map", map);
				map.put("userId",userId);
				map.put("isBack", isBack);
				if ("true".equals(isBack)) {
					map.put("pid", !StringUtils.isEmpty(pid) ? pid : userId);
				}
				view.setViewName("/page/usermanager/ins_sonaccount");
				return view;
			}else if(StringUtils.isEmpty(goPage)){
				pageNum=null;
				pageSize=null;
			}
			if(!StringUtils.isEmpty(userId)){
				Person person=aheadUserService.queryPersonInfo(userId);
				if(person==null||(person.getUsertype()!=3&&person.getUsertype()!=2)){
					view.addObject("map",map);
					view.addObject("msg", "0");
					map.put("userId",userId);
					map.put("isBack", isBack);
					if ("true".equals(isBack)) {
						map.put("pid", !StringUtils.isEmpty(pid) ? pid : userId);
					}
					view.setViewName("/page/usermanager/ins_sonaccount");
					return view;
				}else if(person.getUsertype()==3){
					map.put("pid","");
					map.put("userId",userId);
				}else if(person.getUsertype()==2){
					map.put("pid",userId);
					map.put("userId","");
				}
			}
			map.put("pageNum", (Integer.parseInt(pageNum==null?"1":pageNum)-1)*Integer.parseInt((pageSize==null?"1":pageSize)));
			map.put("pageSize", Integer.parseInt(pageSize==null?"20":pageSize));
			PageList pageList = aheadUserService.getSonaccount(map);
			pageList.setPageNum(Integer.parseInt(pageNum==null?"1":pageNum));//当前页
			pageList.setPageSize(Integer.parseInt(pageSize==null?"20":pageSize));//每页显示的数量
			map.put("pageList", pageList);
			map.put("userId",userId);
			map.put("isBack", isBack);
			if ("true".equals(isBack)) {
				map.put("pid", !StringUtils.isEmpty(pid) ? pid : userId);
			}
			view.addObject("map",map);
			view.addObject("msg", "0");
			view.setViewName("/page/usermanager/ins_sonaccount");
			return view;
		}
		/**
		 *	机构用户订单跳转
		 */
		@RequestMapping("order")
		public ModelAndView groupOrder(String userId){
			ModelAndView view = new ModelAndView();
			view.addObject("userId", userId);
			view.setViewName("/page/usermanager/group_pay_order");
			return view;
		}
		  /**
	     * 跳转机构子账号信息管理页面
	     *
	     * @param userId 用户Id（此参数用于判断是否由信息管理页面跳转而来）
	     * @param model
	     * @return
	     */
	    @RequestMapping("/personbind")
	    public String toBindInfoManagement(String userId, Model model) {

	        if (userId != null && !"".equals(userId)) {
	            model.addAttribute("userId", userId);
	            model.addAttribute("upPage", true);
	        } else {
	            model.addAttribute("upPage", null);
	        }
	        model.addAttribute("pager", null);
	        return "/page/usermanager/user_binding_manager";
	    }
	    /**
		 *	机构用户批量注册跳转
		 */
		@RequestMapping("batchregister")
		public ModelAndView batchRegister(HttpServletRequest request){
			ModelAndView view = new ModelAndView();
			view.addObject("arrayArea", SettingUtil.getRegionCode());//区域
			view.addObject("org", Organization.values());//机构账户类型
			view.setViewName("/page/usermanager/ins_batchregister");
			return view;
		}

		/**
		 *	机构用户批量修改
		 */
		@RequestMapping("batchmodify")
		public ModelAndView batchUpdate(){
			ModelAndView view = new ModelAndView();
			view.addObject("org", Organization.values());//机构账户类型
			view.setViewName("/page/usermanager/ins_batchupdate");
			return view;
		}
		/**
		 *	批量冻结和解冻跳转
		 */
		@RequestMapping("batchblock")
		public ModelAndView batchblock(){
			ModelAndView view = new ModelAndView();
			view.setViewName("/page/usermanager/ins_batchblock");
			return view;
		}
		/**
		 *	操作记录
		 */
		@RequestMapping("operatelog")
		public ModelAndView opration(String pageNum, String pageSize, String startTime, String endTime,
				String userId, String person, String projectId) {
			ModelAndView view = new ModelAndView();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pageNum", (Integer.parseInt(pageNum==null?"1":pageNum)-1)*Integer.parseInt((pageSize==null?"1":pageSize)));
			map.put("pageSize", Integer.parseInt(pageSize==null?"10":pageSize));
			if(!StringUtils.isEmpty(startTime)){
				map.put("startTime", startTime);
			}
			if(!StringUtils.isEmpty(endTime)){
				map.put("endTime", endTime);
			}
			if(!StringUtils.isEmpty(person)){
				map.put("person", person);
			}
			if(!StringUtils.isEmpty(userId)){
				map.put("userId", userId);	
			}
			if(!StringUtils.isEmpty(projectId)){
				map.put("projectId", projectId);
			}
			PageList pageList = opreationLogs.selectOperationLogs(map);
			pageList.setPageNum(Integer.parseInt(pageNum==null?"1":pageNum));//当前页
			pageList.setPageSize(Integer.parseInt(pageSize==null?"10":pageSize));//每页显示的数量
			map.put("pageList", pageList);
			view.addObject("map", map);
			if(pageList.getTotalRow()>0){
				for(Object obj:pageList.getPageRow()){
					Map<String,Object> mm=(Map<String, Object>) obj;
					String str=String.valueOf(mm.get("reason"));
					JSONObject json = JSONObject.fromObject(str);
					String type=String.valueOf(json.get("projectType"));
					if(type.equals("balance")){
						mm.put("projectType", "余额");
						mm.put("totalMoney", json.get("totalMoney"));
					}else if(type.equals("count")){
						mm.put("projectType", "次数");
						mm.put("purchaseNumber", json.get("purchaseNumber"));
					}else if(type.equals("time")){
						mm.put("projectType", "限时");
					}
					mm.put("validityEndtime", json.get("validityEndtime"));
					mm.put("validityStarttime", json.get("validityStarttime"));
					mm.put("projectname", json.get("projectname"));
				}
			}
			List<Map<String,String>> project=opreationLogs.getProjectByUserId(userId);
			view.addObject("project", project);//获取用户购买项目
			view.setViewName("/page/usermanager/ins_oprationrecord");
			return view;
		}
		/**
		 *	机构用户预警设置
		 */
		@RequestMapping("warn")
		public ModelAndView warning(){
			ModelAndView view = new ModelAndView();
			WarningInfo war = aheadUserService.findWarning();
			view.addObject("war",war);
			view.setViewName("/page/usermanager/ins_warning");
			return view;
		}
		
		/**
		 *	跳转到机构数据solr发布界面
		 */
		@RequestMapping("solrsync")
		public ModelAndView toSolrData(ModelAndView view){
			view.setViewName("/page/usermanager/toSolrData");
			return view;
		}
		
		/**
		 * 企业设置跳转链接
		 */
		@RequestMapping("openEnterpriseLike")
		@ResponseBody
		public String openEnterpriseLike(String userId,String enterpriseType){
			String uri=pro.getProperty(enterpriseType)+"?accountid=Group."+userId;
			return uri;
		}
}
