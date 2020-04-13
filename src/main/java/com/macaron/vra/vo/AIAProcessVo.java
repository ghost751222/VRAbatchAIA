package com.macaron.vra.vo;

import java.util.List;

import com.macaron.vra.entity.ADSPolicyInfo;
import com.macaron.vra.entity.ADSVoiceDetail;

public class AIAProcessVo {

	private ADSPolicyInfo adsPolicyInfo;	
	private List<ADSVoiceDetail> adsVoiceDetails;
	
	
	public List<ADSVoiceDetail> getAdsVoiceDetails() {
		return adsVoiceDetails;
	}

	public void setAdsVoiceDetails(List<ADSVoiceDetail> adsVoiceDetails) {
		this.adsVoiceDetails = adsVoiceDetails;
	}

	private String TMRPendingCodeName;
	private String TMRPendingCode1Name;
	private String TMRPendingCode2Name;
	private String SCFPendingCodeName;
	private String SCFPendingCode1Name;
	private String SCFPendingCode2Name;

	private String ProcessCode1Name;
	private String ProcessCode2Name;

	private String SCFRejectRemedyPendingCode1Name;
	private String SCFRejectRemedyPendingCode2Name;

	private String SCFAgentName;
	private String TMRSellerName;
	private String isDealDone;
	public String getTMRPendingCodeName() {
		return TMRPendingCodeName;
	}

	public String getProcessCode1Name() {
		return ProcessCode1Name;
	}

	public void setProcessCode1Name(String processCode1Name) {
		ProcessCode1Name = processCode1Name;
	}

	public String getProcessCode2Name() {
		return ProcessCode2Name;
	}

	public void setProcessCode2Name(String processCode2Name) {
		ProcessCode2Name = processCode2Name;
	}

	public String getSCFRejectRemedyPendingCode1Name() {
		return SCFRejectRemedyPendingCode1Name;
	}

	public void setSCFRejectRemedyPendingCode1Name(String sCFRejectRemedyPendingCode1Name) {
		SCFRejectRemedyPendingCode1Name = sCFRejectRemedyPendingCode1Name;
	}

	public String getSCFRejectRemedyPendingCode2Name() {
		return SCFRejectRemedyPendingCode2Name;
	}

	public void setSCFRejectRemedyPendingCode2Name(String sCFRejectRemedyPendingCode2Name) {
		SCFRejectRemedyPendingCode2Name = sCFRejectRemedyPendingCode2Name;
	}

	public void setTMRPendingCodeName(String tMRPendingCodeName) {
		TMRPendingCodeName = tMRPendingCodeName;
	}

	public String getTMRPendingCode1Name() {
		return TMRPendingCode1Name;
	}

	public void setTMRPendingCode1Name(String tMRPendingCode1Name) {
		TMRPendingCode1Name = tMRPendingCode1Name;
	}

	public String getTMRPendingCode2Name() {
		return TMRPendingCode2Name;
	}

	public void setTMRPendingCode2Name(String tMRPendingCode2Name) {
		TMRPendingCode2Name = tMRPendingCode2Name;
	}

	public String getSCFPendingCodeName() {
		return SCFPendingCodeName;
	}

	public void setSCFPendingCodeName(String sCFPendingCodeName) {
		SCFPendingCodeName = sCFPendingCodeName;
	}

	public String getSCFPendingCode1Name() {
		return SCFPendingCode1Name;
	}

	public void setSCFPendingCode1Name(String sCFPendingCode1Name) {
		SCFPendingCode1Name = sCFPendingCode1Name;
	}

	public String getSCFPendingCode2Name() {
		return SCFPendingCode2Name;
	}

	public void setSCFPendingCode2Name(String sCFPendingCode2Name) {
		SCFPendingCode2Name = sCFPendingCode2Name;
	}

	public ADSPolicyInfo getAdsPolicyInfo() {
		return adsPolicyInfo;
	}

	public void setAdsPolicyInfo(ADSPolicyInfo adsPolicyInfo) {
		this.adsPolicyInfo = adsPolicyInfo;
	}

	public String getSCFAgentName() {
		return SCFAgentName;
	}

	public void setSCFAgentName(String sCFAgentName) {
		SCFAgentName = sCFAgentName;
	}

	public String getTMRSellerName() {
		return TMRSellerName;
	}

	public void setTMRSellerName(String tMRSellerName) {
		TMRSellerName = tMRSellerName;
	}

	public String getIsDealDone() {
		return isDealDone;
	}

	public void setIsDealDone(String isDealDone) {
		this.isDealDone = isDealDone;
	}
}
