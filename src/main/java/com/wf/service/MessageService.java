package com.wf.service;

import javax.servlet.http.HttpSession;

import com.wf.bean.Message;
import com.wf.bean.PageList;
public interface MessageService {
	/**
	 * 条件查询学科分类的信息
	 */
	PageList getMessage(int pageNum,int pageSize,String branch,String human,String colums,String startTime,String endTime);
	/**
	 * 查询单条资讯
	 * @param id 查询ID
	 * @return
	 */
	Message findMessage(String id);
	/**
	 * 添加资讯
	 * @param message
	 * @return
	 */
	Boolean insertMessage(Message message,HttpSession session);
	/**
	 * 删除资讯
	 * @param ids 删除的ID集合
	 * @return
	 */
	Boolean deleteMessage(String ids);
	/**
	 * 更新资讯
	 * @param message 插入的实体类
	 * @return
	 */
	Boolean updateMessage(Message message);
	
	Boolean updataMessageStick(Message message,String colums);
	/**
	 * 发布/下撤/再发布
	 * @param id
	 * @param issueState
	 * @return
	 */
	boolean updateIssue(String id,String colums,String issueState);
}
