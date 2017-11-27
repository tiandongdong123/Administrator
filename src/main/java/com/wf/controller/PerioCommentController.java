package com.wf.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.ecs.xhtml.object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exportExcel.ExportExcel;
import com.redis.getClcFromRedis;
import com.utils.CookieUtil;
import com.utils.JsonUtil;
import com.wf.bean.CommentInfo;
import com.wf.bean.PageList;
import com.wf.bean.Wfadmin;
import com.wf.service.AdminService;
import com.wf.service.PerioCommentService;

@Controller
@RequestMapping("periocomment")
public class PerioCommentController {

	@Autowired
	private PerioCommentService pc;
	
	@Autowired
	private AdminService adminS;
	
	@RequestMapping("periocomment")
	public String PerioComment(){
		
		return "/page/contentmanage/periocomment";
	}
	
	@RequestMapping("getcomment")
	@ResponseBody
	public PageList getComment(CommentInfo info,
			@RequestParam(value="dataStateArr[]",required=false)String[] dataState,
			@RequestParam(value="complaintStatusArr[]",required=false)String[] complaintStatus,
			String startTime,String endTime,
			String sauditm,String eauditm,String slayoutm,
			String elayoutm,Integer pagenum,Integer pagesize,HttpServletRequest request ,HttpServletResponse response) throws Exception{
		pagenum = (pagenum-1)*pagesize;
		response.setContentType("text/html;charset=utf-8");
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNotEmpty(startTime)&&StringUtils.isEmpty(endTime)){
			startTime=startTime+" 00:00";
		}else if(StringUtils.isNotEmpty(endTime)&&StringUtils.isEmpty(startTime)){
			endTime=endTime+" 23:59";
		}else if(StringUtils.isNotEmpty(endTime)&&StringUtils.isNotEmpty(startTime)){
			Date dateTime1 = dateFormat.parse(startTime);
			Date dateTime2 = dateFormat.parse(endTime);
			int compare = dateTime1.compareTo(dateTime2); 
			if(compare==0){
				startTime=startTime+" 00:00";
				endTime=endTime+" 23:59";
			}else if(compare>0){
				startTime=startTime+" 00:00";
				endTime=endTime+" 23:59";
			}else{
				endTime=endTime+" 00:00";
				startTime=startTime+" 23:59";
			}
		}
		
		PageList pl  = this.pc.getComment(info,dataState,complaintStatus,startTime, endTime, sauditm, eauditm, slayoutm, elayoutm, pagenum, pagesize);
		getClcFromRedis clc=new getClcFromRedis();
		List<Object> json=pl.getPageRow();
		for(int i=0;i<json.size();i++){
			CommentInfo com=(CommentInfo)json.get(i);
			com.setPublishing_discipline(clc.getClcName(com.getPublishing_discipline()));
			com.setGoods(pc.getGoodForCommont(com.getId())+"");
			if(StringUtils.isNotEmpty(com.getAuditor())){
				com.setAuditor(adminS.getAdminById(com.getAuditor()).getUser_realname());
			}
			json.remove(i);
			json.add(i, com);
		}
		
		pl.setPageRow(json);
		
		return pl;
	}
	
	
	@RequestMapping("controltype")
	@ResponseBody
	public Object name(CommentInfo info) {
	
		Boolean i=this.pc.changetype(info);
		String key="";
		if(i){
			key="ok";
		}else{
			key="no";
		}
		return key;
	}
	
	@RequestMapping("/findNote")
	public String findNote(Model model,HttpServletRequest request){
		//info.setHandlingStatus(2);
		String id = request.getParameter("id");
		
			getClcFromRedis clc=new getClcFromRedis();
			CommentInfo com=this.pc.getcommentByid(id);
			com.setPublishing_discipline(clc.getClcName(com.getPublishing_discipline()));
			com.setGoods(pc.getGoodForCommont(com.getId())+"");
			model.addAttribute("info", com);
		return "/page/contentmanage/periocom";
	}
	
	@RequestMapping("/findNotes")
	public String findNotes(Model model,CommentInfo info){
		CommentInfo notes =this.pc.findNotes( info);
		model.addAttribute("info", notes);
		return "/page/contentmanage/periocom";
	}
	
	@RequestMapping("/updateNotes")
	@ResponseBody
	public Object updateNotes(HttpServletRequest request) throws Exception{
		String id = request.getParameter("id");
		String dataState = request.getParameter("dataState");
		String appealReason = request.getParameter("appealReason");
		
		Wfadmin admin =CookieUtil.getWfadmin(request);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String user_id=admin.getId();
		String date=sdf.format(new Date());
		
		boolean b=this.pc.updateNotes(id,dataState,appealReason,user_id,date);
		return b;
	}
	
	
	
	
	/**
	 * 根据查询条件导出期刊
	 * @param info 期刊信息
	 * @param dataState 数据状态
	 * @param complaintStatus 申诉状态
	 * @param startTime 点评日期开始
	 * @param endTime 点评日期结束
	 * @param sauditm 审稿费开始
	 * @param eauditm 审稿费结束
	 * @param slayoutm 版面费开始
	 * @param elayoutm 版面费结束
	 * @param request
	 * @param response
	 */
	@RequestMapping("/exportPerio")
	public void exportPerio(CommentInfo info,
			String[]dataStateArr,String[]complaintStatusArr,
			String startTime,String endTime,
			String sauditm,String eauditm,String slayoutm,
			String elayoutm,HttpServletRequest request ,HttpServletResponse response){
		
			if(dataStateArr.length==0) dataStateArr=null;
			if(complaintStatusArr.length==0) complaintStatusArr=null;
		
		    ExportExcel exportExcel=new ExportExcel();
		    List<Object> list=new ArrayList<Object>();
			list= this.pc.exportPerio(info,dataStateArr,complaintStatusArr,startTime, endTime, sauditm, eauditm, slayoutm, elayoutm);
			JSONArray array=JSONArray.fromObject(list);
			List<String> names=Arrays.asList(new String[]{"序号","期刊名称","录用情况","审稿周期","审稿费","版面费","自定义点评","处理意见","点评用户名","数据状态","申诉状态"});
			
			exportExcel.exportPerio(response, array, names);
		
	}
	
}
