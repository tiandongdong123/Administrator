package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PageList;
import com.wf.dao.CardMapper;
import com.wf.service.CardService;
import com.wf.service.CardTypeService;

@Service
public class CardServiceImpl implements CardService{
	@Autowired
	private CardMapper cardMapper;
	@Autowired
	private CardTypeService cardtype;
	
	@Override
	public PageList queryCard(String batchName, String numStart, String numEnd,
			String applyDepartment, String applyPerson, String startTime,
			String endTime, String cardType, String batchState,String invokeState,
			int pageNum,int pageSize) {
		//-----------去除首尾空格-----------
		batchName = batchName.trim();
		numStart = numStart.trim();
		numEnd = numEnd.trim();
		applyDepartment = applyDepartment.trim();
		applyPerson = applyPerson.trim();
		
		if(StringUtils.isEmpty(batchName)) batchName = null;
		if(StringUtils.isEmpty(numStart)) numStart = null;
		if(StringUtils.isEmpty(numEnd)) numEnd = null;
		if(StringUtils.isEmpty(applyDepartment)) applyDepartment = null;
		if(StringUtils.isEmpty(applyPerson)) applyPerson = null;
		if(StringUtils.isEmpty(startTime)) startTime = null;
		if(StringUtils.isEmpty(endTime)) endTime = null;
		if(StringUtils.isEmpty(cardType)) cardType = null;
		if(StringUtils.isEmpty(batchState)) batchState = null;
		if(StringUtils.isEmpty(invokeState)) invokeState = null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchName", batchName);
		map.put("numStart", numStart);
		map.put("numEnd", numEnd);
		map.put("applyDepartment", applyDepartment);
		map.put("applyPerson", applyPerson);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("cardType", cardType);
		map.put("batchState", batchState);
		map.put("invokeState", invokeState);
		int pageStart = (pageNum-1)*pageSize;//当前页的第一条
		map.put("pageNum", pageStart);
		map.put("pageSize", pageSize);
		List<Object> list = cardMapper.queryCard(map);
		//-------------查询一共有多少条-----------------------------
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("batchName", batchName);
		map1.put("numStart", numStart);
		map1.put("numEnd", numEnd);
		map1.put("applyDepartment", applyDepartment);
		map1.put("applyPerson", applyPerson);
		map1.put("startTime", startTime);
		map1.put("endTime", endTime);
		map1.put("cardType", cardType);
		map1.put("batchState", batchState);
		map.put("invokeState", invokeState);
		List<Object> all = cardMapper.queryAll(map1);
		PageList pl = new PageList();
		pl.setPageRow(list);//查询结果列表
		pl.setTotalRow(all.size());//总条数
		pl.setPageNum(pageNum);//当前页
		pl.setPageSize(pageSize);//每页显示的数量
		return pl;
	}
	/**
	 * 单张万方卡详情页
	 * @param id
	 * @return
	 */
	@Override
	public Map<String,Object> queryOneById(String id) {
		Map<String,Object> map = cardMapper.queryOneById(id);
		return map;
	}

	/**
	 * 根据batchId  万方卡列表
	 * @param batchId
	 * @return
	 */
	@Override
	public PageList queryCardBybatchId(String batchId,int pageNum,int pageSize) {
		if(StringUtils.isEmpty(batchId)) batchId = null;
		Map<String,Object> map = new HashMap<String, Object>();
		int pageStart = (pageNum - 1) * pageSize;
		map.put("batchId", batchId);
		map.put("pageNum", pageStart);
		map.put("pageSize", pageSize);
		List<Object> list = cardMapper.queryCardBybatchId(map);
		List<Map<String,Object>> allList = cardMapper.queryAllBybatchId(batchId);
		PageList p = new PageList();
		p.setTotalRow(allList.size());
		p.setPageNum(pageNum);
		p.setPageSize(pageSize);
		p.setPageRow(list);
		return p;
	}
	/**
	 * 修改万方卡激活状态
	 * @param id
	 * @return
	 */
	@Override
	public boolean updateInvokeState(String id,String invokeState) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("invokeState", 2);
		map.put("invokeUser", "zhangsan");
		map.put("invokeIp", "221.156.95.21");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("invokeDate", sdf.format(new Date()));
		boolean flag = false;
		int i = cardMapper.updateInvokeState(map);
		if(i > 0){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 根据batchId  万方卡列表
	 * @param batchId
	 * @return
	 */
	@Override
	public List<Map<String,Object>> queryAllCardBybatchId(String batchId) {
		if(StringUtils.isEmpty(batchId)) batchId = null;
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("batchId", batchId);
		List<Map<String,Object>> allList = cardMapper.queryAllBybatchId(batchId);
		return allList;
	}
	/**
	 * 查询所有已审核card
	 */
	@Override
	public List<Map<String, Object>> queryAllCard() {
		List<Map<String, Object>> list = cardMapper.queryAllCard();
		return list;
	}
	
}
