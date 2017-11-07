package com.wf.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import wfks.accounting.setting.PayChannelModel;

import com.alibaba.citrus.util.StringUtil;
import com.redis.RedisUtil;
import com.utils.CookieUtil;
import com.utils.DateUtil;
import com.utils.HttpClientUtil;
import com.utils.IPConvertHelper;
import com.wanfangdata.encrypt.PasswordHelper;
import com.wf.bean.Authority;
import com.wf.bean.AuthoritySetting;
import com.wf.bean.CommonEntity;
import com.wf.bean.OperationLogs;
import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.ResourceLimitsDTO;
import com.wf.bean.StandardUnit;
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

	@Autowired
	private AheadUserService aheadUserService;
	
	@Autowired
	private PersonService personservice;
	
	@Autowired
	private OpreationLogsService opreationLogs;
	
	private RedisUtil redis = new RedisUtil();
	private static Logger log = Logger.getLogger(AheadUserController.class);
	
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
	}
	
	/**
	 *	查询相似机构名称 
	 */
	@RequestMapping("getkeywords")
	@ResponseBody
	public List<String> getkeywords(String value){
		long time=System.currentTimeMillis();
		List<String> list=aheadUserService.queryInstitution(value);
		log.info("查询相似机构名称["+value+"],耗时："+(System.currentTimeMillis()-time)+"ms");
		return list;
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
		int uuf = aheadUserService.updateUserFreeze(aid, flag);
		if (uuf > 0) {
			if ("1".equals(flag)) { //冻结
				redis.set(aid, "true", 12);
				redis.expire(aid, 3600 * 24, 12); //设置超时时间
			} else if ("2".equals(flag)) { //解冻
				redis.del(12, aid);
			}
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
	public String addadmin(CommonEntity com){
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
	 *	机构用户注册
	 * @throws ParseException
	 */
	@RequestMapping("registerInfo")
	@ResponseBody
	public Map<String,String> registerInfo(CommonEntity com,ModelAndView view,HttpServletRequest req,HttpServletResponse res) throws Exception{
		long time=System.currentTimeMillis();
		String adminId = CookieUtil.getCookie(req);
		Map<String,String> hashmap = new HashMap<String, String>();
		List<ResourceDetailedDTO> list = com.getRdlist();
		if(list==null){
			hashmap.put("flag", "fail");
			hashmap.put("fail",  "购买项目不能为空");
			return hashmap;
		}
		for (ResourceDetailedDTO dto : list) {
			hashmap = this.getValidate(dto,true);
			if (hashmap.size() > 0) {
				return hashmap;
			}
		}
		
		int resinfo = aheadUserService.addRegisterInfo(com);
		if(com.getLoginMode().equals("0") || com.getLoginMode().equals("2")){
			//校验ip的合法性
			if(IPConvertHelper.validateIp(com.getIpSegment())){
				aheadUserService.addUserIp(com);
			}else{
				hashmap.put("flag", "fail");
				hashmap.put("fail",  "ip不合法");
				return hashmap;
			}
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
		if (resinfo > 0) {
			//更新前台用户信息
			if(com.getLoginMode().equals("0") || com.getLoginMode().equals("2")){
				HttpClientUtil.updateUserData(com.getUserId(), false);
			}
			hashmap.put("flag", "success");
			log.info("机构用户["+com.getUserId()+"]注册成功，耗时:"+(System.currentTimeMillis()-time)+"ms");
		} else {
			hashmap.put("flag", "fail");
			log.info("机构用户["+com.getUserId()+"]注册失败，耗时:"+(System.currentTimeMillis()-time)+"ms");
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
		String path = request.getServletContext().getRealPath("/") + "page/usermanager/" + "excel";
		view.addObject("path",path);
		view.setViewName("/page/usermanager/ins_batchregister");
		return view;
	}
	
	/**
	 *	机构用户批量注册
	 */
	@RequestMapping("addbatchRegister")
	@ResponseBody
	public Map<String,String> addbatchRegister(MultipartFile file,CommonEntity com,ModelAndView view,
			HttpServletRequest req,HttpServletResponse res)throws Exception{
		long time=System.currentTimeMillis();
		String adminId = CookieUtil.getCookie(req);
		String adminIns = com.getAdminOldName().substring(com.getAdminOldName().indexOf("/")+1);
		String adminOldName = null;
		if(StringUtils.isNotBlank(com.getAdminOldName())){			
			adminOldName = com.getAdminOldName().substring(0, com.getAdminOldName().indexOf("/"));
		}
		Map<String,String> hashmap = new HashMap<String, String>();
		List<Map<String, Object>> listmap = aheadUserService.getExcelData(file);
		for (int i = 0; i < listmap.size(); i++) {
			Map<String, Object> map = listmap.get(i);
			if ("".equals(map.get("userId")) && "".equals(map.get("institution"))) {
				listmap.remove(i);
				i--;
			}
		}
		List<ResourceDetailedDTO> list = com.getRdlist();
		if (list == null || list.size() == 0) {
			hashmap.put("flag", "fail");
			hashmap.put("fail", "请选择项目");
			return hashmap;
		}
		for (ResourceDetailedDTO dto : list) {
			if(dto.getProjectid()!=null){
				hashmap = this.getValidate(dto,false);
				if (hashmap.size() > 0) {
					return hashmap;
				}
			}
		}
		int in = 0;
		for(Map<String, Object> map : listmap){
			if(!map.get("userId").equals("") && !map.get("institution").equals("")){
				//用户是否存在
				Person p = aheadUserService.queryPersonInfo(map.get("userId").toString());
				String msg = aheadUserService.validateOldUser(map.get("userId").toString());
				if(p!=null || msg.equals("false")){
					hashmap.put("flag", "fail");
					hashmap.put("fail", map.get("userId")+"该用户已存在");
					return hashmap;
				}
				if(StringUtils.isNotBlank(com.getCheckuser()) && com.getManagerType().equals("old") && !map.get("institution").equals(adminIns)){
					hashmap.put("flag", "fail");
					hashmap.put("fail", map.get("userId")+"该用户机构名与管理员机构名不符");
					return hashmap;
				}
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
			com.setPassword(String.valueOf(map.get("password")));
			int resinfo = aheadUserService.addRegisterInfo(com);
			if(StringUtils.isNotBlank(com.getChecks())){			
				aheadUserService.addAccountRestriction(com);
			}
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
			if (resinfo > 0) {
				in += 1;
				log.info("机构用户["+com.getUserId()+"]注册成功");
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
		log.info("批量注册成功："+in+"条，耗时:"+(System.currentTimeMillis()-time)+"ms");
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
	public Map<String,String> updateBatchRegister(MultipartFile file,CommonEntity com,ModelAndView view,
			HttpServletRequest req,HttpServletResponse res)throws Exception{
		long time=System.currentTimeMillis();
		String adminId = CookieUtil.getCookie(req);
		String adminIns = com.getAdminOldName().substring(com.getAdminOldName().indexOf("/")+1);
		String adminOldName = null;
		if(StringUtils.isNotBlank(com.getAdminOldName())){			
			adminOldName = com.getAdminOldName().substring(0, com.getAdminOldName().indexOf("/"));
		}
		Map<String, String> hashmap = new HashMap<String, String>();
		if (file == null || file.isEmpty()) {
			hashmap.put("flag", "fail");
			hashmap.put("fail", "请上传附件");
			return hashmap;
		}
		List<Map<String, Object>> listmap = aheadUserService.getExcelData(file);
		for (int i = 0; i < listmap.size(); i++) {
			Map<String, Object> map = listmap.get(i);
			if ("".equals(map.get("userId"))) {//institution不填写就默认不修改
				listmap.remove(i);
				i--;
			}
		}
		List<ResourceDetailedDTO> list = com.getRdlist();
		if (list == null || list.size() == 0) {
			hashmap.put("flag", "fail");
			hashmap.put("fail", "请选择项目");
			return hashmap;
		}
		for (ResourceDetailedDTO dto : list) {
			hashmap = this.getValidate(dto,false);
			if (hashmap.size() > 0) {
				return hashmap;
			}
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
				String password = String.valueOf(map.get("password"));
				if (password.contains("不变")) {
					password = "";
				}
				com.setPassword(password);
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
						}
					}
				}
			}
			if(resinfo>0){
				in+=1;
				log.info("机构用户["+com.getUserId()+"]修改成功");
			}
			this.saveOperationLogs(com,"2", req);
		}
		hashmap.put("flag", "success");
		hashmap.put("success", "成功更新："+in+"条");
		log.info("批量修改成功："+in+"条，耗时:"+(System.currentTimeMillis()-time)+"ms");
		return hashmap;
	}
	
	
	@RequestMapping("/worddownload")
	public void worddownload(Model model,HttpServletResponse response,HttpServletRequest request) {
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
			inStream = new FileInputStream(request.getServletContext().getRealPath("/") + "page/usermanager/excel/"+TextName);
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
					if ("1".equals(radio)) { //冻结
						redis.set(str, "true", 12);
						redis.expire(str, 3600 * 24, 12); //设置超时时间
					} else if ("2".equals(radio)) { //解冻
						redis.del(12, str);
					}
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
		if (!StringUtil.isEmpty(ipSegment)) {
			if(IPConvertHelper.validateOneIp(ipSegment)){
				map.put("ipSegment", IPConvertHelper.IPToNumber(ipSegment));
			}else{
				map.put("ipSegment", ipSegment);
				view.addObject("map", map);
				view.setViewName("/page/usermanager/ins_information");
				return view;
			}
		}
		map.put("institution", institution);
		map.put("adminname", adminname);
		map.put("adminIP", adminIP);
		map.put("pageNum", (Integer.parseInt(pageNum==null?"1":pageNum)-1)*Integer.parseInt((pageSize==null?"1":pageSize)));
		map.put("pageSize", Integer.parseInt(pageSize==null?"10":pageSize));
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(ipSegment)&& StringUtils.isEmpty(institution)) {
			view.setViewName("/page/usermanager/ins_information");
			return view;
		}
		PageList pageList = aheadUserService.findListInfo(map);
		pageList.setPageNum(Integer.parseInt(pageNum==null?"1":pageNum));//当前页
		pageList.setPageSize(Integer.parseInt(pageSize==null?"10":pageSize));//每页显示的数量
		if(!StringUtil.isEmpty(ipSegment)){
			map.put("ipSegment", ipSegment);
		}
		map.put("pageList", pageList);
		//获取权限列表
		List<AuthoritySetting> settingList=aheadUserService.getAuthoritySettingList();
		view.addObject("map", map);
		view.addObject("timelimit",DateUtil.getTimeLimit());
		view.addObject("settingList",settingList);
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
	 *	删除购买项目 
	 */
	private void removeproject(HttpServletRequest req,List<String> list) throws Exception{
		String adminId = CookieUtil.getCookie(req);
		CommonEntity com = new CommonEntity();
		List<ResourceDetailedDTO> rdlist = new ArrayList<ResourceDetailedDTO>();
		for (String json : list) {
			JSONObject obj = JSONObject.fromObject(json);
			String userId = obj.getString("userId");
			String institution = obj.getString("institution");
			String payChannelid = obj.getString("payChannelid");
			String beginDateTime = obj.getString("beginDateTime");
			String endDateTime = obj.getString("endDateTime");
			String projectname = obj.getString("projectname");
			String type = obj.getString("type");
			Double balance = 0.0D;
			if(!StringUtils.isEmpty(obj.getString("balance"))){
				balance=Double.parseDouble(obj.getString("balance"));
			}
			com.setUserId(userId);
			com.setInstitution(institution);
			ResourceDetailedDTO dto = new ResourceDetailedDTO();
			dto.setProjectid(payChannelid);
			dto.setValidityStarttime(beginDateTime);
			dto.setValidityEndtime(endDateTime);
			dto.setProjectname(projectname);
			dto.setProjectType(type);
			if ("balance".equals(type)) {
				dto.setTotalMoney(balance);
			} else if ("count".equals(type)) {
				dto.setPurchaseNumber(balance.intValue());
			}
			int i = aheadUserService.deleteAccount(com, dto, adminId);
			if (i > 0) {
				aheadUserService.deleteResources(com, dto, false);
			}
			rdlist.add(dto);
		}
		com.setRdlist(rdlist);
		this.saveOperationLogs(com, "3", req);
	}
	
	/**
	 *	账号修改
	 */
	@RequestMapping("updateinfo")
	@ResponseBody
	public Map<String,String> updateinfo(MultipartFile file,CommonEntity com,HttpServletRequest req,HttpServletResponse res) throws Exception{
		long time=System.currentTimeMillis();
		String adminId = CookieUtil.getCookie(req);
		Map<String,String> hashmap = new HashMap<String, String>();
		List<String> delList=new ArrayList<String>();
		List<ResourceDetailedDTO> list=new ArrayList<ResourceDetailedDTO>();
		if(com.getRdlist()==null){
			hashmap.put("flag", "fail");
			hashmap.put("fail",  "购买项目不能为空");
			return hashmap;
		}
		for (ResourceDetailedDTO dto : com.getRdlist()) {
			if (StringUtils.isEmpty(dto.getProjectname())) {
				delList.add(dto.getProjectid());
				continue;
			}
			hashmap = this.getValidate(dto,true);
			if (hashmap.size() > 0) {
				return hashmap;
			}
			list.add(dto);
		}
		//删除项目
		if(delList.size()>0){
			this.removeproject(req,delList);
		}
		com.setRdlist(list);
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
			//校验ip的合法性
			if(IPConvertHelper.validateIp(com.getIpSegment())){
				aheadUserService.updateUserIp(com);
			}else{
				hashmap.put("flag", "fail");
				hashmap.put("fail",  "ip不合法");
				return hashmap;
			}
		}else{
			aheadUserService.deleteUserIp(com.getUserId());
		}
		if(StringUtils.isNotBlank(com.getChecks())){			
			aheadUserService.updateAccountRestriction(com);		
		}
		//修改项目
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
		if (resinfo > 0) {
			// 更新前台用户信息
			if (com.getLoginMode().equals("0") || com.getLoginMode().equals("2")) {
				HttpClientUtil.updateUserData(com.getUserId(), false);
			}
			hashmap.put("flag", "success");
			log.info("机构用户["+com.getUserId()+"]更新成功，耗时:"+(System.currentTimeMillis()-time)+"ms");
		} else {
			hashmap.put("flag", "fail");
			log.info("机构用户["+com.getUserId()+"]更新失败，耗时:"+(System.currentTimeMillis()-time)+"ms");
		}
		this.saveOperationLogs(com,"2", req);
		return hashmap;
	}
	
	/**
	 *  注册或者修改机构用户的非空校验
	 * @param dto
	 * @param isBatch false是批量,true是非批量
	 * @return
	 */
	private Map<String,String> getValidate(ResourceDetailedDTO dto,boolean isBatch){
		Map<String,String> hashmap=new HashMap<String,String>();
		boolean flag=true;//判断是否有选中的数据库
		String projectname=dto.getProjectname()==null?"":dto.getProjectname();
		if (StringUtils.isBlank(dto.getValidityEndtime())) {
			hashmap.put("flag", "fail");
			hashmap.put("fail", projectname+"时限结束时间不能为空");
			return hashmap;
		} else if(isBatch){
			if (dto.getProjectType().equals("balance")) {
				if (dto.getTotalMoney() == null) {
					hashmap.put("flag", "fail");
					hashmap.put("fail",  projectname+"金额不能为空");
					return hashmap;
				}
			} else if (dto.getProjectType().equals("count")) {
				if (dto.getPurchaseNumber() == null) {
					hashmap.put("flag", "fail");
					hashmap.put("fail",  projectname+"次数不能为空");
					return hashmap;
				}
			}
		}
		for(ResourceLimitsDTO rldto:dto.getRldto()){
			if(rldto.getResourceid()!=null){
				flag=false;
				break;
			}
		}
		if(flag){
			hashmap.put("flag", "fail");
			hashmap.put("fail", projectname+"必须选择一个数据库");
			return hashmap;
		}
		return hashmap;
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
	 *	服务权限设置跳转
	 */
	@RequestMapping("showAuthority")
	public ModelAndView showAuthority(String msg,String userId) throws Exception {
		ModelAndView view = new ModelAndView();
		WfksAccountidMapping wfks = aheadUserService.getAddauthority(userId,msg);
		WfksUserSetting setting =  aheadUserService.getUserSetting(userId, msg);
		String trial="notTrial";
		if(setting!=null && msg.equals("PartyAdminTime")){			
			Person ps = aheadUserService.queryPersonInfo(setting.getPropertyValue());
			if(ps!=null){				
				ps.setPassword(PasswordHelper.decryptPassword(ps.getPassword()));
				view.addObject("ps", ps);
				String jsonStr = ps.getExtend();
				JSONObject json = JSONObject.fromObject(jsonStr);
				boolean flag = (boolean) json.get("IsTrialPartyAdminTime");
				if (flag) {
					trial = "isTrial";
				}
			}
		}
		view.addObject("trial",trial);
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
		String partyId=authority.getPartyAdmin();
		String oldPartyId=authority.getOldPartyAdmin();
		Person person=null;
		if(!StringUtils.isEmpty(partyId)){
			person=aheadUserService.queryPersonInfo(partyId);
			if (person != null && 4!=person.getUsertype()) {
				map.put("flag", "fail");
				map.put("msg","该用户ID已被使用");//用户id已存在(非全权限类用户)
			}
			if(StringUtils.isEmpty(oldPartyId)||!StringUtils.equals(partyId,oldPartyId)){
				String msg = aheadUserService.validateOldUser(partyId);
				if("false".equals(msg)){
					map.put("flag", "fail");
					map.put("msg","旧平台存在该用户ID");
					return map;
				}
			}
		}
		int result = aheadUserService.setAddauthority(authority,person);
		if (result > 0) {
			map.put("flag", "success");
		} else {
			map.put("flag", "fail");
			if (result == -1) {// 已存在其它类型的用户
				map.put("msg", "该用户ID已被使用");
			}
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
		String adminId = CookieUtil.getCookie(req);
		int i = aheadUserService.personCharge(userId, turnover, reason, adminId, res);
		if(i>0){
			m.put("flag", "success");
		}else{			
			m.put("flag", "fail");
		}
		return m;
    }
	
	
	/**
	 *	个人充值记录
	 */
	@RequestMapping("charge_order")
	public ModelAndView perAward(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/charge_order");
		return view;
	}
	
	
	/**
	 *	个人订单记录
	 */
	@RequestMapping("pay_order")
	public ModelAndView order(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/pay_order");
		return view;
	}
	
	/**
	 *	万方卡绑定记录
	 */
	@RequestMapping("wfcard_bind")
	public ModelAndView wfcard_bind(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/wfcard_bind");
		return view;
	}
	
	
	/**
	 *	充值管理
	 */
	@RequestMapping("pay")
	public ModelAndView pay(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/pay_manager");
		return view;
	}
	
	/**
	 * 添加操作日志信息
	 * @return
	 */
	private void saveOperationLogs(CommonEntity com,String flag,HttpServletRequest req){
		if(com!=null){
			Wfadmin admin =CookieUtil.getWfadmin(req);
			List<ResourceDetailedDTO> list = com.getRdlist();
			if(list!=null &&list.size()>0){
				for(ResourceDetailedDTO dto:list){
					if(dto.getProjectid()!=null){
						if (dto.getProjectType().equals("balance") && dto.getTotalMoney() == 0) {
							continue;
						} else if (dto.getProjectType().equals("count")
								&& dto.getPurchaseNumber() == 0) {
							continue;
						} else if (dto.getProjectType().equals("time")) {
							if (StringUtils.equals(dto.getValidityEndtime(),dto.getValidityEndtime2())
									&& StringUtils.equals(dto.getValidityStarttime(),dto.getValidityStarttime2())) {
								continue;
							}
						}
						OperationLogs op = new OperationLogs();
						op.setUserId(com.getUserId());
						op.setPerson(admin.getWangfang_admin_id());
						op.setOpreation(flag == "2" ? "更新" : "删除");
						if (com.getRdlist() != null) {
							JSONObject json=JSONObject.fromObject(dto);
							//json.remove("rldto");
							op.setReason(json.toString());
						}
						op.setProjectId(dto.getProjectid());
						op.setProjectName(dto.getProjectname());
						opreationLogs.addOperationLogs(op);
					}
				}
			}
		}
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
	
	/**
	 * 校验标准机构是否合法
	 * @param userId
	 * @param orgCode
	 * @param companySimp
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findStandardUnit")
    public Map<String,Object> findStandardUnit(String userId, String orgName, String companySimp) throws Exception {
		log.info("校验标准机构:userId="+userId+",orgName="+orgName+",companySimp="+companySimp);
		Map<String, Object> m = new HashMap<String, Object>();
		if(StringUtils.isEmpty(orgName)&&StringUtils.isEmpty(companySimp)){
			m.put("msg", "参数不存在");
			m.put("result", "-1");
			return m;
		}
		List<StandardUnit> list = aheadUserService.findStandardUnit(orgName, companySimp);//元数据
		if (list.size() == 0) {
			m.put("msg", "机构名称有重复");
			m.put("result", "0");
			return m;
		}
		for (StandardUnit unit : list) {
			if (!userId.equals(unit.getUserId())) {
				if (!StringUtils.isEmpty(orgName) && orgName.equals(unit.getOrgName())) {
					m.put("msg", "机构名称有重复");
					m.put("result", "1");
				} else if (!StringUtils.isEmpty(companySimp)
						&& companySimp.equals(unit.getCompanySimp())) {
					m.put("msg", "机构用户简写有重复");
					m.put("result", "2");
				}else{
					m.put("result", "0");
				}
				return m;
			}
		}
		m.put("result", "0");
		return m;
	}
	
}