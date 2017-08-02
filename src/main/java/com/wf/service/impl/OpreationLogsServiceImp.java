package com.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.OperationLogs;
import com.wf.bean.PageList;
import com.wf.dao.OperationLogsMapper;
import com.wf.service.OpreationLogsService;

@Service
public class OpreationLogsServiceImp implements OpreationLogsService{
	
	@Autowired
	OperationLogsMapper operation;
	
	@Override
	public boolean addOperationLogs(OperationLogs op) {
		return operation.addOperationLogs(op);
	}

	@Override
	public PageList selectOperationLogs(Map<String, Object> map){
		PageList pageList = new PageList();
		List<Object> list=operation.selectOperationLogs(map);
		pageList.setPageRow(list);
		int total=operation.selectOperationLogsNum(map);
		pageList.setTotalRow(total);
		pageList.setPageNum((int)map.get("pageNum"));
		pageList.setPageSize((int)map.get("pageSize"));
		return pageList;
	}

}
