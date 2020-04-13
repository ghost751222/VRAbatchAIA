package com.macaron.vra.dao.impl;


import com.macaron.vra.entity.QaUsermanageUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QaUsermanageUserDaoImpl {
    @Autowired
    @Qualifier("postgreSqlJdbc")
    private NamedParameterJdbcTemplate jdbcTemplate;


    public List<QaUsermanageUser> findAll() {
        String sql = "select * from qa_usermanage_user";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QaUsermanageUser.class));
    }

    public Map<Integer, String> findAlltoMap() {
        Map<Integer, String> map = new HashMap<>();
        List<QaUsermanageUser> qaUsermanageUsers = this.findAll();
        qaUsermanageUsers.forEach(qaUsermanageUser -> {
            map.put(qaUsermanageUser.getId(), qaUsermanageUser.getUsername());
        });

        return map;

    }
}
