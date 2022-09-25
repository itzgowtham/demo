package com.example.demo;

import java.util.Properties;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import com.example.demo.fault.ServiceFault;

@Configuration
@EnableWs
public class WebServiceConfig {
	
//	@Bean
//	@ConditionalOnMissingBean(RequestContextListener.class)
//	public RequestContextListener requestContextListener() {
//		return new RequestContextListener();
//	}
	
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/service/*");
	}
	
//	@Bean
//	public ServletRegistrationBean logbackServlet() {
//		LogbackServlet servlet = new LogbackServlet();
//		return new ServletRegistrationBean(servlet, "/loglevel");
//	}
//	
//	@Bean
//	public FilterRegistrationBean gatewayFilter() {
//		FilterRegistrationBean filter = new FilterRegistrationBean();
//		filter.setFilter(new MyGatewayFilter());
//		filter.setUrlPatterns(Arrays.asList("/*"));
//		return filter;
//	}
	
//	@Bean
//	public SimpleWsdl11Definition simpleWsdl() {
//		SimpleWsdl11Definition simpleWsdl = new SimpleWsdl11Definition();
//		simpleWsdl.setWsdl(new ClassPathResource("school.wsdl"));
//		return simpleWsdl;
//	}
	
	@Bean(name = "studentDetailsWsdl")
	  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) 
	  {
	    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
	    wsdl11Definition.setPortTypeName("StudentDetailsPort");
	    wsdl11Definition.setLocationUri("/service/student-details");
	    wsdl11Definition.setTargetNamespace("http://www.example.com/xml/school");
	    wsdl11Definition.setSchema(countriesSchema);
	    return wsdl11Definition;
	  }
	 
	  @Bean
	  public XsdSchema countriesSchema() 
	  {
	    return new SimpleXsdSchema(new ClassPathResource("school.xsd"));
	  }
	
	@Bean
	public SoapFaultMappingExceptionResolver exceptionResolver() {
		SoapFaultMappingExceptionResolver exceptionResolver = new SoapFaultExceptionResolver();

		SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
		faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
		exceptionResolver.setDefaultFault(faultDefinition);

		Properties errorMappings = new Properties();
		errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
		errorMappings.setProperty(ServiceFault.class.getName(), SoapFaultDefinition.SERVER.toString());
		exceptionResolver.setExceptionMappings(errorMappings);
		exceptionResolver.setOrder(1);
		return exceptionResolver;
	}
}
