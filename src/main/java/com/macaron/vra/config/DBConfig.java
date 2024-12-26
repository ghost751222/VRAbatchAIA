package com.macaron.vra.config;

import java.beans.PropertyVetoException;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.macaron.vra.service.impl.EncryptorServiceImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Service
@EncryptablePropertySource(name = "EncryptedProperties", value = "classpath:application.properties")
public class DBConfig {

	@Value("${postgre.datasource.url}")
	String postgreUrl;
	@Value("${postgre.datasource.username}")
	String postgreUsername;
	@Value("${postgre.datasource.password}")
	String postgrePassword;
	@Value("${postgre.datasource.driver-class-name}")
	String postgresDriverClass;
	
	
	
	@Value("${mssql.datasource.url}")
	String mssqlUrl;
	@Value("${mssql.datasource.username}")
	String mssqlUsername;
	@Value("${mssql.datasource.password}")
	String mssqlPassword;
	@Value("${mssql.datasource.driver-class-name}")
	String mssqlDriverClass;
	
	@Autowired
	EncryptorServiceImpl encryptorServiceImpl;
	
	@Bean(destroyMethod="close")
	public ComboPooledDataSource postgreSqlDataSource() throws PropertyVetoException{
		ComboPooledDataSource source = new ComboPooledDataSource();
		source.setJdbcUrl(postgreUrl);
		source.setUser(postgreUsername);
		source.setPassword(encryptorServiceImpl.decrypt(postgrePassword));
		source.setDriverClass(postgresDriverClass);
		return source;
	}

	@Bean(destroyMethod="close")
	public ComboPooledDataSource msSqlDataSource() throws PropertyVetoException{
		ComboPooledDataSource source = new ComboPooledDataSource();
		source.setJdbcUrl(mssqlUrl);
		source.setUser(mssqlUsername);
		source.setPassword(encryptorServiceImpl.decrypt(mssqlPassword));
		source.setDriverClass(mssqlDriverClass);
		
		return source;
	}
}
