package com.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PageList;
import com.wf.dao.LogMapper;
import com.wf.service.LogService;

@Service
public class LogServiceImpl implements LogService{

	@Autowired
	private LogMapper logMapper;
	
	@Override
	public PageList getLog(String username,
			String ip, String behavior, String startTime, String endTime,
			Integer pageNum) {
		PageList list=new PageList();
		
		int currpage=(pageNum-1)*2;
		List<Object> pageRow=logMapper.getLog("%"+username+"%",ip,behavior,startTime,endTime,currpage,2);
		List<Object> pageTotal=logMapper.getLogCount("%"+username+"%",ip,behavior,startTime,endTime);
		list.setPageRow(pageRow);
		list.setPageTotal(pageTotal.size());
		list.setPageNum(pageNum);
		list.setPageSize(2);
		return list;
	}

	@Override
	public List<Object> exportLog(String username, String ip, String behavior,
			String startTime, String endTime) {
		
		//分页时需要的查询总条数方法  导出时也可以用
		return logMapper.getLogCount(username, ip, behavior, startTime, endTime);
	}

	@Override
	public Integer deleteLogByID(Integer[]ids) {
		return logMapper.deleteLogByID(ids);
	}

}





