package com.macaron.vra.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.macaron.vra.entity.ADSExChangeInfo;

@Repository
public class ADSExChangeInfoDaoImpl {
	
	
	@Autowired
	@Qualifier("msSqlJdbc")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public int update(ADSExChangeInfo adsExChangeInfo) {
		  
		String sql ="update ADS_ExChangeInfo set STTResult = :STTResult,STTRemark=:STTRemark,STTResultUpdateTime=:STTResultUpdateTime, STTSCFAgent = :STTSCFAgent,STTSCFAgentUpdateTime=:STTSCFAgentUpdateTime,STTTypeResult=:STTTypeResult where AppId = :AppId";
		Map<String,Object> params =new HashMap<String,Object>();
		params.put("appId", adsExChangeInfo.getAppId());	
		params.put("sttResult", adsExChangeInfo.getSTTResult());
		params.put("sttRemark", adsExChangeInfo.getSTTRemark());
		params.put("STTResultUpdateTime", adsExChangeInfo.getSTTResultUpdateTime());
		params.put("STTSCFAgent", adsExChangeInfo.getSTTSCFAgent());	
		params.put("STTSCFAgentUpdateTime", adsExChangeInfo.getSTTSCFAgentUpdateTime());
		params.put("STTTypeResult", adsExChangeInfo.getSTTTypeResult());
		return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(adsExChangeInfo));
	}
	
	public List<ADSExChangeInfo> findByDateRange(Date startDate,Date endDate) {
		String sql ="select * from  ADS_ExChangeInfo where (ADSPendingCodeUpdateTime between :startDate and :endDate) or (ADSResultUpdateTime between :startDate and :endDate) or (ADSPolicyNoUpdateTime between :startDate and :endDate )";
		Map<String,Object> params =new HashMap<String,Object>();
		params.put("startDate", startDate);		
		params.put("endDate", endDate);			
		return jdbcTemplate.query(sql, 	params,new BeanPropertyRowMapper<ADSExChangeInfo>(ADSExChangeInfo.class));
	}
	
	
	public List<ADSExChangeInfo> findByAppId(String appId) {
		String sql ="select * from  ADS_ExChangeInfo where AppId =:appId";
		Map<String,Object> params =new HashMap<String,Object>();
		params.put("appId", appId);			
		return jdbcTemplate.query(sql, 	params,new BeanPropertyRowMapper<ADSExChangeInfo>(ADSExChangeInfo.class));
	}
	
	
}
