package com.macaron.vra.dao.impl;

import com.macaron.vra.entity.QaInterfacePsaemodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QaInterfacePsaemodelDaoImpl {
    @Autowired
    @Qualifier("postgreSqlJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;


    public List<QaInterfacePsaemodel> findById(String id) {
        String sql = "select * from  qa_interface_psaemodel where id = :id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QaInterfacePsaemodel.class));
    }
}
