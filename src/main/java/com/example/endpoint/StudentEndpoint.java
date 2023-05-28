package com.example.endpoint;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeaderElement;

import com.example.fault.ServiceFault;
import com.example.schema.StudentDetailsRequest;
import com.example.schema.StudentDetailsResponse;

public interface StudentEndpoint {
	public StudentDetailsResponse getStudent(SoapHeaderElement soapHeaderElement,
			StudentDetailsRequest request, MessageContext message) throws ServiceFault;
}
