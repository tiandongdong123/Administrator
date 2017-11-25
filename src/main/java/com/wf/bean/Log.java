package com.wf.bean;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.utils.GetIP;

public class Log {

	private Integer id;
	private String username;
	private String behavior;
	private String url;
	private String time;
	private String ip;
	private String module;
	private String operation_content;
	private String operation_content_json;

	public Log(){}
	
	
	/**
	 * 
	 * @param behavior 操作类型
	 * @param module 操作模块
	 * @param operation_content_json 机构用户操作日志记录内容
	 * @param request 通过request获取操作人以及当前客户端IP
	 */
	public Log(String module,String behavior,HttpServletRequest request,String operation_content_json) {
		super();
		this.username = CookieUtil.getWfadmin(request).getUser_realname();
		this.behavior = behavior;
		this.url = request.getRequestURL().toString();
		this.time = DateTools.getSysTime();
		this.ip =GetIP.getIP();
		this.module = module;
		this.operation_content_json = operation_content_json;
	}
	
	
	/**
	 * 
	 * @param behavior 操作类型
	 * @param module 操作模块
	 * @param operation_content 操作内容
	 * @param request 通过request获取操作人以及当前客户端IP
	 */
	public Log(String module,String behavior,String operation_content,HttpServletRequest request) {
		super();
		this.username = CookieUtil.getWfadmin(request).getUser_realname();
		this.behavior = behavior;
		this.url = request.getRequestURL().toString();
		this.time = DateTools.getSysTime();
		this.ip =GetIP.getIP();
		this.module = module;
		this.operation_content = operation_content;
	}
	
	public Log(Integer id, String username, String behavior, String url,
			String time, String ip, String module, String operation_content) {
		super();
		this.id = id;
		this.username = username;
		this.behavior = behavior;
		this.url = url;
		this.time = time;
		this.ip = ip;
		this.module = module;
		this.operation_content = operation_content;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBehavior() {
		return behavior;
	}

	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	public String getOperation_content() {
		return operation_content;
	}

	public void setOperation_content(String operation_content) {
		this.operation_content = operation_content;
	}
	
	public String getOperation_content_json() {
		return operation_content_json;
	}

	public void setOperation_content_json(String operation_content_json) {
		this.operation_content_json = operation_content_json;
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", username=" + username + ", behavior="
				+ behavior + ", url=" + url + ", time=" + time + ", ip=" + ip
				+ ", module=" + module + ", operation_content="
				+ operation_content + ", operation_content_json="
				+ operation_content_json + "]";
	}
	
	

}
