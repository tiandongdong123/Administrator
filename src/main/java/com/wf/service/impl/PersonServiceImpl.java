package com.wf.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.dao.CardMapper;
import com.wf.dao.IdentifyScholarMapper;
import com.wf.dao.PersonAccountMapper;
import com.wf.dao.PersonMapper;
import com.wf.dao.SubjectMapper;
import com.wf.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	private PersonMapper persondao;
	@Autowired
	private SubjectMapper subjectdao;//学科分类
	@Autowired
	private PersonAccountMapper accountdao;//账户余额
	@Autowired
	private CardMapper carddao;//万方卡
	@Autowired
	private IdentifyScholarMapper identifyScholardao; //发文单位
	@Override
	public Map<String,Object> QueryPersion(Person person,Integer pagenum,Integer pagesize,String[] roles) {
		Map<String,Object> pl = new HashMap<String,Object>();
		int num = 0;
		pagesize = pagesize==null ? 10:pagesize;
		pagenum = pagenum==null ? 1:pagenum;
		int pm = (pagenum-1)*pagesize;
		//判断是否是模糊查询
		int idType = 1;//机构id
		int nameType = 1;//机构名称
		String userid = person.getUserId();
		String userRealname = person.getUserRealname();
		if(StringUtils.isNotBlank(userid)){
			if(userid.contains("%")){
				idType = 2;//模糊查询
				if(!userid.startsWith("%")){//判断是否是以%开头
					//不是以%开头  补全%
					userid = "%" + userid;
				}
				if(!userid.endsWith("%")){//判断是否是以%结尾
					//不是以%结尾  补全%
					userid = userid + "%";
				}
			}
		}
		if(StringUtils.isNotBlank(userRealname)){
			if(userRealname.contains("%")){
				nameType = 2;//模糊查询
				if(!userRealname.startsWith("%")){//判断是否是以%开头
					//不是以%开头  补全%
					userRealname = "%" + userRealname;
				}
				if(!userRealname.endsWith("%")){//判断是否是以%结尾
					//不是以%结尾  补全%
					userRealname = userRealname + "%";
				}
			}
		}
		person.setUserId(userid);
		person.setUserRealname(userRealname);
		try {
			List<Map<String,Object>>  p = this.persondao.QueryPerson(person,pm,pagesize,roles,idType,nameType);
			List<String> awardList=null;
			for(int i = 0; i<p.size();i++){
				Map<String,Object> map = p.get(i);
				String userId = (String) map.get("userId");
				//获取学历信息
				String educations = persondao.getEducations(userId);
				List<Map<String,Object>> eduMap = JSONArray.fromObject(educations);
				//获取学科信息
				List<String> subList = subjectdao.queryNameByNum(String.valueOf(map.get("subject")).replaceAll("%", ","));
				//获取账户余额
				BigDecimal balance = accountdao.queryBalance(userId);
				//获取万方卡
				Integer cardValue = carddao.queryCardValue(userId);
				//获取获奖情况
				
				if(null!=map.get("award")){
					awardList=Arrays.asList(map.get("award").toString().split("\n"));
				}
				//获取发文单位
				String publish_unit = identifyScholardao.getPublishunitByUserId(userId);
				map.put("educations", eduMap);
				map.put("subList", subList);
				map.put("balance", balance);
				map.put("cardValue", cardValue);
				map.put("publish_unit", publish_unit);
				map.put("awardList", awardList);
			}
			num = this.persondao.QueryPersonNum(person,roles,idType,nameType);
			pl.put("pageNum", pagenum);
			pl.put("pageRow", p);
			pl.put("pageSize", pagesize);
			pl.put("totalRow", num);
//			pl.setPageNum(pagenum);
//			pl.setPageRow(p);
//			pl.setPageSize(pagesize);
//			pl.setTotalRow(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pl;
	}
	@Override
	public Person findById(String userId) {
		Person ps=persondao.queryPersonInfo(userId);
		return ps;
	}
	/**
	 * 获取机构用户
	 */
	@Override
	public PageList QueryIns(String userId, String institution,
			Integer pagenum, Integer pagesize) {
		PageList pageList = new PageList();
		pagesize = pagesize==null ? 10:pagesize;
		pagenum = pagenum==null ? 1:pagenum;
		int pm = (pagenum-1)*pagesize;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("institution", institution);
		map.put("idType", 1);//1表示精确查询 2表示模糊查询（按id）
		map.put("nameType", 1);//1表示精确查询 2表示模糊查询（按机构名称）
		map.put("pageNum", pm);
		map.put("pageSize", pagesize);
		List<Object> list = persondao.findListInfo(map);
		pageList.setPageNum(pagenum);
		pageList.setPageRow(list);
		pageList.setPageSize(pagesize);
		pageList.setTotalRow(persondao.findListCount(map));
		return pageList;
	}
	
	@Override
	public List<Map<String, Object>> getAllInstitutional(String institution) {
		
		return persondao.getAllInstitutional(institution);
	}
	
	@Override
	public List<String> getAllInstitution(String institution) {
		return persondao.getAllInstitution(institution);
	}
	
	
}
