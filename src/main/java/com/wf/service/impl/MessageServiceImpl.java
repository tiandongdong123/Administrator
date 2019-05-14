package com.wf.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wanfangdata.setting.Setting;
import com.wf.bean.JudgeMessageTitleParameter;
import com.wf.bean.MessageSearchRequest;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.ecs.html.S;
import org.apache.log4j.Logger;
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
	private String UNSPECIAL = "<li><a href=\"/informationController/getDetails.do?type=${type}&amp;" +
			"id=${id}\" target=\"_blank\" title=\"${title}\">\n" +
			" ${showTitle}</a>";
	private String SPECIAL = "<li><a href=\"${linkAddress}\" target=\"_blank\" title=\"${title}\">\n" +
			" ${showTitle}</a>";
	private String IMG = "<img style=\"margin-left: 10px;\" src=\"/page/images/new.gif\">";
	private String LI = "</li>";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger log = Logger.getLogger(MessageServiceImpl.class);
	private static final int SHOWIMAGE = 4;

	@Autowired
	MessageMapper dao;

	private String hosts=XxlConfClient.get("wf-public.solr.url", null);

	@Override
	public PageList getMessage(MessageSearchRequest request) {
		PageList p=new PageList();
        if (request.getIssueState() != null && request.getIssueState().equals("4")) {
            request.setIssueState("2");
            request.setTopState("1");
        }
		List<Object> pageRow = dao.getMessageList(request);
		int num = dao.getMessageCount(request);
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

    /**
     * 修改资讯信息
     * @param message 插入的实体类
     * @return
     */
	@Override
	public Boolean updateMessage(Message message) {
		int n =dao.updateMessage(message);
		boolean b=n>0?true:false;
		return b;
	}

    /**
     * 置顶、取消置顶
     * @param message
     * @return
     */
	@Override
	public Boolean updataMessageStick(Message message) {
		boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", message.getId());
		map.put("stick", new Date());
		map.put("isTop", message.getIsTop());
		map.put("human", message.getHuman());
		map.put("branch", message.getBranch());
		int num = dao.updateIssue(map);
		if (num > 0) {
			flag = true;
            if (message.getIsTop() != null) {
                setData(message.getColums(), 2, message.getId());
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
	public boolean updateIssue(String id, String colums, String issueState, String human,String branch) {
		boolean flag = false;
		int issue = Integer.valueOf(issueState);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("issueState", issue);
		map.put("stick", new Date());
		map.put("isTop", "0");
		map.put("human", human);
		map.put("branch",branch);
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
		}else if("会议速递".equals(colums) || "基金申报".equals(colums)){
			type="fund";
		}else if("万方资讯".equals(colums)){
			type="activity";
		}
		//修改/下撤成功发布至solr
		if(issue==2){//发布
			Message message=dao.findMessage(id);
			deployInformation("information",type,message);
		}else if(issue==3){//下撤
			String collection = RedisUtil.get("information", 3);
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
        //如果栏目为会议速递或基金申报
        if ("会议速递".equals(colums) || "基金申报".equals(colums)){
            topMap.put("colum1", "会议速递");
            topMap.put("colum2", "基金申报");
            list = dao.selectIsTop2(topMap);
        }else {
            list = dao.selectIsTop(topMap);//根据栏目获取发布置顶状态资讯信息3个
        }
        int topSize=list.size();//获取到的符合条件的资讯个数
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("colums", colums);
        if ("会议速递".equals(colums) || "基金申报".equals(colums)) {
            map.put("colum1", "会议速递");
            map.put("colum2", "基金申报");
        }
        map.put("size", 3);
        if("专题聚焦".equals(colums)){
            //清空redis中对应的key
        	RedisUtil.del("ztID");
        	RedisUtil.del("special");
            if(topSize<3){
                map.put("size", 10-topSize);
                List<Object> ls = dao.selectBycolums(map);//获取发布未置顶状态资讯
                list.addAll(ls);//合并到发布置顶状态集合中
            }
			StringBuffer specials = new StringBuffer();
            for(int i = 0;i < list.size();i++){
                Message m = (Message) list.get(i);
                m.setContent("");
                String object = JSONObject.fromObject(m).toString();
                RedisUtil.zadd("ztID", i, m.getId());//发布到redis  根据i大小进行排列，i越大存储的m.getId()越在后面
                RedisUtil.hset("special", m.getId(), object);//special中添加m.getId(), object键值对
				// 存储资讯的id、title并且只获得3个用作展示
                if (i < 3) {
                    if (m.getLinkAddress() != null) {
                        specials.append(SPECIAL.replace("${linkAddress}", m.getLinkAddress())
                                .replace("${title}", m.getTitle())
                                .replace("${showTitle}", m.getTitle()));
                        try {
                            if ((new Date().getTime() - sdf.parse(m.getCreateTime()).getTime()) / (1000 * 3600 * 24) < SHOWIMAGE) {
                                specials.append(IMG);
                            }
                        } catch (ParseException e) {
                            log.error("更改资讯信息判断日期出错！出错资讯id：" + m.getId(), e);
                        }
                        specials.append(LI);
                    }
                }
            }
            // 专题聚焦存储redis后修改zk用于智搜首页快看展示
			try {
				if (specials.length() > 0) {
					Setting.set("Home/Subject", specials.toString());
				}
			} catch (IOException e) {
			}

		}else if("科技动态".equals(colums)){
        	RedisUtil.del("hyID");
        	RedisUtil.del("conference");
            if(topSize<3){
                map.put("size", 3-topSize);
                List<Object> ls = dao.selectBycolums(map);
                list.addAll(ls);
            }
			StringBuffer conferences = new StringBuffer();
            for(int i = 0;i < list.size();i++){
                Message m = (Message) list.get(i);
                m.setContent("");
                String object = JSONObject.fromObject(m).toString();
                RedisUtil.zadd("hyID", i, m.getId());
                RedisUtil.hset("conference", m.getId(), object);
				if (i < 3) {
					if (m.getLinkAddress() != null) {
						conferences.append(UNSPECIAL.replace("${type}", "conference")
								.replace("${id}",m.getId())
								.replace("${title}", m.getTitle())
								.replace("${showTitle}", m.getTitle()));
                        try {
                            if ((new Date().getTime() - sdf.parse(m.getCreateTime()).getTime()) / (1000 * 3600 * 24) < SHOWIMAGE) {
                                conferences.append(IMG);
                            }
                        } catch (ParseException e) {
                            log.error("更改资讯信息判断日期出错！出错资讯id：" + m.getId(), e);
                        }
                        conferences.append(LI);
					}
				}
            }
			// 科技动态存储redis后修改zk用于智搜首页快看展示
			try {
				if (conferences.length() > 0) {
					Setting.set("Home/TechNews", conferences.toString());
				}
			} catch (IOException e) {
			}
        }else if("会议速递".equals(colums) || "基金申报".equals(colums)){
        	RedisUtil.del("jjID");
            RedisUtil.del("fund");
            if(topSize<3){
                map.put("size", 3-topSize);
                List<Object> ls = dao.selectBycolums2(map);
                list.addAll(ls);
            }
			StringBuffer funds = new StringBuffer();
            String fund = UNSPECIAL;
            for(int i = 0;i < list.size();i++){
                Message m = (Message) list.get(i);
                m.setContent("");
                String object = JSONObject.fromObject(m).toString();
                RedisUtil.zadd("jjID", i, m.getId());
                RedisUtil.hset("fund", m.getId(), object);
				if (i < 3) {
					if (m.getLinkAddress() != null) {
						funds.append(fund.replace("${type}", "fund")
								.replace("${id}",m.getId())
								.replace("${title}", m.getTitle())
								.replace("${showTitle}", m.getTitle()));
                        try {
                            if ((new Date().getTime() - sdf.parse(m.getCreateTime()).getTime()) / (1000 * 3600 * 24) < SHOWIMAGE) {
                                funds.append(IMG);
                            }
                        } catch (ParseException e) {
                            log.error("更改资讯信息判断日期出错！出错资讯id：" + m.getId(), e);
                        }
                        funds.append(LI);
					}
				}
            }
			// 会议速递存储redis后修改zk用于智搜首页快看展示
			try {
				if (funds.length() > 0) {
					Setting.set("Home/Fund", funds.toString());
				}
			} catch (IOException e) {
			}
        }else if("万方资讯".equals(colums)){
        	RedisUtil.del("kkID");
        	RedisUtil.del("activity");
            if(topSize<3){
                map.put("size", 3-topSize);
                List<Object> ls = dao.selectBycolums(map);
                list.addAll(ls);
            }
			StringBuffer activities = new StringBuffer();
            for(int i = 0;i < list.size();i++){
                Message m = (Message) list.get(i);
                m.setContent("");
                String object = JSONObject.fromObject(m).toString();
                RedisUtil.zadd("kkID", i, m.getId());
                RedisUtil.hset("activity", m.getId(), object);
				if (i < 3) {
					if (m.getLinkAddress() != null) {
						activities.append(UNSPECIAL.replace("${type}", "activity")
								.replace("${id}",m.getId())
								.replace("${title}", m.getTitle())
								.replace("${showTitle}", m.getTitle()));
                        try {
                            if ((new Date().getTime() - sdf.parse(m.getCreateTime()).getTime()) / (1000 * 3600 * 24) < SHOWIMAGE) {
                                activities.append(IMG);
                            }
                        } catch (ParseException e) {
                            log.error("更改资讯信息判断日期出错！出错资讯id：" + m.getId(), e);
                        }
                        activities.append(LI);
					}
				}
            }
			// 万方资讯存储redis后修改zk用于智搜首页快看展示
			try {
				if (activities.length() > 0) {
					Setting.set("Home/News", activities.toString());
				}
			} catch (IOException e) {
                log.error("更改Home/News出错！", e);
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
		String channel = message.getChannel();
		String label = message.getLabel();

		newMap.put("id", id);
		newMap.put("type", type);
		newMap.put("stringIS_channel", channel);
		newMap.put("stringIS_label", label);
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
		String collection = RedisUtil.get(core, 3);
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
		String collection = RedisUtil.get("information", 3);
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

	@Override
	public Boolean judgeMessageTitle(JudgeMessageTitleParameter parameter) {
		if (dao.judgeMessageTitle(parameter)>0){
			return false;
		}return true;
	}

}
