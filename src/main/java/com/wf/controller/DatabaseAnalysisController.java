package com.wf.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.DataUtil;
import com.utils.DateUtil;
import com.wf.bean.DB_Source;
import com.wf.bean.DatabaseUseDaily;
import com.wf.bean.Datamanager;
import com.wf.bean.PageList;
import com.wf.service.DB_SourceService;
import com.wf.service.DataManagerService;
import com.wf.service.DatabaseAnalysisService;
import com.wf.service.PersonService;

@Controller
@RequestMapping("databaseAnalysis")
public class DatabaseAnalysisController {

	@Autowired
	private DatabaseAnalysisService databaseAnalysisService;
	
	@Autowired
	private DB_SourceService db_SourceService;
	
	@Autowired
	private DataManagerService dataManagerService;
	
	@Autowired
	private PersonService personService;
	
	/**
	* @Title: databaseAnalysis
	* @Description: TODO(数据库分析页面) 
	* @return String 返回类型 
	* @author LiuYong 
	* @date 12 Dis 2016 4:44:32 PM
	 */
	@RequestMapping("databaseAnalysis")
	public String databaseAnalysis(Map<String,Object> map){
		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		map.put("yesterday", yesterday);
		//所有数据库名称
		map.put("allData",dataManagerService.selectAll());
		//所有数据来源
		map.put("allDataSource",db_SourceService.selectAll());
		return "/page/othermanager/data_use_manager";
		}
	
	@ResponseBody
	@RequestMapping("getDatabaseBySourceCode")
	public List<Datamanager> getDatabaseBySourceCode(String code){
		return  dataManagerService.getDatabaseByCode(code);
	}
	
	
	/**
	* @Title: getPage
	* @Description: TODO(列表展示数据)
	* @param startTime
	* @param endTime
	* @param pagenum
	* @param pagesize
	* @return
	* @return List 返回类型 
	* @author LiuYong 
	 * @throws Exception 
	* @date 13 Dis 2016 4:26:24 PM
	 */
	@RequestMapping("getPage")
	@ResponseBody
	public Map getPage(DatabaseUseDaily databaseUseDaily, String startTime,String endTime,Integer pagenum,Integer pagesize) throws Exception{
		//列表展示		
		Map map=databaseAnalysisService.getDatabaseAnalysisList(databaseUseDaily, startTime, endTime, pagenum, pagesize);
				
		return map;
	}
	
	/**
	* @Title: getChars
	* @Description: TODO(图表展示数据)
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	 * @throws Exception 
	* @date 13 Dis 2016 5:06:00 PM
	 */
	@RequestMapping("getChart")
	@ResponseBody
	public Map<String,Object> getChart(DatabaseUseDaily databaseUseDaily, String startTime,String endTime,@RequestParam(value="urlType[]",required=false)Integer[]urlType,
			@RequestParam(value="databaseNames[]",required=false)String[]databaseNames) throws Exception{
		
		//图表展示
		Map<String,Object> map=databaseAnalysisService.DatabaseAnalysisStatistics(databaseUseDaily,startTime,endTime,urlType,databaseNames);
				
		return map;
	}
	
	/**
	 * 导出---数据库使用分析
	 * @param response
	 * @param databaseUseDaily
	 * @param startTime
	 * @param endTime
	 * @throws Exception 
	 */
	@RequestMapping("exportDatabase")
	public void exportDatabase(HttpServletRequest request, HttpServletResponse response, DatabaseUseDaily databaseUseDaily, String startTime, String endTime) throws Exception{
		
		String time1="";
		String time2="";
		
		//标题拼装
		StringBuffer sb=new StringBuffer("");

		if(StringUtils.isNotBlank(databaseUseDaily.getInstitution_name())){
			String name = java.net.URLDecoder.decode(request.getParameter("institution_name"), "utf-8");
			databaseUseDaily.setInstitution_name(name);
			sb.append(name);
		}

		if(StringUtils.isNotBlank(databaseUseDaily.getDate())){
			time1=time2=databaseUseDaily.getDate();
			String[]day=databaseUseDaily.getDate().split("-");
			sb.append(day[0]+"年"+Integer.valueOf(day[1]).intValue()+"月"+Integer.valueOf(day[2]).intValue()+"日-");
			sb.append(day[0]+"年"+Integer.valueOf(day[1]).intValue()+"月"+Integer.valueOf(day[2]).intValue()+"日");
		}else{
			time1=startTime;
			time2=endTime;
			String[]date1=startTime.split("-");
			String[]date2=endTime.split("-");
			sb.append(date1[0]+"年"+Integer.valueOf(date1[1]).intValue()+"月"+Integer.valueOf(date1[2]).intValue()+"日-");
			sb.append(date2[0]+"年"+Integer.valueOf(date2[1]).intValue()+"月"+Integer.valueOf(date2[2]).intValue()+"日");
		}
		
		sb.append("数据库使用统计表");
		
		//表头拼装
		List<String> namelist=new ArrayList<String>();
		namelist.add("数据库名称");
		namelist.add("分析指标");
		List<Map<String, String>> monList=DateUtil.getMonthMap(time1,time2);
		for (Map<String, String> map : monList) {
			String time[]=map.get("time").split("-");
			namelist.add(time[0]+"年"+time[1]+"月");
		}
		namelist.add("合计");
		
		//数据包装

		Map<String,List<Map<String,String>>> data=databaseAnalysisService.exportDatabase(databaseUseDaily, startTime, endTime);
		for (Entry<String, List<Map<String, String>>> entry : data.entrySet()) {
			
			Integer downloadSum = 0;
			Integer searchSum = 0;
			Integer browseSum = 0;
			
			//循环一次创建一个模块的统计数据
			String title=entry.getKey();
			List<Map<String, String>> tjList=entry.getValue();
			//循环
			List<Map<String, String>> tempList=new ArrayList<Map<String, String>>();
			for(int c=0;c<monList.size();c++){
				String monStr=monList.get(c).get("time");
				boolean flag=false; 	
				Map<String,String> tempMap= new HashMap<String,String>();
				for(int m=0;m<tjList.size();m++){
					Map<String,String> tjMap=tjList.get(m);
					String tjStr=String.valueOf(tjMap.get("year"))+"-"+String.valueOf(tjMap.get("month"));
					if(monStr.equals(tjStr)){
						String downloadNum = String.valueOf(tjMap.get("downloadNum"));
						String searchNum =  String.valueOf(tjMap.get("searchNum"));
						String browseNum = String.valueOf(tjMap.get("browseNum"));
						tempMap.put("downloadNum",downloadNum.substring(0,downloadNum.length()-2) );
						tempMap.put("searchNum",searchNum.substring(0,searchNum.length()-2));
						tempMap.put("browseNum", browseNum.substring(0,browseNum.length()-2));
						tempMap.put("time", monStr);
						flag=true;
						break;
					}
				}
				if(!flag){
					tempMap.put("downloadNum", "0");
					tempMap.put("searchNum", "0");
					tempMap.put("browseNum","0");
				}

				Double downloadsum = Double.valueOf(tempMap.get("downloadNum"));
				Double searchsum = Double.valueOf(tempMap.get("searchNum"));
				Double browsesum = Double.valueOf(tempMap.get("browseNum"));

				downloadSum = downloadsum.intValue();
				searchSum = searchsum.intValue();
				browseSum = browsesum.intValue();

				tempList.add(tempMap);
			}
			Map<String,String> sum=new HashMap<String, String>();
			sum.put("downloadSum",downloadSum.toString());
			sum.put("searchSum",searchSum.toString());
			sum.put("browseSum",browseSum.toString());
			tempList.add(sum);
			data.put(title, tempList);
		}
		
		//标题、列名、导出的数据 生成完毕  开始进行导出工作
		try {
			
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
			String filename="数据库使用分析"+format.format(new Date())+".xlsx";
			
			XSSFWorkbook workbook=new XSSFWorkbook();
			XSSFSheet sheet=workbook.createSheet("数据库使用分析");
			
			XSSFRow rowheader = sheet.createRow(0);
			XSSFCell cellheader = rowheader.createCell(0);
			CellRangeAddress range = new CellRangeAddress(0, 0, 0, namelist.size()-1);
			sheet.addMergedRegion(range);
			XSSFCellStyle cellStyle = workbook.createCellStyle();  
			cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
			cellheader.setCellStyle(cellStyle);
			cellheader.setCellValue(sb.toString());
			
			XSSFRow row=sheet.createRow(1);
			for (int i = 0; i < namelist.size(); i++) {
				sheet.setColumnWidth(i,namelist.get(i).getBytes().length*1*256);
				row.createCell(i).setCellValue(namelist.get(i));
			}
			
			
			int i=0;
			String[] type=new String[]{"检索数","浏览数","下载数"};
			String[] type2=new String[]{"searchNum","browseNum","downloadNum"};
			String[] type3=new String[]{"searchSum","browseSum","downloadSum"};
			for (Entry<String, List<Map<String, String>>> entry : data.entrySet()) {
				
				List<Map<String, String>> resultList=entry.getValue();
				String title=entry.getKey();
				for(int r=0;r<type.length;r++){
					row=sheet.createRow(i+r+2);
					row.createCell(0).setCellValue(title);
					row.createCell(1).setCellValue(type[r]);
					int sum = 0 ;
					for (int j =1; j <resultList.size(); j++) {
						row.createCell(j+1).setCellValue(resultList.get(j-1).get(type2[r]));
						sum+=Integer.parseInt(resultList.get(j-1).get(type2[r]));
					}
					row.createCell(namelist.size()-1).setCellValue(sum);
					if(r==type.length-1){
						sheet.addMergedRegion(new CellRangeAddress(i+r,i+r+2,0,0));
					}
				}
				
				i=i+3;
			}
			
			//设置Content-Disposition
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="+ new String(filename.getBytes(),"iso-8859-1")); 
	        OutputStream out = response.getOutputStream();
			// 写文件
			workbook.write(out);  
			// 关闭输出流
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取所有机构名称
	 * @param institution 机构名称
	 * @return
	 */
	@RequestMapping("/getAllInstitution")
	@ResponseBody
	public List<String> getAllInstitution(String institution){ 
		return personService.getAllInstitution(institution);
	}
	
	@RequestMapping("/getDB_SourceByInstitution")
	@ResponseBody
	public List<DB_Source> getDB_SourceByInstitution(String institution){
		return db_SourceService.getInstitutionDB_Source(institution);
	}

}
