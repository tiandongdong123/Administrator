package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.JudgeMessageTitleParameter;
import com.wf.bean.Message;
import com.wf.bean.MessageSearchRequest;
import org.springframework.web.bind.annotation.RequestParam;

public interface MessageMapper {
	
	/**
	 * 查询学科分类信息(分页数据)
	 * @return
	 */
	public List<Object> getMessageList(MessageSearchRequest request);
	
	/**
	 * 查询学科分类信息(分页计数)
	 * @return
	 */
	public int getMessageCount(MessageSearchRequest request);
	
	/**
	 * 根据咨询类型查前n行(发布未置顶状态资讯)
	 */
	public List<Object> selectBycolums(Map<String,Object> map);
	public List<Object> selectBycolums2(Map<String,Object> map);

	/**
	 * 查询专题聚焦（发布置顶状态资讯）
	 */
	public List<Object> selectIsTop(Map<String,Object> map);
	public List<Object> selectIsTop2(Map<String,Object> map);

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

	/**
	 * 判断标题是否存在
	 * @param parameter
	 * @return
	 */
	int judgeMessageTitle(JudgeMessageTitleParameter parameter);
}
