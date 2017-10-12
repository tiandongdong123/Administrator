package com.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.CardBatch;


public interface CardBatchMapper {
   /**
    * 生成万方卡批次
    */
	int insertCardBatch(CardBatch cardBatch);
	/**
	 * 万方卡批次排序计算批次号
	 */
	String queryBatchName();
	/**
	 * 查询当日同种金额卡号的序列号
	 */
	String querySameMoney(String card);
	/**
	 * 查询所有批次
	 */
	List<Map<String,Object>> queryAll();
	/**
	 * 万方卡审核
	 * @param map
	 * @return
	 */
	List<Object> queryCheck(Map<String,Object> map);
	/**
	 * 查询当前条件下数量
	 */
	int queryCheckSize(Map<String,Object> map);
	/**
	 * 根据batchId  万方卡批次详情页
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
	
	/**
	 * 修改附件(未审核)
	 * @param batchId
	 * @param adjunct
	 * @return
	 */
	int updateAttachment(@Param("batchId") String batchId, @Param("adjunct") String adjunct);
}