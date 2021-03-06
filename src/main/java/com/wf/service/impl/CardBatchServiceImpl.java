package com.wf.service.impl;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.GetUuid;
import com.utils.StringUtil;
import com.wf.bean.Card;
import com.wf.bean.CardBatch;
import com.wf.bean.PageList;
import com.wf.bean.Wfadmin;
import com.wf.dao.CardBatchMapper;
import com.wf.dao.CardMapper;
import com.wf.service.CardBatchService;
@Service
public class CardBatchServiceImpl implements CardBatchService{
	
	private static Logger log = Logger.getLogger(CardBatchServiceImpl.class);
	
	@Autowired
	private CardBatchMapper cbm;
	@Autowired
	private CardMapper cardMapper;
	@Override
	public boolean insertCardBatch(String type,String valueNumber,String validStart,String validEnd,String applyDepartment,
			String applyPerson,String applyDate,String adjunct) {
		applyDepartment = applyDepartment.trim();
		applyPerson = applyPerson.trim();
		CardBatch cardBatch = new CardBatch();
		String batchId = GetUuid.getId();
		cardBatch.setBatchId(batchId);// 批次ID
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy");
		String batch = sdf1.format(date);
		String max = cbm.queryBatchName();// 获取最大的批次
		if (max != null) {
			String maxYear = max.substring(0, 4); // 数据库中最大年份
			String nowYear = sd.format(date); // 当年年份
			if (StringUtils.equals(maxYear, nowYear)) { // 当前日期已经生成过批次
				String max1 = max.substring(8, 11);
				DecimalFormat df = new DecimalFormat("000");
				batch = batch + df.format(Integer.parseInt(max1) + 1); // 批次号
			} else { // 当前日期没有生成过批次号
				batch = batch + "001";// 批次号
			}
		} else {
			batch = batch + "001"; // 批次号
		}
		cardBatch.setBatchName(batch);// 批次号
		cardBatch.setType(type);// 万方卡类型
		cardBatch.setValueNumber(valueNumber);// 面值/数量
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cardBatch.setValidStart(validStart);// 有效期开始
		cardBatch.setValidEnd(validEnd);// 有效期结束s
		cardBatch.setApplyDate(applyDate);// 申请日期
		cardBatch.setCreateDate(sdf.format(new Date()));// 生成日期
		JSONArray array = JSONArray.fromObject(valueNumber);
		int amount = 0;
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = (JSONObject) array.get(i);
			int value = Integer.valueOf(obj.get("value").toString());
			int number = Integer.valueOf(obj.get("number").toString());
			amount += value * number;
		}
		cardBatch.setAmount(amount);// 总金额
		cardBatch.setApplyDepartment(applyDepartment);// 申请部门
		cardBatch.setApplyPerson(applyPerson);// 申请人
		cardBatch.setCheckState(1);// 审核初始状态
		cardBatch.setBatchState(1);// 批次初始状态
		cardBatch.setAdjunct(adjunct);
		boolean flag = false;
		int cb = cbm.insertCardBatch(cardBatch);
		if (cb > 0) {
			flag = true;
		}
		return flag;
	}
	
	private <T> List<List<T>> createList(List<T> targe, int size) {
		List<List<T>> listArr = new ArrayList<List<T>>();
		int arrSize = targe.size() % size == 0 ? targe.size() / size : targe.size() / size + 1;
		for (int i = 0; i < arrSize; i++) {
			List<T> sub = new ArrayList<T>();
			for (int j = i * size; j <= size * (i + 1) - 1; j++) {
				if (j <= targe.size() - 1) {
					sub.add(targe.get(j));
				}
			}
			listArr.add(sub);
		}
		return listArr;
	}
	/**
	 * 万方卡审核
	 * @param batchName 批次
	 * @param applyDepartment 申请部门
	 * @param applyPerson 申请人
	 * @param start 申请日期开始
	 * @param end 申请日期结束
	 * @param CardType 卡类型
	 * @return
	 */
	@Override
	public PageList queryCheck(String batchName, String applyDepartment,
			String applyPerson, String startTime, String endTime, String cardType,String checkState,String batchState,int pageNum,int pageSize) {
		batchName = batchName.trim();
		applyDepartment = applyDepartment.trim();
		applyPerson = applyPerson.trim();
		if(StringUtils.isEmpty(batchName)) batchName = null;
		if(StringUtils.isEmpty(applyDepartment)) applyDepartment = null;
		if(StringUtils.isEmpty(applyPerson)) applyPerson = null;
		if(StringUtils.isEmpty(startTime)) startTime = null;
		if(StringUtils.isEmpty(endTime)) endTime = null;
		if(StringUtils.isEmpty(cardType)) cardType = null;
		if(StringUtils.isEmpty(checkState)) checkState =null;
		if(StringUtils.isEmpty(batchState)) batchState = null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchName", batchName);
		map.put("applyDepartment", applyDepartment);
		map.put("applyPerson", applyPerson);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("cardType", cardType);
		map.put("checkState", checkState);
		map.put("batchState", batchState);
		int pageStart = (pageNum-1)*pageSize;//当前页的第一条
		map.put("pageNum", pageStart);
		map.put("pageSize", pageSize);
		List<Object> list = cbm.queryCheck(map);
		int size = cbm.queryCheckSize(map);
		PageList pl = new PageList();
		pl.setPageRow(list);//查询结果列表
		pl.setTotalRow(size);//总条数
		pl.setPageNum(pageNum);//当前页
		pl.setPageSize(pageSize);//每页显示的数量
		return pl;
	}
	/**
	 * 根据batchId  万方卡批次详情页
	 * @param batchId
	 * @return
	 */
	@Override
	public Map<String,Object> queryOneByBatchId(String batchId) {
		Map<String,Object> o = cbm.queryOneByBatchId(batchId);
		return o;
	}
	/**
	 * 修改审核状态
	 */
	@Override
	public boolean updateCheckState(Wfadmin admin,String batchId) {
		boolean flag = false;
		try {
			long time=System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("batchId", batchId);
			map.put("checkDepartment", "运维部门");
			map.put("checkPerson", admin.getUser_realname());
			map.put("checkDate", sdf.format(new Date()));
			map.put("checkState", "2");//审核通过
			map.put("batchState", "2");//批次审批通过
			//修改批量表
			int batchCard = cbm.updateCheckState(map);
			//查询批量表
			Map<String,Object> batchMap=cbm.queryOneByBatchId(batchId);
			//批量生成卡号表
			String valueNumber=String.valueOf(batchMap.get("valueNumber"));
			Date date = sdf.parse(batchMap.get("createDate").toString());
			JSONArray array = JSONArray.fromObject(valueNumber);
			Format f1 = new DecimalFormat("0000");
			Format f2 = new DecimalFormat("00000");
			List<Card> cardList = new ArrayList<Card>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = (JSONObject) array.get(i);
				int value = Integer.valueOf(obj.get("value").toString());
				int number = Integer.valueOf(obj.get("number").toString());
				String money = f1.format(value);
				int index=1;
				String ser=cbm.querySameMoney(sdf1.format(date)+money);
				if(!StringUtil.isEmpty(ser)&&ser.length()>12){
					index=Integer.parseInt(ser.substring(12))+1;
				}
				// 生成万方卡
				for (int j = 0; j < number; j++) {
					Card card = new Card();
					card.setId(GetUuid.getId());// 万方卡id
					card.setBatchId(batchId);// 万方卡批次id
					String cardNum = sdf1.format(date) + money + f2.format(index++);// 卡号
					card.setCardNum(cardNum);// 卡号
					card.setPassword(String.valueOf(new Random().nextInt(899999999) + 100000000));// 密码
					card.setValue(value);// 面值
					card.setInvokeState(1);// 初始激活状态
					cardList.add(card);
				}
			}
			int cardNum = 0;
			if (cardList.size() > 0) {
				// 一批一万
				List<List<Card>> tempList = this.createList(cardList, 10000);
				for (List<Card> cardls : tempList) {
					cardNum += cardMapper.insertCards(cardls);
				}
			}
			if (batchCard > 0 && cardNum > 0) {
				flag = true;
			}
			log.info("批次:"+batchId+"生成万方卡耗时:"+(System.currentTimeMillis()-time)+"ms");
		} catch (Exception e) {
			log.error("批量导入万方卡异常：", e);
		}
		return flag;
	}
	/**
	 * 修改批次状态
	 */
	@Override
	public boolean updateBatchState(String batchId,String batchState, String pullDepartment,
			String pullPerson) {
		pullDepartment = pullDepartment.trim();
		pullPerson = pullPerson.trim();
		if(StringUtils.isBlank(pullDepartment)) pullDepartment = "";
		if(StringUtils.isBlank(pullPerson)) pullPerson = "";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("batchId", batchId);
		map.put("batchState", (Integer.valueOf(batchState)+1));
		map.put("pullDepartment", pullDepartment);
		map.put("pullPerson", pullPerson);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("pullDate", sdf.format(new Date()));
		boolean flag = false;
		int i = cbm.updateBatchState(map);
		if(i > 0){
			flag = true;
		}
		return flag;
	}
	/**
	 * 查询所有批次（已审核）
	 */
	@Override
	public List<Map<String, Object>> queryAllBatch() {
		List<Map<String, Object>> list = cbm.queryAll();
		return list;
	}

	/**
	 * 修改附件(未审核)
	 */
	@Override
	public boolean updateAttachment(String batchId, String adjunct) {
		int i = cbm.updateAttachment(batchId, adjunct);
		return i > 0 ? true : false;
	}
}
