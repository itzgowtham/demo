package com.example;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Result;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import com.example.exception.Exceptions;
import com.example.exception.ObjectFactory;
import com.example.fault.ServiceFault;

public class SoapFaultExceptionResolver extends SoapFaultMappingExceptionResolver {

	private static JAXBContext headerJaxbContext = null;
	
	static {
		try {
			headerJaxbContext = JAXBContext.newInstance(Exceptions.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
		super.customizeFault(endpoint, ex, fault);
		System.out.println(fault.getFaultStringOrReason());
		ObjectFactory objFactory = new ObjectFactory();
		Exceptions exceptions = objFactory.createExceptions();
		if(ex instanceof ServiceFault)
			exceptions = ((ServiceFault) ex).getExceptions();
		Result result = fault.addFaultDetail().getResult();

		try {
			headerJaxbContext.createMarshaller().marshal(objFactory.createExceptionList(exceptions), result);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

}