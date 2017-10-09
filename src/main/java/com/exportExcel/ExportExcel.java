package com.exportExcel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportExcel {
		/**
		 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
		 * 
		 * @param title
		 *            表格标题名
		 * @param headers
		 *            表格属性列名数组
		 * @param dataset
		 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
		 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
		 * @param out
		 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
		 * @param pattern
		 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
		 */

		/**
		 * 万方卡批次已审核未领取导出
		 * @param title  文本名称
		 * @param type	类型
		 * @param data	保存数据  list<map>
		 * @param realspath 真实路径
		 * @return
		 */
	public String exportExccel1(HttpServletResponse response, List<Map<String, Object>> list,
			List<String> namelist) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(new Date());
		String newpate = date + ".xlsx";
		try {
			// 工作区
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("审核未领取");
			XSSFRow row = sheet.createRow(0);
			for (int i = 0; i < namelist.size(); i++) {
				row.createCell(i).setCellValue(namelist.get(i));
			}
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map=list.get(i);
				// 创建第一个sheet
				// 生成第一行
				row = sheet.createRow(i + 1);
				// 给这一行的第一列赋值
				row.createCell(0).setCellValue(String.valueOf(map.get("cardTypeName")));
				row.createCell(1).setCellValue(String.valueOf(map.get("cardNum")));
				row.createCell(2).setCellValue(String.valueOf(map.get("password")));
				row.createCell(3).setCellValue(String.valueOf(map.get("value")));
				row.createCell(4).setCellValue(String.valueOf(map.get("validStart")) + "--"+ String.valueOf(map.get("validEnd")));
			}
			// 设置Content-Disposition
			response.setHeader("Content-Disposition", "attachment;filename=" + newpate);
			OutputStream out = response.getOutputStream();
			// 写文件
			wb.write(out);
			// 关闭输出流
			out.close();
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
			
		
	/**
	 * 万方卡批次已领取导出
	 * @param title 文本名称
	 * @param data 保存数据 list<map>
	 * @param realspath 真实路径
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportExcel2(HttpServletResponse response, List<Map<String, Object>> batchList,
			List<String> batchNamelist, List<Map<String, Object>> cardList,List<String> cardNamelist) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(new Date());
		String newpate = date + ".xlsx";
		try {
			// 工作区
			XSSFWorkbook wb = new XSSFWorkbook();
			// 创建第一个sheet
			XSSFSheet sheet1 = wb.createSheet("批次详情");

			XSSFRow row1 = sheet1.createRow(0);
			for (int i = 0; i < batchNamelist.size(); i++) {
				row1.createCell(i).setCellValue(batchNamelist.get(i));
			}
			for (int i = 0; i<batchList.size(); i++) {
				Map<String, Object> batchMap=batchList.get(i);
				// 生成第一行
				row1 = sheet1.createRow(i+1);
				//处理面值数量
				String valueAndNumber = "";
				List<Map<String,Object>> valueNumber = JSONArray.fromObject(batchMap.get("valueNumber"));
				for(int j = 0;j < valueNumber.size();j++){
					Map<String,Object> map = valueNumber.get(j);
					valueAndNumber += map.get("value") + "/" + map.get("number") + ",";
				}
				//处理批次状态
				String batchState = String.valueOf(batchMap.get("batchState"));
				if("1".equals(batchState)){
					batchState = "未审核";
				}else if("2".equals(batchState)){
					batchState = "已审核未领取";
				}else if("3".equals(batchState)){
					batchState = "已领取";
				}
				// 生成第一行
				row1 = sheet1.createRow(i+1);
				// 给这一行的第一列赋值
				row1.createCell(0).setCellValue(String.valueOf(batchMap.get("batchName")));
				row1.createCell(1).setCellValue(String.valueOf(batchMap.get("cardTypeName")));
				row1.createCell(2).setCellValue(valueAndNumber.substring(0, valueAndNumber.length()-1));
				row1.createCell(3).setCellValue(String.valueOf(batchMap.get("amount")));
				row1.createCell(4).setCellValue(String.valueOf(batchMap.get("validStart")) + "--" + String.valueOf(batchMap.get("validEnd")));
				row1.createCell(5).setCellValue(String.valueOf(batchMap.get("createDate")));
				row1.createCell(6).setCellValue(String.valueOf(batchMap.get("applyDepartment")));
				row1.createCell(7).setCellValue(String.valueOf(batchMap.get("applyPerson")));
				row1.createCell(8).setCellValue(String.valueOf(batchMap.get("applyDate")));
				row1.createCell(9).setCellValue(batchState);
				row1.createCell(10).setCellValue(String.valueOf(batchMap.get("checkDepartment")).equals("null")?"":String.valueOf(batchMap.get("checkDepartment")));
				row1.createCell(11).setCellValue(String.valueOf(batchMap.get("checkPerson")).equals("null")?"":String.valueOf(batchMap.get("checkPerson")));
				row1.createCell(12).setCellValue(String.valueOf(batchMap.get("checkDate")).equals("null")?"":String.valueOf(batchMap.get("checkDate")));
				row1.createCell(13).setCellValue(String.valueOf(batchMap.get("pullDepartment")).equals("null")?"":String.valueOf(batchMap.get("pullDepartment")));
				row1.createCell(14).setCellValue(String.valueOf(batchMap.get("pullPerson")).equals("null")?"":String.valueOf(batchMap.get("pullPerson")));
				row1.createCell(15).setCellValue(String.valueOf(batchMap.get("pullDate")).equals("null")?"":String.valueOf(batchMap.get("pullDate")));
				
			}
			// 创建第二个sheet
			XSSFSheet sheet2 = wb.createSheet("卡详情");
			XSSFRow row2 = sheet2.createRow(0);
			for(int i=0;i<cardNamelist.size();i++){
				row2.createCell(i).setCellValue(cardNamelist.get(i));
			}
			for (int i =0; i<cardList.size(); i++) {
				Map<String,Object> map=cardList.get(i);
				//处理激活状态
				String invokeState = String.valueOf(map.get("invokeState"));
				if("1".equals(invokeState)){
					invokeState = "未激活";
				}else if("2".equals(invokeState)){
					invokeState = "已激活";
				}else if("3".equals(invokeState)){
					invokeState = "已过期";
				}
				// 生成第一行
				row2 = sheet2.createRow(i+1);
				// 给这一行的第一列赋值
				row2.createCell(0).setCellValue(String.valueOf(map.get("batchName")));
				row2.createCell(1).setCellValue(String.valueOf(map.get("cardNum")));
				row2.createCell(2).setCellValue(String.valueOf(map.get("password")));
				row2.createCell(3).setCellValue(String.valueOf(map.get("value")));
				row2.createCell(4).setCellValue(invokeState);
				row2.createCell(5).setCellValue(String.valueOf(map.get("invokeDate")).equals("null")?"":String.valueOf(map.get("invokeDate")));
				row2.createCell(6).setCellValue(String.valueOf(map.get("invokeUser")).equals("null")?"":String.valueOf(map.get("invokeUser")));
				row2.createCell(7).setCellValue(String.valueOf(map.get("invokeIp")).equals("null")?"":String.valueOf(map.get("invokeIp")));
			}
			// 设置Content-Disposition
			response.setHeader("Content-Disposition", "attachment;filename=" + newpate);
			OutputStream out = response.getOutputStream();
			// 写文件
			wb.write(out);
			// 关闭输出流
			out.close();
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
		
		/**
		 * 万方卡详情页导出
		 * @param title  文本名称
		 * @param type	类型
		 * @param data	保存数据  list<map>
		 * @param realspath 真实路径
		 * @return
		 */
			@SuppressWarnings("unchecked")
			public String exportExccel3(HttpServletResponse response,List<Map<String,Object>> list,List<String> namelist) {
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
				String date=sdf.format(new Date());
				String newpate = date+".xlsx";
				try {
					// 工作区
					XSSFWorkbook wb = new XSSFWorkbook();
					// 创建第一个sheet
					XSSFSheet sheet = wb.createSheet("Sheet1");
					XSSFRow row = sheet.createRow(0);
					for(int i=0;i<namelist.size();i++){
						row.createCell(i).setCellValue(namelist.get(i));
					}
					for (int i =0; i<list.size(); i++) {
						Map<String,Object> mapLs=list.get(i);
						//处理面值数量
						String valueAndNumber = "";
						List<Map<String,Object>> valueNumber = JSONArray.fromObject(mapLs.get("valueNumber"));
						for(int j = 0;j < valueNumber.size();j++){
							Map<String,Object> map = valueNumber.get(j);
							valueAndNumber += map.get("value") + "/" + map.get("number") + ",";
						}
						//处理批次状态
						String batchState = String.valueOf(mapLs.get("batchState"));
						if("1".equals(batchState)){
							batchState = "未审核";
						}else if("2".equals(batchState)){
							batchState = "已审核未领取";
						}else if("3".equals(batchState)){
							batchState = "已领取";
						}
						// 生成第一行
						row = sheet.createRow(i+1);
						// 给这一行的第一列赋值
						row.createCell(0).setCellValue(String.valueOf(mapLs.get("batchName")));
						row.createCell(1).setCellValue(String.valueOf(mapLs.get("cardTypeName")));
						row.createCell(2).setCellValue(valueAndNumber.substring(0, valueAndNumber.length()-1));
						row.createCell(3).setCellValue(String.valueOf(mapLs.get("amount")));
						row.createCell(4).setCellValue(String.valueOf(mapLs.get("validStart")) + "--" + String.valueOf(mapLs.get("validEnd")));
						row.createCell(5).setCellValue(String.valueOf(mapLs.get("createDate")));
						row.createCell(6).setCellValue(String.valueOf(mapLs.get("applyDepartment")));
						row.createCell(7).setCellValue(String.valueOf(mapLs.get("applyPerson")));
						row.createCell(8).setCellValue(String.valueOf(mapLs.get("applyDate")));
						row.createCell(9).setCellValue(batchState);
						row.createCell(10).setCellValue(String.valueOf(mapLs.get("checkDepartment")));
						row.createCell(11).setCellValue(String.valueOf(mapLs.get("checkPerson")));
						row.createCell(12).setCellValue(String.valueOf(mapLs.get("checkDate")));
						row.createCell(13).setCellValue(String.valueOf(mapLs.get("pullDepartment")));
						row.createCell(14).setCellValue(String.valueOf(mapLs.get("pullPerson")));
						row.createCell(15).setCellValue(String.valueOf(mapLs.get("pullDate")));
					}
					//设置Content-Disposition  
			        response.setHeader("Content-Disposition", "attachment;filename="+ newpate); 
			        OutputStream out = response.getOutputStream();
					// 写文件
					wb.write(out);  
					// 关闭输出流
					out.close();
				
					return date;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return date;
			}
	
	/**
	 * 导出预警信息
	 * @param dataList
	 * @param namelist
	 * @return
	 */
	public Map<String,String> exportExccel(List<Map<String,Object>> dataList,List<String> namelist) {
		String msg="预警账号";
		Map<String,String> map=new HashMap<String,String>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String date=sdf.format(new Date());
		String newpate = msg+date+".xlsx";
		map.put("fileName", newpate);
		try {
			JSONArray data=JSONArray.fromObject(dataList);
			// 工作区
			XSSFWorkbook wb = new XSSFWorkbook();
			// 创建第一个sheet
			XSSFSheet sheet = wb.createSheet(msg);
			XSSFRow row = sheet.createRow(0);
			for(int i=0;i<namelist.size();i++){
				row.createCell(i).setCellValue(namelist.get(i));
			}
			for (int i =0; i<data.size(); i++) {
				// 生成第一行
				row = sheet.createRow(i+1);
				// 给这一行的第一列赋值
				row.createCell(0).setCellValue(String.valueOf(data.getJSONObject(i).get("userName")));
				row.createCell(1).setCellValue(String.valueOf(data.getJSONObject(i).get("userId")));
				row.createCell(2).setCellValue(String.valueOf(data.getJSONObject(i).get("name")));
				row.createCell(3).setCellValue(String.valueOf(data.getJSONObject(i).get("date")));
				String balance=data.getJSONObject(i).get("balance")==null?"":data.getJSONObject(i).get("balance").toString();
				row.createCell(4).setCellValue(balance);
				String count=data.getJSONObject(i).get("count")==null?"":data.getJSONObject(i).get("count").toString();
				row.createCell(5).setCellValue(count);
				row.createCell(6).setCellValue("");//过期情况暂无
				String balanceDesc=data.getJSONObject(i).get("balanceDesc")==null?"":data.getJSONObject(i).get("balanceDesc").toString();
				row.createCell(7).setCellValue(balanceDesc);
				String countDesc=data.getJSONObject(i).get("countDesc")==null?"":data.getJSONObject(i).get("countDesc").toString();
				row.createCell(8).setCellValue(countDesc);
			}
			// 写文件
			String path=null;
	        try {
	        	path=System.getProperty("user.dir")+"/"+newpate;
	        	FileOutputStream fout = new FileOutputStream(path);  
	            wb.write(fout);  
	            fout.close();
	            map.put("filePath", path);
	        }catch (Exception e){  
	            e.printStackTrace();  
	        }  
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}	
			
			/**
			 * 期刊管理导出
			 * @param request
			 * @param array 导出数据
			 * @param names  excel列名
			 * @return
			 */
			public void exportPerio(HttpServletResponse response,JSONArray array,List<String> names){
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
				String date=sdf.format(new Date());
				String newpate = date+".xlsx";
				
				try {
					//工作区
					XSSFWorkbook wb=new XSSFWorkbook();
					//创建第一个sheet
					XSSFSheet sheet=wb.createSheet("期刊管理");
					XSSFRow row=sheet.createRow(0);
					
					for (int i = 0; i < names.size(); i++) {
						row.createCell(i).setCellValue(names.get(i));
					}
					
					for (int i = 0; i < array.size(); i++) {
		
						//创建行
						row=sheet.createRow(i+1);
						//行内列赋值
						row.createCell(0).setCellValue(i+1);
						row.createCell(1).setCellValue(array.getJSONObject(i).get("perioName").toString());
						row.createCell(2).setCellValue(array.getJSONObject(i).get("hireCon").toString());
						row.createCell(3).setCellValue(array.getJSONObject(i).get("subTime").toString()+"个月");
						row.createCell(4).setCellValue(array.getJSONObject(i).get("auditMoney").toString());
						row.createCell(5).setCellValue(array.getJSONObject(i).get("layoutMoney").toString());
						row.createCell(6).setCellValue(array.getJSONObject(i).get("commentContent").toString());
						row.createCell(7).setCellValue(array.getJSONObject(i).get("finalOpinion").toString());
						row.createCell(8).setCellValue(array.getJSONObject(i).get("authorName").toString()); 
						row.createCell(9).setCellValue(array.getJSONObject(i).get("dataState").toString().equals("1")?"正常":"禁用");
						row.createCell(10).setCellValue(array.getJSONObject(i).get("complaintStatus").toString().equals("1")?"正常":"申诉");
					}
					
						//设置Content-Disposition  
						response.setHeader("Content-Disposition", "attachment;filename="+ newpate); 
				        OutputStream out = response.getOutputStream();
						// 写文件
						wb.write(out);  
						// 关闭输出流
						out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			
			/**
			 * 笔记审核管理 导出
			 * @param response
			 * @param array 导出数据
			 * @param names Excel列名
			 */
			public void ExportNotes(HttpServletResponse response,JSONArray array,List<String> names){
				
				try {
					
					SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
					String date=dateFormat.format(new Date());
					String newpate = date+".xlsx";
					
					//创建工作区
					XSSFWorkbook workbook=new XSSFWorkbook();
					//创建表
					XSSFSheet sheet=workbook.createSheet("笔记审核管理");
					//创建行
					XSSFRow row=sheet.createRow(0);
					//赋值列名
					for (int i = 0; i <names.size(); i++) {
						 row.createCell(i).setCellValue(names.get(i));
					}
					
					//赋值导出数据
					for (int i = 0; i < array.size(); i++) {
						row=sheet.createRow(i+1);
						row.createCell(0).setCellValue(i+1);
						row.createCell(1).setCellValue(array.getJSONObject(i).get("noteNum").toString());
						row.createCell(2).setCellValue(array.getJSONObject(i).get("resourceNum").toString());
						row.createCell(3).setCellValue(array.getJSONObject(i).get("resourceName").toString());
						row.createCell(4).setCellValue(array.getJSONObject(i).get("resourceType").toString());
						row.createCell(5).setCellValue(array.getJSONObject(i).get("noteContent").toString());
						row.createCell(6).setCellValue(array.getJSONObject(i).get("finalOpinion").toString());
						row.createCell(7).setCellValue(array.getJSONObject(i).get("userName").toString());
						row.createCell(8).setCellValue(array.getJSONObject(i).get("noteDate").toString());
						row.createCell(9).setCellValue(array.getJSONObject(i).get("dataState").toString().equals("1")?"正常":"禁用");
						row.createCell(10).setCellValue(array.getJSONObject(i).get("complaintStatus").toString().equals("1")?"正常":"申诉");
					}
					
					//设置Content-Disposition  
					response.setHeader("Content-Disposition", "attachment;filename="+ newpate); 
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
			 *   导出文辑
			 * @param response
			 * @param array 导出数据
			 * @param names 列名
			 */
			public void exportVolumeDocu(HttpServletResponse response,JSONArray array,List<String> names,String volumeType){
				
				try {
					
					SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
					String filename=format.format(new Date())+".xlsx";
					
					XSSFWorkbook workbook=new XSSFWorkbook();
					XSSFSheet sheet=workbook.createSheet("文辑");
					XSSFRow row=sheet.createRow(0);
					
					for (int i = 0; i < names.size(); i++) {
						row.createCell(i).setCellValue(names.get(i));
					}
					
					//1:用户文辑 2:优选文辑
					if(volumeType.equals("1")){
						
						for (int i = 0; i <array.size(); i++) {
							 row=sheet.createRow(i+1);
							 row.createCell(0).setCellValue(i+1);
							 row.createCell(1).setCellValue(array.getJSONObject(i).get("volumeNo").toString());
							 row.createCell(2).setCellValue(array.getJSONObject(i).get("volumeName").toString());
							 row.createCell(3).setCellValue(array.getJSONObject(i).get("keyword").toString());
							 row.createCell(4).setCellValue(array.getJSONObject(i).get("volumeState").toString().equals("1")?"公开":"私有");
							 row.createCell(5).setCellValue(array.getJSONObject(i).get("publishPerson").toString());
							 row.createCell(6).setCellValue(array.getJSONObject(i).get("publishDate").toString());
							 row.createCell(7).setCellValue(array.getJSONObject(i).get("docuNumber").toString());
						}
						
					}else{
						
						for (int i = 0; i <array.size(); i++) {
							 row=sheet.createRow(i+1);
							 row.createCell(0).setCellValue(i+1);
							 row.createCell(1).setCellValue(array.getJSONObject(i).get("volumeNo").toString());
							 row.createCell(2).setCellValue(array.getJSONObject(i).get("subjectName").toString());
							 row.createCell(3).setCellValue(array.getJSONObject(i).get("volumeName").toString());
							 row.createCell(4).setCellValue(array.getJSONObject(i).get("keyword").toString());
							 row.createCell(5).setCellValue(array.getJSONObject(i).get("publishPerson").toString());
							 row.createCell(6).setCellValue(array.getJSONObject(i).get("publishDate").toString());
							 row.createCell(7).setCellValue(array.getJSONObject(i).get("docuNumber").toString());
							 row.createCell(8).setCellValue(array.getJSONObject(i).get("volumePrice").toString());
						}
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
			
			/**
			 * 导出分享模板
			 * @param response
			 * @param array
			 * @param names
			 */
			public void exportshareTemplate(HttpServletResponse response,JSONArray array,List<String> names){
				
				try {
					
					SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
					String filename=format.format(new Date())+".xlsx";
					
					XSSFWorkbook workbook=new XSSFWorkbook();
					XSSFSheet sheet=workbook.createSheet("分享");
					XSSFRow row=sheet.createRow(0);
					
					for (int i = 0; i < names.size(); i++) {
						row.createCell(i).setCellValue(names.get(i));
					}
					
					for (int i = 0; i < array.size(); i++) {
						row=sheet.createRow(i+1);
						row.createCell(0).setCellValue(i+1);
						row.createCell(1).setCellValue(array.getJSONObject(i).get("shareType").toString());
						row.createCell(2).setCellValue(array.getJSONObject(i).get("shareContent").toString());
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
			
			/**
			 * 导出资源类型
			 * @param response
			 * @param array
			 * @param names
			 */
			public void exportResource(HttpServletResponse response,JSONArray array,List<String> names){
				
				try {
					
					SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
					String filename=format.format(new Date())+".xlsx";
					
					XSSFWorkbook workbook=new XSSFWorkbook();
					XSSFSheet sheet=workbook.createSheet("资源类型");
					XSSFRow row=sheet.createRow(0);
					
					for (int i = 0; i < names.size(); i++) {
						row.createCell(i).setCellValue(names.get(i));
					}
					
					for (int i = 0; i < array.size(); i++) {
						row=sheet.createRow(i+1);
						row.createCell(0).setCellValue(i+1);
						row.createCell(1).setCellValue(array.getJSONObject(i).get("typeName").toString());
						row.createCell(2).setCellValue(array.getJSONObject(i).get("typedescri").toString());
						row.createCell(3).setCellValue(array.getJSONObject(i).get("typeCode").toString());
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
			
			
			/**
			 * 导出功能模块
			 * @param response
			 * @param array
			 * @param names
			 */
			public void exportModular(HttpServletResponse response,JSONArray array,List<String> names){
				
				try {
					
					SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
					String filename=format.format(new Date())+".xlsx";
					
					XSSFWorkbook workbook=new XSSFWorkbook();
					XSSFSheet sheet=workbook.createSheet("功能模块");
					XSSFRow row=sheet.createRow(0);
					
					for (int i = 0; i < names.size(); i++) {
						row.createCell(i).setCellValue(names.get(i));
					}
					
					for (int i = 0; i < array.size(); i++) {
						row=sheet.createRow(i+1);
						row.createCell(0).setCellValue(i+1);
						row.createCell(1).setCellValue(array.getJSONObject(i).get("modularName").toString());
						row.createCell(2).setCellValue(array.getJSONObject(i).get("linkAddress").toString());
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
			
			/**
			 * 导出  ------页面分析
			 * @param response
			 * @param array
			 * @param names
			 */
			public void exportPage(HttpServletResponse response,JSONArray array,List<String> names){
				
				try {
					
					SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
					String filename=format.format(new Date())+".xlsx";
					
					XSSFWorkbook workbook=new XSSFWorkbook();
					XSSFSheet sheet=workbook.createSheet("分析页面");
					XSSFRow row=sheet.createRow(0);
					
					for (int i = 0; i < names.size(); i++) {
						row.createCell(i).setCellValue(names.get(i));
					}
					
					for (int i = 0; i < array.size(); i++) {
						row=sheet.createRow(i+1);
						row.createCell(0).setCellValue(i+1);
						row.createCell(1).setCellValue(array.getJSONObject(i).get("modularId").toString());
						row.createCell(2).setCellValue(array.getJSONObject(i).get("pageName").toString());
						row.createCell(3).setCellValue(array.getJSONObject(i).get("linkAddress").toString());
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
			
			
			/**
			 * 导出----数据库使用分析
			 * @param response
			 * @param array
			 * @param names
			 */
			public void exportDatabase(HttpServletResponse response,JSONArray array,List<String> names,List<String> paramter){
				
				try {
					
					SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
					String filename="数据库使用分析"+format.format(new Date())+".xlsx";
					
					XSSFWorkbook workbook=new XSSFWorkbook();
					XSSFSheet sheet=workbook.createSheet("数据库使用分析");
					
					//合并单元格 设置表格高度
					sheet.addMergedRegion(new CellRangeAddress(0,0,0,names.size()-1)); 
					XSSFRow row=sheet.createRow(0);
					row.setHeightInPoints(100);
					XSSFCell cell=row.createCell(0); 
					
					//设置前几个字:   查询条件  字体样式
					XSSFFont font = (XSSFFont) workbook.createFont();
					font.setFontHeightInPoints((short) 12); // 字体高度
					font.setFontName("宋体"); // 字体
					font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
					
					StringBuffer sb=new StringBuffer("查询条件:\n");
					for (int i = 0; i <paramter.size(); i++) {
						sb.append("("+(i+1)+")"+paramter.get(i)+"\n");
					}
					
					XSSFRichTextString xxt =new  XSSFRichTextString(sb.toString());
					xxt.applyFont(0, 5, font);
					cell.setCellValue(xxt);
					
					row=sheet.createRow(1);
					for (int i = 0; i < names.size(); i++) {
						row.createCell(i).setCellValue(names.get(i));
					}
					
					for (int i = 0; i < array.size(); i++) {
						row=sheet.createRow(i+2);
						row.createCell(0).setCellValue(i+1);
						row.createCell(1).setCellValue(array.getJSONObject(i).get("database_name").toString());
						row.createCell(2).setCellValue(array.getJSONObject(i).get("sum1").toString());
						row.createCell(3).setCellValue(array.getJSONObject(i).get("sum2").toString());
						row.createCell(4).setCellValue(array.getJSONObject(i).get("sum3").toString());
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
			 * 导出----资源类型使用分析
			 * @param response
			 * @param array
			 * @param names
			 */
			public void exportresourceType(HttpServletResponse response,JSONArray array,List<String> names,String restype,List<String> paramter){
				try {
					
					SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
					String filename="资源类型使用分析"+format.format(new Date())+".xlsx";
					
					XSSFWorkbook workbook=new XSSFWorkbook();
					XSSFSheet sheet=workbook.createSheet("资源类型使用分析");
					
					//合并单元格 设置表格高度
					sheet.addMergedRegion(new CellRangeAddress(0,0,0,names.size()-1)); 
					XSSFRow row=sheet.createRow(0);
					row.setHeightInPoints(100);
					XSSFCell cell=row.createCell(0); 
					
					//设置前几个字:   查询条件  字体样式
					XSSFFont font = (XSSFFont) workbook.createFont();
					font.setFontHeightInPoints((short) 12); // 字体高度
					font.setFontName("宋体"); // 字体
					font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
					
					
					StringBuffer sb=new StringBuffer("查询条件:\n");
					for (int i = 0; i <paramter.size(); i++) {
						sb.append("("+(i+1)+")"+paramter.get(i)+"\n");
					}
					
					XSSFRichTextString xxt =new  XSSFRichTextString(sb.toString());
					xxt.applyFont(0, 5, font);
					cell.setCellValue(xxt);
					
					row=sheet.createRow(1);
					for (int i = 0; i < names.size(); i++) {
						row.createCell(i).setCellValue(names.get(i));
					}
					
					for (int i = 0; i < array.size(); i++) {
						row=sheet.createRow(i+2);
						row.createCell(0).setCellValue(i+1);
						
						if(restype.equals("期刊") || restype.equals("会议") || restype.equals("学位")){
							row.createCell(1).setCellValue(array.getJSONObject(i).get("sourceName").toString());
							row.createCell(2).setCellValue(array.getJSONObject(i).get("sourceTypeName").toString());
							row.createCell(3).setCellValue(array.getJSONObject(i).get("browseNum").toString());
							row.createCell(4).setCellValue(array.getJSONObject(i).get("downloadNum").toString());
							row.createCell(5).setCellValue(array.getJSONObject(i).get("searchNum").toString());
							row.createCell(6).setCellValue(array.getJSONObject(i).get("shareNum").toString());
							row.createCell(7).setCellValue(array.getJSONObject(i).get("collectNum").toString());
							row.createCell(8).setCellValue(array.getJSONObject(i).get("exportNum").toString());
							row.createCell(9).setCellValue(array.getJSONObject(i).get("noteNum").toString());
							row.createCell(10).setCellValue(array.getJSONObject(i).get("subscibeNum").toString());
						}else{
							row.createCell(1).setCellValue(array.getJSONObject(i).get("sourceTypeName").toString());
							row.createCell(2).setCellValue(array.getJSONObject(i).get("browseNum").toString());
							row.createCell(3).setCellValue(array.getJSONObject(i).get("downloadNum").toString());
							row.createCell(4).setCellValue(array.getJSONObject(i).get("searchNum").toString());
							row.createCell(5).setCellValue(array.getJSONObject(i).get("shareNum").toString());
							row.createCell(6).setCellValue(array.getJSONObject(i).get("collectNum").toString());
							row.createCell(7).setCellValue(array.getJSONObject(i).get("exportNum").toString());
							row.createCell(8).setCellValue(array.getJSONObject(i).get("noteNum").toString());
							row.createCell(9).setCellValue(array.getJSONObject(i).get("subscibeNum").toString());
						}
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
			 * 导出-----学科分类
			 * @param response
			 * @param array
			 * @param names
			 */
			public void exportSubject(HttpServletResponse response,JSONArray array,List<String> names){
				
				try {
					
					SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
					String filename=format.format(new Date())+".xlsx";
					
					XSSFWorkbook workbook=new XSSFWorkbook();
					XSSFSheet sheet=workbook.createSheet("学科分类");
					XSSFRow row=sheet.createRow(0);
					
					for (int i = 0; i < names.size(); i++) {
						row.createCell(i).setCellValue(names.get(i));
					}
					
					for (int i = 0; i < array.size(); i++) {
						row=sheet.createRow(i+1);
						row.createCell(0).setCellValue(i+1);
						row.createCell(1).setCellValue(array.getJSONObject(i).get("level").toString());
						row.createCell(2).setCellValue(array.getJSONObject(i).get("classNum").toString());
						row.createCell(3).setCellValue(array.getJSONObject(i).get("className").toString());
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
		
			/**
			 * 配置管理 导出数据库
			 * @param response
			 * @param list 导出的数据库数据
			 * @param names 列名
			 */
		public void exportData(HttpServletResponse response,JSONArray array,List<String> names){
			
			try {
				
				SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
				String filename=format.format(new Date())+".xlsx";
				
				XSSFWorkbook workbook=new XSSFWorkbook();
				XSSFSheet sheet=workbook.createSheet("数据库配置导出");
				XSSFRow row=sheet.createRow(0);
				String status;
				for (int i = 0; i < names.size(); i++) {
					row.createCell(i).setCellValue(names.get(i));
				}
				
				for (int i = 0; i < array.size(); i++) {
					
					if(array.getJSONObject(i).get("status").toString().equals("1")){
			    		status="已发布";
			    	}else if(null==array.getJSONObject(i).get("status").toString()){
			    		status="未发布";
			    	}else{
			    		status="下撤";
			    	}
					
					row=sheet.createRow(i+1);
					row.createCell(0).setCellValue(i+1);
					row.createCell(1).setCellValue(array.getJSONObject(i).get("tableName").toString());
					row.createCell(2).setCellValue(array.getJSONObject(i).get("tableDescribe").toString());
					row.createCell(3).setCellValue(array.getJSONObject(i).get("sourceDb").toString());
					row.createCell(4).setCellValue(array.getJSONObject(i).get("dbtype").toString());
					row.createCell(5).setCellValue(array.getJSONObject(i).get("resType").toString());
					row.createCell(6).setCellValue(array.getJSONObject(i).get("language").toString());
					row.createCell(7).setCellValue(array.getJSONObject(i).get("customPolicy")==null?"":array.getJSONObject(i).get("customPolicy").toString());
					row.createCell(8).setCellValue(status);
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
		
		/**
		 * 日志导出 -----日志管理
		 * @param response
		 * @param array
		 * @param names
		 */
		public void exportLog(HttpServletResponse response,JSONArray array,List<String> names){
			
			try {
				
				SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
				String filename=format.format(new Date())+".xlsx";
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