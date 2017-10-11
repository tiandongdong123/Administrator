package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.CardBatch;


public interface CardBatchMapper {
   /**
    * 生成充值卡批次
    */
	int insertCardBatch(CardBatch cardBatch);
	/**
	 * 充值卡批次排序计算批次号
	 */
	String queryBatchName();
	/**
	 * 查询当日同种金额卡号的序列号
	 */
	String querySameMoney(String card);
	/**
	 * 查询当前条件下所有批次
	 */
	List<Object> queryAllBatch(Map<String,Object> map);
	/**
	 * 查询所有批次
	 */
	List<Map<String,Object>> queryAll();
	/**
	 * 充值卡审核
	 * @param map
	 * @return
	 */
	List<Object> queryCheck(Map<String,Object> map);
	/**
	 * 根据batchId  充值卡批次详情页
	 * @param batchId
	 * @return
	 */
	Map<String,Object> queryOneByBatchId(String batchId);
	/**
	 * 修改审核状态
	 */
	int updateCheckState(Map<String,Object> map);
	/**
	 * 修改批次状态
	 */
	int updateBatchState(Map<String,Object> map);
}