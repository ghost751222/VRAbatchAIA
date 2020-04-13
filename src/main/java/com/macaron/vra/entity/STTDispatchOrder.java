package com.macaron.vra.entity;

import java.util.Date;

public class STTDispatchOrder {
	private String applicationFormID;
	private String dataType;

	public String getApplicationFormID() {
		return applicationFormID;
	}

	public void setApplicationFormID(String applicationFormID) {
		this.applicationFormID = applicationFormID;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private Date createTime;
}
