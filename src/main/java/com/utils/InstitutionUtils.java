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
		//调用用户权限
		StringBuffer type=new StringBuffer("");
		if ("trical".equals(openLimit)) {
			type.append(type.length() > 0 ? "," : "").append("'trical'");
		}
		if ("openApp".equals(openLimit)) {
			type.append(type.length() > 0 ? "," : "").append("'openApp'");
		}
		if ("openWeChat".equals(openLimit)) {
			type.append(type.length() > 0 ? "," : "").append("'openWeChat'");
		}
		if ("PartyAdminTime".equals(openLimit)) {
			type.append(type.length() > 0 ? "," : "").append("'PartyAdminTime'");
		}
		if(type.length()>0){
			map.put("accounttype", type.toString());
		}
		StringBuffer rekey=new StringBuffer("");
		if(!StringUtils.isBlank(Organization)){
			rekey.append(rekey.length() > 0 ? "," : "").append("'"+Organization+"'");
		}
		if(!StringUtils.isBlank(PostCode)){
			rekey.append(rekey.length() > 0 ? "," : "").append("'"+PostCode+"'");
		}
		if(!StringUtils.isBlank(OrderType)){
			if(!StringUtils.isBlank(OrderContent)&&"inner".equals(OrderType)){
				rekey.append(rekey.length() > 0 ? "," : "").append("'"+OrderContent.trim()+"'");
			}else{
				rekey.append(rekey.length() > 0 ? "," : "").append("'"+OrderType+"'");
			}
		}
		if(rekey.length()>0){
			map.put("relatedidKey", rekey.toString());
		}
		if("binding".equals(openLimit)){//个人机构绑定的暂时没做
			
		}
		map.put("openLimit", openLimit);
		map.put("Organization", Organization);
		map.put("PostCode", PostCode);
		map.put("OrderType", OrderType);
		map.put("proType", proType);
		map.put("resource", resource);
		map.put("OrderContent", OrderContent.trim());
		map.put("pageNum", (Integer.parseInt(pageNum==null?"1":pageNum)-1)*Integer.parseInt((pageSize==null?"1":pageSize)));
		map.put("pageSize", Integer.parseInt(pageSize==null?"20":pageSize));
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(ipSegment)&& StringUtils.isEmpty(institution)&&StringUtils.isEmpty(openLimit)
				&&StringUtils.isEmpty(Organization)&&StringUtils.isEmpty(PostCode)&&StringUtils.isEmpty(OrderType)&&StringUtils.isEmpty(proType)) {
			flag=false;
		}
		return flag;
	}
}
