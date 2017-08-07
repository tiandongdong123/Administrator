package com.wf.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import wfks.accounting.setting.PayChannelModel;

import com.redis.RedisUtil;
import com.utils.DateUtil;
import com.utils.IPConvertHelper;
import com.wanfangdata.encrypt.PasswordHelper;
import com.webservice.WebServiceUtil;
import com.wf.bean.Authority;
import com.wf.bean.CommonEntity;
import com.wf.bean.OperationLogs;
import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.ResourceLimitsDTO;
import com.wf.bean.UserIp;
import com.wf.bean.WarningInfo;
import com.wf.bean.Wfadmin;
import com.wf.bean.WfksAccountidMapping;
import com.wf.bean.WfksUserSetting;
import com.wf.service.AheadUserService;
import com.wf.service.OpreationLogsService;
import com.wf.service.PersonService;

/**
 *	@author: CuiCan
 * 	@Date: 2016-10-12
 *	@模块描述: 机构用户Controller
 */

@Controller
@RequestMapping("auser")
public class AheadUserController {
	
	private static final String LOGIN_URL = "/user/toLogin.do";

	@Autowired
	private AheadUserService aheadUserService;
	
	@Autowired
	private PersonService personservice;
	
	@Autowired
	private OpreationLogsService opreationLogs;
	
	/**
	 *	判断ip段是否重复
	 */
	@RequestMapping("validateip")
	@ResponseBody
	public JSONObject validateIp(String ip,String userId){
		JSONObject map = new JSONObject();
		StringBuffer sb = new StringBuffer();
		StringBuffer sbf = new StringBuffer();
		String [] str = ip.split("\n");
		//校验<文本框内>是否存在IP重复
/*		for(int i = 0; i < str.length; i++){		
			for(int j = 0; j < str.length; j++){
				if(i!=j && StringUtils.isNoneBlank(str[i]) && StringUtils.isNoneBlank(str[j])){					
					String beginIp = str[i].substring(0, str[i].indexOf("-"));
					String endIp = str[i].substring(str[i].indexOf("-")+1, str[i].length());
					//比较起始IP
					boolean bool = IPConvertHelper.ipExistsInRange(beginIp, str[j]);
					//比较结束IP
					boolean bo = IPConvertHelper.ipExistsInRange(endIp, str[j]);
					if(bool || bo){
						set.add(str[i]+"</br>"+str[j]+"</br>");
					}
				}
			}
		}
		if(set.size()>0){
			map.put("flag", "true");
			map.put("userId", "用户ID："+userId);
			map.put("errorIP", set.toString().replace("[", "").replace("]", ""));
			map.put("tableIP", "");
			return map;
		}else{*/		
			//校验<数据库>是否存在IP重复
			for(int i = 0; i < str.length; i++){		
				String beginIp = str[i].substring(0, str[i].indexOf("-"));
				String endIp = str[i].substring(str[i].indexOf("-")+1, str[i].length());
				List<UserIp> bool = aheadUserService.validateIp(userId,IPConvertHelper.IPToNumber(beginIp),IPConvertHelper.IPToNumber(endIp));
				if(bool.size()>0){
					map.put("flag", "true");
					map.put("userId", "用户ID："+userId);
					sbf.append(str[i]+"</br>");
					map.put("errorIP", sbf.toString());
					for(UserIp userIp : bool){
						sb.append(userIp.getUserId()+","+IPConvertHelper.NumberToIP(userIp.getBeginIpAddressNumber())
						+"-"+IPConvertHelper.NumberToIP(userIp.getEndIpAddressNumber())+"</br>");
					}
					map.put("tableIP", sb.toString());
				}
			}
		//}
		if(map.size()<=0){
			map.put("flag", "false");
		}
		return map;
	}
	
	
	/**
	 *	查询机构管理员信息
	 */
	@RequestMapping("findadmin")
	@ResponseBody
	public Map<String,Object> findAdmin(String pid){
		return aheadUserService.findInfoByPid(pid);
	}
	
	
	/**
	 *	查询相似机构管理员
	 */
	@RequestMapping("getadminname")
	@ResponseBody
	public List<String> getAdminName(String value){
		return null;
		//return aheadUserService.queryAdminName(value);
	}
	
	/**
	 *	查询相似机构名称 
	 */
	@RequestMapping("getkeywords")
	@ResponseBody
	public List<String> getkeywords(String value){
		//return null;
		return aheadUserService.queryInstitution(value);
	}
	
	
	/**
	 *	验证机构用户名是否存在
	 */
	@RequestMapping("getPersion")
	@ResponseBody
	public JSONObject getPersion(String userId){
		JSONObject object = new JSONObject();
		Person p = aheadUserService.queryPersonInfo(userId);
		String msg = aheadUserService.validateOldUser(userId);
		if(p==null && msg.equals("true")){
			object.put("flag", "true");
		}else{
			object.put("flag", "false");
		}
		return object;
	}
	
	/**
	 *	更新用户解冻/冻结状态
	 */
	@RequestMapping("setfreeze")
	@ResponseBody
	public String setFreeze(String aid,String flag){
		int uuf = aheadUserService.updateUserFreeze(aid,flag);
		if(uuf>0){
			return "true";
		}
		return "false";
	}
	
	/**
	 *	移除管理员
	 */
	@RequestMapping("deladmin")
	@ResponseBody
	public String deladmin(String aid){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", aid);
		map.put("pid", "");
		int resinfo = aheadUserService.updatePid(map);
		if(resinfo>0){
			return "true";
		}
		return null;
	}
	
	
	/**
	 *	跳转添加管理员
	 */
	@RequestMapping("goaddadmin")
	public ModelAndView go(ModelAndView view,String pid,String userId,String institution,String flag){
		if(StringUtils.isNotBlank(pid)){			
			Map<String, Object> map = aheadUserService.findInfoByPid(pid);
			view.addObject("map",map);
		}
		view.addObject("flag",flag);
		view.addObject("userId",userId);
		view.addObject("institution",institution);
		view.setViewName("/page/usermanager/add_admin");
		return view;
	}
	
	/**
	 *	添加管理员/修改
	 */
	@RequestMapping("addadmin")
	@ResponseBody
	public String addadmin(CommonEntity com,HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(com.getAdminname()) || StringUtils.isNotBlank(com.getAdminOldName())){
			if(com.getManagerType().equals("new")){				
				aheadUserService.deleteUser(com.getAdminname());
				aheadUserService.addRegisterAdmin(com);
				if(StringUtils.isNotBlank(com.getAdminIP())){
					aheadUserService.deleteUserIp(com.getAdminname());
					aheadUserService.addUserAdminIp(com);
				}
				map.put("pid", com.getAdminname());
			}else{
				map.put("pid", com.getAdminOldName());				
			}
			map.put("userId", com.getUserId());
			int resinfo = aheadUserService.updatePid(map);
			if(resinfo>0){
				return "true";
			}
		}
		return null;
	}
	
	/**
	 *	查询专利IPC分类信息
	 */
	@RequestMapping("findpatent")
	@ResponseBody
	public Map<String,Object> findPatent(String num){
		RedisUtil redis = new RedisUtil();
		String str = redis.get("PatentIPC",0);
		JSONArray array = JSONArray.fromObject(str);
		for(int i = 0; i < array.size();i++){
			JSONObject  obj = array.getJSONObject(i);
			String name = obj.getString("name");
			String id = obj.getString("value");
			obj.element("name",id+"_"+name);
			obj.put("num", num);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ztreeJson", array);
		map.put("number", num);
		return map;
	}
	
	/**
	 * 查询地方志的地区和专辑分类信息
	 */
	@RequestMapping("findGazetteer")
	@ResponseBody
	public Map<String, Object> findGazetteer(String pid,String area) {
		if (pid == null || "".equals(pid)) {
			pid = "0";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String gazetter = "";
		RedisUtil redis = new RedisUtil();
		if ("0".equals(pid)) {
			gazetter = redis.get("gazetteerTypeDIC", 13);// 专辑分类
			map.put("arrayGazetter", JSONArray.fromObject(gazetter));
		}
		JSONArray arrayArea = new JSONArray();
		String reg = redis.get("Region", 13);// 省级区域
		JSONArray region = JSONArray.fromObject(reg);
		for (int i = 0; i < region.size(); i++) {
			JSONObject obj = region.getJSONObject(i);
			if (obj.get("pid").equals(pid)) {
				arrayArea.add(obj);
			}
		}
		if (area != null && !"".equals(area)) {
			String[] city = area.split("_");
			for (int s = 0; s < city.length; s++) {
				for (int i = 0; i < region.size(); i++) {
					JSONObject obj = region.getJSONObject(i);
					if (obj.get("name").equals(city[s])) {
						JSONObject json = new JSONObject();
						json.put("id", obj.getString("id"));
						json.put("name", obj.getString("name"));
						if (s == 0) {
							map.put("sheng", json);
						} else if (s == 1) {
							map.put("shi", json);
						} else if (s == 2) {
							map.put("xian", json);
						}
					}
				}
			}
		}
		map.put("arrayArea", arrayArea);
		return map;
	}
	
	/**
	 * 查询地方志的地区
	 */
	@RequestMapping("findArea")
	@ResponseBody
	public Map<String, Object> findArea(String pid) {
		if (pid == null || "".equals(pid)) {
			pid = "0";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		RedisUtil redis = new RedisUtil();
		JSONArray arrayArea = new JSONArray();
		String area = redis.get("Region", 13);// 省级区域
		JSONArray region = JSONArray.fromObject(area);
		for (int i = 0; i < region.size(); i++) {
			JSONObject obj = region.getJSONObject(i);
			if (obj.get("pid").equals(pid)) {
				arrayArea.add(obj);
			}
		}
		map.put("arrayArea", arrayArea);
		return map;
	}
	
	/**
	 *	查询其他中图分类信息
	 */
	@RequestMapping("findsubject")
	@ResponseBody
	public Map<String,Object> findSubject(String num){
		RedisUtil redis = new RedisUtil();
		String str = redis.get("CLCDic",0);
		JSONArray array = JSONArray.fromObject(str);
		for(int i = 0; i < array.size();i++){
			JSONObject  obj = array.getJSONObject(i);
			String name = obj.getString("name");
			String id = obj.getString("value");
			obj.element("name",id+"_"+name);
			obj.put("num", num);
			
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ztreeJson", array);
		map.put("number", num);
		return map;
	}
	
	/**
	 *	机构用户预警设置
	 */
	@RequestMapping("warning")
	public ModelAndView warning(){
		ModelAndView view = new ModelAndView();
		WarningInfo war = aheadUserService.findWarning();
		view.addObject("war",war);
		view.setViewName("/page/usermanager/ins_warning");
		return view;
	}
	
	/**
	 *	机构用户预警信息提交
	 */
	@RequestMapping("getWarning")
	@ResponseBody
	public String updateWarning(String flag,Integer amountthreshold,Integer datethreshold,Integer remindtime,String remindemail,Integer countthreshold){
		int i = 0;
		if(flag.equals("1")){			
			i = aheadUserService.updateWarning(amountthreshold,datethreshold,remindtime,remindemail,countthreshold);
		}else{
			i = aheadUserService.addWarning(amountthreshold,datethreshold,remindtime,remindemail,countthreshold);
		}
		String msg = "";
		if(i>0){
			msg="true";
		}else{
			msg = "false";
		}
		return msg;
	}
	
	/**
	 * 查询所有数据库信息
	 */
	@RequestMapping("getdata")
	@ResponseBody
	public List<Map<String, Object>> findDataManage(String proid){
		List<Map<String, Object>> list = aheadUserService.selectListByRid(proid);
		if(list.size()>0){
			return list;
		}
		return null;
	}
	
	/**
	 *	机构用户注册跳转
	 */
	@RequestMapping("register")
	public ModelAndView register(HttpServletResponse httpResponse){
		ModelAndView view = new ModelAndView();
		List<PayChannelModel> list = aheadUserService.purchaseProject();
		view.addObject("project",list);
		view.setViewName("/page/usermanager/ins_register");
		return view;
	}
	
	/**
	 *	删除购买项目 
	 */
	@RequestMapping("removeproject")
	@ResponseBody
	public Map<String,String> removeproject(String payChannelid,String type,String beginDateTime,String endDateTime,String institution,
			HttpServletRequest req,HttpServletResponse res,HttpSession session,String userId,String projectname) throws Exception{
		int i = 0;
		Map<String,String> map = new HashMap<>();
		String adminId = this.checkLogin(req,res);
		CommonEntity com = new CommonEntity();
		com.setUserId(userId);
		com.setInstitution(institution);
		ResourceDetailedDTO dto = new ResourceDetailedDTO();
		dto.setProjectid(payChannelid);
		dto.setValidityStarttime(beginDateTime);
		dto.setValidityEndtime(endDateTime);
		dto.setProjectname(projectname);
		/*if(type.equals("balance")){			
			com.setResetMoney("true");
			dto.setTotalMoney(0.00);
			i = aheadUserService.chargeProjectBalance(com, dto,adminId);
		}else if(type.equals("count")){
			com.setResetCount("true");
			dto.setPurchaseNumber(0);
			i = aheadUserService.chargeCountLimitUser(com, dto,adminId);
		}else if(type.equals("time")){
			i = 1;
		}*/
		i = aheadUserService.deleteAccount(com, dto, adminId);
		if(i > 0){
			aheadUserService.deleteResources(com,dto,false);
			map.put("flag", "true");
		}else{
			map.put("flag", "false");
		}
		//记录日志
		List<ResourceDetailedDTO> rdlist = new ArrayList<ResourceDetailedDTO>();
		rdlist.add(dto);
		com.setRdlist(rdlist);
		this.saveOperationLogs(com, "3", session);
		return map;
	}
	
	
	private String checkLogin(HttpServletRequest req,HttpServletResponse res) throws Exception{
        //检查cookie
		String castgc = null;
		Cookie[] cookies = req.getCookies();
		if(cookies!=null && cookies.length>0){
			for(Cookie ck : cookies){
				if(ck.getName().equals("CASTGC")){							
					castgc = ck.getValue();
					break;
				}
			}
		}
		if(StringUtils.isBlank(castgc)){
			res.sendRedirect(req.getContextPath()+LOGIN_URL);
		}
		return castgc;
	}
	
	/**
	 *	机构用户注册
	 * @throws ParseException
	 */
	@RequestMapping("registerInfo")
	@ResponseBody
	public Map<String,String> registerInfo(MultipartFile file,CommonEntity com,ModelAndView view,HttpServletRequest req,HttpServletResponse res) throws Exception{
		String adminId = this.checkLogin(req,res);
		Map<String,String> hashmap = new HashMap<String, String>();
		List<ResourceDetailedDTO> list = com.getRdlist();
		for(ResourceDetailedDTO dto : list){
			if(dto.getProjectid()!=null){
				if(StringUtils.isBlank(dto.getValidityEndtime())){
					hashmap.put("flag", "fail");
					return hashmap;
				}else{
					if(dto.getProjectType().equals("balance")){
						if(dto.getTotalMoney()==null){
							hashmap.put("flag", "fail");
							return hashmap;
						}
					}else if(dto.getProjectType().equals("count")){
						if(dto.getPurchaseNumber()==null){
							hashmap.put("flag", "fail");
							return hashmap;
						}
					}
				}
			}
		}
		//上传附件
		this.uploadFile(file, list);
		
		int resinfo = aheadUserService.addRegisterInfo(com);
		if(!com.getLoginMode().equals("1")){
			aheadUserService.addUserIp(com);
		}
		if(StringUtils.isNotBlank(com.getChecks())){		
			aheadUserService.addAccountRestriction(com);
		}
		if(StringUtils.isNotBlank(com.getAdminname())&&StringUtils.isNotBlank(com.getAdminpassword())){
			aheadUserService.addRegisterAdmin(com);
			aheadUserService.addUserAdminIp(com);
		}
		//购买详情信息
		for(ResourceDetailedDTO dto : list){
			if(dto.getProjectid()!=null){				
				if(dto.getProjectType().equals("balance")){
					if(aheadUserService.addProjectBalance(com, dto,adminId) > 0){					
						aheadUserService.addProjectResources(com, dto);
					}
				}else if(dto.getProjectType().equals("time")){
					//增加限时信息
					if(aheadUserService.addProjectDeadline(com, dto,adminId) > 0){						
						aheadUserService.addProjectResources(com, dto);
					}
				}else if(dto.getProjectType().equals("count")){
					//增加次数信息
					if(aheadUserService.addProjectNumber(com, dto,adminId) > 0){
						aheadUserService.addProjectResources(com, dto);
					}
				}
			}
		}
		if(resinfo>0){
			WfksAccountidMapping wfks = aheadUserService.getAddauthority(com.getUserId(),"ViewHistoryCheck");
			int msg = WebServiceUtil.submitOriginalDelivery(com, true, wfks, null);
			System.out.println("注册接口执行结果："+com.getUserId()+"_"+msg);			
			hashmap.put("flag", "success");
		}else{
			hashmap.put("flag", "fail");
		}
		return hashmap;
	}
	
	
	/**
	 *	机构用户批量注册跳转
	 */
	@RequestMapping("batchregister")
	public ModelAndView batchRegister(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		List<PayChannelModel> list = aheadUserService.purchaseProject();
		view.addObject("project",list);
		String path = request.getRealPath("/") + "page/usermanager/" + "excel";
		view.addObject("path",path);
		view.setViewName("/page/usermanager/ins_batchregister");
		return view;
	}
	
	/**
	 *	机构用户批量注册
	 */
	@RequestMapping("addbatchRegister")
	@ResponseBody
	public Map<String,String> addbatchRegister(MultipartFile file,CommonEntity com,HttpSession session,ModelAndView view,
			HttpServletRequest req,HttpServletResponse res)throws Exception{
		String adminId = this.checkLogin(req,res);
		String adminIns = com.getAdminOldName().substring(com.getAdminOldName().indexOf("/")+1);
		String adminOldName = null;
		if(StringUtils.isNotBlank(com.getAdminOldName())){			
			adminOldName = com.getAdminOldName().substring(0, com.getAdminOldName().indexOf("/"));
		}
		Map<String,String> hashmap = new HashMap<String, String>();
		List<Map<String, Object>> listmap = aheadUserService.getExcelData(file);
		int in = 0;
		for(Map<String, Object> map : listmap){
			if(!map.get("userId").equals("") && !map.get("institution").equals("")){
				//用户是否存在
				Person p = aheadUserService.queryPersonInfo(map.get("userId").toString());
				String msg = aheadUserService.validateOldUser(map.get("userId").toString());
				if(p!=null && msg.equals("false")){
					hashmap.put("flag", "fail");
					hashmap.put("fail", map.get("userId")+"该用户已存在");
					return hashmap;
				}
				if(StringUtils.isNotBlank(com.getCheckuser()) && com.getManagerType().equals("old") && !map.get("institution").equals(adminIns)){
					hashmap.put("flag", "fail");
					hashmap.put("fail", map.get("userId")+"该用户机构名与管理员机构名不符");
					return hashmap;
				}
				List<ResourceDetailedDTO> list = com.getRdlist();
				List<Map<String, Object>> lm =  (List<Map<String, Object>>) map.get("projectList");
				//判断金额或次数，不可存在负数
				for(Map<String, Object> pl : lm){
					if(Double.valueOf(pl.get("totalMoney").toString())<0){
						hashmap.put("flag", "fail");
						hashmap.put("fail", map.get("userId")+"的购买项目中存在负数");
						return hashmap;
					}
				}
				//预加载校验页面项目是否和Excel中一致
				if(list.size()==lm.size()){
					for(ResourceDetailedDTO dto : list){
						if(dto.getProjectid()!=null){
							if(lm.toString().contains(dto.getProjectid())){
								continue;
							}else{							
								hashmap.put("flag", "fail");
								hashmap.put("fail", map.get("userId")+"该用户购买的项目无法匹配");
								return hashmap;
							}
						}
					}
				}else{
					hashmap.put("flag", "fail");
					hashmap.put("fail", map.get("userId")+"该用户购买的项目无法匹配");
					return hashmap;
				}
			}else{
				hashmap.put("flag", "fail");
				hashmap.put("fail", "Excel中缺失必填项");
				return hashmap;
			}
		}
		for(Map<String, Object> map : listmap){
			//Excel表格中部分账号信息
			com.setInstitution(map.get("institution").toString());
			com.setUserId(map.get("userId").toString());
			com.setPassword(map.get("password").toString());
			int resinfo = aheadUserService.addRegisterInfo(com);
			if(StringUtils.isNotBlank(com.getChecks())){			
				aheadUserService.addAccountRestriction(com);
			}
			List<ResourceDetailedDTO> list = com.getRdlist();
			List<Map<String, Object>> lm =  (List<Map<String, Object>>) map.get("projectList");
			for(ResourceDetailedDTO dto : list){
				for(Map<String, Object> pro : lm) {
					if(dto.getProjectid()!=null && dto.getProjectid().equals(pro.get("projectid"))){						
						dto.setTotalMoney(Double.valueOf(pro.get("totalMoney").toString()));
						if(dto.getProjectType().equals("balance")){
							if(aheadUserService.addProjectBalance(com, dto,adminId) > 0){						
								aheadUserService.addProjectResources(com, dto);
							}
						}else if(dto.getProjectType().equals("time")){
							//增加限时信息
							if(aheadUserService.addProjectDeadline(com, dto,adminId) > 0){						
								aheadUserService.addProjectResources(com, dto);
							}
						}else if(dto.getProjectType().equals("count")){
							dto.setPurchaseNumber(Integer.valueOf(pro.get("totalMoney").toString()));
							//增加次数信息
							if(aheadUserService.addProjectNumber(com, dto,adminId) > 0){
								aheadUserService.addProjectResources(com, dto);
							}
						}
					}
				}
			}
			if(resinfo>0){
				WfksAccountidMapping wfks = aheadUserService.getAddauthority(com.getUserId(),"ViewHistoryCheck");
				int msg = WebServiceUtil.submitOriginalDelivery(com, true, wfks, null);
				System.out.println("批量注册接口执行结果："+com.getUserId()+"_"+msg);
				in+=1;
			}
		}
		if(StringUtils.isNotBlank(com.getAdminname()) || StringUtils.isNotBlank(adminOldName)){
			if(com.getManagerType().equals("new")){
				aheadUserService.addRegisterAdmin(com);
				aheadUserService.addUserAdminIp(com);						
			}else{						
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("userId", com.getUserId());
				m.put("pid", adminOldName);
				aheadUserService.updatePid(m);
			}
		}
		hashmap.put("flag", "success");
		hashmap.put("success", "成功导入："+in+"条");
		return hashmap;
	}
	
	
	/**
	 *	机构用户批量修改
	 */
	@RequestMapping("batchupdate")
	public ModelAndView batchUpdate(){
		ModelAndView view = new ModelAndView();
		List<PayChannelModel> list = aheadUserService.purchaseProject();
		view.addObject("project",list);
		view.setViewName("/page/usermanager/ins_batchupdate");
		return view;
	}
	
	/**
	 *	机构用户批量更新
	 */
	@RequestMapping("updatebatchregister")
	@ResponseBody
	public Map<String,String> updateBatchRegister(MultipartFile file,CommonEntity com,HttpSession session,ModelAndView view,
			HttpServletRequest req,HttpServletResponse res)throws Exception{
		String adminId = this.checkLogin(req,res);
		Map<String,String> hashmap = new HashMap<String, String>();
		List<Map<String, Object>> listmap = aheadUserService.getExcelData(file);
		String adminIns = com.getAdminOldName().substring(com.getAdminOldName().indexOf("/")+1);
		String adminOldName = null;
		if(StringUtils.isNotBlank(com.getAdminOldName())){			
			adminOldName = com.getAdminOldName().substring(0, com.getAdminOldName().indexOf("/"));
		}
		int in = 0;
		for(Map<String, Object> map : listmap){
			if(!map.get("userId").equals("")){
				//用户是否存在
				Person ps = aheadUserService.queryPersonInfo(map.get("userId").toString());
				if(ps==null){
					hashmap.put("flag", "fail");
					hashmap.put("fail", map.get("userId")+"该用户不存在");
					return hashmap;
				}
				if(StringUtils.isNotBlank(com.getCheckuser())){
					if(map.get("institution").equals("")){						
						hashmap.put("flag", "fail");
						hashmap.put("fail", "如开通管理员请填写机构名称");
						return hashmap;
					}else{
						if(com.getManagerType().equals("old") && !map.get("institution").equals(adminIns)){
							hashmap.put("flag", "fail");
							hashmap.put("fail", map.get("userId")+"该用户机构名与管理员机构名不符");
							return hashmap;
						}
					}
				}
				List<ResourceDetailedDTO> list = com.getRdlist();
				List<Map<String, Object>> lm =  (List<Map<String, Object>>) map.get("projectList");
				for(Map<String, Object> pl : lm){
					//判断如果重置金额或次数，不可存在负数
					if(StringUtils.isNotBlank(com.getResetMoney()) || StringUtils.isNotBlank(com.getResetCount())){
						if(Double.valueOf(pl.get("totalMoney").toString())<0){
							hashmap.put("flag", "fail");
							hashmap.put("fail", map.get("userId")+"的购买项目中存在负数(不能被重置)");
							return hashmap;
						}
					}
				}
				if(list!=null){
					//预加载校验页面项目是否和Excel中一致
					for(ResourceDetailedDTO dto : list){
						if(dto.getProjectid()!=null){
							if(lm.toString().contains(dto.getProjectid())){
								continue;
							}else{							
								hashmap.put("flag", "fail");
								hashmap.put("fail", map.get("userId")+"该用户购买的项目无法匹配");
								return hashmap;
							}
						}
					}
				}
			}else{
				hashmap.put("flag", "fail");
				hashmap.put("fail", "机构用户名必填");
				return hashmap;
			}
		}
		for(Map<String, Object> map : listmap){
			Person ps = aheadUserService.queryPersonInfo(map.get("userId").toString());
			//Excel表格中部分账号信息
			if(map.get("institution")!=null && map.get("institution")!=""){				
				com.setInstitution(map.get("institution").toString());
			}else{
				com.setInstitution(ps.getInstitution());
			}
			if(map.get("password")!=null && map.get("password")!=""){				
				com.setPassword(map.get("password").toString());
			}else{
				com.setPassword(PasswordHelper.decryptPassword(ps.getPassword()));
			}
			com.setUserId(map.get("userId").toString());
			//更新机构账号
			int resinfo = aheadUserService.updateRegisterInfo(com, ps.getPid(), adminId);
			//未存在管理员添加新的
			if(StringUtils.isBlank(ps.getPid())){
				if(StringUtils.isNotBlank(com.getAdminname()) || StringUtils.isNotBlank(adminOldName)){
					if(com.getManagerType().equals("new")){
						aheadUserService.addRegisterAdmin(com);
						aheadUserService.addUserAdminIp(com);						
					}else{						
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("userId", com.getUserId());
						m.put("pid", adminOldName);
						aheadUserService.updatePid(m);
					}
				}
			}
			if(StringUtils.isNotBlank(com.getChecks())){			
				aheadUserService.updateAccountRestriction(com);		
			}
			List<ResourceDetailedDTO> list = com.getRdlist();
			List<Map<String, Object>> lm =  (List<Map<String, Object>>) map.get("projectList");
			if(list!=null){
			for(ResourceDetailedDTO dto : list){
				for(Map<String, Object> pro : lm) {						
					if(dto.getProjectid()!=null && dto.getProjectid().equals(pro.get("projectid"))){
						dto.setTotalMoney(Double.valueOf(pro.get("totalMoney").toString()));
						if(dto.getProjectType().equals("balance")){
							if(aheadUserService.chargeProjectBalance(com, dto, adminId)>0){
								aheadUserService.deleteResources(com,dto,false);
								aheadUserService.updateProjectResources(com, dto);
							}
						}else if(dto.getProjectType().equals("time")){
							//增加限时信息
							if(aheadUserService.addProjectDeadline(com, dto,adminId)>0){
								aheadUserService.deleteResources(com,dto,false);
								aheadUserService.updateProjectResources(com, dto);
							}
						}else if(dto.getProjectType().equals("count")){
							dto.setPurchaseNumber(Integer.valueOf(pro.get("totalMoney").toString()));
							//增加次数信息
							if(aheadUserService.chargeCountLimitUser(com, dto, adminId) > 0){
								aheadUserService.deleteResources(com,dto,false);
								aheadUserService.updateProjectResources(com, dto);
							}
						}
						this.saveOperationLogs(com,"2", session);
					}
				}
			}
			}
			if(resinfo>0){
				WfksAccountidMapping wfks = aheadUserService.getAddauthority(com.getUserId(),"ViewHistoryCheck");
				int msg = WebServiceUtil.submitOriginalDelivery(com, false, wfks, null);
				System.out.println("更新接口执行结果："+com.getUserId()+"_"+msg);
				in+=1;
			}
		}
		hashmap.put("flag", "success");
		hashmap.put("success", "成功更新："+in+"条");
		return hashmap;
	}
	
	
	@RequestMapping("/worddownload")
	public void wordouyang(Model model,HttpServletResponse response,HttpServletRequest request) {
        // 下载本地文件
		String fileName = request.getParameter("title"); // 文件的默认保存名
		InputStream inStream = null;
		try{
			
			fileName = URLDecoder.decode(fileName, "UTF-8");
			String TextName="";
			if(fileName.equals("机构账号批量模板（更新）")){
				TextName="GX.xlsx";
				fileName=fileName+".xlsx";
			}else if(fileName.equals("机构账号批量模板（冻结）")){
				TextName="DJ.xlsx";
				fileName=fileName+".xlsx";
			}else if(fileName.equals("机构账号批量模板（注册）")){
				TextName="ZC.xlsx";
				fileName=fileName+".xlsx";
			}
			fileName=fileName;
			inStream = new FileInputStream(request.getRealPath("/") + "page/usermanager/excel/"+TextName);
			// 设置输出的格式
			response.reset();
			response.setContentType("bin");
			response.setCharacterEncoding("UTF-8");
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			response.addHeader("Content-Disposition", "attachment; filename=\""+ fileName );
			// 循环取出流中的数据
			byte[] b = new byte[100];
			int len;
			while ((len = inStream.read(b)) > 0) {
				response.getOutputStream().write(b, 0, len);
			}
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 *	批量冻结和解冻
	 */
	@RequestMapping("blockunlock")
	@ResponseBody
	public Map<String,Object> blockUnlock(MultipartFile file,String radio){
		Map<String,Object> hashmap = new HashMap<String, Object>();
		List<String> list = aheadUserService.getExceluser(file);
		int in = 0;
		for(String str : list){
			Person person = aheadUserService.queryPersonInfo(str);
			if(person!=null){				
				int i = aheadUserService.updateUserFreeze(str,radio);
				if(i>0){
					in+=1;
				}
			}else{
				hashmap.put("flag", "fail");
				hashmap.put("userId", str);
				return hashmap;
			}
		}
		hashmap.put("flag", "success");
		hashmap.put("num", in);
		hashmap.put("count", list.size());
		return hashmap;
	}
	
	
	/**
	 *	机构信息列表（查询机构下管理员）
	 */
	@RequestMapping("findins")
	@ResponseBody
	public List<Map<String, Object>> findInstitutionAdmin(String institution,String userId){
		//查询机构下机构管理员列表
		return aheadUserService.findInstitutionAdmin(institution,userId);
	}
	
	/**
	 * 更新机构管理员
	 */
	@RequestMapping("updateins")
	@ResponseBody
	public Map<String, Object> updateInstitutionAdmin(String institution,String oldins,String adminList){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray array = JSONArray.fromObject(adminList);
		int i = 0;
		for(Object object : array){
			JSONObject json = JSONObject.fromObject(object);
			CommonEntity com = new CommonEntity();
			com.setAdminname(json.getString("adminId"));
			com.setAdminpassword(json.getString("adminpassword"));
			com.setAdminEmail(json.getString("adminEmail"));
			com.setAdminIP(json.getString("adminIP"));
			com.setInstitution(institution);
			aheadUserService.deleteUser(json.getString("adminId"));
			int in = aheadUserService.addRegisterAdmin(com);
			if(StringUtils.isNotBlank(json.getString("adminIP"))){
				aheadUserService.deleteUserIp(json.getString("adminId"));
				aheadUserService.addUserAdminIp(com);
			}else{
				aheadUserService.deleteUserIp(json.getString("adminId"));
			}
			if(in>0){
				i++;
			}
		}
		aheadUserService.updateInstitution(institution,oldins);
		if(i==array.size()){
			map.put("flag", "true");
		}else{
			map.put("flag", "false");
		}
		return map;
	}
	
	/**
	 *	机构用户信息管理查询
	 */
	@RequestMapping("toInformation")
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
		if(ipSegment!=null&&!ipSegment.equals("")){			
			map.put("ipSegment", ipSegment);
		}
		if(adminIP!=null&&!adminIP.equals("")){			
			map.put("adminIP", adminIP);
		}
		view.addObject("map", map);
		view.setViewName("/page/usermanager/ins_information");
		return view;
	}
	
	/**
	 *	机构用户信息管理列表
	 */
	@RequestMapping("information")
	public ModelAndView information(String userId,String ipSegment,String institution,String adminname,
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
		PageList pageList = aheadUserService.findListInfo(map);
		pageList.setPageNum(Integer.parseInt(pageNum==null?"1":pageNum));//当前页
		pageList.setPageSize(Integer.parseInt(pageSize==null?"10":pageSize));//每页显示的数量
		if(ipSegment!=null&&!ipSegment.equals("")){			
			map.put("ipSegment", ipSegment);
		}
		map.put("pageList", pageList);
		view.addObject("map", map);
		view.addObject("timelimit",DateUtil.getTimeLimit());
		view.setViewName("/page/usermanager/ins_information");
		return view;
	}
	
	
	
	/**
	 *	账号修改页面返显
	 */
	@RequestMapping("numupdate")
	public ModelAndView numUpdate(String userId) throws Exception{
		ModelAndView view = new ModelAndView();
		Map<String, Object> map = aheadUserService.findListInfoById(userId);
		if(map.get("pid")!=null&&StringUtils.isNoneBlank(map.get("pid").toString())){  
			Map<String, Object> m = aheadUserService.findInfoByPid(map.get("pid").toString());
			map.put("adminname", m.get("userId"));
			map.put("adminpassword", m.get("password"));
			map.put("adminIP", m.get("adminIP"));
			map.put("adminEmail", m.get("adminEmail"));
		}
		aheadUserService.getprojectinfo(userId,map);
		view.addObject("map",map);
		List<PayChannelModel> list = aheadUserService.purchaseProject();
		view.addObject("project",list);
		view.addObject("timelimit",DateUtil.getTimeLimit());
		view.setViewName("/page/usermanager/ins_numupdate");
		return view;
	}
	
	
	/**
	 *	账号修改
	 */
	@RequestMapping("updateinfo")
	@ResponseBody
	public Map<String,String> updateinfo(MultipartFile file,CommonEntity com,HttpSession session,HttpServletRequest req,HttpServletResponse res) throws Exception{
		String adminId = this.checkLogin(req,res);
		Map<String,String> hashmap = new HashMap<String, String>();
		//更新条件Map
		Map<String, Object> map = new HashMap<String, Object>();
		int resinfo = 0;	
		resinfo = aheadUserService.updateUserInfo(com, adminId);
		if(StringUtils.isNotBlank(com.getAdminname()) || StringUtils.isNotBlank(com.getAdminOldName())){
			if(com.getManagerType().equals("new")){				
				//更新机构管理员
				aheadUserService.deleteUser(com.getAdminname());
				aheadUserService.addRegisterAdmin(com);
				//更新管理员IP
				aheadUserService.deleteUserIp(com.getAdminname());
				aheadUserService.addUserAdminIp(com);
			}else{
				map.put("userId", com.getUserId());
				map.put("pid", com.getAdminOldName());
				aheadUserService.updatePid(map);
			}
		}else{
			map.put("userId", com.getUserId());
			map.put("pid", "");
			aheadUserService.updatePid(map);
		}
		if(com.getLoginMode().equals("0") || com.getLoginMode().equals("2")){			
			aheadUserService.updateUserIp(com);
		}else{
			aheadUserService.deleteUserIp(com.getUserId());
		}
		if(StringUtils.isNotBlank(com.getChecks())){			
			aheadUserService.updateAccountRestriction(com);		
		}
		List<ResourceDetailedDTO> list = com.getRdlist();
		//上传附件
		this.uploadFile(file,list);
		for(ResourceDetailedDTO dto : list){
			if(dto.getProjectid()!=null){
				if(dto.getProjectType().equals("balance")){
					if(aheadUserService.chargeProjectBalance(com, dto, adminId)>0){
						aheadUserService.deleteResources(com,dto,false);
						aheadUserService.updateProjectResources(com, dto);
					}
				}else if(dto.getProjectType().equals("time")){
					//增加限时信息
					if(aheadUserService.addProjectDeadline(com, dto,adminId)>0){
						aheadUserService.deleteResources(com,dto,false);
						aheadUserService.updateProjectResources(com, dto);
					}
				}else if(dto.getProjectType().equals("count")){
					//增加次数信息
					if(aheadUserService.chargeCountLimitUser(com, dto, adminId) > 0){
						aheadUserService.deleteResources(com,dto,false);
						aheadUserService.updateProjectResources(com, dto);
					}
				}
			}
		}
		if(resinfo>0){
			WfksAccountidMapping wfks = aheadUserService.getAddauthority(com.getUserId(),"ViewHistoryCheck");
			int msg = WebServiceUtil.submitOriginalDelivery(com, false, wfks, null);
			System.out.println("更新接口执行结果："+com.getUserId()+"_"+msg);
			hashmap.put("flag", "success");
		}else{
			hashmap.put("flag", "fail");
		}
		this.saveOperationLogs(com,"2", session);
		return hashmap;
	}
	
	//上传附件模块
	private void uploadFile(MultipartFile file,List<ResourceDetailedDTO> list){
		if (file == null || file.getSize()==0) {
			return;
		}
		List<String> lsList = aheadUserService.getExceluser(file);
		if(lsList.size()==0){
			return;
		}
		StringBuffer idBuff = new StringBuffer("");
		for (int i = 0; i < lsList.size(); i++) {
			if (i == 0) {
				idBuff.append(lsList.get(i));
			} else {
				idBuff.append(";").append(lsList.get(i));
			}
		}
		for (ResourceDetailedDTO dto : list) {
			if (dto.getProjectid() != null) {
				List<ResourceLimitsDTO> limitList = dto.getRldto();
				for (ResourceLimitsDTO limit : limitList) {
					if (limit.getGazetteersLevel() != null) {
						String gazetteersId = limit.getGazetteersId();
						if (gazetteersId != null && !"".equals(gazetteersId)) {
							idBuff.append(";").append(gazetteersId);
						}
						limit.setGazetteersId(idBuff.toString());
					}
				}
			}
		}
	
	}
	/**
	 *	机构用户订单跳转
	 */
	@RequestMapping("groupOrder")
	public ModelAndView groupOrder(String userId){
		ModelAndView view = new ModelAndView();
		view.addObject("userId", userId);
		view.setViewName("/page/usermanager/group_pay_order");
		return view;
	}
	
	/**
	 *	子账号列表页跳转
	 */
	@RequestMapping("tosonaccountnumber")
	public ModelAndView toSonAccountNumber(String userId,String sonId,String start_time,String end_time){
		ModelAndView view = new ModelAndView();
		List<Map<String,Object>> list = aheadUserService.sonAccountNumber(userId,sonId,start_time,end_time);
		for(Map<String,Object> p : list){
			try{
				p.put("password",PasswordHelper.decryptPassword(p.get("password").toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		view.addObject("userId",userId);
		view.addObject("sonId",sonId);
		view.addObject("start_time",start_time);
		view.addObject("end_time",end_time);
		view.addObject("list",list);
		view.setViewName("/page/usermanager/ins_sonaccount");
		return view;
	}
	
	/**
	 *	操作记录
	 */
	@RequestMapping("opration")
	public ModelAndView opration(String pageNum,String pageSize,String startTime,String endTime,String userId,String person,HttpSession session){
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
					mm.put("purchaseNumber", json.get("totalMoney"));
				}else if(type.equals("time")){
					mm.put("projectType", "限时");
				}
				mm.put("validityEndtime", json.get("validityEndtime"));
				mm.put("validityStarttime", json.get("validityStarttime"));
				mm.put("projectname", json.get("projectname"));
			}
		}
		view.setViewName("/page/usermanager/ins_oprationrecord");
		return view;
	}
	
	
	/**
	 *	服务权限设置跳转
	 */
	@RequestMapping("showAuthority")
	public ModelAndView showAuthority(String msg,String userId) throws Exception {
		ModelAndView view = new ModelAndView();
		WfksAccountidMapping wfks = aheadUserService.getAddauthority(userId,msg);
		WfksUserSetting setting =  aheadUserService.getUserSetting(userId, msg);
		if(setting!=null && msg.equals("PartyAdminTime")){			
			Person ps = aheadUserService.queryPersonInfo(setting.getPropertyValue());
			if(ps!=null){				
				ps.setPassword(PasswordHelper.decryptPassword(ps.getPassword()));
				view.addObject("ps", ps);
			}
		}
		view.addObject("setting", setting);
		view.addObject("wfks", wfks);
		view.addObject("msg", msg);
		view.addObject("sdate", DateUtil.getSysDate());
		view.addObject("edate", DateUtil.beforeOrAfterNDay(3));
		view.addObject("userId", userId);
		view.setViewName("/page/usermanager/authority");
		return view;
	}
	
	/**
	 *	服务权限设置
	 */
	@RequestMapping("addauthority")
	@ResponseBody
	public Map<Object,String> addauthority(Authority authority) throws Exception{
		Map<Object,String> map = new HashMap<Object,String>();
		int a = aheadUserService.setAddauthority(authority);
		if(a>0){
			map.put("flag", "success");
			Person ps = aheadUserService.queryPersonInfo(authority.getUserId());
			if(ps.getLoginMode().equals("")){
				
			}
			CommonEntity com = new CommonEntity();
			com.setUserId(ps.getUserId());
			com.setInstitution(ps.getInstitution());
			WfksAccountidMapping wfks = aheadUserService.getAddauthority(authority.getUserId(),authority.getRelatedIdAccountType());
			WfksUserSetting setting =  aheadUserService.getUserSetting(authority.getUserId(), authority.getRelatedIdAccountType());
			int msg = WebServiceUtil.submitOriginalDelivery(com, false, wfks, setting);
			System.out.println("更新接口执行结果："+com.getUserId()+"_"+msg);
		}else{
			map.put("flag", "fail");
		}
		return map;
	}
	
	
	
	@RequestMapping("perManagers")
	public ModelAndView perManagers() {
		ModelAndView view = new ModelAndView();
		view.addObject("PageList", "null");
		view.addObject("person", "null");
		view.addObject("roles1", "null");
		view.addObject("roles2", "null");
		view.addObject("roles3", "null");
		view.setViewName("/page/usermanager/per_manager");
		return view;
	}
	
	
	/**
	 *	个人用户管理管理 
	 */
	@RequestMapping("perManager")
	public ModelAndView perManager(Person person,Integer pageNum,Integer pageSize,String userRoless, String flag){
		ModelAndView view = new ModelAndView();
		String[] roles=null;
		if(userRoless!=null&&!"".equals(userRoless)){
			roles=userRoless.split(",");
		}
		
		Map<String,Object> p = personservice.QueryPersion(person,pageNum,pageSize,roles);
		view.addObject("PageList", p);
//		if("1".equals(flag)){
			view.addObject("person", person);
//		}else{
//			view.addObject("person",new Person());
//		}
		
		//确认页面checkbox选中状态
		String roles1="";
		String roles2="";
		String roles3="";
		if(roles!=null&&roles.length==3){
			roles1=roles[0];
			roles2=roles[1];
			roles3=roles[2];
		}else if(roles!=null&&roles.length==2){
			if(!"0".equals(roles[0])){
				roles2=roles[0];
				roles3=roles[1];
			}else if(!"1".equals(roles[0])){
				roles1=roles[0];
				roles3=roles[1];
			}else if(!"2".equals(roles[0])){
				roles1=roles[0];
				roles2=roles[1];
			}
		}else if(roles!=null&&roles.length==1){
			if("0".equals(roles[0])){
				roles1=roles[0];
			}else if("1".equals(roles[0])){
				roles2=roles[0];
			}else if("2".equals(roles[0])){
				roles3=roles[0];
			}
		}
//		if("1".equals(flag)||flag==null){
//			view.addObject("flag", "0");
//		}
		view.addObject("roles1", roles1);
		view.addObject("roles2", roles2);
		view.addObject("roles3", roles3);
		view.setViewName("/page/usermanager/per_manager");
		return view;
	}
	/**
	* @Title: addDept
	* @Description: TODO(返回修改页面) 
	* @return String 返回类型 
	* @author LiuYong 
	* @date 10 Dis 2016 10:41:45 AM
	 */
	@RequestMapping("updatePerson")
	public ModelAndView updatePerson(String userId){
		ModelAndView view = new ModelAndView();
		/*Person ps=personservice.findById(userId);*/
		view.addObject("userId", userId);
		view.setViewName("/page/usermanager/update_person_manager");
		return view;
	}
	
	
	/** 个人账号充值（修改） 
	 * userId 用户id
	 * turnover 充值金额
	 * reason 充值原因
	 * authToken 凭证
	 */
	@ResponseBody
	@RequestMapping("doUpdatePerson")
    public Map<String,Object> personCharge(String userId, String turnover, String reason, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Map<String,Object> m = new HashMap<String,Object>();
		String adminId = this.checkLogin(req,res);
		int i = aheadUserService.personCharge(userId, turnover, reason, adminId, res);
		if(i>0){
			m.put("flag", "success");
		}else{			
			m.put("flag", "fail");
		}
		return m;
    }
	
	
	/**
	 *	个人充值管理
	 */
	@RequestMapping("charge_order")
	public ModelAndView perAward(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/charge_order");
		return view;
	}
	
	
	/**
	 *	个人订单管理
	 */
	@RequestMapping("pay_order")
	public ModelAndView order(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/pay_order");
		return view;
	}
	
	
	/**
	 *	充值管理
	 */
	@RequestMapping("pay")
	public ModelAndView pay(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/charge_order");
		return view;
	}
	
	/**
	 * 添加操作日志信息
	 * @return
	 */
	private void saveOperationLogs(CommonEntity com,String flag,HttpSession session){
		if(com!=null){
			String adminId = ((Wfadmin)session.getAttribute("wfAdmin")).getWangfang_admin_id();
			List<ResourceDetailedDTO> list = com.getRdlist();
			if(list!=null &&list.size()>0){
				for(ResourceDetailedDTO dto:list){
					if(dto.getProjectid()!=null){						
						OperationLogs op=new OperationLogs();
						op.setUserId(com.getUserId());
						op.setPerson(adminId);
						op.setOpreation(flag=="2"?"更新":"删除");
						if(com.getRdlist()!=null){
							//dto.setRldto(null);
							op.setReason(JSONObject.fromObject(dto).toString());
						}
						opreationLogs.addOperationLogs(op);
					}
				}
			}
		}
	}
	
	public static void main(String[] args){
		String str = "[\"a\",\"b\",\"c\"]";
		System.out.println(JSONArray.fromObject(str));
	}
	
	/**
	 * 根据机构名称获取管理员
	 * @param response
	 * @param institution 机构名称
	 * @throws Exception 
	 */
	@RequestMapping("getOldAdminNameByInstitution")
	public void getOldAdminNameByInstitution(HttpServletResponse response,String institution) throws Exception{
		List<Map<String, Object>> userids = personservice.getAllInstitutional(institution);
		JSONArray array = JSONArray.fromObject(userids);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(array.toString());
	}
	
}
