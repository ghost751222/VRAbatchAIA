package com.macaron.vra.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import com.macaron.vra.entity.STTDispatchOrder;

@Service
public class STTDispatchOrderDaoImpl {
		

	@Autowired
	@Qualifier("msSqlJdbc")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public int insert(STTDispatchOrder vo) {
		String sql ="insert into STT_DispatchOrder(ApplicationFormID,DataType,CreateTime) values(:applicationFormID,:dataType,:createTime)";
		return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(vo));
	}
	
	public int[] insertAll(List<STTDispatchOrder> vos) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(vos.toArray());
		String sql ="insert into STT_DispatchOrder(ApplicationFormID,DataType,CreateTime) values(:ApplicationFormID,:DataType,:CreateTime)";
		return jdbcTemplate.batchUpdate(sql, batch);
	}
}
