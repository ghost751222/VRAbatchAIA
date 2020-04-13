package com.macaron.vra.dao.impl;

import com.macaron.vra.entity.ADSPQACase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ADSPQACaseDaoImpl {
    @Autowired
    @Qualifier("msSqlJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;

   public int[] insertBatch(List<ADSPQACase> adspqaCases){
       SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(adspqaCases.toArray());
       String sql ="insert into ADS_PQACase(AppID,CheckItem,CheckResult,CreateTime) values(:AppID,:CheckItem,:CheckResult,:CreateTime)";
       return jdbcTemplate.batchUpdate(sql, batch);
   }
}
