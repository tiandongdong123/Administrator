package com.wf.bean;

import java.util.Date;

public class Standard {
	private boolean isBK;//是否包库
	private boolean isZJ;//是否是元数据+全文
	private String UserId;//用户Id
	private String Username;//用户名称
	private String UserEnName;//用户英文名称
	private Date StartTime;//元数据+全文开始时间
	private Date EndTime;//元数据+全文结束时间
	private Date BK_StartTime;//包库开始时间
	private Date BK_EndTime;//包库结束时间
	private String OrgCode;//机构名称
	private String OrgName;//单位名称
	private String CompanySimp;//机构单位简写
	private String[] IPLimits;//元数据+全文ip限定
	private String[] BK_IPLimits;//包库ip限定
	private String Rdptauth;//版权阅读打印
	private int Onlines;//在线用户数
	private int Copys;//副本数
	private int Prints;
	private int Sigprint;//
	private String[] source_db;//数据库 WF、质检出版社
}