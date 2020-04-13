package com.macaron.vra.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConst {
	
	
	public static String AIAPsttFtpHost;
	
	
	public static Integer AIAPsttFtpPort;
	
	
	public static String AIAPsttFtpUsername ;
	
	
	public static String AIAPsttFtpPassword ;
	
	
	public static String AIAPsttFtpDataSourceDirPath ;

	@Value("${AIAPsttFtpHost}")
	public  void setAIAPsttFtpHost(String aIAPsttFtpHost) {
		AIAPsttFtpHost = aIAPsttFtpHost;
	}

	@Value("${AIAPsttFtpPort}")
	public  void setAIAPsttFtpPort(Integer aIAPsttFtpPort) {
		AIAPsttFtpPort = aIAPsttFtpPort;
	}

	@Value("${AIAPsttFtpUsername}")
	public  void setAIAPsttFtpUsername(String aIAPsttFtpUsername) {
		AIAPsttFtpUsername = aIAPsttFtpUsername;
	}

	@Value("${AIAPsttFtpPassword}")
	public  void setAIAPsttFtpPassword(String aIAPsttFtpPassword) {
		AIAPsttFtpPassword = aIAPsttFtpPassword;
	}

	@Value("${AIAPsttFtpDataSourceDirPath}")
	public  void setAIAPsttFtpDataSourceDirPath(String aIAPsttFtpDataSourceDirPath) {
		AIAPsttFtpDataSourceDirPath = aIAPsttFtpDataSourceDirPath;
	}
	
	
}
