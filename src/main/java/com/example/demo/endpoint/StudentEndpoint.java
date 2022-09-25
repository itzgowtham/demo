package com.example.demo.endpoint;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeaderElement;

import com.example.demo.fault.ServiceFault;
import com.example.xml.school.StudentDetailsRequest;
import com.example.xml.school.StudentDetailsResponse;

public interface StudentEndpoint {
	public StudentDetailsResponse getStudent(SoapHeaderElement soapHeaderElement, StudentDetailsRequest request, MessageContext message) throws ServiceFault;
}
