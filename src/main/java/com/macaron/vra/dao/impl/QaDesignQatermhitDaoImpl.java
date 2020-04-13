package com.macaron.vra.dao.impl;

import com.macaron.vra.entity.QaDesignQatermhit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QaDesignQatermhitDaoImpl {

    @Autowired
    @Qualifier("postgreSqlJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;


    public int delByJobId(int jobId) {
        String sql = "delete from  qa_design_qatermhit where job_id = :jobId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jobId", jobId);
        return jdbcTemplate.update(sql, params);
    }

    public List<QaDesignQatermhit> findByJobIdAndMachinehit(int jobId,String machineHit) {
        String sql = "select * from  qa_design_qatermhit where job_id = :jobId and machine_hit=:machineHit";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jobId", jobId);
        params.put("machineHit", machineHit);
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QaDesignQatermhit.class));
    }

    public List<QaDesignQatermhit> findByJobId(int jobId) {
        String sql = "select * from  qa_design_qatermhit where job_id = :jobId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jobId", jobId);
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QaDesignQatermhit.class));
    }
}
