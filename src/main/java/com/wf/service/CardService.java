package com.wf.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.wf.bean.PageList;

public interface CardService {
	/**
	 * 查询充值卡列表
	 * @param batchName 批次号
	 * @param numStart 卡号开始
	 * @param numEnd 卡号结束
	 * @param applyDepartment 申请部门
	 * @param applyPerson 申请人
	 * @param startTime 申请时间开始
	 * @param endTime 申请时间结束
	 * @param cardType 卡类型
	 * @param batchState 批次状态
	 * @param invokeState 充值卡状态
	 * @param pageNum 页码
	 * @param pageSize 每页显示数量
	 * @return
	 */
	PageList queryCard(String batchName,String numStart,String numEnd,
			String applyDepartment,String applyPerson,String startTime,
			String endTime,String cardType,String batchState,String invokeState,int pageNum,int pageSize);
	
	/**
	 * 根据id  单张充值卡详情页
	 * @param id
	 * @return
	 */
	Map<String,Object> queryOneById(String id);
	/**
	 * 根据batchId  充值卡列表
	 * @param batchId
	 * @return
	 */
	PageList queryCardBybatchId(String batchId,int pageNum,int pageSize);
	/**
	 * 根据batchId  充值卡列表
	 * @param batchId
	 * @return
	 */
	List<Map<String,Object>> queryAllCardBybatchId(String batchId);
	/**
	 * 查询所有card
	 * @return
	 */
	List<Map<String,Object>> queryAllCard();
	/**
	 * 修改充值卡激活状态
	 * @param id
	 * @return
	 */
	boolean updateInvokeState(HttpSession session,String id,String invokeState);
}
