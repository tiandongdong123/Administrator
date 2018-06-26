package com.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.citrus.util.StringUtil;
import com.wf.bean.Query;

public class InstitutionUtils {

	/**
	 * 校验机构用户查询条件
	 * @param map
	 * @param query
	 * @return
	 */
	public static boolean validateQuery(Map<String, Object> map,Query query){
		boolean flag=true;
		String userId=query.getUserId();
		String ipSegment=query.getIpSegment();
		String institution=query.getInstitution();
		String openLimit=query.getOpenLimit();
		String Organization=query.getOrganization();
		String PostCode=query.getPostCode();
		String OrderType=query.getOrderType();
		String proType=query.getProType();
		String resource=query.getResource();
		String pageNum=query.getPageNum();
		String OrderContent=query.getOrderContent();
		String pageSize=query.getPageSize();
		
		map.put("userId",userId);
		if (!StringUtil.isEmpty(ipSegment)) {
			if(IPConvertHelper.validateOneIp(ipSegment)){
				map.put("ipSegment", IPConvertHelper.IPToNumber(ipSegment));
			}else{
				map.put("ipSegment", ipSegment);
				flag=false;
			}
		}
		if (!StringUtils.isEmpty(institution)) {
			map.put("institution", institution.replace("_", "\\_"));
		}
		//调用用户权限 (个人绑定未加入)
		if (AuthorityLimit.TRICAL.getName().equals(openLimit)) {
			map.put("mapping", openLimit);
		}else if (AuthorityLimit.OPENAPP.getName().equals(openLimit)) {
			map.put("mapping", openLimit);
		}else if (AuthorityLimit.OPENWECHAT.getName().equals(openLimit)) {
			map.put("mapping", openLimit);
		}else if (AuthorityLimit.PARTYADMINTIME.getName().equals(openLimit)) {
			map.put("mapping", openLimit);
		}else if(AuthorityLimit.PID.getName().equals(openLimit)){
			map.put("admin", openLimit);
		}else if(AuthorityLimit.STATISTICS.getName().equals(openLimit)){
			map.put("tongji", openLimit);
		}else if(AuthorityLimit.SUBACCOUNT.getName().equals(openLimit)){
			map.put("Subaccount", openLimit);
		}
		map.put("openLimit", openLimit);
		if (!StringUtils.isEmpty(Organization) || !StringUtils.isEmpty(PostCode)
				|| !StringUtils.isEmpty(OrderType)) {
			map.put("ginfo", "0");
		}
		map.put("Organization", Organization);
		map.put("PostCode", PostCode);
		map.put("OrderType", OrderType);
		map.put("OrderContent", OrderContent==null?"":OrderContent.trim());
		
		map.put("proType", proType);
		map.put("resource", resource);
		map.put("pageNum", (Integer.parseInt(pageNum==null?"1":pageNum)-1)*Integer.parseInt((pageSize==null?"1":pageSize)));
		map.put("pageSize", Integer.parseInt(pageSize==null?"20":pageSize));
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(ipSegment)&& StringUtils.isEmpty(institution)&&StringUtils.isEmpty(openLimit)
				&&StringUtils.isEmpty(Organization)&&StringUtils.isEmpty(PostCode)&&StringUtils.isEmpty(OrderType)&&StringUtils.isEmpty(proType)) {
			flag=false;
		}
		return flag;
	}
}
