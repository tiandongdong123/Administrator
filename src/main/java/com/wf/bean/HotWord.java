package com.wf.bean;

public class HotWord {
	private Integer id;
	private String word;
	private Integer searchCount;
	private String wordNature;
	private String operationTime;
	private Integer wordStatus;
	private String dateTime;
	private String operation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Integer getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Integer searchCount) {
		this.searchCount = searchCount;
	}

	public String getWordNature() {
		return wordNature;
	}

	public void setWordNature(String wordNature) {
		this.wordNature = wordNature;
	}

	public String getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}

	public Integer getWordStatus() {
		return wordStatus;
	}

	public void setWordStatus(Integer wordStatus) {
		this.wordStatus = wordStatus;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}


}
