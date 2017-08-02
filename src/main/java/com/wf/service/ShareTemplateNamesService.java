package com.wf.service;

import java.util.List;

import com.wf.bean.ShareTtemplateNames;
import com.wf.dao.ShareTemplateNamesMapper;

public interface ShareTemplateNamesService {

	/**
	 * 获取所有分享类型
	 * 
	 * @return
	 */
	List<ShareTtemplateNames> getAllShareTemplateNames();
}
