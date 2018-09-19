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
import com.utils.SolrService;
import com.wf.bean.Message;
import com.wf.bean.PageList;
import com.wf.dao.MessageMapper;
import com.wf.service.MessageService;
import com.xxl.conf.core.XxlConfClient;
@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	MessageMapper dao;
	
	RedisUtil redis = new RedisUtil();
	private String hosts=XxlConfClient.get("wf-public.solr.url", null);
	
	@Override
	public PageList getMessage(int pageNum,int pageSize,String branch,String human,String colums,String startTime,String endTime,String isTop) {
		if(StringUtils.isEmpty(branch)) branch=null;
		if(StringUtils.isEmpty(human)) human=null;
		if(StringUtils.isEmpty(colums)) colums=null;
		if(StringUtils.isEmpty(startTime)) startTime=null;
		if(StringUtils.isEmpty(endTime)) endTime=null;
		if(StringUtils.isEmpty(isTop)) isTop=null;
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
		mp.put("isTop", isTop);
		List<Object> pageRow = dao.getMessageList(mp);
		int num = dao.getMessageCount(mp);
		p.setPageNum(pageNum);
		p.setPageSize(pageSize);
		p.setPageRow(pageRow);
		p.setTotalRow(num);
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
	public Boolean updataMessageStick(Message message) {
		boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", message.getId());
		map.put("issueState", 2);
		map.put("stick", new Date());
		map.put("isTop", "1");
		int num = dao.updateIssue(map);
		if (num > 0) {
			flag = true;
			setData(message.getColums(), 2, message.getId());
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("issueState", issue);
		map.put("stick", new Date());
		map.put("isTop", "0");
		int num = dao.updateIssue(map);
		if (num > 0) {
			flag = true;
			setData(colums, issue, id);
		}
		return flag;
	}
	
	//操作数据
	private void setData(String colums,int issue,String id){
		//发布redis
		setRedis(colums);
		String type="";
		if("专题聚焦".equals(colums)){
			type="special";
		}else if("科技动态".equals(colums)){
			type="conference";
		}else if("基金会议".equals(colums)){
			type="fund";
		}else if("万方资讯".equals(colums)){
			type="activity";
		}
		//修改/下撤成功发布至solr
		if(issue==2){//发布
			Message message=dao.findMessage(id);
			deployInformation("information",type,message);
		}else if(issue==3){//下撤
			RedisUtil redisUtil = new RedisUtil();
			String collection = redisUtil.get("information", 3);
			SolrService.getInstance(hosts+"/"+collection);
			SolrService.deleteIndex(id);
		}
	}
	
	/**
	 * redis插入数据
	 * @param colums
	 */
	private void setRedis(String colums){
		List<Object> list = new ArrayList<Object>();
		Map<String,Object> topMap=new HashMap<String,Object>();
		topMap.put("colums", colums);
		topMap.put("size", 3);
		list = dao.selectIsTop(topMap);//获取
		int topSize=list.size();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("colums", colums);
		map.put("size", 3);
		if("专题聚焦".equals(colums)){
			//清空redis中对应的key
			redis.del("ztID");
			redis.del("special");
			if(topSize<3){
				map.put("size", 10-topSize);
				List<Object> ls = dao.selectBycolums(map);
				list.addAll(ls);	
			}
			for(int i = 0;i < list.size();i++){
				Message m = (Message) list.get(i);
				m.setContent("");
				String object = JSONObject.fromObject(m).toString();
				redis.zadd("ztID", i, m.getId());//发布到redis
				redis.hset("special", m.getId(), object);
			}
		}else if("科技动态".equals(colums)){
			redis.del("hyID");
			redis.del("conference");
			if(topSize<3){
				map.put("size", 3-topSize);
				List<Object> ls = dao.selectBycolums(map);
				list.addAll(ls);
			}
			for(int i = 0;i < list.size();i++){
				Message m = (Message) list.get(i);
				m.setContent("");
				String object = JSONObject.fromObject(m).toString();
				redis.zadd("hyID", i, m.getId());
				redis.hset("conference", m.getId(), object);
			}
		}else if("基金会议".equals(colums)){
			redis.del("jjID");
			redis.del("fund");
			if(topSize<3){
				map.put("size", 3-topSize);
				List<Object> ls = dao.selectBycolums(map);
				list.addAll(ls);
			}
			for(int i = 0;i < list.size();i++){
				Message m = (Message) list.get(i);
				m.setContent("");
				String object = JSONObject.fromObject(m).toString();
				redis.zadd("jjID", i, m.getId());
				redis.hset("fund", m.getId(), object);
			}
		}else if("万方资讯".equals(colums)){
			redis.del("kkID");
			redis.del("activity");
			if(topSize<3){
				map.put("size", 3-topSize);
				List<Object> ls = dao.selectBycolums(map);
				list.addAll(ls);
			}
			for(int i = 0;i < list.size();i++){
				Message m = (Message) list.get(i);
				m.setContent("");
				String object = JSONObject.fromObject(m).toString();
				redis.zadd("kkID", i, m.getId());
				redis.hset("activity", m.getId(), object);
			}
		}
	}
	
	private void deployInformation(String core,String type,Message message){
		Map<String,Object> newMap = new HashMap<>();
		List<Map<String,Object>> list = new ArrayList<>();
		String abstracts = message.getAbstracts();
		String author = message.getAuthor();
		String branch = message.getBranch();
		String colums = message.getColums();
		String content = message.getContent();
		String createTime = message.getCreateTime();
		String human = message.getHuman();
		String id = message.getId();
		String imageUrl = message.getImageUrl();
		Integer issueState = message.getIssueState();
		String linkAddress = message.getLinkAddress();
		String organName = message.getOrganName();
		String stick = message.getStick();
		String title = message.getTitle();
		String isTop=message.getIsTop();
		
		newMap.put("id", id);
		newMap.put("type", type);
		newMap.put("auto_stringITS_abstracts", abstracts);
		//处理特殊字符
		newMap.put("auto_stringITS_content", content);
		newMap.put("auto_stringITS_title", title);
		newMap.put("stringIS_author", author);
		newMap.put("stringIS_branch", branch);
		newMap.put("stringIS_colums", colums);
		if(createTime.endsWith(".0")){
			createTime=createTime.substring(0,createTime.length()-2);
		}
		newMap.put("stringIS_createTime", createTime);
		newMap.put("stringIS_human", human);
		newMap.put("stringIS_imageUrl", imageUrl);
		newMap.put("intIS_issueState", issueState);
		newMap.put("stringIS_linkAddress", linkAddress);
		newMap.put("stringIS_organName", organName);
		if(stick.endsWith(".0")){
			stick=stick.substring(0,stick.length()-2);
		}
		newMap.put("stringIS_stick", stick);
		newMap.put("stringIS_isTop", isTop);
		newMap.put("longIS_sort", this.getLongSort(isTop, stick, createTime));
		list.add(newMap);
		RedisUtil redisUtil = new RedisUtil();
		String collection = redisUtil.get(core, 3);
		SolrService.getInstance(hosts+"/"+collection);
		SolrService.createIndexFound(list);
	}
	private long getLongSort(String isTop,String stick,String createTime){
		long sort=0;
		try{
			if (isTop != null && "1".equals(isTop)) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sort = sdf1.parse(stick).getTime();
			} else {
				String pattern = "yyyy-MM-dd";
				if (createTime.length() > 10) {
					pattern = "yyyy-MM-dd HH:mm:ss";
				}
				SimpleDateFormat sdf1 = new SimpleDateFormat(pattern);
				sort = sdf1.parse(createTime).getTime();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sort;
	}
	
	@Override
	public List<Object> exportMessage(String branch,String colums,String human,String startTime,String endTime) {
		
		if(StringUtils.isEmpty(branch)) branch=null;
		if(StringUtils.isEmpty(human)) human=null;
		if(StringUtils.isEmpty(colums)) colums=null;
		if(StringUtils.isEmpty(startTime)) startTime=null;
		if(StringUtils.isEmpty(endTime)) endTime=null;
		Map<String, Object> mpPara=new HashMap<String,Object>();
		mpPara.put("branch", branch);
		mpPara.put("human", human);
		mpPara.put("colums", colums);
		mpPara.put("startTime", startTime);
		mpPara.put("endTime", endTime);
		return dao.selectMessageInforAll(mpPara);
	}     

	@Override
	public List<Object> getAllMessage(Map<String, Object> map) {
		return dao.selectMessageInforAll(map);
	}
	
	@Override
	public void updateBatch(List<Object> list){
		setRedis("专题聚焦");
		setRedis("科技动态");
		setRedis("基金会议");
		setRedis("万方资讯");
		RedisUtil redisUtil = new RedisUtil();
		String collection = redisUtil.get("information", 3);
		SolrService.getInstance(hosts+"/"+collection);
		List<Map<String,Object>> indexList=new ArrayList<Map<String,Object>>();
		for(Object obj:list){
			Message message=(Message) obj;
			String abstracts = message.getAbstracts();
			String author = message.getAuthor();
			String branch = message.getBranch();
			String colums = message.getColums();
			String content = message.getContent();
			String createTime = message.getCreateTime();
			String human = message.getHuman();
			String id = message.getId();
			String imageUrl = message.getImageUrl();
			Integer issueState = message.getIssueState();
			String linkAddress = message.getLinkAddress();
			String organName = message.getOrganName();
			String stick = message.getStick();
			String title = message.getTitle();
			String isTop=message.getIsTop();
			String type="";
			if("专题聚焦".equals(colums)){
				type="special";
			}else if("科技动态".equals(colums)){
				type="conference";
			}else if("基金会议".equals(colums)){
				type="fund";
			}else if("万方资讯".equals(colums)){
				type="activity";
			}
			Map<String,Object> newMap = new HashMap<>();
			newMap.put("id", id);
			newMap.put("type", type);
			newMap.put("auto_stringITS_abstracts", abstracts);
			//处理特殊字符
			newMap.put("auto_stringITS_content", content);
			newMap.put("auto_stringITS_title", title);
			newMap.put("stringIS_author", author);
			newMap.put("stringIS_branch", branch);
			newMap.put("stringIS_colums", colums);
			if(createTime.endsWith(".0")){
				createTime=createTime.substring(0,createTime.length()-2);
			}
			newMap.put("stringIS_createTime", createTime);
			newMap.put("stringIS_human", human);
			newMap.put("stringIS_imageUrl", imageUrl);
			newMap.put("intIS_issueState", issueState);
			newMap.put("stringIS_linkAddress", linkAddress);
			newMap.put("stringIS_organName", organName);
			if(stick.endsWith(".0")){
				stick=stick.substring(0,stick.length()-2);
			}
			newMap.put("stringIS_stick", stick);
			newMap.put("stringIS_stick", stick);
			newMap.put("stringIS_isTop", isTop);
			newMap.put("longIS_sort", this.getLongSort(isTop, stick, createTime));
			indexList.add(newMap);
			if(indexList.size()==1000){
				SolrService.createIndexFound(indexList);
				System.out.println("发送1000条");
				indexList.clear();
			}
		}
		//批量发送
		if(indexList.size()>0){
			SolrService.createIndexFound(indexList);
			System.out.println("发送"+indexList.size()+"条");
		}
		System.out.println("一键发布完毕");
	}     

}
