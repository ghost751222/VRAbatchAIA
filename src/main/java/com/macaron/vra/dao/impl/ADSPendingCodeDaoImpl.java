package com.macaron.vra.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.macaron.vra.entity.ADSPendingCode;

@Repository
public class ADSPendingCodeDaoImpl {

	
	@Autowired
	@Qualifier("msSqlJdbc")
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ADSPendingCode> queryAll() {
		String sql = "select * from ADS_PendingCode";
		List<ADSPendingCode> vos = jdbcTemplate.query(sql.toString(),
				new BeanPropertyRowMapper<ADSPendingCode>(ADSPendingCode.class));
	
		return vos;
	}

	public Map<String,String> queryAllToMap() {
		Map<String,String> map= new HashMap<String,String>();
		List<ADSPendingCode> vos = this.queryAll();
		for(ADSPendingCode v : vos) {
			map.put(v.getPendingCodeId(), v.getName());
		}	
		return map;
	}
}
