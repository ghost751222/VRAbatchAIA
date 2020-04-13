package com.macaron.vra.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.macaron.vra.entity.ADSUsers;

@Service
public class ADSUsersDaoImpl {
	@Autowired
	@Qualifier("msSqlJdbc")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public List<ADSUsers> findAll() {
		String sql = "select * from ads_users";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<ADSUsers>(ADSUsers.class));
	}

	public int test(){
		String sql ="select 1";
		return jdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(new ADSUsers()),Integer.class);
	}
}
