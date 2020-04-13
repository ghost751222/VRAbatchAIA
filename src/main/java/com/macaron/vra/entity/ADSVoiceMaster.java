package com.macaron.vra.entity;

import java.util.Date;

public class ADSVoiceMaster {
	private String batchNo;
	private Date createTime;
	private Date completeTime;
	private Integer processRec;
	private String processStatus;

	
	private Date STTStartTime;
	private Date STTCompleteTime;
	private String STTProcessStatus;
	

	
	
	public String getBatchNo() {
		return batchNo;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public Date getSTTStartTime() {
		return STTStartTime;
	}

	public void setSTTStartTime(Date sTTStartTime) {
		STTStartTime = sTTStartTime;
	}

	public Date getSTTCompleteTime() {
		return STTCompleteTime;
	}

	public void setSTTCompleteTime(Date sTTCompleteTime) {
		STTCompleteTime = sTTCompleteTime;
	}

	public String getSTTProcessStatus() {
		return STTProcessStatus;
	}

	public void setSTTProcessStatus(String sTTProcessStatus) {
		STTProcessStatus = sTTProcessStatus;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public Integer getProcessRec() {
		return processRec;
	}

	public void setProcessRec(Integer processRec) {
		this.processRec = processRec;
	}

}
