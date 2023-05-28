package com.example.endpoint.old;

import javax.jws.WebService;
import javax.xml.ws.Holder;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.repository.StudentRepository;
import com.example.schema.Header;
import com.example.schema.StudentDetailsRequest;
import com.example.schema.StudentDetailsResponse;

@WebService(endpointInterface = "com.example.endpoint.StudentEndpoint", targetNamespace = "http://www.example.com/schema", 
	serviceName = "Student", portName = "StudentDetailsPort")
public class StudentEndpointImpl implements StudentEndpoint {
	
	@Autowired
	private StudentRepository studentRepository;

	public StudentDetailsResponse getStudent(Holder<Header> header, StudentDetailsRequest request) {
		StudentDetailsResponse response = new StudentDetailsResponse();
		response.setStudent(studentRepository.findStudent(request.getName()));

		return response;
	}
}