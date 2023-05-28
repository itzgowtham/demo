package com.example.endpoint.old;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Holder;

import com.example.schema.Header;
import com.example.schema.StudentDetailsRequest;
import com.example.schema.StudentDetailsResponse;

@WebService(name = "Student", targetNamespace = "http://www.example.com/schema")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso( { com.example.schema.ObjectFactory.class })
public interface StudentEndpoint {
	@WebMethod
	@WebResult(name = "StudentDetailsResponse", targetNamespace = "http://www.example.com/schema", partName = "response")
	public StudentDetailsResponse getStudent(
			@WebParam(name = "Header", targetNamespace = "http://www.example.com.com/header", header = true, mode = WebParam.Mode.INOUT, partName = "header")
	        Holder<Header> header,
			@WebParam(name = "StudentDetailsRequest", targetNamespace = "http://www.example.com/schema", partName = "request") 
			StudentDetailsRequest request);
}
