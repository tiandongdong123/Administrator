package com.wf.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.ecs.storage.Hash;
import org.apache.ecs.xhtml.map;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exportExcel.ExportExcel;
import com.utils.CookieUtil;
import com.utils.DateTools;
import com.utils.JsonUtil;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.service.LogService;

@Controller
@RequestMapping("log")
public class LogController {

	@Autowired
	private LogService logService;

	/**
	 * 日志
	 * 
	 */
	@RequestMapping("getLog")
	public String getLog(Map<String, Object> model) {
		
		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		model.put("yesterday", yesterday);
		model.put("getAllModel", logService.getAllLogModel());
		return "/page/systemmanager/log";
	}

	/**
	 * 后台日志管理分页
	 * @param request 
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("getLogJson")
	public void getLogJson(HttpServletResponse response,String username,
			String ip, String module,String behavior, String startTime, String endTime,
			Integer pageNum, HttpServletRequest request) throws Exception {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar  calendar=new  GregorianCalendar(); 
		
		if(StringUtils.isEmpty(endTime)){
			endTime=null;
		}else{
			calendar.setTime(format.parse(endTime));
			calendar.add(calendar.DATE,1);
			endTime=format.format(calendar.getTime());
		}
		  
		 
		PageList p=logService.getLog(username,ip,module,behavior,startTime,endTime, pageNum);
		
		
		if("机构用户信息管理".equals(module)){
			if(p.getPageRow().size()>0){
				for(Object obj:p.getPageRow()){
					Map<String,Object> mm=(Map<String, Object>) obj;
					String content_json=String.valueOf(mm.get("operation_content_json"));
					JSONObject jsonobj = JSONObject.fromObject(content_json);
					String str=String.valueOf(jsonobj.get("reason"));
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
					mm.put("totalMoney", json.get("totalMoney").toString().equals("0")?"":json.get("totalMoney"));
					mm.put("purchaseNumber",json.get("purchaseNumber").toString().equals("0")?"":json.get("purchaseNumber"));
					mm.put("userId", jsonobj.get("userId"));
				}
			}

		}
		
		response.setCharacterEncoding("UTF-8");
		JSONObject json=JSONObject.fromObject(p);
		response.getWriter().write(json.toString());
	}
	
	/**
	 * 导出日志管理
	 * @param response
	 * @param username
	 * @param ip
	 * @param behavior
	 * @param startTime
	 * @param endTime
	 * @param request 
	 * @throws ParseException 
	 * @throws Exception 
	 */
	@RequestMapping("exportLog")
	public void exportLog(HttpServletResponse response, String username,
			String ip,String module,String behavior, String startTime, String endTime, HttpServletRequest request) throws ParseException{
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar  calendar=new  GregorianCalendar(); 
		
		if(StringUtils.isEmpty(endTime)){
			endTime=null;
		}else{
			calendar.setTime(format.parse(endTime));
			calendar.add(Calendar.DATE,1);
			endTime=format.format(calendar.getTime());
		}
		
		
		if("机构用户信息管理".equals(module)){
			
			List<Object> list=logService.exportLog(username, ip, module,behavior, startTime, endTime);
			List<String> names=Arrays.asList(new String[]{"序号","操作用户","操作IP","操作时间","操作类型","用户ID","购买项目名称","金额","次数","有效开始时间","有效结束时间"});
			
			
			if(list.size()>0){
				for(Object obj:list){
					Map<String,Object> mm=(Map<String, Object>) obj;
					String content_json=String.valueOf(mm.get("operation_content_json"));
					JSONObject jsonobj = JSONObject.fromObject(content_json);
					String str=String.valueOf(jsonobj.get("reason"));
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
					mm.put("totalMoney", json.get("totalMoney").toString().equals("0")?"":json.get("totalMoney"));
					mm.put("purchaseNumber",json.get("purchaseNumber").toString().equals("0")?"":json.get("purchaseNumber"));
					mm.put("userId", jsonobj.get("userId"));
				}
			}
			
			
			try {
				
				SimpleDateFormat format1=new SimpleDateFormat("yyyyMMddHHmmss");
				String filename=format1.format(new Date())+".xlsx";
				String time;
				XSSFWorkbook workbook=new XSSFWorkbook();
				XSSFSheet sheet=workbook.createSheet("日志导出");
				XSSFRow row=sheet.createRow(0);
				for (int i = 0; i < names.size(); i++) {
					row.createCell(i).setCellValue(names.get(i));
				}
				
				for (int i = 0; i < list.size(); i++) {
					Map<String,Object> mm=(Map<String, Object>) list.get(i);
					time=mm.get("time").toString();
					row=sheet.createRow(i+1);
					row.createCell(0).setCellValue(i+1);
					row.createCell(1).setCellValue(mm.get("username").toString());
					row.createCell(2).setCellValue(mm.get(("ip")).toString());
					row.createCell(3).setCellValue(time.substring(0,time.length()-2));
					row.createCell(4).setCellValue(mm.get("behavior").toString());
					row.createCell(5).setCellValue(mm.get("module").toString());
					row.createCell(6).setCellValue(mm.get("userId").toString());
					row.createCell(7).setCellValue(mm.get("projectname").toString());
					row.createCell(8).setCellValue(mm.get("totalMoney").toString());
					row.createCell(9).setCellValue(mm.get("purchaseNumber").toString());
					row.createCell(10).setCellValue(mm.get("validityStarttime").toString());
					row.createCell(10).setCellValue(mm.get("validityEndtime").toString());
				}
				
				//设置Content-Disposition  
				response.setHeader("Content-Disposition", "attachment;filename="+ filename); 
		        OutputStream out = response.getOutputStream();
				// 写文件
				workbook.write(out);  
				// 关闭输出流
				out.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}	

		}else{
			
			List<Object> list=logService.exportLog(username, ip, module,behavior, startTime, endTime);
			JSONArray array=JSONArray.fromObject(list);
			List<String> names=Arrays.asList(new String[]{"序号","操作用户","操作IP","操作时间","操作类型","操作模块","日志内容"});
			try {
				
				SimpleDateFormat format1=new SimpleDateFormat("yyyyMMddHHmmss");
				String filename=format1.format(new Date())+".xlsx";
				String time;
				XSSFWorkbook workbook=new XSSFWorkbook();
				XSSFSheet sheet=workbook.createSheet("日志导出");
				XSSFRow row=sheet.createRow(0);
				for (int i = 0; i < names.size(); i++) {
					row.createCell(i).setCellValue(names.get(i));
				}
				
				for (int i = 0; i < array.size(); i++) {
					time=array.getJSONObject(i).get("time").toString();
					row=sheet.createRow(i+1);
					row.createCell(0).setCellValue(i+1);
					row.createCell(1).setCellValue(array.getJSONObject(i).get("username").toString());
					row.createCell(2).setCellValue(array.getJSONObject(i).get("ip").toString());
					row.createCell(3).setCellValue(time.substring(0,time.length()-2));
					row.createCell(4).setCellValue(array.getJSONObject(i).get("behavior").toString());
					row.createCell(4).setCellValue(array.getJSONObject(i).get("module").toString());
					row.createCell(4).setCellValue(array.getJSONObject(i).get("operation_content").toString());
				}
				
				//设置Content-Disposition  
				response.setHeader("Content-Disposition", "attachment;filename="+ filename); 
		        OutputStream out = response.getOutputStream();
				// 写文件
				workbook.write(out);  
				// 关闭输出流
				out.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	
	}
	
	/**
	 * 根据日志ID删除日志
	 * @param response
	 * @param request 
	 * @param id 日志ID
	 * @throws UnknownHostException 
	 */
	@RequestMapping("deleteLog")
	@ResponseBody
	public Integer deleteLog(HttpServletResponse response,@RequestParam(value="ids[]",required=false)Integer[]ids,
			HttpServletRequest request){
		return logService.deleteLogByID(ids);
	}
	
	/**
	 * 根据模块名称获取对应的模块操作类型
	 * @param model 模块名称
	 * @return 模块操作类型
	 */
	@RequestMapping("getResTypeByModel")
	@ResponseBody
	public List<String> getResTypeByModel(@RequestParam(value="modelname",required=false)String modelname){
		return logService.getResTypeByModel(modelname);
		
	}

}
