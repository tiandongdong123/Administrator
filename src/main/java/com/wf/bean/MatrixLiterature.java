package com.wf.bean;
/**
 * 资源提供商的 母体文献
 */
public class MatrixLiterature {
	private String id;
	private String title;  //篇名
	private Integer providerId; //提供商
	private Integer proResourceId; //资源类型  
	private Integer psubjectId; //学科
	private String nameen; //英文名称
	private String author; //作者
	private String abstracts;//摘要
	private String datePeriod; //年/期
	private String cover; //封面
	
	public Integer getProResourceId() {
		return proResourceId;
	}
	public void setProResourceId(Integer proResourceId) {
		this.proResourceId = proResourceId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getProviderId() {
		return providerId;
	}
	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
	public Integer getPsubjectId() {
		return psubjectId;
	}
	public void setPsubjectId(Integer psubjectId) {
		this.psubjectId = psubjectId;
	}
	public String getNameen() {
		return nameen;
	}
	public void setNameen(String nameen) {
		this.nameen = nameen;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
	public String getDatePeriod() {
		return datePeriod;
	}
	public void setDatePeriod(String datePeriod) {
		this.datePeriod = datePeriod;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	@Override
	public String toString() {
		return "MatrixLiterature [id=" + id + ", title=" + title
				+ ", providerId=" + providerId + ", proResourceId="
				+ proResourceId + ", psubjectId=" + psubjectId + ", nameen="
				+ nameen + ", author=" + author + ", abstracts=" + abstracts
				+ ", datePeriod=" + datePeriod + ", cover=" + cover + "]";
	}
	
}
