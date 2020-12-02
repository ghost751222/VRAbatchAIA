package com.macaron.vra.config;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
public class NamedParameterJdbcTemplateConfig {


    @Bean

    public NamedParameterJdbcTemplate msSqlJdbc(@Qualifier("msSqlDataSource") ComboPooledDataSource source) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(source);
        return jdbcTemplate;

    }

    @Bean
    public NamedParameterJdbcTemplate postgreSqlJdbc(@Qualifier("postgreSqlDataSource") ComboPooledDataSource source) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(source);
        return jdbcTemplate;

    }

    @Bean
    public PlatformTransactionManager msManager(@Qualifier("msSqlDataSource") DataSource dataSource1) {
        return new DataSourceTransactionManager(dataSource1);
    }

    @Bean
    public PlatformTransactionManager postgresManager(@Qualifier("postgreSqlDataSource") DataSource dataSource2) {
        return new DataSourceTransactionManager(dataSource2);
    }
}
