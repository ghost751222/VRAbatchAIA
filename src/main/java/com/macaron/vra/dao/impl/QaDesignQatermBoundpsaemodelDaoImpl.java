package com.macaron.vra.dao.impl;

import com.macaron.vra.entity.QaDesignQatermBoundpsaemodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QaDesignQatermBoundpsaemodelDaoImpl {
    @Autowired
    @Qualifier("postgreSqlJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;


    public List<QaDesignQatermBoundpsaemodel> findByQaTermId(int qaTermId) {
        String sql = "select * from  qa_design_qaterm_boundpsaemodel where qaterm_id = :qaTermId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("qaTermId", qaTermId);
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QaDesignQatermBoundpsaemodel.class));
    }
}
