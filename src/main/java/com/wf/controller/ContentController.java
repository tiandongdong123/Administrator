package com.wf.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wf.Setting.ResourceTypeSetting;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.bigdata.framework.common.api.volume.IVolumeService;
import org.bigdata.framework.common.model.SearchPageList;
import org.bigdata.framework.forbidden.iservice.IForbiddenSerivce;
import org.bigdata.framework.search.iservice.ISearchCoreResultService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.resteasy.plugins.server.sun.http.HttpServerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.exportExcel.ExportExcel;
import com.redis.RedisUtil;
import com.utils.CookieUtil;
import com.utils.DateTools;
import com.utils.GetUuid;
import com.utils.Getproperties;
import com.utils.JsonUtil;
import com.utils.ParamUtils;
import com.wf.bean.Department;
import com.wf.bean.HotWord;
import com.wf.bean.HotWordSetting;
import com.wf.bean.Log;
import com.wf.bean.Message;
import com.wf.bean.Notes;
import com.wf.bean.PageList;
import com.wf.bean.ResourceType;
import com.wf.bean.ShareTemplate;
import com.wf.bean.ShareTemplateNamesFileds;
import com.wf.bean.ShareTtemplateNames;
import com.wf.bean.Subject;
import com.wf.bean.Volume;
import com.wf.bean.Wfadmin;
import com.wf.service.DepartmentService;
import com.wf.service.HotWordService;
import com.wf.service.HotWordSettingService;
import com.wf.service.LogService;
import com.wf.service.MessageService;
import com.wf.service.NotesService;
import com.wf.service.ResourceTypeService;
import com.wf.service.ShareTemplateNamesFiledsService;
import com.wf.service.ShareTemplateNamesService;
import com.wf.service.ShareTemplateService;
import com.wf.service.SubjectService;
import com.wf.service.VolumeService;

@Controller
@RequestMapping("/content")   
public class ContentController{
	@Autowired
	SubjectService subjectService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	ResourceTypeService resourceTypeService;
	
	@Autowired
	ShareTemplateService shareTemplateService;
	
	@Autowired
	NotesService notesService;
	
	@Autowired
	VolumeService volumeService;//文辑接口
	
	@Autowired
	IVolumeService iVolume;//文辑存到solr接口
	
	@Autowired
	ISearchCoreResultService searchAllResult;
	
	@Autowired
	ShareTemplateNamesService shareTemplateNamesService;
	
	@Autowired
	ShareTemplateNamesFiledsService filedsService;

	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	HotWordService hotWordService;
	
	@Autowired
	HotWordSettingService hotWordSettingService;
	
	@Autowired
	IForbiddenSerivce forbiddenSerivce;
	
	@Autowired
	LogService logService;
	
	RedisUtil redis = new RedisUtil();
	/**
	 * 学科分类信息查询
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/subjectquery")
	public String getSubject(
			@RequestParam(value="level",required=false) String level,
			@RequestParam(value="classNum",required=false) String classNum,
			@RequestParam(value="className",required=false) String className,
			HttpServletRequest request,Model model){
		
		int pageNum=1;
		int pageSize=5;
		PageList subjectList =subjectService.getSubject(pageNum,pageSize,level,classNum,className);
		Map<String,String> mapPara=new HashMap<String, String>();
		mapPara.put("level", level);
		mapPara.put("className", className);
		mapPara.put("classNum", classNum);
		model.addAttribute("pageList",subjectList);
		model.addAttribute("paraList",mapPara);
		
		//记录日志
		Log log=new Log("学科分类管理","查询","查询条件:级别:"+level+",分类号:"+classNum+",分类名称:"+className,request);
		logService.addLog(log);
		
		return "/page/contentmanage/subject";
	}
	/**
	 * 学科分类信息分页
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/subjectJson")
	public void getJsonSubject(
			@RequestParam(value="level",required=false) String level,
			@RequestParam(value="classNum",required=false) String classNum,
			@RequestParam(value="className",required=false) String className,
			@RequestParam(value="page",required=false) int pageNum,
			HttpServletResponse response,HttpServletRequest request) throws IOException{
		int pageSize=5;
		//int pageNum=Integer.parseInt(request.getParameter("page"));
		
		//记录日志
		Log log=new Log("学科分类管理","查询","查询条件:级别:"+level+",分类号:"+classNum+",分类名称:"+className,request);
		logService.addLog(log);

		PageList subjectList =subjectService.getSubject(pageNum,pageSize,level,classNum,className);
		JSONObject json=JSONObject.fromObject(subjectList);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
	}
	
	@RequestMapping("subjectTree")
	public void subjectTree(HttpServletResponse response,HttpServletRequest request) throws IOException{
		PageList subjectList =subjectService.getSubject(1,2504,"","","");
		JSONArray array=JSONArray.fromObject(subjectList.getPageRow());
		for (int i = 0; i < array.size(); i++) {
			array.getJSONObject(i).put("name",array.getJSONObject(i).get("className"));
			array.getJSONObject(i).put("pId",array.getJSONObject(i).get("pid"));
		}
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(array.toString());
	}
	
	
	/**
	 * 增加学科分类跳转
	 * @param model
	 * @return
	 */
	@RequestMapping("/subjectadd")
	public String addSubject(Model model){
		model.addAttribute("addupdate", "add");
		return "/page/contentmanage/addSubjectType";
	}
	/**
	 * 增加学科分类信息
	 * @param subject
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("/addSubjectType")
	public void addSubjectType(Subject subject,HttpServletResponse response,HttpServletRequest request) throws Exception{
		subject.setId(GetUuid.getId());
		Boolean b=subjectService.insertSubject(subject);
		
		//记录日志
		Log log=new Log("学科分类管理","增加",subject.toString(),request);
		logService.addLog(log);

		JsonUtil.toJsonHtml(response, b);
	}
	/**
	 * 修改学科分类信息跳转
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/subjectmodify")
	public String updateSubject(
			@RequestParam(value="idSub",required=false) String id,
			HttpServletRequest request,Model model){
		Subject  subject=subjectService.findSubject(id);
		model.addAttribute("addupdate", "update");
		model.addAttribute("subject", subject);
		return "/page/contentmanage/addSubjectType";
	}
	@RequestMapping("showMessage")
	public String showMessage(HttpServletRequest request){
		return "/page/contentmanage/messageshow";
	}
	/**
	 * 修改学科分类信息
	 * @param subject
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("/updateSubjectJson")
	public void updateSubjectJson(Subject subject,HttpServletResponse response,HttpServletRequest request) throws Exception{
		boolean b =subjectService.updateSubject(subject);
		
		//记录日志
		Log log=new Log("学科分类管理","修改",subject.toString(),request);
		logService.addLog(log);
		
		JsonUtil.toJsonHtml(response, b);
	}
	/**
	 * 删除学科分类信息
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("/deleteSubject")
	public void deleteSub(
			@RequestParam(value="ids",required=false) String ids,
			HttpServletResponse response,HttpServletRequest request) throws Exception{
		if(StringUtils.isEmpty(ids))ids=null;
		Boolean b=subjectService.deleteSubject(ids);
		JsonUtil.toJsonHtml(response, b);
		
		//记录日志
		Log log=new Log("学科分类管理","删除",ids.toString(),request);
		logService.addLog(log);
		
	}
	
	/**
	 * 增加资讯跳转
	 * @return
	 */
	@RequestMapping("/add")
	public String addMessage(Model model){
		model.addAttribute("addupdate", "add");
		return "/page/contentmanage/addMessage";
	}
	
	
	@RequestMapping("/addMessageJson")
	public void addMessageJson(Message message,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		Wfadmin admin=CookieUtil.getWfadmin(request);
		message.setId(GetUuid.getId());
		message.setHuman(admin.getUser_realname());
		message.setBranch(admin.getDept().getDeptName());
		message.setIssueState(1);
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		message.setCreateTime(sdf1.format(new Date()));
		message.setStick(sdf1.format(new Date()));
		boolean b =messageService.insertMessage(message);
		JsonUtil.toJsonHtml(response, b);
		//记录日志
		//Log log=new Log("资讯管理","增加",message.toString(),request);
		//log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		//logService.addLog(log);
	}
	
	/**
	 * 资讯查询
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/index")
	public String message(HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("startTime", "");
		mp.put("endTime", "");
		mp.put("branch", "");
		mp.put("colums", "");
		model.addAttribute("meaasgeMap", mp);
		List<String> deptList = new ArrayList<String>();
		for (Object department : departmentService.getAllDept()) {
			deptList.add(((Department) department).getDeptName());
		}
		model.addAttribute("deptList", deptList);
		return "/page/contentmanage/message";
	}
	
	/**
	 * 资讯查询分页
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/messageJson")
	@ResponseBody
	public Object getMessageJson(String branch,String human,String colums,String isTop,String startTime,String endTime,int pageNum,int pageSize,
			HttpServletResponse response,HttpServletRequest request) throws IOException{
		
		PageList messageList=messageService.getMessage(pageNum, pageSize, branch, human, colums, startTime, endTime,isTop);
		//记录日志
		Log log=new Log("资讯管理","查询","查询条件:添加部门:"+branch+",添加人:"+human+",添加日期:"+startTime+"-"+endTime+",栏目:"+colums,request);
		logService.addLog(log);
		return  messageList;
	}
	
	
	/**
	 * 咨询详情跳转
	 * @return
	 */
	@RequestMapping("/detail")
	public String getDetails(String id,Model model,HttpServletRequest request){
		 String ip = request.getHeader("X-Forwarded-For");  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getHeader("Proxy-Client-IP");
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getHeader("WL-Proxy-Client-IP"); 
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getHeader("HTTP_CLIENT_IP");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getRemoteAddr()+":"+request.getServerPort() ; 
         }
		
		Message message=messageService.findMessage(id);
		model.addAttribute("message", message);
		model.addAttribute("url", "http://"+ip);
		return "/page/contentmanage/messageDetail";
	}
	
	
	/**
	 * 删除资讯信息
	 * @param response
	 * @param request
	 * @throws Exception 
	 */
	@RequestMapping("/deleteMessage")
	public void deleteMessage(
			@RequestParam(value="ids",required=false) String ids,
			HttpServletResponse response,HttpServletRequest request) throws Exception{
		boolean b =messageService.deleteMessage(ids);
		
		//记录日志
		Log log=new Log("资讯管理","删除",ids.toString(),request);
		logService.addLog(log);

		JsonUtil.toJsonHtml(response, b);
	}
	/**
	 * 修改资讯
	 * @param id
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/modify")
	public String updateMessage(@RequestParam(value="id",required=false) String id,
			HttpServletRequest request,Model model){
		
		Message message=messageService.findMessage(id);
		model.addAttribute("message", message);
		model.addAttribute("addupdate", "update");
		return "/page/contentmanage/addMessage";
	}
	
	@RequestMapping("/updateMessageJson")
	public void updateMessageJson(Message message,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Wfadmin admin=CookieUtil.getWfadmin(request);
		message.setHuman(admin.getUser_realname());
		boolean b =messageService.updateMessage(message);
		//记录日志
		//Log log=new Log("资讯管理","修改",message.toString(),request);
		//logService.addLog(log);
		JsonUtil.toJsonHtml(response, b);
	}
	/**
	 * 发布/下撤/再发布
	 * @param id
	 * @param issueState
	 * @param request 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/updateIssue")
	@ResponseBody
	public boolean updateIssue(String id,String colums,String issueState, HttpServletRequest request) throws Exception{
		
		boolean b =messageService.updateIssue(id,colums,issueState);
		
		//记录日志
		Log log=new Log("资讯管理","发布/下撤/再发布","资讯ID:"+id+",栏目:"+colums+",发布状态:"+issueState,request);
		logService.addLog(log);

		return b;
	}
	/**
	 * 资源类型管理查询
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/resourcequery")
	public String getResourceManage(
			HttpServletRequest request,Model model){
		int pageNum=1;
		int pageSize=10;
		PageList p=resourceTypeService.getResourceType(pageNum, pageSize);
		model.addAttribute("pageList",p);
		return "/page/contentmanage/resourceManage";
	}

	/**
	 * 根据name查找资源类型
	 */

	@RequestMapping("/findResourseByName")
	public void findResourseByName(
			@RequestParam(value="typeName",required=false) String typeName,
            HttpServletResponse response,HttpServletRequest request,Model model) throws IOException {
		int pageNum=1;
		int pageSize=10;
		PageList p=new PageList();
		if(null==typeName || "".equals(typeName)){
			 p=resourceTypeService.getResourceType(pageNum, pageSize);
		}else{
			p=resourceTypeService.getResourceTypeByName(pageNum, pageSize,typeName);
		}
		
		model.addAttribute("pageList",p);
        JSONObject json=JSONObject.fromObject(p);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
        
		//记录日志
		Log log=new Log("资源类型管理","查询","查询条件:资源类型名称:"+typeName,request);
		logService.addLog(log);
	}



	/**
	 * 资源类型管理分页
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/resourceManageJson")
	public void getJsonResourceManage(
			@RequestParam(value="page",required=false) int pageNum,String typeName,
			HttpServletResponse response,HttpServletRequest request) throws IOException{
		
		int pageSize=10;
		PageList p=resourceTypeService.getResourceType(pageNum, pageSize);
		JSONObject json=JSONObject.fromObject(p);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
		
		//记录日志
		Log log=new Log("资源类型管理","查询","查询条件:资源类型名称:"+typeName,request);
		logService.addLog(log);

	}
	
	/**
	 * 添加资源跳转
	 * @return
	 */
	@RequestMapping("/resourceadd")
	public String addResource(Model model){
		model.addAttribute("addupdate", "add");
		return "/page/contentmanage/addResource";
	}
	/**
	 * 添加资源
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("/addResourceJson")
	@ResponseBody
	public void addResourceJson(ResourceType resourceType,HttpServletResponse response,HttpServletRequest request) throws Exception{
		resourceType.setId(GetUuid.getId());
		boolean result=resourceTypeService.addResourceType(resourceType);
		
		JsonUtil.toJsonHtml(response, result);
		
		//记录日志
		Log log=new Log("资源类型管理","增加",resourceType.toString(),request);
		logService.addLog(log);
	}

	/**
	 * 资源类型上移
	 */
	@RequestMapping("/moveUpResource")
	public void moveUpResource(
			@RequestParam(value="id",required=false) String id,HttpServletResponse response,HttpServletRequest request) throws Exception {
		
		boolean result=resourceTypeService.moveUpResource(id);
		//存到zookeeper后会有反应时间，sleep防止数据不能实时更新
		Thread.sleep(100);
		JSONArray list = resourceTypeService.getAll1();
		redis.del("sourcetype");
		redis.set("sourcetype", list.toString(), 6);
		JsonUtil.toJsonHtml(response, result);
		
		//记录日志
		Log log=new Log("资源类型管理","上移",id,request);
		logService.addLog(log);

	}
	/**
	 * 资源类型下移
	 */
	@RequestMapping("/moveDownResource")
	public void moveDownResource(
			@RequestParam(value="id",required=false) String id,HttpServletResponse response,HttpServletRequest request) throws Exception {
	
		boolean result=resourceTypeService.moveDownResource(id);
		//存到zookeeper后会有反应时间，sleep防止数据不能实时更新
		Thread.sleep(100);
		JSONArray list = resourceTypeService.getAll1();
			redis.del("sourcetype");
			redis.set("sourcetype", list.toString(), 6);
		JsonUtil.toJsonHtml(response, result);
		
		//记录日志
		Log log=new Log("资源类型管理","下移",id,request);
		logService.addLog(log);
	}
	/**
	 *判断资源类型是否发布
	 */
	@RequestMapping("/checkResourceForOne")
	public void checkResourceForOne(String id,HttpServletResponse response,HttpServletRequest request) throws Exception {
		boolean result = resourceTypeService.checkResourceForOne(id);
		JsonUtil.toJsonHtml(response, result);
	}


	/**
	 * 修改学科分类信息跳转
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/resourcemodify")
	public String updateResource(
			@RequestParam(value="idRes",required=false) String id,
			HttpServletRequest request,Model model){
		//ResourceType  resourceType=resourceTypeService.findResourceType(id);
		ResourceTypeSetting resourceTypeSetting = new ResourceTypeSetting();
		ResourceType  resourceType= resourceTypeSetting.findResourceTypeById(id);
		model.addAttribute("addupdate", "update");
		model.addAttribute("resourceType", resourceType);
		return "/page/contentmanage/addResource";
	}
	/**
	 * 修改资源信息
	 * @param subject
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("/updateResourceJson")
	public void updateResourceJson(ResourceType resourceType,HttpServletResponse response,HttpServletRequest request) throws Exception{

		boolean b =resourceTypeService.updateResourceType(resourceType);
		JsonUtil.toJsonHtml(response, b);
		
		//记录日志
		Log log=new Log("资源类型管理","修改",resourceType.toString(),request);
		logService.addLog(log);

		//更新REDIS资源类型状态
/*		JSONArray list=	resourceTypeService.getAll1();
		redis.del("sourcetype");
		redis.set("sourcetype", list.toString(), 6);	*/
	}
	/**
	 * 删除资源信息
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("/deleteResourceType")
	public void deleteResourceType(
			@RequestParam(value="ids",required=false) String ids,
			HttpServletResponse response,HttpServletRequest request) throws Exception{
		if(StringUtils.isEmpty(ids))ids=null;
		
		//记录日志
		Log log=new Log("资源类型管理","删除","删除的资源类型ID:"+ids,request);
		logService.addLog(log);

		Boolean b=resourceTypeService.deleteResourceType(ids);
		JsonUtil.toJsonHtml(response, b);
		/*//更新REDIS资源类型状态
		JSONArray list=	resourceTypeService.getAll1();
		redis.del("sourcetype");
		redis.set("sourcetype", list.toString(), 6);*/
	}
	/**
	 * 图片上传
	 * @param file
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/uploadImg")
	public void upload(@RequestParam("file") MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception {
	    String path = request.getSession().getServletContext().getRealPath("upload");  
	    String fileName = file.getOriginalFilename();
	    String fileNameStr = (new Date().getTime())+"-"+fileName;
	    File targetFile = new File(path, fileNameStr);
	    if(!targetFile.exists()){  
	        targetFile.mkdirs();
	    }  
	    //保存  
	    try {  
	        file.transferTo(targetFile);  
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    JSONObject jo = new JSONObject();
	    String url=request.getContextPath()+"/upload/"+fileNameStr;
	    jo.accumulate("url", url);
	    response.setCharacterEncoding("UTF-8");
		response.getWriter().print(jo);
		
	}
	
	/**
	 * 分享模板
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/templatequery")
	public String getShareTemplate(
			@RequestParam(value="shareType",required=false) String shareType,
			HttpServletRequest request,Model model){
		int pageNum=1;
		int pageSize=10;
		PageList p=shareTemplateService.getShareTemplate(pageNum, pageSize, shareType);
		List<ShareTemplate> list = shareTemplateService.selectAll();
		model.addAttribute("pageList",p);
		model.addAttribute("shareType", shareType);
		model.addAttribute("templates", list);
		
		//记录日志
		Log log=new Log("分享模板管理","查询","查询条件:分享类型:"+shareType,request);
		logService.addLog(log);

		return "/page/contentmanage/shareTemplate";
	}
	/**
	 * 分享模板管理分页
	 * @param response
	 * @param request
	 * @throws Exception 
	 */
	@RequestMapping("/shareTemplateJson")
	public void getJsonShareTemplate(
			@RequestParam(value="shareType",required=false) String shareType,
			@RequestParam(value="page",required=false) int pageNum,
			HttpServletResponse response,HttpServletRequest request) throws Exception{
		
		//记录日志
		Log log=new Log("分享模板管理","查询","查询条件:分享类型:"+shareType,request);
		logService.addLog(log);
		
		int pageSize=10;
		PageList p=shareTemplateService.getShareTemplate(pageNum, pageSize, shareType);
		JSONObject json=JSONObject.fromObject(p);
		JsonUtil.toJsonHtml(response, json.toString());
	}
	
	/**
	 * 分享模板增加跳转
	 * @return
	 */
	@RequestMapping("/templateadd")
	public String addShareTemplate(Model model){
		List<ShareTtemplateNames> names=shareTemplateNamesService.getAllShareTemplateNames();
		model.addAttribute("names",names);
		model.addAttribute("addupdate", "add");
		return "/page/contentmanage/addShareTemplate";
	}
	/**
	 * 分享模板新增
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("/addShareTemplateJson")
	public void addShareTemplateJson(ShareTemplate shareTemplate,@RequestParam(value="checkValue[]",required=false)String[] checkValue,HttpServletResponse response,HttpServletRequest request) throws Exception{
		
		shareTemplate.setId(GetUuid.getId());
		String str="";
		
		for (String string : checkValue) {
			str+=string;
		}
		shareTemplate.setShareContent(str);
		boolean b=shareTemplateService.addShareTemplate(shareTemplate);
		
		//记录日志
		Log log=new Log("分享模板管理","增加","新增分享模块信息:"+shareTemplate.toString(),request);
		logService.addLog(log);
		
		JsonUtil.toJsonHtml(response,b);
	}
	
	@RequestMapping("/deleteShareTemplate")
	public void deleteShareTemplate(HttpServletResponse response,String ids, HttpServletRequest request) throws Exception{
		
		//记录日志
		Log log=new Log("分享模板管理","删除","删除的分享模块ID:"+ids,request);
		logService.addLog(log);

		boolean b =shareTemplateService.deleteShareTemplate(ids);
		JsonUtil.toJsonHtml(response, b);
	}
	
	@RequestMapping("/templatemodify")
	public String updateShareTemplate(
			@RequestParam(value="ids",required=false) String ids,
			Model model){
		List<ShareTtemplateNames> names=shareTemplateNamesService.getAllShareTemplateNames();
		ShareTemplate shareTemplate =shareTemplateService.findShareTemplate(ids);
		List<ShareTemplateNamesFileds> fileds=filedsService.getFiledsByShareNameType(shareTemplate.getShareType());
		for (int i = 0; i < fileds.size(); i++) {
			 if(StringUtils.isNotBlank(fileds.get(i).getField_eng())){
				 if(shareTemplate.getShareContent().contains(fileds.get(i).getField_eng())){
					 fileds.get(i).setCheck(true);
				 }
			 }	 
		}
		
		
		model.addAttribute("names",names);
		model.addAttribute("addupdate", "update");
		model.addAttribute("shareTemplate", shareTemplate);
		model.addAttribute("fileds", fileds);
		return "/page/contentmanage/addShareTemplate";
	}
	
	@RequestMapping("/updateShareTemplates")
	public void updateShareTemplate(HttpServletResponse response,ShareTemplate shareTemplate,@RequestParam(value="checkValue[]",required=false)String[]checkValue, HttpServletRequest request) throws Exception{
		
		String str="";
		for (String string : checkValue) {
			str+=string;
		}
		shareTemplate.setShareContent(str);
		boolean b =shareTemplateService.updateShareTemplate(shareTemplate);
		
		//记录日志
		Log log=new Log("分享模板管理","修改","修改后的分享模板信息:"+shareTemplate.toString(),request);
		logService.addLog(log);

		JsonUtil.toJsonHtml(response, b);
	}
	
	@RequestMapping("checkShareTemplates")
	public void checkShareTemplates(HttpServletResponse response,ShareTemplate shareTemplate,String addOrUpdate) throws Exception{
		boolean isExists=shareTemplateService.checkShareTemplate(shareTemplate,addOrUpdate)==null?true:false;
		JsonUtil.toJsonHtml(response,isExists);
	}
	
	/**
	 * 笔记
	 * @param model
	 * @return
	 */
	@RequestMapping("/notemanage")
	public String notes(Model model){
		int pageNum=1;
		int pageSize=10;
		PageList pageList =notesService.getNotes(pageNum, pageSize,null, null, null, null, null, null, null, null,null,null);
		model.addAttribute("pageList",pageList);
		model.addAttribute("res",resourceTypeService.getAll());
		return "/page/contentmanage/notes";
	}
	
	@RequestMapping("noteShow")
	public String noteShow(Model model,HttpServletRequest request) {
		String id=request.getParameter("id");
	
	Notes notes=notesService.findNotes(id);
	model.addAttribute("text",notes.getNoteContent());
		return "/page/contentmanage/notesText";
	}
	
	

	
	
	/**
	 * 笔记管理
	 * @param model
	 * @param response
	 * @param userName
	 * @param noteNum
	 * @param resourceName
	 * @param resourceType
	 * @param dataState
	 * @param complaintStatus
	 * @param startTime
	 * @param endTime
	 * @param pageNum
	 * @param request 
	 * @throws IOException
	 * @throws ParseException 
	 */
	
	@RequestMapping("/notesJson")
	public void notesJson(HttpServletResponse response,
			@RequestParam(value="userName",required=false) String userName,
			@RequestParam(value="noteNum",required=false) String noteNum,
			@RequestParam(value="resourceName",required=false) String resourceName,
			@RequestParam(value="resourceType[]",required=false) String[] resourceType,
			@RequestParam(value="dataState[]",required=false) String[] dataState,
			@RequestParam(value="complaintStatus[]",required=false) String[] complaintStatus,
			@RequestParam(value="startTime",required=false) String startTime,
			@RequestParam(value="endTime",required=false) String endTime,
			@RequestParam(value="noteProperty[]",required=false) String[] noteProperty,
			@RequestParam(value="performAction[]",required=false) String[] performAction,
			@RequestParam(value="pagesize",required=false) int pagesize,
			@RequestParam(value="page",required=false) int pageNum, HttpServletRequest request
			) throws Exception{
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar  calendar=new  GregorianCalendar(); 
		
		if(StringUtils.isEmpty(userName)) userName=null;
		if(StringUtils.isEmpty(noteNum)) noteNum=null;
		if(StringUtils.isEmpty(resourceName)) resourceName=null;
		if(StringUtils.isEmpty(startTime)) startTime=null;
		
		if(StringUtils.isEmpty(endTime)){
			endTime=null;
		}else{
			calendar.setTime(format.parse(endTime));
			calendar.add(calendar.DATE,1);
			endTime=format.format(calendar.getTime());
		}
		
		PageList NotepageList =notesService.getNotes(pageNum, pagesize, userName, noteNum, resourceName, resourceType, dataState, complaintStatus, startTime, endTime,noteProperty,performAction);
		JSONObject json=JSONObject.fromObject(NotepageList);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
		
		//记录日志
		Log log=new Log("笔记管理","查询","笔记管理查询条件:用户ID:"+userName+",笔记编号:"+noteNum
				+",资源名称:"+resourceName+",笔记日期:"+startTime+"-"+endTime
				+ ",资源类型:"+(resourceType==null?"":Arrays.asList(resourceType))+",数据状态:"+(dataState==null?"":Arrays.asList(dataState))+","
				+"申诉状态:"+(complaintStatus==null?"":Arrays.asList(complaintStatus)),request);
		logService.addLog(log);
		
	}
	
	@RequestMapping("/findNote")
	public String findNote(Model model,@RequestParam(value="id",required=false) String id){
	
		boolean b=notesService.handlingNote(id);

		Notes notes =notesService.findNotes(id);
		String noteDate = notes.getNoteDate();
		if(!"".equals(noteDate)){
			noteDate = noteDate.substring(0, 4) + "年" + noteDate.substring(5, 7) + "月" + noteDate.substring(8, 10) + "日" + noteDate.substring(10, 19);
		}
		notes.setNoteDate(noteDate);
		model.addAttribute("notes", notes);
		return "/page/contentmanage/notes_detail";
	}
	
	
	@RequestMapping("/findNotes")
	public String findNotes(Model model,@RequestParam(value="id",required=false) String id){
		Notes notes =notesService.findNotes(id);
		model.addAttribute("notes", notes);
		return "/page/contentmanage/notes_detail";
	}
	
	@RequestMapping("/findNotes_close_note")
	public String findNotes_close_note(Model model,@RequestParam(value="id",required=false) String id,@RequestParam(value="type",required=false) String type){
		Notes notes =notesService.findNoteOne(id);
		model.addAttribute("notes", notes);
		model.addAttribute("type",type);
		return "/page/contentmanage/notes_detail";
	}
	
	@RequestMapping("/updateNotes")
	public void updateNotes(Notes notes,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		notes.setAuditTime(dateString);//审核日期
		Wfadmin admin = CookieUtil.getWfadmin(request);
		notes.setAuditId(admin.getWangfang_admin_id());//审核人ID
		Boolean b = notesService.updateNotes(notes);
		JsonUtil.toJsonHtml(response, b);
		//记录日志
		Log log=new Log("笔记管理","修改","修改后的笔记信息:"+notes.toString(),request);
		logService.addLog(log);
	}
	
	@RequestMapping("/stick")
	public void stick(Message message,HttpServletResponse response) throws Exception{
		message.setStick(DateTools.getSysTime());
		boolean b =messageService.updataMessageStick(message);
		JsonUtil.toJsonHtml(response, b);
	}
	
	/**
	 * 文辑管理
	 * @return
	 */
	@RequestMapping("/papercollectquery")
	public ModelAndView volumeDocu(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/contentmanage/volume/volume_docu");
		return mav;
	}
	/**
	 * 列表
	 * @param startTime
	 * @param endTime
	 * @param searchWord
	 * @param volumeType
	 * @param volumeState
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getVolume")
	@ResponseBody
	public PageList getVolume(String startTime,String endTime,String searchWord,String volumeType,String volumeState,
			String sortColumn,String sortWay,int pageNum,int pageSize){
		PageList p = volumeService.queryList(startTime, endTime, searchWord, volumeType, volumeState, sortColumn,sortWay,pageNum, pageSize);
		return p;
	}
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public boolean delete(String id){
		boolean flag = volumeService.delete(id);
		//在后台删除文辑同时删除前台显示
		List<String> ids = new ArrayList<String>();
		ids.add(id);
		boolean flag2 = iVolume.deleteVolumeList(ids);
		return flag;
	}
	/**
	 * 推优
	 * @param id
	 * @param subject
	 * @param price
	 * @return
	 */
	@RequestMapping("/push")
	@ResponseBody
	public boolean push(String id,String subject,String subjectName, double price){
		boolean flag = volumeService.updateVolumeType(id, subject,subjectName, price);
		return flag;
	}
	/**
	 * 获取知识发现路径
	 */
	@RequestMapping("geturl")
	public void geturl(HttpServletResponse response,HttpServletRequest request){
		response.setCharacterEncoding("UTF-8");
		JSONObject json = new JSONObject();
		json.put("search", Getproperties.getPros("httpurl.properties").getProperty("menu.index.wf_fw_zsfx"));
		PrintWriter out  = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(json);
		out.flush();
		out.close();
	}
	/**
	 * 文辑管理
	 * @return
	 */
	@RequestMapping("/papercollectdetail")
	public ModelAndView volumeDetails(String id){
		Map<String,Object> map = volumeService.queryDetails(id);
		int volumeChapter = (Integer) map.get("volumeChapter");
		ModelAndView mav = new ModelAndView();
		mav.addObject("map",map);
		if(Integer.valueOf(volumeChapter) == 1){
			mav.setViewName("/page/contentmanage/volume/volume_details");//有章节
		}else{
			mav.setViewName("/page/contentmanage/volume/volume_detailsNone");//无章节
		}
		return mav;
	}
	
	/**
	 * 文辑发布/下撤/再发布
	 * @param id
	 * @return
	 */
	@RequestMapping("/issue")
	@ResponseBody
	public boolean issue(String id,String issueNum){
		if("3".equals(issueNum)){//下撤
			//删除前台显示
			List<String> ids = new ArrayList<String>();
			ids.add(id);
			boolean flag1 = iVolume.deleteVolumeList(ids);
		}else{//发布和再发布
			
			Map<String,Object> map = volumeService.queryDetails(id);
			Volume volume = (Volume) map.get("volume");
			//--------------------存到solr里-----------------
			boolean flag1 = iVolume.sendSolrByVolumeId(id);//存到solr里
		}
		//文辑发布/下撤/再发布后修改数据库文辑状态
		boolean flag2 = volumeService.updateIssue(id,issueNum);
		return flag2;
	}
	/**
	 * 修改价格
	 * @param id
	 * @return
	 */
	@RequestMapping("/updatePrice")
	@ResponseBody
	public boolean updatePrice(String price,String volumeId){
		boolean flag = volumeService.updatePrice(price,volumeId);
		return flag;
	}
	//----------------------------------------创建文辑-----------------------------------
		/**
		 * 创建文辑第一步
		 * @return
		 */
		@RequestMapping("/papercollectadd")
		public ModelAndView stepOne(HttpServletRequest request,@ModelAttribute Volume volume){
			Wfadmin admin=CookieUtil.getWfadmin(request);
			String publishPerson = admin.getUser_realname();
			ModelAndView mav = new ModelAndView();
			mav.addObject("volume", volume);
			mav.addObject("publishPerson", publishPerson);
			mav.setViewName("/page/contentmanage/volume/step_one");
			return mav;
		}
		/**
		 * 创建文辑第二步
		 * @return
		 */
		@RequestMapping("/stepTwo")
		public ModelAndView stepTwo(@ModelAttribute Volume volume){
			ModelAndView mav = new ModelAndView();
			mav.addObject("volume", volume);
			mav.setViewName("/page/contentmanage/volume/step_two");
			return mav;
		}
		/**
		 * 创建文辑第三步
		 * @return
		 */
		@RequestMapping("/stepThree")
		public ModelAndView stepThree(@ModelAttribute Volume volume,String listContent){
			ModelAndView mav = new ModelAndView();
			mav.addObject("volume", volume);
			mav.addObject("listContent", listContent);
			mav.setViewName("/page/contentmanage/volume/step_three");
			return mav;
		}
		/**
		 * 搜全站
		 */
		@RequestMapping("/queryGlobal")
		@ResponseBody
		public SearchPageList queryGlobal(String queryWords,String sort,int pageNum,int pageSize){
			String paramStrs = queryWords;	//检索参数
			//字段处理
//			paramStrs = "$title:" + paramStrs + "+$summary:" + paramStrs + "+$keywords:" + paramStrs;//标题、摘要、关键词 或的关系连接
			paramStrs = ParamUtils.getParam(paramStrs);
			//排序字段
			HashMap<String,String>[] sortMap = new HashMap[1];
			sortMap[0] = new HashMap<String,String>();
			if(StringUtils.isNotBlank(sort)){
				
				if("correlation".equals(sort)){//相关度
					sort = "$correlation";
				}else if("down_num".equals(sort)){//下载次数
					sort = "$download_num";
				}else if("cite_num".equals(sort)){//被引用次数
					sort = "$cite_num";
				}else if("publish_year".equals(sort)){//发表时间
					sort = "$publish_year";
				}
				sortMap[0].put(sort,"desc");
			}else{
				sortMap = null;
			}
			String[] typeProject = new String[]{"perio","degree","conference","books","tbooks","patent","tech","techResult","standards","legislations","Yearbooks","gazetteer_new"};
			String[] docuTypes = new String[]{"perio_artical","degree_artical","conf_artical","ebooks","reference_book","patent_element","tech_report","tech_result","standards","legislations","yearbook_single","gazetteers_item"};
			SearchPageList pageList = searchAllResult.getCoreSearchList(typeProject, docuTypes, paramStrs, sortMap, null, Integer.valueOf(pageNum), Integer.valueOf(pageSize), "", "", "");
			return pageList;
		}
		/**
		 * 创建文辑第四步(有章节)
		 * @return
		 */
		@RequestMapping("/stepFourChapter")
		public ModelAndView stepFourChapter(@ModelAttribute Volume volume,String listContent){
			List<Map<String,Object>> list = JSONArray.fromObject(listContent);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("volume", volume);//文辑
			map.put("chapters", list);//章节
			ModelAndView mav = new ModelAndView();
			mav.addObject("map", map);
			mav.addObject("listContent", listContent);
			mav.setViewName("/page/contentmanage/volume/step_four_chapter");
			return mav;
		}
		/**
		 * 创建文辑第四步(无章节)
		 * @return
		 */
		@RequestMapping("/stepFourNoChapter")
		public ModelAndView stepFourNoChapter(@ModelAttribute Volume volume,String listContent){
			List<Map<String,Object>> list = JSONArray.fromObject(listContent);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("volume", volume);//文辑
			map.put("sections", list);//章节
			ModelAndView mav = new ModelAndView();
			mav.addObject("map", map);
			mav.addObject("listContent", listContent);
			mav.setViewName("/page/contentmanage/volume/step_four_noChapter");
			return mav;
		}
		
		/**
		 * 保存文辑
		 * @return
		 */
		@RequestMapping("/commit")
		@ResponseBody
		public boolean commit(HttpServletRequest request,@ModelAttribute Volume volume,String listContent){
			if(StringUtils.isEmpty(volume.getId())){//创建文辑
				Wfadmin admin=CookieUtil.getWfadmin(request);
				String userId = admin.getWangfang_admin_id();
				String publishPerson = admin.getUser_realname();
				volume.setUserId(userId);
				Map<String,Object>  map1 = volumeService.insert(publishPerson, volume, "2", listContent);//后台生成的都是优选文辑
				return (Boolean) map1.get("flag");
			}else{//修改文辑
				boolean flag = volumeService.updateVolume(volume, listContent);
				if(flag){
					//修改文辑后删除前台显示重新发布
					List<String> ids = new ArrayList<String>();
					ids.add(volume.getId());
					iVolume.deleteVolumeList(ids);
					//--------------------存到solr里-----------------
					iVolume.sendSolrByVolumeId(volume.getId());//存到solr里
				}
				return flag;
			}
		}

		/**
		 * 修改文辑第一步
		 * @return
		 */
		@RequestMapping("/papercollectmodify")
		public ModelAndView updateOne(HttpServletRequest request,@ModelAttribute Volume volume,String id){
			Wfadmin admin=CookieUtil.getWfadmin(request);
			String publishPerson = admin.getUser_realname();
			Map<String,Object> map = volumeService.queryDetails(id);
			if(volume.getVolumeName() == null){//第一次跳进修改
				volume = (Volume) map.get("volume");
			}
			ModelAndView mav = new ModelAndView();
			mav.addObject("volume", volume);
			mav.addObject("publishPerson", publishPerson);
			mav.setViewName("/page/contentmanage/volume/update_one");
			return mav;
		}
		/**
		 * 修改文辑第二步
		 * @return
		 */
		@RequestMapping("/updateTwo")
		public ModelAndView updateTwo(@ModelAttribute Volume volume){
			Map<String,Object> map = volumeService.queryDetails(volume.getId());
			int volumeChapter = (Integer) map.get("volumeChapter");
			List<Map<String,Object>> listContent = new ArrayList<Map<String,Object>>();
			if(volumeChapter == 1){//分章节
				listContent = (List<Map<String, Object>>) map.get("chapters");
			}else{//不分章节
				listContent = (List<Map<String, Object>>) map.get("sections");
			}
			ModelAndView mav = new ModelAndView();
			mav.addObject("volume", volume);
			mav.addObject("listContent", JSONArray.fromObject(listContent).toString());
			mav.setViewName("/page/contentmanage/volume/update_two");
			return mav;
		}
		/**
		 * 创建文辑获取学科一级
		 * @param request
		 * @param response
		 */
		@RequestMapping("getsubject")
		@ResponseBody
		public JSONArray getsubject(HttpServletResponse response){
			JSONArray jsons = new JSONArray();
			JSONArray ajsons = new JSONArray();
			RedisUtil r = new RedisUtil();
			String subject = r.get("CLCDic",0);
			jsons = JSONArray.fromObject(subject);
			for (int i = 0; i < jsons.size(); i++) {
				JSONObject json = jsons.getJSONObject(i);
				String id = json.getString("id");
				if(id.indexOf(".")<=0 && !id.equals("A")){
					JSONArray bjsons2 = new JSONArray();
					for (int j = 0; j < jsons.size(); j++) {
						JSONObject json2 = jsons.getJSONObject(j);
						String id2 = json2.getString("id");
						String pid2 = json2.getString("pid");
						String[] ids2 = id2.split("\\.");
						if(ids2.length==2){
							JSONArray cjsons3 = new JSONArray();
							for (int k = 0; k < jsons.size(); k++) {
								JSONObject json3 = jsons.getJSONObject(k);
								String id3 = json3.getString("id");
								String pid3 = json3.getString("pid");
								String[] ids3 = id3.split("\\.");
								if(ids3.length==3){
									if(id2.equals(pid3)){
										cjsons3.add(json3);
									}
								}
							}
								if(id.equals(pid2)){
									json2.put("data", cjsons3);
									bjsons2.add(json2);
								}
							}
						}
						json.put("data",bjsons2);
						ajsons.add(json);
					}
					
				}
			return ajsons;
		}
	/**
	 * 查询库所有库信息
	 * @return
	 */
	@RequestMapping("/getallcore")
	@ResponseBody
	public JSONArray getallcore(HttpServletRequest request){
		SAXReader saxReader = new SAXReader();
		JSONArray cores = new JSONArray();
		try {
			Document document = saxReader.read(new File(this.getClass().getClassLoader().getResource("").getPath()+"bcm1.xml"));
			Element root = document.getRootElement();
			 //Element node = root.element("class"); 
		        //首先获取当前节点的所有属性节点  
		       // List<Attribute> list = node.attributes();  
			 Iterator<Element> iterator = root.elementIterator();
			 while(iterator.hasNext()){
				JSONObject core = new JSONObject();
		        Element e = iterator.next();
		        String core_name = e.attribute("name").getValue();
	            String core_field = e.attribute("field").getValue();
	            core.put("name",core_name);
	            core.put("field",core_field);
	            cores.add(core);
			 }
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cores;
	}
	/**
	 * 查询库所有库信息
	 * @return
	 */
	@RequestMapping("/getallfield")
	@ResponseBody
	public JSONArray getallfield(HttpServletRequest request){
		String field_value  = request.getParameter("field");
		JSONArray fields = new JSONArray();
		if(StringUtils.isNotBlank(field_value)){
			SAXReader saxReader = new SAXReader();
			try {
				Document document = saxReader.read(new File(this.getClass().getClassLoader().getResource("").getPath()+"bcm1.xml"));
				Element root = document.getRootElement();
				
				 
				 //Element node = root.element("class"); 
				 
			        //首先获取当前节点的所有属性节点  
			       // List<Attribute> list = node.attributes();  
				 
				 Iterator<Element> iterator = root.elementIterator();
				 while(iterator.hasNext()){
			        Element e = iterator.next();
		            String core_field = e.attribute("field").getValue();
		            if(field_value.equals(core_field)){
		            	//同时迭代当前节点下面的所有子节点  
				        //使用递归  
				        Iterator<Element> iterator2 = e.element("data-property").elementIterator(); 
		            	while (iterator2.hasNext()) {
		            		Element e2 = iterator2.next();
		            		String field_name = e2.attribute("name").getValue();
				            String field_field = e2.attribute("field").getValue();
				            JSONObject field = new JSONObject();
				            field.put("name", field_name);
				            field.put("field_field", field_field);
				            fields.add(field);
						}
		            }
				 }
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fields;
	}
	
	/**
	* @Title: addSubject
	* @Description: (redis取值批量插入学科数据) 
	* @return boolean 返回类型 
	* @author LiuYong 
	* @date 3 Dis 2016 2:21:34 PM
	 */
	@RequestMapping("/addSubjects")
	@ResponseBody
	public boolean addSubjects(){
		boolean b =subjectService.insertSubjects();
		return b;
	}
	
	/**
	* @Title: subjectPublish
	* @Description: (学科发布功能) 
	* @return boolean 返回类型 
	* @author LiuYong 
	* @param type 学科分类
	* @date 2 Dis 2016 4:23:40 PM
	 */
	@RequestMapping("/subjectPublish")
	@ResponseBody
	public boolean subjectPublish(){
		boolean b =subjectService.subjectPublish();
		return b;
	}
	
	/**
	* @Title: resourcePublish
	* @Description: (资源发布功能) 
	* @return boolean 返回类型 
	* @author LiuYong 
	* @date 2 Dis 2016 4:23:40 PM
	 */
	@RequestMapping("/resourcePublish")
	@ResponseBody
	public boolean resourcePublish(){
		boolean b =resourceTypeService.resourcePublish();
		return b;
	}
	
	
	@RequestMapping("closenote")
	@ResponseBody
	public boolean changeNote(String id,String finalOpinion){
		boolean b = notesService.closeNote(id,finalOpinion);
		return b;
	}
	
	@RequestMapping("opennote")
	@ResponseBody
	public boolean openNote(String id){
		boolean b = notesService.openNote(id);
		return b;
	}
	
	@RequestMapping("pushdata")
	@ResponseBody
	public boolean pushData(int state,String id) throws InterruptedException {
		int result = resourceTypeService.updateResourceTypeState(state, id);
		//存到zookeeper后会有反应时间，sleep防止数据不能实时更新
		Thread.sleep(100);
		JSONArray list = resourceTypeService.getAll1();
				boolean b = false;
				if (result>0){
			redis.del("sourcetype");
			redis.set("sourcetype", list.toString(), 6);
			b = true;
		}
		return b;
	}
	
	/**
	 *	笔记审核管理 导出
	 * @param request
	 * @param response 
	 * @param userName 用户ID
	 * @param noteNum 笔记编号
	 * @param resourceName 资源名称
	 * @param resourceType 资源类型
	 * @param dataState  数据状态
	 * @param complaintStatus 申诉状态
	 * @param startTime 笔记日期 开始
	 * @param endTime 笔记日期 结束
	 * @param pageNum 当前页
	 * @throws Exception 
	 */
	@RequestMapping("/exportNotes")
	public void exportNotes(HttpServletRequest request,HttpServletResponse response,String userName,
			String noteNum, String resourceName,String[] resourceType,String[] dataState,
			String[] complaintStatus,String startTime,String endTime, String[] noteProperty, String[] performAction)throws Exception{
				
		ExportExcel excel=new ExportExcel();
	
		if(StringUtils.isEmpty(userName)) userName=null;
		if(StringUtils.isEmpty(noteNum)) noteNum=null;
		if(StringUtils.isEmpty(resourceName)) resourceName=null;
		if(StringUtils.isEmpty(startTime)) startTime=null;
		if(resourceType.length==0) resourceType=null;
		if(dataState.length==0) dataState=null;
		if(complaintStatus.length==0) complaintStatus=null;
		if(noteProperty.length==0) noteProperty=null;
		if(performAction.length==0) performAction=null;
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar  calendar=new  GregorianCalendar(); 
		if(StringUtils.isEmpty(endTime)){
			endTime=null;
		}else{
			calendar.setTime(format.parse(endTime));
			calendar.add(calendar.DATE,1);
			endTime=format.format(calendar.getTime());
		}
		
		
		List<Object> list= notesService.exportNotes(userName, noteNum, resourceName, resourceType, dataState, complaintStatus, startTime, endTime,  noteProperty, performAction);
		JSONArray array=JSONArray.fromObject(list);
		List<String> names=Arrays.asList(new String[]{"序号","笔记ID","文献标题","资源类型","笔记内容","笔记用户ID","笔记时间","执行操作","笔记性质","数据状态","审核人","审核时间"});		
		excel.ExportNotes(response, array, names);
		
		//记录日志
		Log log=new Log("笔记审核管理","导出","导出条件:用户ID:"+userName+",笔记编号:"+noteNum+",资源名称:"+resourceName
				+",资源类型:"+(resourceType==null?"":Arrays.asList(resourceType))
				+",数据状态:"+(dataState==null?"":Arrays.asList(dataState))
				+",申诉状态:"+(complaintStatus==null?"":Arrays.asList(complaintStatus)),request);
		logService.addLog(log);		
				
	}

	/**
	 * 导出 ---文辑
	 * @param startTime
	 * @param endTime
	 * @param searchWord
	 * @param volumeType
	 * @param volumeState
	 * @param sortColumn
	 * @param sortWay
	 * @param pageNum
	 * @param pageSize
	 * @param response
	 * @param request 
	 * @throws Exception 
	 */
	@RequestMapping("exportVolumeDocu")
	public void exportVolumeDocu(String startTime,String endTime,String searchWord,String volumeType,String volumeState,
			String sortColumn,String sortWay,HttpServletResponse response, HttpServletRequest request) throws Exception{
		
		List<Object> list=new ArrayList<Object>();
		list= volumeService.exportVolumeDocu(startTime, endTime, searchWord, volumeType, volumeState, sortColumn,sortWay);
		JSONArray array=JSONArray.fromObject(list);
		
		List<String> names=volumeType.equals("1")?
			Arrays.asList(new String[]{"序号","文辑编号","文辑名称","关键词","文辑状态","发布用户名","发布日期","文辑文献数量"})
			:Arrays.asList(new String[]{"序号","文辑编号","学科","文辑名称","关键词","发布用户名","发布日期","文辑文献数量","价格"});
		
		ExportExcel excel=new ExportExcel();
		excel.exportVolumeDocu(response, array, names,volumeType);
		
		//记录日志
		Log log=new Log("文辑管理","导出","文辑管理:导出条件:开始时间"+startTime+",结束时间:"+endTime+
				",关键字:"+searchWord+",文辑类型:"+volumeType+",文辑状态:"+volumeState+
				"排序列:"+sortColumn+",顺序:"+sortWay,request);
		logService.addLog(log);

	}
	
	/**
	 * 导出分享模板
	 * @param response
	 * @param shareType 分享类型
	 * @param request 
	 * @throws Exception 
	 */
	@RequestMapping("exportshareTemplate")
	public void exportshareTemplate(HttpServletResponse response,String shareType, HttpServletRequest request){

		List<Object> list=new ArrayList<Object>();
		
		list=shareTemplateService.exportshareTemplate(shareType);
		JSONArray array=JSONArray.fromObject(list);		
		
		List<String> names=Arrays.asList(new String[]{"序号","分享类型","分享内容"});
		
		ExportExcel excel=new ExportExcel();
		excel.exportshareTemplate(response, array, names);
		
	}
	
	/**
	 * 导出资源类型
	 * @param response
	 * @param pageNum 当前页
	 * @param resouceType 资源类型
	 * @param request 
	 * @throws Exception 
	 */
	@RequestMapping("exportResource")
	public void exportResource(HttpServletResponse response,String resouceType, HttpServletRequest request){
		
		List<Object> list=new ArrayList<Object>();
		
		try {
			
			if(resouceType!=null&&resouceType!=""){
				resouceType=URLDecoder.decode(resouceType,"UTF-8");
				resouceType="%"+resouceType+"%";
			}
			
			list=resourceTypeService.exportResource(resouceType);
			JSONArray array=JSONArray.fromObject(list);
						
			List<String> names=Arrays.asList(new String[]{"序号","资源类型名称","资源类型描述","资源类型code"});
			
			ExportExcel excel=new ExportExcel();
			excel.exportResource(response, array, names);

			//记录日志
			Log log=new Log("资源类型管理","导出","导出条件:资源类型:"+resouceType,request);
			logService.addLog(log);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 学科导出
	 * @param response
	 * @param pageNum
	 * @param level
	 * @param classNum
	 * @param className
	 * @param request 
	 * @throws Exception 
	 */
	@RequestMapping("exportSubject")
	public void exportSubject(HttpServletResponse response,Integer pageNum,String level,String classNum,String className, HttpServletRequest request){
		
		List<Object> list=new ArrayList<Object>();
		list=subjectService.exportSubject(level,classNum,className);
		JSONArray array=JSONArray.fromObject(list);
		
		List<String> names=Arrays.asList(new String[]{"序号","级别","分类号","分类名称"});
		
		ExportExcel excel=new ExportExcel();
		excel.exportSubject(response, array, names);
		
		//记录日志
		Log log=new Log("学科分类管理","导出","导出条件:级别:"+level+",分类号:"+classNum+",分类名称:"+className,request);
		logService.addLog(log);

	}
	
	/**
	 * 检查资源Code是否重复
	 * @param response
	 * @param typeCode 资源Code
	 * @throws Exception 
	 */
	@RequestMapping("checkRsTypeCode")
	public void checkRsTypeCode(HttpServletResponse response,String typeCode) throws Exception{
		Boolean isExist=resourceTypeService.checkRsTypeCode(typeCode);
		JsonUtil.toJsonHtml(response,isExist);
	} 
	
	/**
	 * 检查资源名称是否重复
	 * @param response
	 * @param typeCode 资源Code
	 * @throws Exception 
	 */
	@RequestMapping("checkRsTypeName")
	public void checkRsTypeName(HttpServletResponse response,String typeName) throws Exception{
		Boolean isExist=resourceTypeService.checkRsTypeName(typeName);
		JsonUtil.toJsonHtml(response,isExist);
	}
	
	/**
	 * 选择分享类型  展示相应的分享类型字段
	 * @throws Exception 
	 */
	@RequestMapping("shareTypeChange")
	public void shareTypeChange(HttpServletResponse response,Integer id) throws Exception{
		List<ShareTemplateNamesFileds> fileds=filedsService.getFiledsByShareNameId(id);
		JSONArray array=JSONArray.fromObject(fileds);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(array.toString());
	}
	
	/**
	 * 资讯导出
	 * @param branch
	 * @param clum
	 * @param human
	 * @param startTime
	 * @param endTime
	 * @param request 
	 * @throws Exception 
	 */
	@RequestMapping("exportMessage")
	public void exportMessage(HttpServletResponse response,String branch,String colums,
				String human,String startTime,String endTime, HttpServletRequest request){
		
		List<Object> list=new ArrayList<>();
		list= messageService.exportMessage(branch,colums,human,startTime,endTime);
		JSONArray array=JSONArray.fromObject(list);
		List<String> names=Arrays.asList(new String[]{"序号","栏目","标题","原文链接","添加人","添加日期"});
		ExportExcel excel=new ExportExcel();
		excel.exportMessage(response, array, names);
		
		//记录日志
		Log log=new Log("资讯管理","导出","导出条件:添加部门:"+branch+",添加人:"+human+",添加日期:"+startTime+"-"+endTime+",栏目:"+colums,request);
		logService.addLog(log);

	}
	
	/**
	 *一键发布
	 * @param request
	 * @param response
	 */
	@RequestMapping("/oneKeyDeploy")
	@ResponseBody
	public Boolean oneKeyDeploy(HttpServletRequest request, HttpServletResponse response) {
		boolean isOK = true;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("issue_state", 2);
			List<Object> list = messageService.getAllMessage(map);
			messageService.updateBatch(list);
		} catch (Exception e) {
			isOK = false;
			e.printStackTrace();
		}
		return isOK;
	}
	
	@RequestMapping("/hotwordmanage")
	public String hotword(Model model){
		Map map=new HashMap();
		map.put("status",1);
		model.addAttribute("item",hotWordSettingService.getOneHotWordSettingShow(map));
		return "/page/contentmanage/hotword";
	}
	
	@RequestMapping("/hotwordpublish")
	public String hotWordPublish(){
		return "/page/contentmanage/hotwordPublish";
	}
	
	@RequestMapping("/addWordSetting")
	public String addWordSetting(Model model){
		String nextPublishTime=hotWordSettingService.getNextPublishTime();
		model.addAttribute("isupdate","add");
		model.addAttribute("get_time_cur",nextPublishTime==null?"":nextPublishTime.substring(11,nextPublishTime.length()));
		model.addAttribute("isFirst",hotWordSettingService.checkFirst()>0);
		return "/page/contentmanage/add_word_setting";
	}
	/**
	 * 添加手动设置的页面跳转
	 * @param model
	 * @return
	 */
	@RequestMapping("/addWordManualSetting")
	public String addWordManualSetting(Model model){
		model.addAttribute("isupdate","add");
		return "/page/contentmanage/add_word_Manual_setting";
	}

	
	/**
	 * 资讯查询分页
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/hotwordJson")
	@ResponseBody
	public Object getHotWordJson(String word,String word_nature,Integer status,int pageNum,int pageSize,HttpServletResponse response,HttpServletRequest request) throws IOException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("word",word);
		map.put("word_nature",word_nature);
		map.put("status",status);
		map.put("pageNum",(pageNum-1)*pageSize);
		map.put("pageSize",pageSize);
		PageList hotWordList=hotWordService.getHotWord(map);
		return  hotWordList;
	}

	@RequestMapping("/checkWordExist")
	@ResponseBody
	public boolean checkWordExist(String word_content){
		return hotWordService.checkWordExist(word_content)>0;
	}

	
	
	/**
	 * @param word_content
	 * @return
	 */
	@RequestMapping("/addWord")
	@ResponseBody
	public boolean addWord(HttpServletRequest request,String word_content){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		HotWord hotWord=new HotWord();
		hotWord.setWord(word_content);
		hotWord.setSearchCount(0);
		hotWord.setWordNature("后台添加");
		hotWord.setOperationTime(df.format(new Date()));
		hotWord.setWordStatus(2);
		hotWord.setDateTime(df.format(new Date()));
		hotWord.setOperation(CookieUtil.getWfadmin(request).getUser_realname());
		return hotWordService.addWord(hotWord)>0;
	}

	/**
	 * @param word_content
	 * @return
	 */
	@RequestMapping("/updateWord")
	@ResponseBody
	public boolean updateWord(HttpServletRequest request,String word_content,Integer id){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		HotWord hotWord=new HotWord();
		hotWord.setId(id);
		hotWord.setWord(word_content);
		hotWord.setSearchCount(0);
		hotWord.setWordNature("后台添加");
		hotWord.setWordStatus(2);
		hotWord.setOperationTime(df.format(new Date()));
		hotWord.setOperation(CookieUtil.getWfadmin(request).getUser_realname());
		return hotWordService.updateWord(hotWord)>0;
	}

	/**
	 * @param word_content
	 * @return
	 */
	@RequestMapping("/updateWordIssue")
	@ResponseBody
	public boolean updateWordIssue(HttpServletRequest request,Integer issueState,Integer id){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		HotWord hotWord=new HotWord();
		hotWord.setId(id);
		hotWord.setOperationTime(df.format(new Date()));
		hotWord.setOperation(CookieUtil.getWfadmin(request).getUser_realname());
		hotWord.setWordStatus(issueState);
		boolean isuccess=hotWordService.updateWordIssue(hotWord)>0;
		isuccess=hotWordService.publishToRedis();
		return isuccess;
	}
	
	
	/**手动发布热搜词
	 * @param word_content
	 * @return
	 */
	@RequestMapping("/updateWordManualIssue")
	@ResponseBody
	public boolean updateWordManualIssue(HttpServletRequest request,Integer issueState,Integer id){
		HotWordSetting set=hotWordSettingService.getOneHotWordManualSetting();
		if(set!=null){
			//判断是否是首次执行
			if(set.getIs_first().equals("0")){
				redis.del(11, "theme");
			}
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		HotWord hotWord=new HotWord();
		hotWord.setId(id);
		hotWord.setOperationTime(df.format(new Date()));
		hotWord.setOperation(CookieUtil.getWfadmin(request).getUser_realname());
		hotWord.setWordStatus(issueState);
		boolean isuccess=hotWordService.updateWordIssue(hotWord)>0;
		isuccess=hotWordService.publishToRedis();
		if(isuccess){
			if(set!=null){
				String now_publish_time_space=set.getNow_get_time_space();
				//更改所有手动目前发布数据时间段
				List<HotWordSetting> list=hotWordSettingService.getHotWordManualSettingList();
				for (HotWordSetting hotWordSetting : list) {
					hotWordSetting.setNow_publish_time_space(now_publish_time_space);
					hotWordSetting.setIs_first("1");
					hotWordSettingService.updateWordSetting(hotWordSetting);
				}
			}
		}
		return isuccess;
	}
	
	/**
	 * @param word_content
	 * @return
	 */
	@RequestMapping("/batch")
	@ResponseBody
	public boolean batch(HttpServletRequest request,
			@RequestParam(value="ids[]",required=false) Integer[]ids,Integer status){
		if(status==1){
			int c=20-hotWordService.checkRedisCount();
			Map map=new HashMap();
			map.put("ids", ids);
			map.put("end", c);
			Integer[] orderid=hotWordService.getHotWordByOrder(map);
			ids=orderid;
		}
		HotWord hotWord=null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		int count=0;
		for (int i = 0; i < ids.length; i++) {
			hotWord=new HotWord();
			hotWord.setId(ids[i]);
			hotWord.setOperationTime(df.format(new Date()));
			hotWord.setOperation(CookieUtil.getWfadmin(request).getUser_realname());
			hotWord.setWordStatus(status);
			count+=hotWordService.updateWordIssue(hotWord);
		}
		boolean isuccess=hotWordService.publishToRedis();
		return count>0 && isuccess;
	}
	/**手动批量发布
	 * @param word_content
	 * @return
	 */
	@RequestMapping("/batchManual")
	@ResponseBody
	public boolean batchManual(HttpServletRequest request,
			@RequestParam(value="ids[]",required=false) Integer[]ids,Integer status){
		HotWordSetting set=hotWordSettingService.getOneHotWordManualSetting();
		//判断是否是首次执行
		if(set!=null){
			if(set.getIs_first().equals("0")){
				redis.del(11, "theme");
			}
		}
		if(status==1){
			int c=20-hotWordService.checkRedisCount();
			Map map=new HashMap();
			map.put("ids", ids);
			map.put("end", c);
			Integer[] orderid=hotWordService.getHotWordByOrder(map);
			ids=orderid;
		}
		
		HotWord hotWord=null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		int count=0;
		for (int i = 0; i < ids.length; i++) {
			hotWord=new HotWord();
			hotWord.setId(ids[i]);
			hotWord.setOperationTime(df.format(new Date()));
			hotWord.setOperation(CookieUtil.getWfadmin(request).getUser_realname());
			hotWord.setWordStatus(status);
			count+=hotWordService.updateWordIssue(hotWord);
		}
		boolean isuccess=hotWordService.publishToRedis();
		if(set!=null){
			if(isuccess){
				String now_publish_time_space=set.getNow_get_time_space();
				//更改所有手动目前发布数据时间段
				List<HotWordSetting> list=hotWordSettingService.getHotWordManualSettingList();
				for (HotWordSetting hotWordSetting : list) {
					hotWordSetting.setNow_publish_time_space(now_publish_time_space);
					hotWordSetting.setIs_first("1");
					hotWordSettingService.updateWordSetting(hotWordSetting);
				}
			}
		}
		return count>0 && isuccess;
	}
	
	/*
	 * 获取redies中的热搜词数 
	 */
	@RequestMapping("/checkCount")
	@ResponseBody
	public Integer checkCount(){
		return hotWordService.checkRedisCount();
	}

	@RequestMapping("/doaddWordSetting")
	@ResponseBody
	public boolean doaddWordSetting(HotWordSetting wordset, HttpServletRequest request,String isFirst){
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			Date d = new Date();
			Calendar cal = Calendar.getInstance();
			String nextPublish="";
			wordset.setFirst_publish_time(sdf.format(d));
			if(isFirst.equals("true")){
				nextPublish=hotWordSettingService.getNextPublishTime();
			}else{
				nextPublish=wordset.getFirst_publish_time()+" "+wordset.getPublish_date();
			}
			Date date =sdf.parse(nextPublish.substring(0, 10));
			cal.setTime(date); 
			int day=cal.get(Calendar.DATE); 
			cal.set(Calendar.DATE,day-wordset.getTime_slot()); 
			sdf=new SimpleDateFormat("yyyy年MM月dd日");
			StringBuffer sb=new StringBuffer("");
			String str=nextPublish.substring(0, 10);
			sb.append(str.replaceFirst("-", "年").replaceFirst("-", "月"));
			sb.append("日");
			String next_publish_time_space=sdf.format(cal.getTime())+" "+wordset.getGet_time()+"───"+sb.toString()+" "+wordset.getGet_time();
			wordset.setPublish_strategy("自动发布");
			wordset.setFirst_publish_time(nextPublish.substring(0, 10));
			wordset.setNext_publish_time(nextPublish);
	 		wordset.setNext_publish_time_space(next_publish_time_space);
	 		wordset.setStatus(2);
	 		wordset.setOperation(CookieUtil.getWfadmin(request).getUser_realname());
	 		sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 		wordset.setOperation_date(sdf.format(d));
	 		
		} catch (ParseException e) {
			e.printStackTrace();
		}

	 		return hotWordSettingService.addWordSetting(wordset)>0;
	}
	
	/**
	 * 添加手动设置
	 * @param wordset
	 * @param request
	 * @param isFirst
	 * @return
	 */
	@RequestMapping("/doaddWordManualSetting")
	@ResponseBody
	public boolean doaddWordManualSetting(HotWordSetting wordset, HttpServletRequest request){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			Date d = new Date();
			String getTime=com.wanfangdata.hotwordsetting.HotWordSetting.getGet_time();//获取设定的几点抓取时间
			wordset.setPublish_strategy("手动发布");
			wordset.setOperation(CookieUtil.getWfadmin(request).getUser_realname());
			sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			wordset.setOperation_date(sdf.format(d));
			wordset.setStatus(2);
			wordset.setGet_time(getTime);
			wordset.setIs_first("0");
	 		return hotWordSettingService.addWordSetting(wordset)>0;
	}
	
	/*
	 * 获取设定的抓取时间点
	 */
	@RequestMapping("/getHotWordSettingGetTime")
	public String getHotWordSettingGetTime(){
		return com.wanfangdata.hotwordsetting.HotWordSetting.getGet_time();
	}
	
	
	/**
	 * 
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/getHotWordSettingJson")
	@ResponseBody
	public Object getHotWordSettingJson(int pageNum,int pageSize,HttpServletResponse response,HttpServletRequest request) throws IOException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("pageNum",(pageNum-1)*pageSize);
		map.put("pageSize",pageSize);
		PageList list=hotWordSettingService.getHotWordSetting(map);
		return  list;
	}

	
	/**
	 * 获取手动设置
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/getHotWordManualSettingJson")
	@ResponseBody
	public Object getHotWordManualSettingJson(int pageNum,int pageSize,HttpServletResponse response,HttpServletRequest request) throws IOException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("pageNum",(pageNum-1)*pageSize);
		map.put("pageSize",pageSize);
		PageList list=hotWordSettingService.getHotWordManualSetting(map);
		return  list;
	}
	/**
	 * 
	 * @param word
	 * @return
	 */
	@RequestMapping("/checkForBiddenWord")
	@ResponseBody
	public boolean checkForBiddenWord(String word){
		return forbiddenSerivce.CheckForBiddenWord(word)>0;
	}
	

	/**
	 * 
	 * @param word
	 * @return
	 */
	@RequestMapping("/getHotWordSetting")
	public String getHotWordSetting(Integer id,Model model){
		HotWordSetting item=hotWordSettingService.getOneHotWordSetting(id); 
		String nextPublishTime=hotWordSettingService.getNextPublishTime();
		model.addAttribute("item",item);
		model.addAttribute("isFirst",hotWordSettingService.checkFirst()>0);
		model.addAttribute("isupdate","update");
		model.addAttribute("get_time_cur",nextPublishTime==null?"":nextPublishTime.substring(11,nextPublishTime.length()));

		return "/page/contentmanage/add_word_setting";
	}
	
	/**
	 * 手动设置的修改页面跳转
	 * @param word
	 * @return
	 */
	@RequestMapping("/getHotWordManualSetting")
	public String getHotWordManualSetting(Integer id,Model model){
		HotWordSetting item=hotWordSettingService.getOneHotWordSetting(id); 
		model.addAttribute("item",item);
		model.addAttribute("isupdate","update");
		return "/page/contentmanage/add_word_Manual_setting";
	}
	
	@RequestMapping("/doupdateWordSetting")
	@ResponseBody
	public boolean doupdateWordSetting(HotWordSetting wordset, HttpServletRequest request,String isFirst){
		try{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		 Date d = new Date();
		 Calendar cal = Calendar.getInstance();
		 String nextPublish="";
		if(isFirst.equals("true")){
			nextPublish=wordset.getNext_publish_time();
		}else{
			nextPublish=wordset.getFirst_publish_time()+" "+wordset.getPublish_date();
		}
		
		 wordset.setNext_publish_time(nextPublish);
		 Date date =sd.parse(wordset.getNext_publish_time().substring(0, 10));
		 cal.setTime(date); 
		int day=cal.get(Calendar.DATE); 
		cal.set(Calendar.DATE,day-wordset.getTime_slot()); 
		StringBuffer sb=new StringBuffer("");
		String str=nextPublish.substring(0, 10);
		sb.append(str.replaceFirst("-", "年").replaceFirst("-", "月"));
		sb.append("日");
		String next_publish_time_space=sdf.format(cal.getTime())+" "+wordset.getGet_time()+"───"+sb.toString()+" "+wordset.getGet_time();
		 wordset.setNext_publish_time_space(next_publish_time_space);
		 wordset.setOperation(CookieUtil.getWfadmin(request).getUser_realname());
		 sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 wordset.setOperation_date(sdf.format(d));
		}catch(Exception e){
			e.printStackTrace();
		}
		 return hotWordSettingService.updateWordSetting(wordset)>0;
	}
	/**
	 * 修改手动设置
	 * @param wordset
	 * @param request
	 * @return
	 */
	@RequestMapping("/doupdateWordManualSetting")
	@ResponseBody
	public boolean doupdateWordManualSetting(HotWordSetting wordset, HttpServletRequest request){
		try{
		SimpleDateFormat sdf =  sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date d = new Date();
		 Calendar cal = Calendar.getInstance();
		 wordset.setOperation(CookieUtil.getWfadmin(request).getUser_realname());
		 sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 wordset.setOperation_date(sdf.format(d));
		}catch(Exception e){
			e.printStackTrace();
		}
		 return hotWordSettingService.updateWordSetting(wordset)>0;
	}


	
	@RequestMapping("/updateWordSettingStatus")
	@ResponseBody
	public boolean updateWordSettingStatus(Integer id,Integer status){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//把所有设置状态改为2，待应用
		hotWordSettingService.updateAllSetting();
		HotWordSetting wordset=new HotWordSetting();
		wordset=new HotWordSetting();
		wordset.setStatus(status);
		wordset.setId(id);
		wordset.setOperation_date(sdf.format(new Date()));
		//更新属性
		Integer update=hotWordSettingService.updateWordSetting(wordset);
		hotWordSettingService.updateAllSettingTime();
		return update>0;
	}	
	
	/**
	 * 应用手动设置
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateWordManualSettingStatus")
	@ResponseBody
	public boolean updateWordManualSettingStatus(Integer id,Integer status){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//把所有设置状态改为2，待应用
		hotWordSettingService.updateAllSetting();
		HotWordSetting wordset=new HotWordSetting();
		wordset=new HotWordSetting();
		wordset.setStatus(status);
		wordset.setId(id);
		wordset.setOperation_date(sdf.format(new Date()));
		sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date d = new Date(); 
		String getTime=com.wanfangdata.hotwordsetting.HotWordSetting.getGet_time();//获取设定的几点抓取时间
		Calendar cal = Calendar.getInstance();
		int day=cal.get(Calendar.DATE); 
		cal.set(Calendar.DATE,day+1);
		wordset.setNext_get_time(sdf.format(cal.getTime())+"  "+getTime);
		wordset.setIs_first("0");
		//更新属性
		Integer update=hotWordSettingService.updateWordSetting(wordset);
		return update>0;
	}	  
	
	@RequestMapping("periodicalcomment")
	public String PerioComment(){
		return "/page/contentmanage/periocomment";
	}
}
