package com.utils;
/**
 * 机构类型
 * @author oygy
 *
 */
public enum Organization {

	COLLEGE("COLLEGE","本科院校"),
	VOCATIONALEDUCATION("VOCATIONALEDUCATION","职教类院校"),
	PUBLICLIBRARY("PUBLICLIBRARY","公共图书馆"),
	INFORMATIONINSTITUTE("INFORMATIONINSTITUTE","情报所"), 
	INSTITUTION("INSTITUTION","科研院所"),
	GOVERNMENT("GOVERNMENT","政府机构"),
	HOSPITAL("HOSPITAL","医院"),
	ENTERPRISE("ENTERPRISE","企业");

	private String name;//名字
	private String desc;// 中文描述

	/**
	 * 私有构造,防止被外部调用
	 * 
	 * @param desc
	 */
	private Organization(String name,String desc) {
		this.name=name;
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
