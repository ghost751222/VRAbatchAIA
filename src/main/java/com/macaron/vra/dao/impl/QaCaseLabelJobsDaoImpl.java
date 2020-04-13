package com.macaron.vra.dao.impl;

import com.macaron.vra.entity.QaCaseLabelJobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QaCaseLabelJobsDaoImpl {
    @Autowired
    @Qualifier("postgreSqlJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public int[] delListByJobId(List<QaCaseLabelJobs> qa_case_label_jobs) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(qa_case_label_jobs.toArray());
        String sql = "delete from  qa_case_label_jobs where job_id = :job_id";
        return jdbcTemplate.batchUpdate(sql, batch);
    }

    public int[] insertById(List<QaCaseLabelJobs> qa_case_label_jobs) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(qa_case_label_jobs.toArray());
        String sql = "insert into qa_case_label_jobs(caselabel_id,job_id) values(:caselabel_id,:job_id)";
        return jdbcTemplate.batchUpdate(sql, batch);
    }

    public List<QaCaseLabelJobs> findByJobId(Integer job_id){
        String sql = "select * from qa_case_label_jobs where job_id=:job_id";
        Map<String,Object> params = new HashMap<>();
        params.put("job_id",job_id);
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(QaCaseLabelJobs.class));

    }

    public int delByJobId(int job_id) {
        String sql = "delete from qa_case_label_jobs where job_id=:job_id";
        Map<String,Object> params = new HashMap<>();
        params.put("job_id",job_id);
        return jdbcTemplate.update(sql,params);
    }
}
