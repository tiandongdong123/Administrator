package com.wf.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exportExcel.ExportExcel;
import com.utils.JsonUtil;
import com.wf.bean.CommentInfo;
import com.wf.bean.PageList;
import com.wf.service.PerioCommentService;

@Controller
@RequestMapping("periocomment")
public class PerioCommentController {

	@Autowired
	private PerioCommentService pc;
	
	@RequestMapping("periocomment")
	public String PerioComment(){
		
		return "/page/contentmanage/periocomment";
	}
	
	@RequestMapping("getcomment")
	@ResponseBody
	public PageList getComment(CommentInfo info,
			@RequestParam(value="dataStateArr[]",required=false)String[]dataState,
			@RequestParam(value="complaintStatusArr[]",required=false)String[]complaintStatus,
			String startTime,String endTime,
			String sauditm,String eauditm,String slayoutm,
			String elayoutm,Integer pagenum,Integer pagesize,HttpServletRequest request ,HttpServletResponse response) throws Exception{
		pagenum = (pagenum-1)*pagesize;
		response.setContentType("text/html;charset=utf-8");
		String name=request.getParameter("perioName");
		info.setPerioName(name);
		
	/*	if(StringUtils.isEmpty(endTime)){
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			Calendar  calendar=new  GregorianCalendar(); 
			calendar.setTime(format.parse(endTime));
			calendar.add(calendar.DATE,1);
			endTime=format.format(calendar.getTime());
		}*/
		
		PageList pl  = this.pc.getComment(info,dataState,complaintStatus,startTime, endTime, sauditm, eauditm, slayoutm, elayoutm, pagenum, pagesize);
		return pl;
	}
	
	
	@RequestMapping("controltype")
	@ResponseBody
	public Object name(CommentInfo info) {
	
		Boolean i=this.pc.changetype(info);
		String key="";
		if(i)
		{
			key="ok";
		}
		else 
		{
			key="no";
		}
		return key;
	}
	
	@RequestMapping("/findNote")
	public String findNote(Model model,CommentInfo info){
		info.setHandlingStatus(2);
		boolean A=this.pc.handlingStatus(info);
		if(A){
			CommentInfo notes =this.pc.findNotes(info);
			model.addAttribute("info", notes);
		}
		
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
	public void updateNotes(Model model,CommentInfo info,HttpServletResponse response) throws Exception{
		boolean b=this.pc.updateNotes(info);
		JsonUtil.toJsonHtml(response, b);
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
