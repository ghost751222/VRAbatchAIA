package com.macaron.vra.service.impl;

import java.io.IOException;
import java.net.Socket;

import org.springframework.stereotype.Service;

@Service
public class AIAServerCheckServiceImpl {

	public  boolean IsHostAvailable(String address,Integer serverPort) { 
	    try (Socket s = new Socket(address, serverPort)) {
	    	s.setSoTimeout(3000);
	    	s.close();
	        return true;
	    } catch (IOException ex) {
	        /* ignore */
	    }
	    return false;
	}
	
}
