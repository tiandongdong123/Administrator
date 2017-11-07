package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.Message;
public interface MessageMapper {
	/**
	 * 查询学科分类信息
	 * @return
	 */
	public List<Object>  selectMessageInfor(Map<String,Object> map);
	/**
	 * 根据咨询类型查所有
	 */
	public List<Object>  selectBycolums(String colums);
	/**
	 * 查询所有的资讯信息
	 * @param map
	 * @return
	 */
	public List<Object>  selectMessageInforAll(Map<String,Object> map);
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
	 * 更新
	 * @param message
	 * @return
	 */
	public int updateMessageStick(Message message);
	/**
	 * 查询
	 * @param param
	 * @return
	 */
	public Message findMessageParam(String title);
	/**
	 *  发布/下撤/再发布
	 * @param map
	 * @return
	 */
	int updateIssue(Map<String,Object> map);
}