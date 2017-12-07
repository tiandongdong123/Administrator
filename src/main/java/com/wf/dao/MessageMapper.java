package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.Message;
public interface MessageMapper {
	
	/**
	 * 查询学科分类信息(分页数据)
	 * @return
	 */
	public List<Object> getMessageList(Map<String,Object> map);
	
	/**
	 * 查询学科分类信息(分页计数)
	 * @return
	 */
	public int getMessageCount(Map<String,Object> map);
	
	/**
	 * 根据咨询类型查前n行
	 */
	public List<Object> selectBycolums(Map<String,Object> map);
	
	/**
	 * 查询专题聚焦
	 */
	public List<Object> selectIsTop(Map<String,Object> map);
	
	/**
	 * 查询所有的资讯信息
	 * @param map
	 * @return
	 */
	public List<Object> selectMessageInforAll(Map<String,Object> map);
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public int deleteMessage(String ids);
	/**
	 * 更新资讯
	 * @param message
	 * @return
	 */
	public int updateMessage(Message message);
	/**
	 * 单个查询资讯
	 * @return
	 */
	public Message findMessage(String id);
	/**
	 * 增加
	 * @param message
	 * @return
	 */
	public int insertMessage(Message message);
	/**
	 *  发布/下撤/再发布/置顶
	 * @param map
	 * @return
	 */
	int updateIssue(Map<String,Object> map);
	
	/**
	 * 重置置顶
	 * @param colums
	 * @return
	 */
	int updateIsTop(String colums);
}
