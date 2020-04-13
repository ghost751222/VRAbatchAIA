package com.macaron.vra.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.macaron.vra.entity.ADSVoiceMaster;

@Repository
public class ADSVoiceMasterImpl {

	@Autowired
	@Qualifier("msSqlJdbc")
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ADSVoiceMaster> queryAllVoiceMaster() {
		String sql = "select * from ADS_VoiceMaster where ProcessStatus ='00' and STTProcessStatus is null order by CreateTime";
		List<ADSVoiceMaster> vos = jdbcTemplate.query(sql.toString(),
				new BeanPropertyRowMapper<ADSVoiceMaster>(ADSVoiceMaster.class));

		return vos;
	}

	public ADSVoiceMaster queryVoiceMaster() {
		List<ADSVoiceMaster> vos = this.queryAllVoiceMaster();

		return vos.size() > 0 ? vos.get(0) : null;
	}

	public ADSVoiceMaster queryVoiceMasterByBatchNo(String batchNo) {
		try{
			String sql = "select * from ADS_VoiceMaster where batchNo =:batchNo";
			Map<String, Object> params = new HashMap<>();
			params.put("batchNo", batchNo);
			List<ADSVoiceMaster> vos = jdbcTemplate.query(sql, params,
					new BeanPropertyRowMapper<>(ADSVoiceMaster.class));
			return vos.size() > 0 ? vos.get(0) : null;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}

	}

	public int update(ADSVoiceMaster vo) {
		String sql = "update ADS_VoiceMaster set STTStartTime = :STTStartTime ,STTCompleteTime =:STTCompleteTime, STTProcessStatus=:STTProcessStatus  where BatchNo = :BatchNo";
		return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(vo));

	}

}
