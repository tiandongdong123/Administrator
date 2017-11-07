package com.wf.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.exportExcel.ExportExcel;
import com.utils.CookieUtil;
import com.utils.FileUploadUtil;
import com.wf.bean.CardType;
import com.wf.bean.PageList;
import com.wf.bean.Remind;
import com.wf.bean.Wfadmin;
import com.wf.service.CardBatchService;
import com.wf.service.CardService;
import com.wf.service.CardTypeService;
import com.wf.service.RemindService;

@Controller
@RequestMapping("card")
public class CardController {
	@Autowired
	CardBatchService cardBatchService;
	
	@Autowired
	CardService cardService;
	
	@Autowired
	CardTypeService cardtype;
	
	@Autowired
	RemindService remindService;//消息提醒接口
	
	/**
	 * 生成万方卡
	 * @return
	 */
	@RequestMapping("createCard")
	public ModelAndView createCard(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("cardList", cardtype.getlist());
		mav.setViewName("/page/othermanager/create_card");
		
		return mav;
	}
	
	/**
	 * 添加万方卡类型
	 * @return
	 */
	@RequestMapping("createCardType")
	public ModelAndView createCardType(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/othermanager/create_card_type");
		return mav;
	}	
	
	/**
	 * 检测万方卡类型是否重复
	 * @param request
	 * @return
	 */
	@RequestMapping("checkcode")
	@ResponseBody
	public int checktype(HttpServletRequest request) {
		
		CardType card=new CardType();
		card.setCardTypeCode(request.getParameter("code"));
		card.setCardTypeName(request.getParameter("name"));
		int i=cardtype.checkcode(card);
		return i;
	}
	
	/**
	 * 添加万方卡
	 * @param request
	 * @return
	 */
	@RequestMapping("addcardtype")
	@ResponseBody
	public int addcode(HttpServletRequest request) {
		CardType card=new CardType();
		card.setCardTypeCode(request.getParameter("code"));
		card.setCardTypeName(request.getParameter("name"));
		int i=cardtype.addcode(card);
		return i;
	}
	
	/**
	 * 获取万方卡类型表
	 * @return
	 */
	@RequestMapping("codelist")
	@ResponseBody
	 public Object codelist() {
		return cardtype.getlist();
	}
	
	/**
	 * 保存下载
	 * @param model
	 * @param response
	 * @param request
	 */
	@RequestMapping("download")
	public void download(Model model,HttpServletResponse response,HttpServletRequest request) {
        // 下载本地文件
		String fileName = request.getParameter("titel"); // 文件的默认保存名
		InputStream inStream = null;
		try{
			fileName = URLDecoder.decode(fileName, "UTF-8") + ".xlsx";
			inStream = new FileInputStream(request.getServletContext().getRealPath("/") + "Word/"+ fileName);
			// 设置输出的格式
			response.reset();
			response.setContentType("bin");
			response.setCharacterEncoding("UTF-8");
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			response.addHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
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
	
	@RequestMapping("addCardBatch")
	@ResponseBody
	public boolean addCardBatch(HttpServletRequest request,String type,String valueNumber,String validStart,String validEnd,String applyDepartment,
			String applyPerson,String applyDate){
		String adjunct = FileUploadUtil.upload(request, "/imgs/te/");
		Boolean flag = cardBatchService.insertCardBatch(type, valueNumber, validStart, validEnd, applyDepartment, applyPerson, applyDate,adjunct);
		return flag;
	}
	
	/**
	 * 修改附件(未审核)
	 * @param batchId
	 * @param adjunct
	 * @return
	 */
	@RequestMapping("updateAttachment")
	@ResponseBody
	public JSONObject updateAttachment(MultipartFile file, HttpServletRequest request,String batchId) {
		String adjunct = FileUploadUtil.upload(request, "/imgs/te/");
		boolean msg = cardBatchService.updateAttachment(batchId, adjunct);
		JSONObject obj = new JSONObject();
		obj.put("adjunct", adjunct);
		obj.put("msg", msg);
		return obj;
	}
	
	/**
	 * 万方卡审核
	 * @return
	 */
	@RequestMapping("cardCheck")
	public ModelAndView cardCheck(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("cardList", cardtype.getlist());
		mav.setViewName("/page/othermanager/card_check");
		return mav;
	}
	
	@RequestMapping("queryCheck")
	@ResponseBody
	public PageList queryCheck(String batchName, String applyDepartment, String applyPerson,
			String startTime, String endTime, String cardType, String checkState,
			String batchState, int pageNum, int pageSize) {
		return cardBatchService.queryCheck(batchName, applyDepartment, applyPerson,
				startTime, endTime, cardType, checkState, batchState, pageNum, pageSize);
	}
	
	/**
	 * 万方卡管理
	 * @return
	 */
	@RequestMapping("cardManager")
	public ModelAndView cardManager(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("cardList", cardtype.getlist());
		mav.setViewName("/page/othermanager/card_manager");
		return mav;
	}
	
	@RequestMapping("queryCard")
	@ResponseBody
	public PageList  queryCard(String batchName,String numStart,String numEnd,
			String applyDepartment,String applyPerson,String startTime,
			String endTime,String cardType,String batchState,String invokeState,int pageNum,int pageSize){
		PageList p = cardService.queryCard(batchName, numStart, numEnd, applyDepartment, applyPerson, startTime, endTime, cardType, batchState,invokeState, pageNum, pageSize);
		return p;
	}
	
	/**
	 * 单张万方卡详情
	 * @return
	 */
	@RequestMapping("details")
	public ModelAndView details(String id){
		Map<String,Object> map = cardService.queryOneById(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("map", map);
		mav.setViewName("/page/othermanager/details");
		return mav;
	}
	
	/**
	 * 附件下载
	 * @param request
	 * @param response
	 * @param url
	 */
	@RequestMapping("download1")
	public void downResourse(HttpServletRequest request,HttpServletResponse response,String url){
		url = request.getServletContext().getRealPath("/") + url;
		String fileName = url;
		String type = fileName.substring(fileName.lastIndexOf("."));
		String name = fileName.substring(fileName.lastIndexOf("/")+1, fileName.lastIndexOf("."));
		try {
	        //设置Content-Disposition  
	        response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(name, "UTF-8")+type);  
			InputStream in = new FileInputStream(fileName);
			OutputStream out = response.getOutputStream();
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批次详情(未审核)
	 * @return
	 */
	@RequestMapping("batchDetails")
	public ModelAndView batchDetails(String batchId){
		Map<String,Object> object = cardBatchService.queryOneByBatchId(batchId);
		List<Map<String,Object>> valueNumber = JSONArray.fromObject(object.get("valueNumber"));
		ModelAndView mav = new ModelAndView();
		mav.addObject("object", object);
		mav.addObject("valueNumber", valueNumber);
		mav.setViewName("/page/othermanager/batch_details");
		return mav;
	}

	/**
	 * 批次详情页已领取
	 * @return
	 */
	@RequestMapping("batchDetailsGet")
	public ModelAndView batchDetailsGet(String batchId){
		Map<String,Object> object = cardBatchService.queryOneByBatchId(batchId);
		List<Map<String,Object>> valueNumber = JSONArray.fromObject(object.get("valueNumber"));
		ModelAndView mav = new ModelAndView();
		mav.addObject("object", object);
		mav.addObject("valueNumber", valueNumber);
		mav.setViewName("/page/othermanager/batch_details_get");
		return mav;
	}
	
	@RequestMapping("queryCardByBatchId")
	@ResponseBody
	public PageList  queryCardByBatchId(String batchId, int pageNum,int pageSize){
		PageList p = cardService.queryCardBybatchId(batchId, pageNum, pageSize);
		return p;
	}
	
	/**
	 * 批次详情页未领取
	 * @param type(0--不能领取；1--能领取)
	 * @return
	 */
	@RequestMapping("batchDetailsUnGet")
	public ModelAndView batchDetailsUnGet(String batchId,int type){
		Map<String,Object> object = cardBatchService.queryOneByBatchId(batchId);
		List<Map<String,Object>> valueNumber = JSONArray.fromObject(object.get("valueNumber"));
		ModelAndView mav = new ModelAndView();
		mav.addObject("object", object);
		mav.addObject("valueNumber", valueNumber);
		mav.addObject("type", type);
		mav.setViewName("/page/othermanager/batch_details_unget");
		return mav;
	}
	
	/**
	 * 修改审核状态
	 */
	@RequestMapping("updateCheckState")
	@ResponseBody
	public boolean updateCheckState(HttpServletRequest request,String batchId){
		Wfadmin admin=CookieUtil.getWfadmin(request);
		return cardBatchService.updateCheckState(admin, batchId);//审核状态改变
	}
	
	/**
	 * 修改批次状态(领取)
	 */
	@RequestMapping("batchUpdate")
	public ModelAndView batchUpdate(String batchId,String batchState,String pullDepartment,String pullPerson){
		cardBatchService.updateBatchState(batchId,batchState, pullDepartment, pullPerson);
		Map<String,Object> object = cardBatchService.queryOneByBatchId(batchId);
		List<Map<String,Object>> valueNumber = JSONArray.fromObject(object.get("valueNumber"));
		ModelAndView mav = new ModelAndView();
		mav.addObject("object", object);
		mav.addObject("valueNumber", valueNumber);
		mav.setViewName("/page/othermanager/batch_details_get");
		return mav;
	}
	
	/**
	 * 修改万方卡激活状态
	 */
	@RequestMapping("updateInvokeState")
	@ResponseBody
	public void updateInvokeState(){
		//TODO 具体的激活流程
		cardService.updateInvokeState("00fd2ff5c9f6483dbde743586de36e28", "1");
	}
	
	@RequestMapping("/remind")
	@ResponseBody
	public boolean remind(String batchName,String type,String applyDepartment,String applyPerson,String applyDate){
		Remind remind = new Remind();
		remind.setBatchName(batchName);
		remind.setType(type);
		remind.setApplyDepartment(applyDepartment);
		remind.setApplyPerson(applyPerson);
		remind.setApplyDate(applyDate);
		boolean flag = remindService.insert(remind);
		return flag;
	}
	
	/**
	 * 万方卡导出
	 * @param request
	 * @param response
	 * @param batchId 万方卡批次id
	 * @param type (1-万方卡批次已审核未领取导出；2-万方卡批次已领取导出；3-万方卡详情页导出)
	 * @return
	 */
	@RequestMapping("exportCard")
	public void exportCard(HttpServletRequest request,HttpServletResponse response,String batchId,int type) {
		ExportExcel exc= new ExportExcel();
		if(type == 1){//万方卡批次已审核未领取导出
			List<Map<String,Object>> list = cardService.queryAllCardBybatchId(batchId);
			List<String> namelist=new ArrayList<String>();
			namelist.add("万方卡类型");
			namelist.add("卡号");
			namelist.add("密码");
			namelist.add("面值");
			namelist.add("有效期");
			exc.exportExccel1(response,list,namelist);
		}else if(type == 2){//万方卡批次已领取导出
			List<Map<String,Object>> batchList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> cardList = new ArrayList<Map<String,Object>>();
			if(StringUtils.isNotBlank(batchId)){//单个批次导出
				//批次详情list
				Map<String,Object> map = cardBatchService.queryOneByBatchId(batchId);
				batchList.add(map);
				//卡详情list
				cardList = cardService.queryAllCardBybatchId(batchId);
			}else{//导出全部批次
				//批次详情list(已审核)
				batchList = cardBatchService.queryAllBatch();
				//卡详情list(所有已审核的card)
				cardList = cardService.queryAllCard();
			}
			//批次详情
			List<String> batchNamelist=new ArrayList<String>();
			batchNamelist.add("批次");
			batchNamelist.add("万方卡类型");
			batchNamelist.add("面值/数值");
			batchNamelist.add("总金额");		
			batchNamelist.add("有效期");
			batchNamelist.add("生成日期");
			batchNamelist.add("申请部门");
			batchNamelist.add("申请人");
			batchNamelist.add("申请日期");		
			batchNamelist.add("批次状态");
			batchNamelist.add("审核部门");
			batchNamelist.add("审核人");
			batchNamelist.add("审核日期");
			batchNamelist.add("领取部门");
			batchNamelist.add("领取人");
			batchNamelist.add("领取日期");
			//卡详情
			List<String> cardNamelist = new ArrayList<String>();
			cardNamelist.add("批次");
			cardNamelist.add("卡号");
			cardNamelist.add("密码");
			cardNamelist.add("面值");		
			cardNamelist.add("激活状态");		
			cardNamelist.add("激活日期");
			cardNamelist.add("激活用户");
			cardNamelist.add("激活ip");
			exc.exportExcel2(response,batchList,batchNamelist,cardList,cardNamelist);
		}else if(type == 3){//万方卡批次整体导出
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = cardBatchService.queryOneByBatchId(batchId);
			list.add(map);
			List<String> namelist=new ArrayList<String>();
			namelist.add("批次");
			namelist.add("万方卡类型");
			namelist.add("面值/数值");
			namelist.add("总金额");		
			namelist.add("有效期");
			namelist.add("生成日期");
			namelist.add("申请部门");
			namelist.add("申请人");
			namelist.add("申请日期");		
			namelist.add("批次状态");
			namelist.add("审核部门");
			namelist.add("审核人");
			namelist.add("审核日期");
			namelist.add("领取部门");
			namelist.add("领取人");
			namelist.add("领取日期");
			exc.exportExccel3(response,list,namelist);
		}
	}
}