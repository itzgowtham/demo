package com.example.demo.fault;

import com.example.exception.Exceptions;

public class ServiceFault extends Exception {

	private Exceptions exceptions;

	public Exceptions getExceptions() {
		return exceptions;
	}
	
	public ServiceFault(String message, Exceptions exceptions) {
	        super(message);
	        this.exceptions = exceptions;
	}
	
	public ServiceFault(String message, Exceptions exceptions, Throwable cause) {
	        super(message, cause);
	        this.exceptions = exceptions;
	   }
}
