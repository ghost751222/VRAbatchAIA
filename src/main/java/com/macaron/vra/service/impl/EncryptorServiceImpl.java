package com.macaron.vra.service.impl;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class EncryptorServiceImpl {
	
	@Value("${jasypt.encryptor.password}")
	private String salt;
	
	@Bean
	public BasicTextEncryptor basicTextEncryptor() {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(salt);
		return encryptor;
	}
	
	@Autowired
	private BasicTextEncryptor encryptor;
	
	public String encrypt(String input)
	{
		return encryptor.encrypt(input);
	}

	public String decrypt(String input)
	{
		try {
			return encryptor.decrypt(input);
		}catch(Exception e){
			return input;
		}
		
	}
	
}
