package com.wf.dao;

import java.util.List;

import com.wf.bean.ShareTtemplateNames;

public interface ShareTemplateNamesMapper {
	
	/**
	 * 获取所有分享类型
	 * @return
	 */
	List<ShareTtemplateNames> getAllShareTemplateNames();
}
