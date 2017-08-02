package com.wf.service;

import java.util.HashMap;
import java.util.List;

import com.wf.bean.PageList;

public interface MatrixLiteratureService {

	PageList getMatrixLiterature(Integer providerId,Integer psubjectId,Integer proResourceId,Integer pagenum,Integer pagesize,String startDate,String endDate,List<String> unEqualId);
	/**
	 * 
	 * @param providerId 提供商
	 * @param psubjectId 学科分类
	 * @param pagenum  当前页
	 * @param pagesize 当前页显示行数
	 * @return
	 */
	List<HashMap<String, Object>> getMatrixLiteratureList(Integer providerId,Integer psubjectId, Integer proResourceId,Integer pagenum, Integer pagesize,String startDate,String endDate,List<String> unEqualId);

	//增加母体文献
	int addMatrixLiterature(Integer providerId,Integer psubjectId,Integer proResourceId,String title,String nameen,String author,String abstracts,String datePeriod,String cover);
	//修改母体文献
	boolean updateMatrixLiterature(Integer providerId,Integer psubjectId,Integer proResourceId,String title,String nameen,String author,String abstracts,String datePeriod,String cover,String id);
	//删除母体文献
	boolean deleteMatrixLiterature(String id);
	//查询单个母体文献
	HashMap<String,Object> getMatrixLiteratureById(String id);

}
