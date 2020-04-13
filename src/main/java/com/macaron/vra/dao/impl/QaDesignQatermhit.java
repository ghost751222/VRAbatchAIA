package com.macaron.vra.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
@Repository
public class QaDesignQatermhit {

    @Autowired
    @Qualifier("postgreSqlJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;


    public int delByJobId(int jobId){
        String sql = "delete from  qa_design_qatermhit where job_id = :jobId";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("jobId", jobId);
        return jdbcTemplate.update(sql,params);
    }
}
