package com.wf.service;

import java.util.List;

import com.qq.connect.api.qzone.Share;
import com.wf.bean.PageList;
import com.wf.bean.ShareTemplate;

public interface ShareTemplateService {
	/**
	 * 分享模板
	 * @param shareTemplate
	 * @return
	 */
	Boolean addShareTemplate(ShareTemplate shareTemplate);
	/**
	 * 查询所有
	 * @return
	 */
	List<ShareTemplate> selectAll();
	/**
	 * 分页查询
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PageList getShareTemplate(int pageNum,int pageSize,String shareType);
	/**
	 * ID查询
	 * @param id
	 * @return
	 */
	ShareTemplate findShareTemplate(String id);
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	Boolean deleteShareTemplate(String ids);
	/**
	 * 更新
	 * @param resourceType
	 * @return
	 */
	Boolean updateShareTemplate(ShareTemplate shareTemplate);
	
	ShareTemplate checkShareTemplate(ShareTemplate shareTemplate,String addOrUpdate);
	
	/**
	 * 导出
	 * @return
	 */
	List<Object> exportshareTemplate(String shareType);

}
