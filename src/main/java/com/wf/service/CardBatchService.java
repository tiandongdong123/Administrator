package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.PageList;
import com.wf.bean.Wfadmin;


public interface CardBatchService {
	/**
	 * 添加万方卡批次
	 */
	boolean insertCardBatch(String type,String valueNumber,String validStart,String validEnd,String applyDepartment,
			String applyPerson,String applyDate,String adjunct);
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
	PageList queryCheck(String batchName,String applyDepartment,String applyPerson,String startTime,String endTime,String cardType,String checkState,String batchState,int pageNum,int pageSize);
	/**
	 * 根据batchId  万方卡批次详情页
	 * @param batchId
	 * @return
	 */
	Map<String,Object> queryOneByBatchId(String batchId);
	/**
	 * 查询所有批次
	 * @return
	 */
	List<Map<String,Object>> queryAllBatch();
	/**
	 * 修改审核状态
	 */
	boolean updateCheckState(Wfadmin admin,String batchId);
	/**
	 * 修改批次状态
	 */
	boolean updateBatchState(String batchId,String batchState,String pullDepartment,String pullPerson);
}
