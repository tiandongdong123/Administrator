package com.wf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		List<Object> list = persondao.findListInfoSimp(map);
		pageList.setPageNum(pagenum);
		pageList.setPageRow(list);
		pageList.setPageSize(pagesize);
		pageList.setTotalRow(persondao.findListCountSimp(map));
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
