package com.example.endpoint;

import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import com.example.MyEscapeHandler;
import com.example.fault.ServiceFault;
import com.example.schema.Header;
import com.example.schema.HeaderObjectFactory;
import com.example.schema.Student;
import com.example.schema.StudentDetailsRequest;
import com.example.schema.StudentDetailsResponse;
import com.example.service.StudentService;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

@Endpoint
public class StudentEndpointImpl implements StudentEndpoint {
	
	@Autowired
	private Jaxb2Marshaller jc;
	
	@Autowired
	private StudentService studentService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@PayloadRoot(namespace = "http://www.example.com/schema", localPart = "StudentDetailsRequest")
	@ResponsePayload
	public StudentDetailsResponse getStudent(@SoapHeader("{http://www.example.com/schema}Header") SoapHeaderElement soapHeaderElement,
			@RequestPayload StudentDetailsRequest request, MessageContext message) throws ServiceFault {
		Header header= new Header();
		try {
			if(soapHeaderElement!=null) {
				header = unmarshallSOAPHeader(soapHeaderElement);
			}
		} catch (Exception e) {
			logger.error("Exception while unmarshalling SOAP header" + e);
		}
		StudentDetailsResponse response = new StudentDetailsResponse();
		Student student = studentService.findStudent(request.getName());
		System.out.println(student.getAddress());
		
		String add = student.getAddress();
		student.setAddress(add);
		System.out.println(Charset.defaultCharset());
		System.out.println(System.getProperty("file.encoding"));
		
		StringWriter writer = new StringWriter();
		
		Marshaller marshaller = jc.createMarshaller();
		try {
			marshaller.setProperty("jaxb.encoding", "Unicode");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(CharacterEscapeHandler.class.getName(), new MyEscapeHandler());
		} catch (PropertyException e1) {
			e1.printStackTrace();
		}
		try {
			marshaller.marshal(response, writer);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		
//		String lol = student.getAddress().replaceAll("\\r", "&#xD;");
//		lol = lol.toString().replace("&amp;","&");
//		String lel = lol.toString().replaceAll("amp;","&");
//		System.out.println(lol);
//		student.setAddress(lol);
		
		response.setStudent(student);
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