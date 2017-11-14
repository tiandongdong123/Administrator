package com.wf.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wf.service.DB_SourceService;
import net.sf.json.JSONArray;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exportExcel.ExportExcel;
import com.wf.bean.PageList;
import com.wf.bean.ResourceStatistics;
import com.wf.bean.ResourceType;
import com.wf.service.DataManagerService;
import com.wf.service.ResourceTypeStatisticsService;

@Controller
@RequestMapping("resourceTypeStatistics")
public class ResourceTypeStatisticsController {

	@Autowired
	private ResourceTypeStatisticsService resource;
	@Autowired
	private DB_SourceService db_SourceService;

	@Autowired
	private DataManagerService dataManagerService;
	
	@RequestMapping("resourceTypeStatistics")
	public String resourceTypeStatistics(Map<String,Object> map){
		List<ResourceType> li = this.resource.getResourceType();
		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.DATE,   -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		map.put("allData",dataManagerService.selectAll());
		//所有数据来源
		map.put("allDataSource",db_SourceService.selectAll());
		map.put("yesterday", yesterday);
		map.put("resourcetype", li);
		return "/page/othermanager/res_use_manager";
	}
	
	@RequestMapping("getline")
	@ResponseBody
	public Map<String,Object> getLine(String starttime,String endtime,@ModelAttribute ResourceStatistics res
			,@RequestParam(value="urls[]",required=false) Integer[] urls,Integer singmore,String[] title,String[] database_name){
		Map<String,Object> map = new HashMap<String, Object>();
		map =this.resource.getAllLine(starttime,endtime,res,urls,singmore,title,database_name);
		return map;
	}
	
	@RequestMapping("gettable")
	@ResponseBody
	public PageList getTable(Integer num,Integer pagenum,Integer pagesize,String starttime,String endtime,@ModelAttribute ResourceStatistics res){
		PageList pageList  = this.resource.gettable(num,starttime,endtime, res,pagenum, pagesize);
		return pageList;
	}
	
	@RequestMapping(value="exportresourceType",produces="text/html;charset=UTF-8")
	public void exportresourceType(HttpServletRequest request,HttpServletResponse response,Integer num,Integer pagenum,String starttime,String endtime,@ModelAttribute ResourceStatistics res) throws UnsupportedEncodingException {
		List<Object> list=new ArrayList<Object>();
		if(StringUtils.isNotBlank(res.getInstitutionName())){
			String name = java.net.URLDecoder.decode(request.getParameter("institutionName"), "utf-8");
			res.setInstitutionName(name);
		}

		list= this.resource.exportresourceType(num,starttime,endtime, res);
		JSONArray array=JSONArray.fromObject(list);
		
		List<String> names=new ArrayList<String>();
		names.add("序号");
		names.add("资源类型");
		String restype=res.getSourceTypeName();
		if("perio".equals(restype)){
			names.add("期刊名称");
		}else if("conference".equals(restype)){
			names.add("会议名称");
		}else if("degree".equals(restype)){
			names.add("授予学位的机构名称");
		}
		names.add("检索数");
		names.add("浏览数");
		names.add("下载数");
		names.add("跳转数");
		names.add("订阅数");
		names.add("收藏数");
		names.add("笔记数");
		names.add("分享数");
		names.add("导出数");
		
		List<String> paramter=new ArrayList<String>();
		String name="degree".equals(restype)?res.getSourceName():res.getInstitutionName();
		if(StringUtils.isNotBlank(name))
			paramter.add("机构名称："+name);
		if(StringUtils.isNotBlank(res.getUserId()))
			paramter.add("用户ID："+res.getUserId());
		
		if(null!=res.getDate()){
			paramter.add("统计日期："+res.getDate());
		}else{
			if(StringUtils.isNotBlank(starttime) && StringUtils.isNotBlank(endtime)){
				paramter.add("统计日期："+starttime+"--"+endtime);
			}else if(StringUtils.isNotBlank(starttime)){
				paramter.add("统计日期： "+starttime+"--");
			}else if(StringUtils.isNotBlank(endtime)){
				paramter.add("统计日期： "+"--"+endtime);
			}
		}
		
		ExportExcel excel=new ExportExcel();
		excel.exportresourceType(response, array, names, restype,paramter);
		
	}
	
}
