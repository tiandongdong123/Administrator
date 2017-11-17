package com.wf.bean;

public class ShareTemplateNamesFileds {
	private Integer id;
	private String field_zh;
	private String field_eng;
	private Integer share_template_names_id;
	private Boolean check;
	
	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getField_zh() {
		return field_zh;
	}

	public void setField_zh(String field_zh) {
		this.field_zh = field_zh;
	}

	public String getField_eng() {
		return field_eng;
	}

	public void setField_eng(String field_eng) {
		this.field_eng = field_eng;
	}

	public Integer getShare_template_names_id() {
		return share_template_names_id;
	}

	public void setShare_template_names_id(Integer share_template_names_id) {
		this.share_template_names_id = share_template_names_id;
	}

	@Override
	public String toString() {
		return "ShareTemplateNamesFileds [id=" + id + ", field_zh=" + field_zh
				+ ", field_eng=" + field_eng + ", share_template_names_id="
				+ share_template_names_id + ", check=" + check + "]";
	}

	
}
