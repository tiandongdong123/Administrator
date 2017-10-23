package com.wf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
		String type="";
		List<Object> list = new ArrayList<Object>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("issueState", issue);
		map.put("stick", new Date());
		int num = dao.updateIssue(map);
		if(num > 0){
			flag = true;
			//--------------修改成功发布至redis---------------
			
				if("专题聚焦".equals(colums)){
					//清空redis中对应的key
					redis.del("ztID");
					redis.del("special");
					type="special";
					list= dao.selectBycolums("special");
					for(int i = 0;i < list.size();i++){
						Message m = (Message) list.get(i);
						String object = JSONObject.fromObject(m).toString();
						redis.zadd("ztID", i, m.getId());//发布到redis
						redis.hset("special", m.getId(), object);
					}
				}else if("科技动态".equals(colums)){
					redis.del("hyID");
					redis.del("conference");
					type="conference";
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
					type="fund";
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
					type="activity";
					list= dao.selectBycolums("activity");
					for(int i = 0;i < list.size();i++){
						Message m = (Message) list.get(i);
						String object = JSONObject.fromObject(m).toString();
						redis.zadd("kkID", i, m.getId());
						redis.hset("activity", m.getId(), object);
					}
				}
				
				//--------------修改/下撤成功发布至solr---------------
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
		return flag;
	}
	
	
	/**
	 * 将数据发布到solr中
	 */
	private  void  deployInformation(String core,String type,Message message) {
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
			
			newMap.put("id", id);
			newMap.put("type", type);
			newMap.put("auto_stringITS_abstracts", abstracts);
			//处理特殊字符
			content =toNoHtml((String)content);
			newMap.put("auto_stringITS_content", content);
			newMap.put("auto_stringITS_title", title);
			
			newMap.put("stringIS_author", author);
			newMap.put("stringIS_branch", branch);
			newMap.put("stringIS_colums", colums);
			newMap.put("stringIS_createTime", createTime);
			newMap.put("stringIS_human", human);
			newMap.put("stringIS_imageUrl", imageUrl);
			newMap.put("intIS_issueState", issueState);
			newMap.put("stringIS_linkAddress", linkAddress);
			newMap.put("stringIS_organName", organName);
			newMap.put("stringIS_stick", stick);
			
			list.add(newMap);
			
		RedisUtil redisUtil = new RedisUtil();
		String collection = redisUtil.get(core, 3);
		
		SolrService.getInstance(hosts+"/"+collection);
		SolrService.createIndexFound(list);
	} 
	
	
	
	public static String toNoHtml(String inputString) {      
        String htmlStr = inputString.replace("&nbsp;", "").replace("&ldquo;", "").replace("&rdquo;", "");    
        htmlStr = StringUtils.deleteWhitespace(htmlStr);
        String textStr ="";      
        java.util.regex.Pattern p_script;      
        java.util.regex.Matcher m_script;      
        java.util.regex.Pattern p_style;      
        java.util.regex.Matcher m_style;      
        java.util.regex.Pattern p_html;      
        java.util.regex.Matcher m_html;      
            
        java.util.regex.Pattern p_html1;      
        java.util.regex.Matcher m_html1;      
         
       try {      
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }      
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }      
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式      
            String regEx_html1 = "<[^>]+";      
            p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);      
            m_script = p_script.matcher(htmlStr);      
            htmlStr = m_script.replaceAll(""); //过滤script标签      
  
            p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);      
            m_style = p_style.matcher(htmlStr);      
            htmlStr = m_style.replaceAll(""); //过滤style标签      
            
            p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);      
            m_html = p_html.matcher(htmlStr);      
            htmlStr = m_html.replaceAll(""); //过滤html标签      
                
            p_html1 = Pattern.compile(regEx_html1,Pattern.CASE_INSENSITIVE);      
            m_html1 = p_html1.matcher(htmlStr);      
            htmlStr = m_html1.replaceAll(""); //过滤html标签      
  
            textStr = htmlStr;      
        }catch(Exception e) {      
              System.err.println("Html2Text: " + e.getMessage());      
        }      
        return textStr;//返回文本字符串      
     }     

	
	
	
}
