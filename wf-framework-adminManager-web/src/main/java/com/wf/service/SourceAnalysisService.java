package com.wf.service;

import java.util.Map;


public interface SourceAnalysisService {

	/**
	* @Title: getDatabaseAnalysisList
	* @Description: TODO(来源分析列表数据展示数据)
	* @param flag 标记来源类型 
	* @param date 当以小时为统计单位时，值为所选日期，即年-月-日，否则为空值
	* @param startTime 当以小时为统计单位时，值为所选开始时数，如8时，否则为开始日期，如年-月-日 
	* @param endTime 当以小时为统计单位时，值为所选结束时数，如8时，否则为结束日期，如年-月-日
	* @param pageNum
	* @param pageSize
	* @return Map<String, Object> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 5:27:07 PM
	 */
	Map<String , Object> SourceAnalysisList(String flag, String date, String startTime,
			String endTime, Integer pageNum, Integer pageSize);

	/**
	* @Title: SourceAnalysisStatistics
	* @Description: TODO(来源分析图表展示数据) 
	* @param flag 标记来源类型
	* @param type 分析指标类型
	* @param date 当以小时为统计单位时，值为所选日期，即年-月-日，否则为空值
	* @param startTime 当以小时为统计单位时，值为所选开始时数，如8时，否则为开始日期，如年-月-日 
	* @param endTime 当以小时为统计单位时，值为所选结束时数，如8时，否则为结束日期，如年-月-日
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 5:33:15 PM
	 */
	Map<String, Object> SourceAnalysisStatistics(
			String flag,String type, String date, String startTime, String endTime);
	
	
	
}
