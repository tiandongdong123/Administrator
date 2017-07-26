package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.MatrixLiterature;


public interface MatrixLiteratureMapper {
	
	List<Object> getMatrixLiterature(@Param("providerId")Integer providerId,@Param("psubjectId")Integer psubjectId,@Param("proResourceId")Integer proResourceId,@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("unEqualIds") List<String> unEqualIds);

	int getMatrixLiteratureNum(@Param("providerId")Integer providerId,@Param("psubjectId")Integer psubjectId,@Param("proResourceId")Integer proResourceId,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("unEqualIds") List<String> unEqualIds);
	//新增母体文献
	int addMatrixLiterature(@Param("id")String id,@Param("providerId")Integer providerId,@Param("psubjectId")Integer psubjectId,@Param("proResourceId")Integer proResourceId,@Param("title")String title,@Param("nameen")String nameen,@Param("author")String author,@Param("abstracts")String abstracts,@Param("datePeriod")String datePeriod,@Param("cover")String cover);
	//修改母体文献
	boolean updateMatrixLiterature(@Param("providerId")Integer providerId,@Param("psubjectId")Integer psubjectId,@Param("proResourceId")Integer proResourceId,@Param("title")String title,@Param("nameen")String nameen,@Param("author")String author,@Param("abstracts")String abstracts,@Param("datePeriod")String datePeriod,@Param("cover")String cover,@Param("id")String id);
	//删除母体文献
	boolean deleteMatrixLiterature(@Param("id")String id);
	//根据id查询母体
	MatrixLiterature getMatrixLiteratureById(@Param("id")String id);

}
