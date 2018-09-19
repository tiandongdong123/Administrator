package com.utils;

/**
 * 开通权限
 * 
 * @author oygy
 * 
 */
public enum AuthorityLimit {
	TRICAL("trical","试用项目"),
	PID("pid","机构管理员"),
	SUBACCOUNT("Subaccount","机构子账号"),
	//BINDING("BINDING","个人绑定机构"),
	STATISTICS("tongji","统计分析"),
	OPENAPP("openApp","APP嵌入服务"),
	OPENWECHAT("openWeChat","微信公众号嵌入服务"),
	PARTYADMINTIME("PartyAdminTime","党建管理员");

	private String name;// 名称
	private String desc;// 中文描述

	/**
	 * 私有构造,防止被外部调用
	 * 
	 * @param desc
	 */
	private AuthorityLimit(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	/**
	 * 定义方法,返回描述,跟常规类的定义没区别
	 * 
	 * @return
	 */
	public String getDesc() {
		return desc;
	}

	public String getName() {
		return name;
	}

}
