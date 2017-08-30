package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.RedisUtil;
import com.wf.bean.Message;
import com.wf.bean.PageList;
import com.wf.bean.Wfadmin;
import com.wf.dao.MessageMapper;
import com.wf.service.MessageService;
@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	MessageMapper dao;
	RedisUtil redis = new RedisUtil();
	@Override
	public PageList getMessage(int pageNum,int pageSize,String branch,String human,String colums,String startTime,String endTime) {
		if(StringUtils.isEmpty(branch)) branch=null;
		if(StringUtils.isEmpty(human)) human=null;
		if(StringUtils.isEmpty(colums)) colums=null;
		if(StringUtils.isEmpty(startTime)) startTime=null;
		if(StringUtils.isEmpty(endTime)) endTime=null;
		PageList p=new PageList();
		Map<String,Object> mp=new HashMap<String, Object>();
		int pagen=(pageNum-1)*pageSize;
		mp.put("pageNum", pagen);
		mp.put("pageSize", pageSize);
		mp.put("branch", branch);
		mp.put("human", human);
		mp.put("colums", colums);
		mp.put("startTime", startTime);
		mp.put("endTime", endTime);
		List<Object> pageRow= dao.selectMessageInfor(mp);
		Map<String,Object> mpPara=new HashMap<String, Object>();
		mpPara.put("branch", branch);
		mpPara.put("human", human);
		mpPara.put("colums", colums);
		mpPara.put("startTime", startTime);
		mpPara.put("endTime", endTime);
		List<Object> pageRowAll= dao.selectMessageInforAll(mpPara);
		int pageTotal=0;
		int b =pageRowAll.size()%pageSize;
		pageTotal=pageRowAll.size()!=0 && b !=0?pageRowAll.size()/pageSize+1:pageRowAll.size()/pageSize;
		p.setPageNum(pageNum);
		p.setPageSize(pageSize);
		p.setPageRow(pageRow);
		p.setPageTotal(pageTotal);
		return p;
	}
	@Override
	public Message findMessage(String id) {
		Message message =dao.findMessage(id);
		return message;
	}
	@Override
	public Boolean insertMessage(Message message) {
		return dao.insertMessage(message)>0?true:false;
	}
	
	@Override
	public Boolean deleteMessage(String ids) {
		int n =dao.deleteMessage(ids);
		boolean b=n>0?true:false;
		return b;
	}
	
	@Override
	public Boolean updateMessage(Message message) {
		int n =dao.updateMessage(message);
		boolean b=n>0?true:false;
		return b;
	}
	
	@Override
	public Boolean updataMessageStick(Message message,String colums) {
		boolean b =dao.updateMessageStick(message)>0?true:false;
		boolean flag = false;
		List<Object> list = new ArrayList<Object>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", message.getId());
		map.put("issueState", 2);
		int num = dao.updateIssue(map);
		if(num > 0){
			flag = true;
			//--------------修改成功发布至redis---------------
			
				if("专题聚焦".equals(colums)){
					//清空redis中对应的key
					redis.del("ztID");
					redis.del("special");
					list= dao.selectBycolums(colums);
					for(int i = 0;i < list.size();i++){
						Message m = (Message) list.get(i);
						String object = JSONObject.fromObject(m).toString();
						redis.zadd("ztID", i, m.getId());//发布到redis
						redis.hset("special", m.getId(), object);
					}
				}else if("科技动态".equals(colums)){
					redis.del("hyID");
					redis.del("conference");
					list= dao.selectBycolums(colums);
					for(int i = 0;i < list.size();i++){
						Message m = (Message) list.get(i);
						String object = JSONObject.fromObject(m).toString();
						redis.zadd("hyID", i, m.getId());
						redis.hset("conference", m.getId(), object);
					}
				}else if("基金会议".equals(colums)){
					redis.del("jjID");
					redis.del("fund");
					list= dao.selectBycolums(colums);
					for(int i = 0;i < list.size();i++){
						Message m = (Message) list.get(i);
						String object = JSONObject.fromObject(m).toString();
						redis.zadd("jjID", i, m.getId());
						redis.hset("fund", m.getId(), object);
					}
				}else if("万方资讯".equals(colums)){
					redis.del("kkID");
					redis.del("activity");
					list= dao.selectBycolums(colums);
					for(int i = 0;i < list.size();i++){
						Message m = (Message) list.get(i);
						String object = JSONObject.fromObject(m).toString();
						redis.zadd("kkID", i, m.getId());
						redis.hset("activity", m.getId(), object);
					}
				}
		}
		return flag;
	}
	
	/**
	 * 发布/下撤/再发布
	 * @param id
	 * @param issueState
	 * @return
	 */
	@Override
	public boolean updateIssue(String id,String colums,String issueState) {
		boolean flag = false;
		int issue = Integer.valueOf(issueState);
		List<Object> list = new ArrayList<Object>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("issueState", issue);
		int num = dao.updateIssue(map);
		if(num > 0){
			flag = true;
			//--------------修改成功发布至redis---------------
			
				if("专题聚焦".equals(colums)){
					//清空redis中对应的key
					redis.del("ztID");
					redis.del("special");
					list= dao.selectBycolums(colums);
					for(int i = 0;i < list.size();i++){
						Message m = (Message) list.get(i);
						String object = JSONObject.fromObject(m).toString();
						redis.zadd("ztID", i, m.getId());//发布到redis
						redis.hset("special", m.getId(), object);
					}
				}else if("科技动态".equals(colums)){
					redis.del("hyID");
					redis.del("conference");
					list= dao.selectBycolums(colums);
					for(int i = 0;i < list.size();i++){
						Message m = (Message) list.get(i);
						String object = JSONObject.fromObject(m).toString();
						redis.zadd("hyID", i, m.getId());
						redis.hset("conference", m.getId(), object);
					}
				}else if("基金会议".equals(colums)){
					redis.del("jjID");
					redis.del("fund");
					list= dao.selectBycolums(colums);
					for(int i = 0;i < list.size();i++){
						Message m = (Message) list.get(i);
						String object = JSONObject.fromObject(m).toString();
						redis.zadd("jjID", i, m.getId());
						redis.hset("fund", m.getId(), object);
					}
				}else if("万方资讯".equals(colums)){
					redis.del("kkID");
					redis.del("activity");
					list= dao.selectBycolums(colums);
					for(int i = 0;i < list.size();i++){
						Message m = (Message) list.get(i);
						String object = JSONObject.fromObject(m).toString();
						redis.zadd("kkID", i, m.getId());
						redis.hset("activity", m.getId(), object);
					}
				}
			}
		return flag;
	}
	
}
