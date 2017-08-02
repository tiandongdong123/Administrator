package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.Card;


public interface CardMapper {
	/**
	 * 生成充值卡
	 */
	int insertCards(List<Card> list);
	/**
	 * 充值卡卡号排序计算卡号
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
	 * 查询充值卡列表
	 */
	List<Object> queryCard(Map<String,Object> map);
	/**
	 * 单张充值卡详情页
	 * @param id
	 * @return
	 */
	Map<String,Object> queryOneById(String id);
	/**
	 * 根据batchId  查询所有充值卡
	 * @param batchId
	 * @return
	 */
	List<Map<String,Object>> queryAllBybatchId(String batchId);
	/**
	 * 根据batchId  分页充值卡列表
	 * @param batchId
	 * @return
	 */
	List<Object> queryCardBybatchId(Map<String,Object> map);
	/**
	 * 修改充值卡激活状态
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