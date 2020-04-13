package com.macaron.vra.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.macaron.vra.entity.ADSVoiceDetail;

@Repository
public class ADSVoiceRelationVoImpl {


	@Autowired
	@Qualifier("msSqlJdbc")
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ADSVoiceDetail> queryByBatchNo(String batchNo) {
		String sql = "select * from ADS_VoiceDetail where batchNo = :batchNo order by batchNo,ContactID";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("batchNo", batchNo);
		
		List<ADSVoiceDetail> vos = jdbcTemplate.query(sql.toString(),params,
				new BeanPropertyRowMapper<ADSVoiceDetail>(ADSVoiceDetail.class));
		
		return vos;
	}

	
	
}
