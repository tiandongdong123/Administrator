package com.wf.controller;

import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import com.exportExcel.ExportExcel;
import com.redis.RedisUtil;
import com.utils.AuthorityLimit;
import com.utils.CookieUtil;
import com.utils.DateUtil;
import com.utils.HttpClientUtil;
import com.utils.IPConvertHelper;
import com.utils.InstitutionUtils;
import com.utils.Organization;
import com.utils.SettingUtil;
import com.utils.SolrThread;
import com.utils.WFMailUtil;
import com.wanfangdata.cas.utils.JsonHelper;
import com.wanfangdata.encrypt.PasswordHelper;
import com.wanfangdata.grpcchannel.BindAccountChannel;
import com.wanfangdata.grpcchannel.GrpcServer;
import com.wanfangdata.rpc.bindauthority.ServiceResponse;
import com.wanfangdata.rpc.messagequeue.ProducerServiceGrpc;
import com.wanfangdata.rpc.messagequeue.SendRequest;
import com.wanfangdata.rpc.messagequeue.SendResponse;
import com.wf.bean.Authority;
import com.wf.bean.BindAuthorityModel;
import com.wf.bean.InstitutionalUser;
import com.wf.bean.Log;
import com.wf.bean.OperationLogs;
import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.bean.Query;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.StandardUnit;
import com.wf.bean.UserInstitution;
import com.wf.bean.UserIp;
import com.wf.bean.WarningInfo;
import com.wf.bean.Wfadmin;
import com.wf.bean.WfksAccountidMapping;
import com.wf.bean.WfksUserSetting;
import com.wf.service.AheadUserService;
import com.wf.service.LogService;
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

	@Autowired
	private LogService logService;

	@Autowired
	private WFMailUtil wfMailUtil;

	@Autowired
	private BindAccountChannel bindAccountChannel;

	private RedisUtil redis = new RedisUtil();
	
	private static Logger log = Logger.getLogger(AheadUserController.class);
	private String[] channelid=new String[]{"GBalanceLimit","GTimeLimit"};
	//二维码邮箱是否勾选
	private static String DEFAULT = "default";
	private static String UNSELECT = "unselect";
	/**
	 *	判断ip段是否重复
	 */
	@RequestMapping("validateip")
	@ResponseBody
	public JSONObject validateIp(String ip,String userId){
		long time=System.currentTimeMillis();
		JSONObject map = new JSONObject();
		StringBuffer sb = new StringBuffer();
		StringBuffer sbf = new StringBuffer();
		 Map<String,String> maps = new LinkedHashMap<String,String>();
		String [] str = ip.split("\n");	
		//校验<数据库>是否存在IP重复
		List<UserIp> list=new ArrayList<UserIp>();
		for(int i = 0; i < str.length; i++){		
			String beginIp = str[i].substring(0, str[i].indexOf("-"));
			String endIp = str[i].substring(str[i].indexOf("-")+1, str[i].length());
			long begin=IPConvertHelper.IPToNumber(beginIp);
			long end=IPConvertHelper.IPToNumber(endIp);
			if(begin>end){
				map.put("flag", "true");
				map.put("errorIP", beginIp+"-"+endIp+" 开始IP大于结束IP");
			}
			UserIp user=new UserIp();
			user.setUserId(userId);
			user.setBeginIpAddressNumber(begin);
			user.setEndIpAddressNumber(end);
			list.add(user);
		}
		if(map.size()==0){
			List<Map<String,Object>> bool = aheadUserService.validateIp(list);
			if(bool.size()>0){
				int index=1;
				for(Map<String,Object> mbo : bool){
					if(StringUtils.equals(String.valueOf(mbo.get("userId")), userId)){
						continue;
					}
					String userid=mbo.get("userId").toString();
					long end=Long.parseLong(String.valueOf(mbo.get("endIpAddressNumber")));
					long begin=Long.parseLong(String.valueOf(mbo.get("beginIpAddressNumber")));
					if(mbo.get("userType")==null||mbo.get("loginCode")==null){
						continue;
					}
					int userType=Integer.parseInt(String.valueOf(mbo.get("userType")));
					int loginCode=Integer.parseInt(String.valueOf(mbo.get("loginCode")));
					if(userType!=2||loginCode!=0){
						continue;
					}
					for(UserIp src:list){
						if(src.getBeginIpAddressNumber()<=end&&src.getEndIpAddressNumber()>=begin){
							maps.put(IPConvertHelper.NumberToIP(src.getBeginIpAddressNumber())
									+"-"+IPConvertHelper.NumberToIP(src.getEndIpAddressNumber())+"</br>", "");
							sb.append("("+(index++)+") "+userid+"， "+IPConvertHelper.NumberToIP(begin)
									+"-"+IPConvertHelper.NumberToIP(end)+"</br>");
						}
					}
				}
				if(maps.size()>0){
					map.put("flag", "true");
					map.put("userId",userId);
					for (String key : maps.keySet()) {
						sbf.append(key);
					}
					map.put("errorIP", sbf.toString());
					map.put("tableIP", sb.toString());
				}
			}
		}
		if(map.size()==0){
			map.put("flag", "false");
		}
		log.info("IP校验："+userId+" "+ip.replace("\n", ",")+"耗时"+(System.currentTimeMillis()-time)+"ms");
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
	
	@RequestMapping("getPerson")
	@ResponseBody
	public Map<String,String> getPerson(String userId){
		InstitutionalUser user=new InstitutionalUser();
		user.setUserId(userId);
		Map<String,String> errorMap = new HashMap<String, String>();
		try {
			errorMap=this.userValidate(user, errorMap);
		} catch (Exception e) {
			log.error("查询异常：",e);
		}
		return errorMap;
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
				HttpClientUtil.updateUserData(aid, "1");
			} else if ("2".equals(flag)) { //解冻
				redis.del(12, aid);
				HttpClientUtil.updateUserData(aid, "0");
			}
			SolrThread.setFreeze(aid,flag);
			return "true";
		}
		return "false";
	}
	
	/**
	 *	移除管理员
	 */
	@RequestMapping("deladmin")
	@ResponseBody
	public String deladmin(String userId,String pid){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("pid", "");
		int resinfo = aheadUserService.updatePid(map);
		InstitutionalUser com=new InstitutionalUser();
		com.setUserId(userId);
		aheadUserService.setPartAccountRestriction(com);
		aheadUserService.addUserIns(com);
		SolrThread.removeAdmin(userId,com);
		if(resinfo>0){
			return "true";
		}
		return null;
	}
	
	
	/**
	 *	跳转添加管理员
	 */
	@RequestMapping("goaddadmin")
	public ModelAndView go(ModelAndView view,String userId,String institution){
		view.addObject("userId", userId);
		view.addObject("institution", institution);
		view.setViewName("/page/usermanager/add_admin");
		return view;
	}
	
	/**
	 *	添加管理员/修改
	 */
	@RequestMapping("addadmin")
	@ResponseBody
	public String addadmin(InstitutionalUser com){
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(com.getAdminname())){
			Person per=aheadUserService.queryPersonInfo(com.getAdminname());
			if(per==null){
				aheadUserService.addRegisterAdmin(com);
			}else if(per.getUsertype()!=1){
				return "false";
			}else{
				aheadUserService.updateRegisterAdmin(com);
			}
			if(StringUtils.isNotBlank(com.getAdminIP())){
				aheadUserService.deleteUserIp(com.getAdminname());
				aheadUserService.addUserAdminIp(com);
			}
			map.put("pid", com.getAdminname());
		}else{
			map.put("pid", com.getAdminOldName());
		}
		// 机构子账号权限
		aheadUserService.setPartAccountRestriction(com);
		// 统计分析权限
		aheadUserService.addUserIns(com);
		map.put("userId", com.getUserId());
		int resinfo = aheadUserService.updatePid(map);
		SolrThread.addAdmin(com.getUserId(),String.valueOf(map.get("pid")),com);
		if(resinfo>0){
			return "true";
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
	@RequestMapping("findPerioSubject")
	@ResponseBody
	public Map<String,Object> findPerioSubject(String num){
		String str = redis.get("PerioInfoDic",13);
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
	public String updateWarning(String flag, Integer amountthreshold, Integer datethreshold,
			Integer remindtime, String remindemail, Integer countthreshold,HttpServletRequest request) {
		
		Log log=null;
		String operation_content="金额阈值:"+amountthreshold+",次数阈值:"+countthreshold+",有效期阈值:"+datethreshold+",邮件提醒间隔时间:"+remindtime+",提醒邮箱:"+remindemail;
		int i = 0;
		if("1".equals(flag)){
			i = aheadUserService.updateWarning(amountthreshold,datethreshold,remindtime,remindemail,countthreshold,null);
			log=new Log("机构用户预警设置","修改",operation_content,request);
		}else{
			i = aheadUserService.addWarning(amountthreshold,datethreshold,remindtime,remindemail,countthreshold);
			log=new Log("机构用户预警设置","增加",operation_content,request);
		}
		logService.addLog(log);
		return i > 0 ? "true" : "false";
	}
	
	/**
	 * 查询所有数据库信息
	 */
	@RequestMapping("getdata")
	@ResponseBody
	public List<Map<String, Object>> findDataManage(String proid) {
		long time = System.currentTimeMillis();
		List<Map<String, Object>> list = aheadUserService.selectListByRid(proid);
		log.info("getdata耗时：" + (System.currentTimeMillis() - time) + "ms");
		return list;
	}
	
	/**
	 * 获取地区
	 */
	@RequestMapping("getRegion")
	@ResponseBody
	public JSONArray getRegion(HttpServletResponse httpResponse){
		return SettingUtil.getRegionCode();
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
	 *	机构用户注册
	 * @throws ParseException
	 */
	@RequestMapping("registerInfo")
	@ResponseBody
	public Map<String, String> registerInfo(InstitutionalUser user,
			BindAuthorityModel bindAuthorityModel, ModelAndView view, HttpServletRequest req,HttpServletResponse res) {

		long time = System.currentTimeMillis();
		Map<String, String> errorMap = new HashMap<String, String>();
		try {
			// 机构注册验证
			errorMap = this.registerValidate(user, errorMap);
			if (errorMap.size() > 0) {
				return errorMap;
			}
			// 添加机构相关信息
			aheadUserService.registerInfo(user);
			//发送solr
			SolrThread.registerInfo(user,true);
			// 添加购买项目
			String msg=this.addProject(user, req);
			if(msg.length()>0&&msg.contains("失败")){
				errorMap.put("flag", "error");
				errorMap.put("fail", msg);
				return errorMap;
			}
			// 个人信息绑定
			this.addBindAuthorityModel(user, bindAuthorityModel);
			// 添加日志
			this.addLogInfo(user.getUserId() + "注册成功", time);
			// 添加数据库统计日志
			this.addOperationLogs(user, "注册", req);
			errorMap.put("flag", "success");
			return errorMap;
		} catch (Exception e) {
			log.error("注册异常：", e);
			errorMap.put("flag", "fail");
			errorMap.put("fail", "注册机构用户异常");
			return errorMap;
		}
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
	 *	机构用户批量注册
	 */
	@RequestMapping("addbatchRegister")
	@ResponseBody
	public Map<String, Object> addbatchRegister(MultipartFile file, InstitutionalUser user,
			BindAuthorityModel bindAuthorityModel, ModelAndView view, HttpServletRequest req,HttpServletResponse res) {
		
		long time=System.currentTimeMillis();
		Map<String,Object> errorMap = new HashMap<>();
		List<Map<String,String>> errorList=new ArrayList<>();
		int sucNum = 0;
		int allNum=0;
		try{
			List<Map<String, Object>> userList = aheadUserService.getExcelData(file,errorMap,errorList);
			if(errorMap.size()>0){
				return errorMap;
			}
			errorMap=this.batchRegisterValidate(userList, user,errorList);
			if(errorMap.size()>0){
				return errorMap;
			}
			if(errorList.size()>0){
				errorMap.put("flag", "list");
				errorMap.put("fail", errorList);
				return errorMap;
			}
			allNum = userList.size();
			for (Map<String, Object> map : userList) {
				// Excel表格中部分账号信息
				String userId=map.get("userId").toString();
				user.setInstitution(map.get("institution").toString());
				user.setUserId(userId);
				user.setPassword(String.valueOf(map.get("password")));
				// 添加机构信息
				aheadUserService.batchRegisterInfo(user, map);
				//发送solr
				SolrThread.registerInfo(user,true);
				//导入金额和次数
				InstitutionUtils.importData(user,map);
				// 添加购买项目
				String msg=this.addProject(user, req);
				if(msg.length()>0&&msg.contains("失败")){
					errorMap.put("flag", "error");
					errorMap.put("fail", "执行到机构账号"+user.getUserId()+"异常:</br>"+msg);
					return errorMap;
				}
				bindAuthorityModel.setUserId(userId);
				if (bindAuthorityModel.getOpenState()!=null&&bindAuthorityModel.getOpenState()){
					aheadUserService.openBindAuthority(bindAuthorityModel);
					log.info("成功开通个人绑定机构权限");
					String email = bindAuthorityModel.getEmail();
                    List<String> userIdList = new ArrayList<>();
                    // 发送邮箱
                    try {
                        if (bindAuthorityModel.getSend()) {
                        	userIdList.add(userId);
                            if (wfMailUtil.sendQRCodeMail(email, userIdList, bindAccountChannel)) {
                                log.info("机构用户批量注册，发送邮件成功，userIdList：" + userIdList
                                        + userIdList.toString() + "，email:" + email);
                            } else {
                            	log.info("发送邮件失败");
                                //throw new Exception("发送邮件失败");
                            }
                        }
                    } catch (Exception e) {
                        log.error("机构用户批量注册，发送邮箱出现异常！，userIdList：" + userId + "，email:" + email, e);
                    }
                }
				this.addOperationLogs(user, "批量注册", req);
				log.info("机构用户[" + user.getUserId() + "]注册成功");
				sucNum++;
			}
			errorMap.put("flag", "success");
			errorMap.put("success", "成功导入："+sucNum+"条");
			log.info("批量注册成功："+sucNum+"条，耗时:"+(System.currentTimeMillis()-time)+"ms");
		}catch(Exception e){
			errorMap.put("flag", "fail");
			errorMap.put("fail", "注册成功"+sucNum+"条,未注册成功"+(allNum-sucNum)+"条");
			log.error("机构用户批量注册异常：",e);
		}
		return errorMap;
	}

	//批量添加校验
	private Map<String,Object> batchRegisterValidate(List<Map<String, Object>> userList,InstitutionalUser user,List<Map<String,String>> errorList) throws Exception{
		Map<String,Object> errorMap = new HashMap<>();
		errorMap=InstitutionUtils.getBatchRegisterValidate(user,userList,true,errorList);
		if(errorMap.size()>0){
			return errorMap;
		}
		this.getUserValidate(user, userList,errorList);
		// 校验机构管理员
		Map<String,String> error = new HashMap<>();
		this.adminValidate(user, error);
		if (error.size() > 0) {
			String fail=(String) error.get("fail");
			InstitutionUtils.addErrorMap(user.getUserId(),user.getInstitution(),fail,errorList);
		}
		errorMap.clear();
		return errorMap;
	}
	
	//批量验证excel中的机构是否存在
	private void getUserValidate(InstitutionalUser user,
			List<Map<String, Object>> userList,List<Map<String,String>> errorList) {
		for(Map<String, Object> map : userList){
			String userId=String.valueOf(map.get("userId"));
			String institution=String.valueOf(map.get("institution"));
			Person p = aheadUserService.queryPersonInfo(userId);
			String msg = aheadUserService.validateOldUser(userId);
			if(p!=null){
				String fail="该机构ID已存在，请重新输入机构ID";
				InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
			}
			if(msg.equals("old")){
				String fail="该机构ID在老平台已存在，请重新输入机构ID";
				InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
			}
			if(msg.equals("error")){
				String fail="旧平台检验机构ID出现异常";
				InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
			}		
		}
	}

	/**
	 *	机构用户批量修改
	 */
	@RequestMapping("batchupdate")
	public ModelAndView batchUpdate(){
		ModelAndView view = new ModelAndView();
		view.addObject("org", Organization.values());//机构账户类型
		view.setViewName("/page/usermanager/ins_batchupdate");
		return view;
	}
	
	/**
	 *	机构用户批量更新
	 */
	@RequestMapping("updatebatchregister")
	@ResponseBody
	public Map<String, Object> updateBatchRegister(MultipartFile file, InstitutionalUser user,
			BindAuthorityModel bindAuthorityModel, ModelAndView view, HttpServletRequest req,HttpServletResponse res) throws Exception {
		
		long time=System.currentTimeMillis();
		Map<String, Object> errorMap = new HashMap<>();
		int sucNum = 0;
		int allNum=0;
		try{
			if (file == null || file.isEmpty()) {
				errorMap.put("flag", "fail");
				errorMap.put("fail", "请上传附件");
				return errorMap;
			}
			List<Map<String,String>> errorList=new ArrayList<>();
			List<Map<String, Object>> userList = aheadUserService.getExcelData(file,errorMap,errorList);
			if(errorMap.size()>0){
				return errorMap;
			}
			errorMap = this.batchUpdateValidate(userList,user,errorList);
			if (errorMap.size() > 0) {
				return errorMap;
			}
			if(errorList.size()>0){
				errorMap.put("flag", "list");
				errorMap.put("fail", errorList);
				return errorMap;
			}
			allNum = userList.size();
			for (Map<String, Object> map : userList) {
				//userId
				String oldLoginModel=aheadUserService.queryPersonInfo(map.get("userId").toString()).getLoginMode().toString();
				
				// 修改机构用户
				aheadUserService.batchUpdateInfo(user, map);
				//发送solr
				SolrThread.updateInfo(user);
				/*Object str="userId:"+user.getUserId()
						+",loginModel:"+oldLoginModel
						+",updateLaterLoginModel:"+user.getLoginMode();*/
				if(!oldLoginModel.equals(user.getLoginMode())){
					JSONObject json=new JSONObject();
					json.put("userId", user.getUserId());
					json.put("loginModel", oldLoginModel);
					json.put("updateLaterLoginModel",user.getLoginMode());
					String key =user.getUserId()+"_"+oldLoginModel+"to"+user.getLoginMode()+"_"+System.currentTimeMillis();
					sendMessage("GroupLoginModeModify",key,json);
				}
				
				//导入金额和次数
				InstitutionUtils.importData(user,map);
				// 修改购买项目
				String msg=this.updateProject(user, req, null);
				if(msg.length()>0&&msg.contains("失败")){
					errorMap.put("flag", "error");
					errorMap.put("userId", map.get("userId")+"");
					errorMap.put("fail", "执行到机构账号"+user.getUserId()+"异常:</br>"+msg);
					return errorMap;
				}
				// 修改或开通个人绑定机构权限
				String userId=map.get("userId").toString();
				this.updateBindAuthorityModel(bindAuthorityModel, userId);
				// 添加日期
				this.addOperationLogs(user, "批量修改", req);
				log.info("机构用户[" + user.getUserId() + "]修改成功");
				sucNum++;
			}
			errorMap.put("flag", "success");
			errorMap.put("success", "成功更新："+sucNum+"条");
			log.info("批量修改成功："+sucNum+"条，耗时:"+(System.currentTimeMillis()-time)+"ms");
		}catch(Exception e){
			errorMap.put("flag", "fail");
			errorMap.put("fail", "成功更新"+sucNum+"条,更新失败"+(allNum-sucNum)+"条");
			log.error("机构用户批量更新", e);
		}
		return errorMap;
	}
	
	//修改个人绑定机构
	private void updateBindAuthorityModel(BindAuthorityModel bindAuthorityModel,String userId) throws Exception {
		if(bindAuthorityModel.getOpenState()==null){
			return;
		}
		//修改或开通个人绑定机构权限
		bindAuthorityModel.setUserId(userId);
		if (bindAuthorityModel.getOpenState()!=null&&bindAuthorityModel.getOpenState()){
			ServiceResponse response =  aheadUserService.editBindAuthority(bindAuthorityModel);
			if (response.getServiceResult()==false){
				log.info(response.getResultMessage());
			}
			String email = bindAuthorityModel.getEmail();
			//发送邮箱
            try {
                if (bindAuthorityModel.getSend()) {
                	 List<String> userIdList = new ArrayList<>();
                	userIdList.add(userId);
                    if (wfMailUtil.sendQRCodeMail(email, userIdList, bindAccountChannel)) {
                        log.info("机构用户批量更新，发送邮件成功，userIdList：+userIdList：" + userIdList.toString() + "，email:" + email);
                        //hashmap.put("emailFlag", "success");
                    } else {
                    	log.info("发送邮件失败");
                        //throw new Exception("发送邮件失败");
                    }
                }
            } catch (Exception e) {
                log.error("机构用户批量更新，发送邮箱出现异常！userIdList:" + userId
                        + "，email:" + email, e);
                //hashmap.put("emailFlag", "fail");
            }
		}else {
			int count = aheadUserService.getBindAuthorityCount(bindAuthorityModel.getUserId());
			if (count>0){
				aheadUserService.closeBindAuthority(bindAuthorityModel);
			}
		}
	}
	
	//批量添加校验
	private Map<String,Object> batchUpdateValidate(List<Map<String, Object>> userList,InstitutionalUser user,List<Map<String,String>> errorList) throws Exception{
		Map<String,Object> errorMap = new HashMap<>();
		errorMap=InstitutionUtils.getBatchRegisterValidate(user, userList,false,errorList);
		if(errorMap.size()>0){
			return errorMap;
		}
		this.getUpdateUserValidate(user, userList,errorList);
		if(errorMap.size()>0){
			return errorMap;
		}
		// 校验机构管理员
		Map<String,String> error=new HashMap<String,String>();
		this.adminValidate(user, error);
		if (error.size() > 0) {
			String fail=error.get("fail");
			InstitutionUtils.addErrorMap(user.getUserId(),user.getInstitution(),fail,errorList);
		}
		errorMap.clear();
		return errorMap;
	}
	
	//批量修改验证excel中的机构是否存在
	private void getUpdateUserValidate(InstitutionalUser user,
			List<Map<String, Object>> userList,List<Map<String, String>> errorList) throws Exception{
		Map<String, String> errorMap = new HashMap<String, String>();
		for(Map<String, Object> map : userList){
			//用户是否存在
			String userId=map.get("userId").toString();
			String institution=map.get("institution").toString();
			Person ps = aheadUserService.queryPersonInfo(userId);
			if(ps==null){
				String fail=userId+"该用户不存在";
				errorMap.put("flag", "fail");
				errorMap.put("fail", fail);
				InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
				continue;
			}else if(ps.getLoginMode()==0){
				if("1".equals(user.getLoginMode())){//用户名密码
					String fail="机构"+userId+"的登录方式不匹配，不为用户名密码";
					InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
				}else if("2".equals(user.getLoginMode())){//用户名密码+IP
					String fail="机构"+userId+"的登录方式不匹配，不为用户名密码+IP";
					InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
				}
			}
			if ("2".equals(user.getLoginMode())) {
				String ip = (String) map.get("ip");
				if (StringUtils.isBlank(ip)) {
					List<Map<String, Object>> ls = aheadUserService.listIpByUserId(userId);
					if (ls == null || ls.size() == 0) {
						String fail=userId + "未添加有效IP段";
						InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
					}
				}
				WfksAccountidMapping[] mapping = aheadUserService.getWfksAccountid(userId,"openApp");
				if (mapping.length > 0) {
					String fail="机构账号"+userId+"开通了APP嵌入服务，登录方式不能为用户名密码+IP";
					InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
				}
				mapping = aheadUserService.getWfksAccountid(userId, "openWeChat");
				if (mapping.length > 0) {
					String fail="机构账号"+userId+"开通了微信公众号嵌入服务，登录方式不能为用户名密码+IP";
					InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
				}
			}
			List<ResourceDetailedDTO> rdList = user.getRdlist();
			List<Map<String, Object>> lm =  (List<Map<String, Object>>) map.get("projectList");
			for(ResourceDetailedDTO dto : rdList){
				for(Map<String, Object> pro : lm) {
					if (!StringUtils.equals(dto.getProjectid(),String.valueOf(pro.get("projectid")))) {
						continue;
					}
					// 验证金额是否正确
					String ptype = dto.getProjectType();
					if (StringUtils.isEmpty(user.getResetMoney()) && ptype.equals("balance")
							|| StringUtils.isEmpty(user.getResetCount()) && ptype.equals("count")) {
						user.setUserId(userId);
						if(ptype.equals("balance")){
							dto.setTotalMoney(pro.get("totalMoney").toString());
						}else if(ptype.equals("count")){
							dto.setPurchaseNumber(pro.get("totalMoney").toString());
						}
						Double val = aheadUserService.checkValue(user, dto);
						if (val == -Double.MAX_VALUE) {
							if((NumberUtils.toDouble(dto.getTotalMoney()) <= 0 && ptype.equals("balance")) || (NumberUtils.toInt(dto.getPurchaseNumber()) <= 0 && ptype.equals("count"))){
								String fail="账号"+userId+"的"+dto.getProjectname()+(ptype.equals("balance") ? "金额输入不正确，请正确填写金额" : "次数输入不正确，请正确填写次数");
								InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
							}
						}else if (val < 0) {
							String fail="账号"+userId+"的"+dto.getProjectname()+(ptype.equals("balance") ? "剩余金额不能小于0，请正确填写金额" : "剩余次数不能小于0，请正确填写次数");
							InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
						}else if(val>InstitutionUtils.maxData){
							String fail="账号"+userId+"的"+dto.getProjectname()+(ptype.equals("balance") ? "修改后的金额大于最大值，请正确填写金额" : "修改后的次数大于最大值，请正确填写次数");
							InstitutionUtils.addErrorMap(userId,institution,fail,errorList);
						}
					}
				}
			}
		}
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
	public Map<String,Object> blockUnlock(MultipartFile file,String radio,HttpServletRequest request){
		
		String operation_content="";
		Map<String,Object> hashmap = new HashMap<String, Object>();
		List<String> list = aheadUserService.getExceluser(file);
		int in = 0;
		for(String str : list){
			Person person = aheadUserService.queryPersonInfo(str);
			if(person!=null){
				operation_content+=str;
				int i = aheadUserService.updateUserFreeze(str,radio);
				if(i>0){
					if ("1".equals(radio)) { //冻结
						redis.set(str, "true", 12);
						redis.expire(str, 3600 * 24, 12); //设置超时时间
						HttpClientUtil.updateUserData(str, "1");
					} else if ("2".equals(radio)) { //解冻
						redis.del(12, str);
						HttpClientUtil.updateUserData(str, "0");
					}
					SolrThread.setFreeze(str, radio);
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
		
		Log log=new Log("批量账号冻结/解冻","1".equals(radio)?"冻结":"解冻",operation_content,request);
		logService.addLog(log);
		
		return hashmap;
	}
	
	/**
	 *	机构信息列表（查询机构下管理员）此方法已经废弃
	 */
	@RequestMapping("findInstitutionAllUser")
	@ResponseBody
	public Map<String,String> findInstitutionAllUser(String institution){
		//查询机构下机构管理员列表
		List<Person> list=aheadUserService.findInstitutionAllUser(institution);
		Map<String,String> idMap=new HashMap<String,String>();
		for(Person per:list){
			String userId=per.getUserId();
			if(per.getUsertype()==1){
				idMap.put("admin", idMap.get("admin")==null?userId:(idMap.get("admin")+","+userId));
			}else if(per.getUsertype()==2){
				idMap.put("user", idMap.get("user")==null?userId:(idMap.get("user")+","+userId));
			}
		}
		return idMap;
	}
	
	
	/**
	 *	机构信息列表（查询机构下管理员）此方法已经废弃
	 */
	@RequestMapping("findins")
	@ResponseBody
	public List<Map<String, Object>> findInstitutionAdmin(String institution,String userId){
		//查询机构下机构管理员列表
		return aheadUserService.findInstitutionAdmin(institution,userId);
	}
	
	/**
	 * 更新机构名称
	 */
	@RequestMapping("updateins")
	@ResponseBody
	public Map<String, Object> updateins(String institution,String oldins){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			aheadUserService.updateInstitution(institution,oldins);
			//更新solr
			SolrThread.updateInstitution(institution, oldins);
			map.put("flag", "true");
		}catch(Exception e){
			log.error("更新机构名称异常:",e);
			map.put("flag", "false");
		}
		return map;
	}
	
	/**
	 *获取用户信息
	 */
	@RequestMapping("getAdmin")
	@ResponseBody
	public Map<String, Object> getAdmin(String userId){
		return aheadUserService.findInfoByPid(userId);
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
	@RequestMapping("information")
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
	 *	账号修改
	 */
	@RequestMapping("updateinfo")
	@ResponseBody
	public Map<String, String> updateinfo(InstitutionalUser user, BindAuthorityModel bindAuthorityModel,
			HttpServletRequest req, HttpServletResponse res) {
		
		long time=System.currentTimeMillis();
		Map<String,String> errorMap = new HashMap<String, String>();
		try{
			String oldLoginModel=aheadUserService.queryPersonInfo(user.getUserId()).getLoginMode().toString();
			
			List<String> delList = new ArrayList<String>();
			// 机构修改校验
			errorMap = this.updateValidate(user, delList);
			if (errorMap.size() > 0) {
				return errorMap;
			}
			// 修改机构信息
			aheadUserService.updateinfo(user);
			//发送solr
			SolrThread.registerInfo(user,false);
			/*Object str="userId:"+user.getUserId()
					+",loginModel:"+oldLoginModel
					+",updateLaterLoginModel:"+user.getLoginMode();*/
			if(!oldLoginModel.equals(user.getLoginMode())){
				JSONObject json=new JSONObject();
				json.put("userId", user.getUserId());
				json.put("loginModel", oldLoginModel);
				json.put("updateLaterLoginModel",user.getLoginMode());
				String key =user.getUserId()+"_"+oldLoginModel+"to"+user.getLoginMode()+"_"+System.currentTimeMillis();
				sendMessage("GroupLoginModeModify",key,json);
			}
			// 修改购买项目
			String msg=this.updateProject(user, req, delList);
			if(msg.length()>0&&msg.contains("失败")){
				errorMap.put("flag", "error");
				errorMap.put("fail", msg);
				return errorMap;
			}
			// 个人信息绑定
			this.updateBindAuthorityModel(user, bindAuthorityModel);
			// 添加日志
			this.addLogInfo(user.getUserId() + "更新成功", time);
			// 添加数据库统计日志
			this.addOperationLogs(user, "更新", req);
			errorMap.put("flag", "success");
			return errorMap;
		}catch(Exception e){
			log.error("机构账号修改异常：", e);
			errorMap.put("flag", "fail");
			errorMap.put("fail", "机构账号修改异常");
			return errorMap;
		}
	}
	
	//机构修改验证
	private Map<String,String> updateValidate(InstitutionalUser user,List<String> delList) throws Exception{
		List<ResourceDetailedDTO> list=new ArrayList<ResourceDetailedDTO>();
		Map<String,String> errorMap = InstitutionUtils.getUpdateValidate(user, delList, list);
		if (errorMap.size() > 0) {
			return errorMap;
		}
		// 验证金额是否正确
		for (ResourceDetailedDTO dto : list) {
			String ptype = dto.getProjectType();
			if (ptype.equals("balance") || ptype.equals("count")) {
				Double val = aheadUserService.checkValue(user, dto);
				if (val == -Double.MAX_VALUE) {
					if((NumberUtils.toDouble(dto.getTotalMoney()) <= 0 && ptype.equals("balance")) || (NumberUtils.toInt(dto.getPurchaseNumber()) <= 0 && ptype.equals("count"))){
						errorMap.put("flag", "fail");
						errorMap.put("fail", dto.getProjectname()+(ptype.equals("balance") ? "金额输入不正确，请正确填写金额" : "次数输入不正确，请正确填写次数"));
						return errorMap;
					}
				}else if (val < 0) {
					errorMap.put("flag", "fail");
					errorMap.put("fail", dto.getProjectname()+(ptype.equals("balance") ? "剩余金额不能小于0，请正确填写金额" : "剩余次数不能小于0，请正确填写次数"));
					return errorMap;
				}else if(val>InstitutionUtils.maxData){
					errorMap.put("flag", "fail");
					errorMap.put("fail", dto.getProjectname()+(ptype.equals("balance") ? "修改后的金额大于最大值，请正确填写金额" : "修改后的次数大于最大值，请正确填写次数"));
					return errorMap;
				}
			}
		}
		// 校验机构管理员
		this.adminValidate(user, errorMap);
		if (errorMap.size() > 0) {
			return errorMap;
		}
		// 校验党建管理
		this.partyAdminValidate(user, errorMap);
		if (errorMap.size() > 0) {
			return errorMap;
		}
		user.setRdlist(list);
		return errorMap;
	}

	//添加机构用户购买项目
	private String addProject(InstitutionalUser user,HttpServletRequest req) throws Exception{
		String adminId = CookieUtil.getCookie(req);
		StringBuilder right=new StringBuilder();
		StringBuilder wrong=new StringBuilder();
		user.setResetCount("true");
		user.setResetMoney("true");
		for(ResourceDetailedDTO dto : user.getRdlist()){
			if(dto.getProjectType().equals("balance")){
				//增加余额信息
				if(aheadUserService.chargeProjectBalance(user, dto,adminId) > 0){
					aheadUserService.addProjectResources(user, dto);
					right.append(dto.getProjectname()+"添加成功</br>");
				}else{
					wrong.append(dto.getProjectname()+"添加失败</br>");
				}
			}else if(dto.getProjectType().equals("time")){
				//增加限时信息
				if(aheadUserService.addProjectDeadline(user, dto,adminId) > 0){
					aheadUserService.addProjectResources(user, dto);
					right.append(dto.getProjectname()+"添加成功</br>");
				}else{
					wrong.append(dto.getProjectname()+"添加失败</br>");
				}
			}else if(dto.getProjectType().equals("count")){
				//增加次数信息
				if(aheadUserService.chargeCountLimitUser(user, dto,adminId) > 0){
					aheadUserService.addProjectResources(user, dto);
					right.append(dto.getProjectname()+"添加成功</br>");
				}else{
					wrong.append(dto.getProjectname()+"添加失败</br>");
				}
			}
		}
		return right.append(wrong).toString();
	}
	
	//个人信息绑定
	private void addBindAuthorityModel(InstitutionalUser user,BindAuthorityModel bindAuthorityModel) throws Exception{
		HttpClientUtil.updateUserData(user.getUserId(), user.getLoginMode());// 更新前台用户信息
		if (bindAuthorityModel.getOpenState() != null && bindAuthorityModel.getOpenState()) {
			aheadUserService.openBindAuthority(bindAuthorityModel);// 成功开通个人绑定机构权限
			String userId = bindAuthorityModel.getUserId();
			String email = bindAuthorityModel.getEmail();
			List<String> userIdList=new ArrayList<>();
			userIdList.add(userId);
			//发送邮箱
			try {
				if (bindAuthorityModel.getSend()) {
					if (wfMailUtil.sendQRCodeMail(email, userIdList, bindAccountChannel)) {
						log.info("机构用户注册，发送邮件成功，userIdList：" + userIdList.toString() + "，email:" + email);
					} else {
						//throw new Exception("发送邮件失败");
						log.error("发送邮件失败");
					}
				}
			} catch (Exception e) {
				log.error("机构用户注册，发送邮箱出现异常！userIdList：" + userIdList.toString() + "，email:" + email, e);
			}
		}
	}
	
	//个人信息绑定修改
	private void updateBindAuthorityModel(InstitutionalUser user,BindAuthorityModel bindAuthorityModel) throws Exception{
		HttpClientUtil.updateUserData(user.getUserId(), user.getLoginMode());// 更新前台用户信息
		//修改或开通个人绑定机构权限
		if (bindAuthorityModel.getOpenState()!=null&&bindAuthorityModel.getOpenState()){
			ServiceResponse response =  aheadUserService.editBindAuthority(bindAuthorityModel);
			if (response.getServiceResult()==false){
				log.error("修改个人绑定机构权限失败");
			}
		} else {
			int count = aheadUserService.getBindAuthorityCount(bindAuthorityModel.getUserId());
			if (count > 0) {
				aheadUserService.closeBindAuthority(bindAuthorityModel);
			}
		}
		String userId = bindAuthorityModel.getUserId();
		String email = bindAuthorityModel.getEmail();
		//发送邮箱
		try {
			if (bindAuthorityModel.getSend()) {
				List<String> userIdList=new ArrayList<>();
				userIdList.add(userId);
				if (wfMailUtil.sendQRCodeMail(email, userIdList, bindAccountChannel)) {
					log.info("账号修改，发送邮件成功，userId：+userId：" + userId + "，email:" + email);
				} else {
					//throw new Exception("发送邮件失败");
					log.info("发送邮件失败");
				}
			}
		} catch (Exception e) {
			log.error("账号修改，发送邮箱出现异常！userId：" + userId + "，email:" + email, e);
		}
	}
	
	//修改机构用户购买项目
	private String updateProject(InstitutionalUser com,HttpServletRequest req,List<String> delList) throws Exception{
		String adminId = CookieUtil.getCookie(req);
		// 删除项目
		if (delList!=null&&delList.size() > 0) {
			this.removeproject(req, delList);
		}
		if (StringUtils.equals(com.getChangeFront(), "GTimeLimit")
				|| StringUtils.equals(com.getChangeFront(), "GBalanceLimit")) {
			if (aheadUserService.deleteChangeAccount(com, adminId) > 0) {
				aheadUserService.deleteResources(com.getUserId(), com.getChangeFront());
			}
		}
		StringBuilder right=new StringBuilder();
		StringBuilder wrong=new StringBuilder();
		for(ResourceDetailedDTO dto : com.getRdlist()){
			if(dto.getProjectid()!=null){
				if(dto.getProjectType().equals("balance")){
					if(aheadUserService.chargeProjectBalance(com, dto, adminId)>0){
						aheadUserService.deleteResources(com,dto,false);
						aheadUserService.updateProjectResources(com, dto);
						right.append(dto.getProjectname()+"添加成功</br>");
					}else{
						wrong.append(dto.getProjectname()+"添加失败</br>");
					}
				}else if(dto.getProjectType().equals("time")){
					//增加限时信息
					if(aheadUserService.addProjectDeadline(com, dto,adminId)>0){
						aheadUserService.deleteResources(com,dto,false);
						aheadUserService.updateProjectResources(com, dto);
						right.append(dto.getProjectname()+"添加成功</br>");
					}else{
						wrong.append(dto.getProjectname()+"添加失败</br>");
					}
				}else if(dto.getProjectType().equals("count")){
					//增加次数信息
					if(aheadUserService.chargeCountLimitUser(com, dto, adminId) > 0){
						aheadUserService.deleteResources(com,dto,false);
						aheadUserService.updateProjectResources(com, dto);
						right.append(dto.getProjectname()+"添加成功</br>");
					}else{
						wrong.append(dto.getProjectname()+"添加失败</br>");
					}
				}
			}
		}
		//子账号延期
		aheadUserService.updateSubaccount(com,adminId);
		return right.append(wrong).toString();
	}
	
	//删除购买项目 
	private void removeproject(HttpServletRequest req,List<String> list) throws Exception{
		String adminId = CookieUtil.getCookie(req);
		InstitutionalUser com = new InstitutionalUser();
		List<ResourceDetailedDTO> rdlist = new ArrayList<ResourceDetailedDTO>();
		for (String json : list) {
			if(StringUtils.isEmpty(json)){
				continue;
			}
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
				balance=NumberUtils.toDouble(obj.getString("balance"));
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
				dto.setTotalMoney(balance.toString());
			} else if ("count".equals(type)) {
				dto.setPurchaseNumber(balance.toString());
			}
			int i = aheadUserService.deleteAccount(com, dto, adminId);
			if (i > 0) {
				aheadUserService.deleteResources(com, dto, false);
			}
			rdlist.add(dto);
		}
		com.setRdlist(rdlist);
		this.addOperationLogs(com, "删除", req);;
	}
	
	//添加日志
	private void addLogInfo(String msg,long time){
		if (log.isInfoEnabled()) {
			log.info(msg+"，耗时:"+(System.currentTimeMillis()-time)+"ms");
		}
	}
	
	//机构信息注册
	private Map<String,String> registerValidate(InstitutionalUser user,Map<String,String> errorMap) throws Exception{
		//字段校验
		errorMap = InstitutionUtils.getRegisterValidate(user);
		if (errorMap.size() > 0) {
			return errorMap;
		}
		//验证机构用户名是否存在
		this.userValidate(user, errorMap);
		if (errorMap.size() > 0) {
			return errorMap;
		}
		// 校验机构管理员
		this.adminValidate(user, errorMap);
		if (errorMap.size() > 0) {
			return errorMap;
		}
		// 校验党建管理
		this.partyAdminValidate(user, errorMap);
		if (errorMap.size() > 0) {
			return errorMap;
		}
		return errorMap;
	}
	
	//验证机构用户名是否存在
	private Map<String,String> userValidate(InstitutionalUser user,Map<String,String> errorMap) throws Exception{
		Person p = aheadUserService.queryPersonInfo(user.getUserId());
		if (p != null) {
			errorMap.put("flag", "fail");
			errorMap.put("fail","该机构ID已存在，请重新输入机构ID");
			return errorMap;
		}
		String msg = aheadUserService.validateOldUser(user.getUserId());
		if ("old".equals(msg)) {
			errorMap.put("flag", "fail");
			errorMap.put("fail","该机构ID在老平台已存在，请重新输入机构ID");
		}else if("error".equals(msg)){
			errorMap.put("flag", "fail");
			errorMap.put("fail","旧平台检验机构ID出现异常");
		}
		return errorMap;
	}

	//验证机构管理员
	private Map<String,String> adminValidate(InstitutionalUser user,Map<String,String> hashmap) throws Exception{
		if (StringUtils.isEmpty(user.getManagerType())) {
			return hashmap;
		}
		if("new".equals(user.getManagerType())&&StringUtils.isEmpty(user.getAdminname())||
				"old".equals(user.getManagerType())&&StringUtils.isEmpty(user.getAdminOldName())){
			return hashmap;
		}
		String adminId = user.getManagerType().equals("new") ? user.getAdminname() : user
				.getAdminOldName();
		Person per = aheadUserService.queryPersonInfo(adminId);
		if (per == null) {
			return hashmap;
		}
		if(user.getManagerType().equals("new")){
			if(per.getUsertype() != 1){
				hashmap.put("flag", "fail");
				hashmap.put("fail", "机构管理员的ID已经被占用");
				return hashmap;
			}else{
				hashmap.put("flag", "fail");
				hashmap.put("fail", "该机构管理员ID已经存在，请重新输入机构管理员ID");
				return hashmap;	
			}
		}
		return hashmap;
	}
	
	//验证党建管理员
	private Map<String,String> partyAdminValidate(InstitutionalUser com,Map<String,String> hashmap) throws Exception{
		if (StringUtils.isEmpty(com.getPartyLimit())) {
			return hashmap;
		}
		if(StringUtils.isBlank(com.getPartyBegintime())||StringUtils.isBlank(com.getPartyEndtime())){
			hashmap.put("flag", "fail");
			hashmap.put("fail", "党建管理员有效期不能为空，请正确填写有效期");
			return hashmap;
		}
		if(DateUtil.stringToDate1(com.getPartyBegintime())==null||DateUtil.stringToDate1(com.getPartyEndtime())==null){
			hashmap.put("flag", "fail");
			hashmap.put("fail", "党建管理员有效期格式不正确，请正确填写有效期");
			return hashmap;
		}
		if(DateUtil.stringToDate1(com.getPartyBegintime()).getTime()>DateUtil.stringToDate1(com.getPartyEndtime()).getTime()){
			hashmap.put("flag", "fail");
			hashmap.put("fail", "党建管理员有效期开始时间不能大于结束时间");
			return hashmap;
		}
		Person per=aheadUserService.queryPersonInfo(com.getPartyAdmin());
		if(per==null){
			return hashmap;
		}
		if(per.getUsertype() != 4){
			hashmap.put("flag", "fail");
			hashmap.put("fail", "党建管理员的ID已经被占用");
			return hashmap;
		}
		if(StringUtils.isEmpty(com.getPartyAdmin())){
			return hashmap;
		}
		WfksAccountidMapping[] maping= aheadUserService.getWfksAccountidByRelatedidKey(com.getPartyAdmin());
		if (maping == null) {
			return hashmap;
		}
		for (WfksAccountidMapping wfks : maping) {
			if ("PartyAdminTime".equals(wfks.getRelatedidAccounttype())
					&& !wfks.getIdKey().equals(com.getUserId())) {
				hashmap.put("flag", "fail");
				hashmap.put("fail", "该党建管理员ID已经存在，请重新输入党建管理员ID");
				return hashmap;
			}
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
	@RequestMapping("tosonaccount")
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
	 * 子账号导出功能
	 */
	@RequestMapping("exportSonAccount")
	public ModelAndView exportSonAccount(HttpServletRequest request, HttpServletResponse response,String userId, String institution,
			String start_time, String end_time, String pageNum, String pageSize,String isBack,String goPage) {
		ModelAndView view = new ModelAndView();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("start_time",start_time);
		map.put("end_time",end_time);
		map.put("institution",institution);
		if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(institution)
				&& StringUtil.isEmpty(start_time) && StringUtil.isEmpty(end_time)) {
			map.put("userId",userId);
			view.addObject("map",map);
			view.setViewName("/page/usermanager/ins_sonaccount");
			return view;
		}
		if(!StringUtils.isEmpty(userId)){
			Person person=aheadUserService.queryPersonInfo(userId);
			if(person==null||(person.getUsertype()!=3&&person.getUsertype()!=2)){
				map.put("userId",userId);
				view.addObject("msg", "0");
				view.addObject("map",map);
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
		try{
			int column = NumberUtils.toInt(SettingUtil.getSetting("sheetMaxColumnSize"));
			int maxSize = NumberUtils.toInt(SettingUtil.getSetting("sheetMaxSize"));
			map.put("pageNum", 0);
			map.put("pageSize", column*maxSize==0?100:column*maxSize);
			PageList pageList = aheadUserService.getSonaccount(map);
			ExportExcel exc= new ExportExcel();
			exc.exportExccel3(response,pageList.getPageRow(),column,maxSize);
		}catch(Exception e){
			log.error("导出excel异常：",e);
		}
		map.put("userId",userId);
		map.put("isBack", isBack);
		view.addObject("map",map);
		view.addObject("msg", "0");
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
	public ModelAndView perManager() {
		ModelAndView view = new ModelAndView();
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
	 *	管理赠送万方卡
	 */
	@RequestMapping("manage_card")
	public ModelAndView manage_card(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/manage_card");
		return view;
	}
	
	/**
	 *	生成赠送万方卡
	 */
	@RequestMapping("creat_wfcard_view")
	public ModelAndView creat_wfcard_view(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/creat_wfcard_view");
		return view;
	}
	
	/**
	 *	审核赠送万方卡
	 */
	@RequestMapping("check_card")
	public ModelAndView check_card(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/check_card");
		return view;
	}
	
	/**
	 *	设置赠送万方卡
	 */
	@RequestMapping("wfcard_type")
	public ModelAndView wfcard_type(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/wfcard_type");
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
	 * 根据机构名称获取管理员
	 * @param response
	 * @param institution 机构名称
	 * @throws Exception 
	 */
	@RequestMapping("getOldAdminNameByInstitution")
	public void getOldAdminNameByInstitution(HttpServletResponse response,String institution,String batch){
		try{
			String msg="";
			if(!StringUtils.isEmpty(institution)){
				List<Map<String, Object>> userids = personservice.getAllInstitutional(institution);
				JSONArray array = JSONArray.fromObject(userids);
				msg=array.toString();
			}else if(!StringUtils.isEmpty(batch)){
				List<Map<String, Object>> userids = personservice.getAllInstitutional("");
				JSONArray array = JSONArray.fromObject(userids);
				msg=array.toString();
			}
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(msg);
		}catch(Exception e){
			log.error("管理员查询异常:", e);
		}
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
	
	/**
	 * 添加操作日志信息
	 * @return
	 */
	private void addOperationLogs(InstitutionalUser com,String flag,HttpServletRequest req) throws Exception{
		if(com!=null){
			Wfadmin admin =CookieUtil.getWfadmin(req);
			List<ResourceDetailedDTO> list = com.getRdlist();
			if(list!=null &&list.size()>0){
				for(ResourceDetailedDTO dto:list){
					if(dto.getProjectid()!=null){
						if (dto.getProjectType().equals("balance")&& NumberUtils.toDouble(dto.getTotalMoney()) == 0
								&& StringUtils.equals(dto.getValidityEndtime(),dto.getValidityEndtime2())
								&& StringUtils.equals(dto.getValidityStarttime(),dto.getValidityStarttime2())) {
							continue;
						} else if (dto.getProjectType().equals("count")&& NumberUtils.toInt(dto.getPurchaseNumber()) == 0
								&& StringUtils.equals(dto.getValidityEndtime(),dto.getValidityEndtime2())
								&& StringUtils.equals(dto.getValidityStarttime(),dto.getValidityStarttime2())) {
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
						if(flag == "1" ){
							op.setOpreation("注册");
						}else if(flag == "2" ){
							op.setOpreation("更新");
						}else if(flag == "3" ){
							op.setOpreation("删除");
						}else{
							op.setOpreation(flag);
						}
						
						if (com.getRdlist() != null) {
							JSONObject json=JSONObject.fromObject(dto);
							op.setReason(json.toString());
						}
						op.setProjectId(dto.getProjectid());
						op.setProjectName(dto.getProjectname());
						opreationLogs.addOperationLogs(op);
						Log log=new Log("机构用户信息管理",op.getOpreation(),req, JSONObject.fromObject(op).toString());
						logService.addLog_Institution(log);
					}
				}
			}
		}
	}
	
	
	/**
	 *	机构用户注册跳转
	 */
	@RequestMapping("getProject")
	@ResponseBody
	public List<PayChannelModel> getProject(HttpServletRequest req, HttpServletResponse res){
		String val=req.getParameter("val");
		if(StringUtils.isEmpty(val)){
			return null;
		}
		List<PayChannelModel> list = aheadUserService.purchaseProject();
		List<PayChannelModel> ls=new ArrayList<PayChannelModel>();
		for(PayChannelModel pay:list){
			if (pay.getProductDetail().contains(val)) {
				ls.add(pay);
			} else if (pay.getResourceType().equals("resource") && "Limit".equals(val)) {
				ls.add(pay);
			}
		}
		return ls;
	}


	/**
	 *	生成充值码
	 */
	@RequestMapping("create_chargeCode")
	public ModelAndView create_ChargeCode(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/create_chargeCode");
		return view;
	}

	/**
	 *	充值码批次查询
	 */
	@RequestMapping("search_chargeCode_Batch")
	public ModelAndView search_ChargeCode_Batch(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_chargeCode_Batch");
		return view;
	}

	/**
	 *	充值码信息查询
	 */
	@RequestMapping("search_chargeCode")
	public ModelAndView search_ChargeCode(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_chargeCode");
		return view;
	}
	
	/**
	 *	获取userType
	 */
	@RequestMapping("getUserType")
	@ResponseBody
	public Integer getUserType(String userId){
		
		Person per=aheadUserService.queryPersonInfo(userId);
		
		return per.getUsertype();
	}

	private boolean sendMessage(String topic,String topicKey,Object obj){
		
		boolean result=false;
		try {

			String grpcServer=GrpcServer.getHost();
			int grpcPort=GrpcServer.getPort();
			
			//1、创建channel
			ManagedChannel channel = NettyChannelBuilder.forAddress(grpcServer, grpcPort)
			                .negotiationType(NegotiationType.PLAINTEXT).build();
			                
			//2、创建连接实例
			ProducerServiceGrpc.ProducerServiceBlockingStub blockingStub =ProducerServiceGrpc
			                    .newBlockingStub(channel);
			//3、创建topic
			SendRequest request=SendRequest.newBuilder().setTopic(topic).setKey(topicKey).setMessage(JsonHelper.serialize(obj)).build();
	        SendResponse response = blockingStub.send(request);
	        result = response.getResult();
	        log.info(topicKey+"发送队列成功");
		} catch (Exception e) {
			log.error(topicKey+"发送队列失败",e);
		}
        return result;
	}
	
}
