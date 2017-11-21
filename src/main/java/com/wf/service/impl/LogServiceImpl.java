package com.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.dao.LogMapper;
import com.wf.service.LogService;

@Service
public class LogServiceImpl implements LogService{

	@Autowired
	private LogMapper logMapper;
	
	@Override
	public PageList getLog(String username,
			String ip,String module,String behavior, String startTime, String endTime,
			Integer pageNum) {
		PageList list=new PageList();
		
		int currpage=(pageNum-1)*10;
		List<Object> pageRow=logMapper.getLog("%"+username+"%",ip,module,behavior,startTime,endTime,currpage,10);
		List<Object> pageTotal=logMapper.getLogCount("%"+username+"%",ip,module,behavior,startTime,endTime);
		list.setPageRow(pageRow);
		list.setPageTotal(pageTotal.size());
		list.setPageNum(pageNum);
		list.setPageSize(10);
		return list;
	}

	@Override
	public List<Object> exportLog(String username, String ip, String module,String behavior,
			String startTime, String endTime) {
		
		//分页时需要的查询总条数方法  导出时也可以用
		return logMapper.getLogCount(username, ip, module,behavior, startTime, endTime);
	}

	@Override
	public Integer deleteLogByID(Integer[]ids) {
		return logMapper.deleteLogByID(ids);
	}

	@Override
	public List<String> getAllLogModel() {
		return  logMapper.getAllLogModel();
	}

	@Override
	public List<String> getResTypeByModel(String modelname) {
		return logMapper.getResTypeByModel(modelname);
	}

	@Override
	public Integer addLog(Log log) {
		return logMapper.addLog(log);
	}

}





