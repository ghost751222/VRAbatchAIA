package com.macaron.vra.dao.impl;


import com.macaron.vra.entity.QaCaseLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QaCaseLabelDaoImpl {
    @Autowired
    @Qualifier("postgreSqlJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;


    public List<QaCaseLabel> findAll(){
        String sql ="select * from qa_case_label";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(QaCaseLabel.class));
    }
}
