package com.wf.bean;

public class DatabaseUseDaily {

	private String user_id;
	private String institution_name;
	private Integer url_type;
	private Integer operate_type;
	private String database_name;
	private String date;
	private String numbers;
	private String product_source_code;
	private String source_db;
	private String sum;
	private String sum1;// 检索数
	private String sum2;// 浏览数
	private String sum3;// 下载数

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Integer getUrl_type() {
		return url_type;
	}

	public void setUrl_type(Integer url_type) {
		this.url_type = url_type;
	}

	public String getInstitution_name() {
		return institution_name;
	}

	public void setInstitution_name(String institution_name) {
		this.institution_name = institution_name;
	}

	public String getDatabase_name() {
		return database_name;
	}

	public void setDatabase_name(String database_name) {
		this.database_name = database_name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getSum1() {
		return sum1;
	}

	public void setSum1(String sum1) {
		this.sum1 = sum1;
	}

	public String getSum2() {
		return sum2;
	}

	public void setSum2(String sum2) {
		this.sum2 = sum2;
	}

	public String getSum3() {
		return sum3;
	}

	public void setSum3(String sum3) {
		this.sum3 = sum3;
	}

	public Integer getOperate_type() {
		return operate_type;
	}

	public void setOperate_type(Integer operate_type) {
		this.operate_type = operate_type;
	}

	public String getProduct_source_code() {
		return product_source_code;
	}

	public void setProduct_source_code(String product_source_code) {
		this.product_source_code = product_source_code;
	}

	public String getSource_db() {
		return source_db;
	}

	public void setSource_db(String source_db) {
		this.source_db = source_db;
	}

}