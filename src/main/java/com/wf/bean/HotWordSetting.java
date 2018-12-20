package com.wf.bean;

public class HotWordSetting {
	
	private Integer id;
	private Integer time_slot;
	private Integer get_cyc;
	private Integer publish_cyc;
	private String publish_strategy;
	private String publish_date;
	private String get_time;
	private String operation;
	private String operation_date;
	private Integer status;
	private String first_publish_time;
	private String next_publish_time;
	private String next_get_time;
	private String now_publish_time_space;
	private String now_get_time_space;
	private String next_publish_time_space;
	private String is_first;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPublish_cyc() {
		return publish_cyc;
	}

	public void setPublish_cyc(Integer publish_cyc) {
		this.publish_cyc = publish_cyc;
	}
	
	public Integer getGet_cyc() {
		return get_cyc;
	}

	public void setGet_cyc(Integer get_cyc) {
		this.get_cyc = get_cyc;
	}

	public Integer getTime_slot() {
		return time_slot;
	}

	public void setTime_slot(Integer time_slot) {
		this.time_slot = time_slot;
	}

	public String getPublish_strategy() {
		return publish_strategy;
	}

	public void setPublish_strategy(String publish_strategy) {
		this.publish_strategy = publish_strategy;
	}

	public String getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperation_date() {
		return operation_date;
	}

	public void setOperation_date(String operation_date) {
		this.operation_date = operation_date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFirst_publish_time() {
		return first_publish_time;
	}

	public void setFirst_publish_time(String first_publish_time) {
		this.first_publish_time = first_publish_time;
	}

	
	public String getNext_get_time() {
		return next_get_time;
	}

	public void setNext_get_time(String next_get_time) {
		this.next_get_time = next_get_time;
	}

	public String getGet_time() {
		return get_time;
	}

	public void setGet_time(String get_time) {
		this.get_time = get_time;
	}

	public String getNext_publish_time() {
		return next_publish_time;
	}

	public void setNext_publish_time(String next_publish_time) {
		this.next_publish_time = next_publish_time;
	}

	public String getNow_get_time_space() {
		return now_get_time_space;
	}

	
	public String getNow_publish_time_space() {
		return now_publish_time_space;
	}

	public void setNow_publish_time_space(String now_publish_time_space) {
		this.now_publish_time_space = now_publish_time_space;
	}

	public void setNow_get_time_space(String now_get_time_space) {
		this.now_get_time_space = now_get_time_space;
	}

	public String getNext_publish_time_space() {
		return next_publish_time_space;
	}

	public void setNext_publish_time_space(String next_publish_time_space) {
		this.next_publish_time_space = next_publish_time_space;
	}

	public String getIs_first() {
		return is_first;
	}

	public void setIs_first(String is_first) {
		this.is_first = is_first;
	}

}