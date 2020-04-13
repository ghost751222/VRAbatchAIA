package com.macaron.vra.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.macaron.vra.entity.ADSPolicyInfo;

@Repository
public class ADSPolicyInfoDaoImpl {


	@Autowired
	@Qualifier("msSqlJdbc")
	private NamedParameterJdbcTemplate jdbcTemplate;

	
	public ADSPolicyInfo queryPolicyInfoByAppId(String appId) {
		String sql = "select * from ADS_PolicyInfo where AppId =:AppId";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("AppId", appId);
		
		
		List<ADSPolicyInfo> vos = jdbcTemplate.query(sql.toString(),params,
				new BeanPropertyRowMapper<ADSPolicyInfo>(ADSPolicyInfo.class));
	
		//return vos.size() > 0 ? vos.get(0) : new ADSPolicyInfo();
		return vos.size() > 0 ? vos.get(0) : null;
		
	}

	public ADSPolicyInfo queryPolicyInfoByApplicationFormID(String applicationFormID) {
		String sql = "select * from ADS_PolicyInfo where ApplicationFormID =:applicationFormID";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("applicationFormID", applicationFormID);
		
		
		List<ADSPolicyInfo> vos = jdbcTemplate.query(sql.toString(),params,
				new BeanPropertyRowMapper<ADSPolicyInfo>(ADSPolicyInfo.class));
		
		return vos.size() > 0 ? vos.get(0) : new ADSPolicyInfo();
		
	}

}
