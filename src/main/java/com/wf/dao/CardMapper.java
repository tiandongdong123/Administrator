package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.Card;


public interface CardMapper {
	/**
	 * 生成万方卡
	 */
	int insertCards(List<Card> list);
	/**
	 * 万方卡卡号排序计算卡号
	 */
	String queryCardNum();
	/**
	 * 查询当前条件下所有
	 */
	List<Object> queryAll(Map<String,Object> map);
	/**
	 * 查询所有
	 * @return
	 */
	List<Map<String,Object>> queryAllCard();
	/**
	 * 查询万方卡列表
	 */
	List<Object> queryCard(Map<String,Object> map);
	/**
	 * 单张万方卡详情页
	 * @param id
	 * @return
	 */
	Map<String,Object> queryOneById(String id);
	/**
	 * 根据batchId  查询所有万方卡
	 * @param batchId
	 * @return
	 */
	List<Map<String,Object>> queryAllBybatchId(String batchId);
	
	/**
	 * 根据batchId  查询所有万方卡的数量
	 * @param batchId
	 * @return
	 */
	int querySzieBybatchId(String batchId);
	
	/**
	 * 根据batchId  分页万方卡列表
	 * @param batchId
	 * @return
	 */
	List<Object> queryCardBybatchId(Map<String,Object> map);
	/**
	 * 修改万方卡激活状态
	 * @param id
	 * @return
	 */
	int updateInvokeState(Map<String,Object> map);
	/**
	 * 查询用户万方卡账余额
	 * @param userId
	 * @return
	 */
	Integer queryCardValue(String userId);
}