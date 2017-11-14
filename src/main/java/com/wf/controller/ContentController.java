package com.wf.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.apache.ecs.storage.Hash;
import org.bigdata.framework.common.api.volume.IVolumeService;
import org.bigdata.framework.common.model.SearchPageList;
import org.bigdata.framework.search.iservice.ISearchCoreResultService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	RedisUtil redis = new RedisUtil();
	/**
	 * 学科分类信息查询
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/subject")
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
	@RequestMapping("/addSubject")
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
		JsonUtil.toJsonHtml(response, b);
	}
	/**
	 * 修改学科分类信息跳转
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/updateSubject")
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
	}
	
	/**
	 * 增加资讯跳转
	 * @return
	 */
	@RequestMapping("/addMessage")
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		message.setCreateTime(sdf.format(new Date()));
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		message.setStick(sdf1.format(new Date()));
		boolean b =messageService.insertMessage(message);
		JsonUtil.toJsonHtml(response, b);
	}
	
	/**
	 * 资讯查询
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/message")
	public String message(
			@RequestParam(value="branch",required=false) String branch,
			@RequestParam(value="human",required=false) String human,
			@RequestParam(value="colums",required=false) String colums,
			@RequestParam(value="startTime",required=false) String startTime,
			@RequestParam(value="endTime",required=false) String endTime,
			@RequestParam(value="page",required=false) String   page,
			HttpServletRequest request,Model model){
		int pagenum=1;
		if(page!=null&&page!=""){
			pagenum=Integer.valueOf(page);
		}
		int pageSize=10;
		PageList messageList=messageService.getMessage(pagenum, pageSize, branch, human, colums, startTime, endTime);
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("branch",branch);
		mp.put("human",human);
		mp.put("colums",colums);
		mp.put("startTime",startTime);
		mp.put("endTime",endTime);
		model.addAttribute("pageList",messageList);
		model.addAttribute("meaasgeMap", mp);
		
		List<String> deptList=new ArrayList<String>();
		for (Object department : departmentService.getAllDept()) {
			deptList.add(((Department)department).getDeptName());
		}
		
		model.addAttribute("deptList",deptList);
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
	public Object getMessageJson(
			@RequestParam(value="branch",required=false) String branch,
			@RequestParam(value="human",required=false) String human,
			String colums,
			@RequestParam(value="startTime",required=false) String startTime,
			@RequestParam(value="endTime",required=false) String endTime,
			@RequestParam(value="page",required=false) int pageNum,
			HttpServletResponse response,HttpServletRequest request) throws IOException{
		int pageSize=10;
		PageList messageList=messageService.getMessage(pageNum, pageSize, branch, human, colums, startTime, endTime);
		return  messageList;
	}
	
	
	/**
	 * 咨询详情跳转
	 * @return
	 */
	@RequestMapping("/getDetails")
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
		JsonUtil.toJsonHtml(response, b);
	}
	/**
	 * 修改资讯
	 * @param id
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/updateMessage")
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
		JsonUtil.toJsonHtml(response, b);
	}
	/**
	 * 发布/下撤/再发布
	 * @param id
	 * @param issueState
	 * @return
	 */
	@RequestMapping("/updateIssue")
	@ResponseBody
	public boolean updateIssue(String id,String colums,String issueState){
		boolean b =messageService.updateIssue(id,colums,issueState);
		return b;
	}
	/**
	 * 资源类型管理查询
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/resourceManage")
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
	}
	
	/**
	 * 添加资源跳转
	 * @return
	 */
	@RequestMapping("/addResource")
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
	@RequestMapping("/updateResource")
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
	 */
	@RequestMapping("/shareTemplate")
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
		int pageSize=10;
		PageList p=shareTemplateService.getShareTemplate(pageNum, pageSize, shareType);
		JSONObject json=JSONObject.fromObject(p);
		JsonUtil.toJsonHtml(response, json.toString());
	}
	
	/**
	 * 分享模板增加跳转
	 * @return
	 */
	@RequestMapping("/addShareTemplate")
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
		JsonUtil.toJsonHtml(response,b);
	}
	
	@RequestMapping("/deleteShareTemplate")
	public void deleteShareTemplate(HttpServletResponse response,String ids) throws Exception{
		boolean b =shareTemplateService.deleteShareTemplate(ids);
		JsonUtil.toJsonHtml(response, b);
	}
	
	@RequestMapping("/updateShareTemplate")
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
	public void updateShareTemplate(HttpServletResponse response,ShareTemplate shareTemplate,@RequestParam(value="checkValue[]",required=false)String[]checkValue) throws Exception{
		
		String str="";
		for (String string : checkValue) {
			str+=string;
		}
		shareTemplate.setShareContent(str);
		boolean b =shareTemplateService.updateShareTemplate(shareTemplate);
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
	@RequestMapping("/notes")
	public String notes(Model model){
		int pageNum=1;
		int pageSize=10;
		PageList pageList =notesService.getNotes(pageNum, pageSize,null, null, null, null, null, null, null, null);
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
			@RequestParam(value="page",required=false) int pageNum
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
		
		int pageSize=10;
		PageList NotepageList =notesService.getNotes(pageNum, pageSize, userName, noteNum, resourceName, resourceType, dataState, complaintStatus, startTime, endTime);
		JSONObject json=JSONObject.fromObject(NotepageList);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
	}
	
	@RequestMapping("/findNote")
	public String findNote(Model model,@RequestParam(value="id",required=false) String id){
	
		boolean b=notesService.handlingNote(id);
		Notes notes =notesService.findNotes(id);
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
	public void updateNotes(Notes notes,HttpServletResponse response) throws Exception{
		notes.setHandlingStatus(3);
		boolean b=notesService.updateNotes(notes);
		JsonUtil.toJsonHtml(response, b);
	}
	@RequestMapping("/stick")
	public void stick(Message message,@RequestParam("colums")String colums,HttpServletResponse response) throws Exception{
		String stick=DateTools.getSysTime();
		message.setStick(stick);
		boolean b =messageService.updataMessageStick(message, colums);
		JsonUtil.toJsonHtml(response, b);
	}
	
	/**
	 * 文辑管理
	 * @return
	 */
	@RequestMapping("/volumeDocu")
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
	@RequestMapping("/volumeDetails")
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
		@RequestMapping("/stepOne")
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
		@RequestMapping("/updateOne")
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
	* @Description: TODO(redis取值批量插入学科数据) 
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
	* @Description: TODO(学科发布功能) 
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
	* @Description: TODO(资源发布功能) 
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
	 */
	@RequestMapping("/exportNotes")
	public void exportNotes(HttpServletRequest request,HttpServletResponse response,String userName,
			String noteNum, String resourceName,String[] resourceType,String[] dataState,
			String[] complaintStatus,String startTime,String endTime){
		
		ExportExcel excel=new ExportExcel();
	
		if(StringUtils.isEmpty(userName)) userName=null;
		if(StringUtils.isEmpty(noteNum)) noteNum=null;
		if(StringUtils.isEmpty(resourceName)) resourceName=null;
		if(StringUtils.isEmpty(startTime)) startTime=null;
		if(StringUtils.isEmpty(endTime)) endTime=null;
		if(resourceType.length==0) resourceType=null;
		if(dataState.length==0) dataState=null;
		if(complaintStatus.length==0) complaintStatus=null;
		
		
		List<Object> list=notesService.exportNotes(userName, noteNum, resourceName, resourceType, dataState, complaintStatus, startTime, endTime);
		JSONArray array=JSONArray.fromObject(list);
		List<String> names=Arrays.asList(new String[]{"序号","笔记编号","资源编号","资源类型","资源类型","笔记内容","处理意见","用户ID","笔记日期","数据状态","申诉状态"});		

		excel.ExportNotes(response, array, names);
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
	 */
	@RequestMapping("exportVolumeDocu")
	public void exportVolumeDocu(String startTime,String endTime,String searchWord,String volumeType,String volumeState,
			String sortColumn,String sortWay,HttpServletResponse response){
		
		List<Object> list=new ArrayList<Object>();
		list= volumeService.exportVolumeDocu(startTime, endTime, searchWord, volumeType, volumeState, sortColumn,sortWay);
		JSONArray array=JSONArray.fromObject(list);
		
		List<String> names=volumeType.equals("1")?
			Arrays.asList(new String[]{"序号","文辑编号","文辑名称","关键词","文辑状态","发布用户名","发布日期","文辑文献数量"})
			:Arrays.asList(new String[]{"序号","文辑编号","学科","文辑名称","关键词","发布用户名","发布日期","文辑文献数量","价格"});
		
		ExportExcel excel=new ExportExcel();
		excel.exportVolumeDocu(response, array, names,volumeType);
	}
	
	/**
	 * 导出分享模板
	 * @param response
	 * @param shareType 分享类型
	 */
	@RequestMapping("exportshareTemplate")
	public void exportshareTemplate(HttpServletResponse response,String shareType){
		
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
	 */
	@RequestMapping("exportResource")
	public void exportResource(HttpServletResponse response,String resouceType){
		
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
	 */
	@RequestMapping("exportSubject")
	public void exportSubject(HttpServletResponse response,Integer pageNum,String level,String classNum,String className){
		List<Object> list=new ArrayList<Object>();
		list=subjectService.exportSubject(level,classNum,className);
		JSONArray array=JSONArray.fromObject(list);
		
		List<String> names=Arrays.asList(new String[]{"序号","级别","分类号","分类名称"});
		
		ExportExcel excel=new ExportExcel();
		excel.exportSubject(response, array, names);
		
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
	 */
	@RequestMapping("exportMessage")
	public void exportMessage(HttpServletResponse response,String branch,String colums,String human,String startTime,String endTime){
		List<Object> list=new ArrayList<>();
		list= messageService.exportMessage(branch,colums,human,startTime,endTime);
		JSONArray array=JSONArray.fromObject(list);
		List<String> names=Arrays.asList(new String[]{"序号","栏目","标题","原文链接","添加人","添加日期"});
		ExportExcel excel=new ExportExcel();
		excel.exportMessage(response, array, names);
	}
	
	/**
	 *一键发布
	 * @param request
	 * @param response
	 */
	@RequestMapping("/oneKeyDeploy")
	@ResponseBody
	public Boolean oneKeyDeploy(HttpServletRequest request,HttpServletResponse response){
		  boolean isOK=true;
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("issue_state",2);
			List<Object> list=messageService.getAllMessage(map);
			for(Object obj:list){
				Message mm=(Message) obj;
					messageService.updateIssue(String.valueOf(mm.getId()),mm.getColums(),"2");
			}
		} catch (Exception e) {
			isOK=false;
			e.printStackTrace();
		}
		return isOK;
	}
}
