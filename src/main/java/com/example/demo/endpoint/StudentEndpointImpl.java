package com.example.demo.endpoint;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import com.example.demo.fault.ServiceFault;
import com.example.demo.service.StudentService;
import com.example.xml.school.Header;
import com.example.xml.school.HeaderObjectFactory;
import com.example.xml.school.StudentDetailsRequest;
import com.example.xml.school.StudentDetailsResponse;

@Endpoint
public class StudentEndpointImpl implements StudentEndpoint {
	
	@Autowired
	private StudentService studentService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@PayloadRoot(namespace = "http://www.example.com/xml/school", localPart = "StudentDetailsRequest")
	@ResponsePayload
	public StudentDetailsResponse getStudent(@SoapHeader("{http://www.example.com/xml/school}Header") SoapHeaderElement soapHeaderElement,
			@RequestPayload StudentDetailsRequest request, MessageContext message) throws ServiceFault {
		Header header= new Header();
		try {
			header = unmarshallSOAPHeader(soapHeaderElement);
		} catch (Exception e) {
			logger.error("Exception while unmarshalling SOAP header" + e);
		}
		StudentDetailsResponse response = new StudentDetailsResponse();
		response.setStudent(studentService.findStudent(request.getName()));
		try {
			if(header.equals(null))
			setHeader(message, header);
		} catch (JAXBException e) {
			logger.error("Exception while setting an header to message context" + e);
		}
		return response;
	}
	
	private Header unmarshallSOAPHeader(SoapHeaderElement soapHeaderElement) throws Exception {
		// create an unmarshaller
		JAXBContext context = JAXBContext.newInstance(HeaderObjectFactory.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// unmarshal the header from the specified source
		@SuppressWarnings("unchecked")
		JAXBElement<Header> headers = (JAXBElement<Header>) unmarshaller.unmarshal(soapHeaderElement.getSource());

		// get the header values
		return headers.getValue();
	}
	
	private void setHeader(MessageContext messageContext, Header header) throws JAXBException {

        SaajSoapMessage soapResponse = (SaajSoapMessage) messageContext.getResponse();
        org.springframework.ws.soap.SoapHeader soapResponseHeader = soapResponse.getSoapHeader();
        JAXBContext context = JAXBContext.newInstance(HeaderObjectFactory.class);
        HeaderObjectFactory objectFactory = new HeaderObjectFactory();
        JAXBElement<Header> ofHeader = objectFactory.createHeader(header);
        context.createMarshaller().marshal(ofHeader, soapResponseHeader.getResult());
    }
}