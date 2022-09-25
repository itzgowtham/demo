package com.example.demo.endpoint.old;

import javax.jws.WebService;
import javax.xml.ws.Holder;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repository.StudentRepository;
import com.example.xml.school.Header;
import com.example.xml.school.StudentDetailsRequest;
import com.example.xml.school.StudentDetailsResponse;

@WebService(endpointInterface = "com.example.demo.endpoint.StudentEndpoint", targetNamespace = "http://www.example.com/xml/school", 
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