package com.example;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import com.example.fault.ServiceFault;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.marshaller.NioEscapeHandler;

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
		return new ServletRegistrationBean(servlet,new String[]{
		        "/service/student-details",
		});
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
	
	@Bean(name = "school")
	public SimpleWsdl11Definition simpleWsdl() {
		SimpleWsdl11Definition simpleWsdl = new SimpleWsdl11Definition();
		simpleWsdl.setWsdl(new ClassPathResource("school.wsdl"));
		return simpleWsdl;
	}
	
//	@Bean(name = "studentDetailsWsdl")
//	  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) 
//	  {
//	    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
//	    wsdl11Definition.setPortTypeName("StudentDetailsPort");
//	    wsdl11Definition.setLocationUri("/service/student-details");
//	    wsdl11Definition.setTargetNamespace("http://www.example.com/schema");
//	    wsdl11Definition.setSchema(countriesSchema);
//	    return wsdl11Definition;
//	  }
//	 
//	  @Bean
//	  public XsdSchema countriesSchema() 
//	  {
//	    return new SimpleXsdSchema(new ClassPathResource("school.xsd"));
//	  }
	
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
	
//	@Autowired
//	private NioEscapeHandler nio;
//	
//	@Bean
//	public CharacterEscapeHandler my() {
//		CharacterEscapeHandler my = new MyEscapeHandler();
//		return my;
//	}
	
	@Bean
	public CharacterEscapeHandler my() {
		CharacterEscapeHandler my = new CharacterEscapeHandler() {
			
			@Override
			public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
		        
		        int limit = start+length;
		        for (int i = start; i < limit; i++) {
		            char c = ch[i];
		            if (c == '&' || c == '<' || c == '>' || c == '\r' || (c == '\n' && isAttVal) || (c == '\"' && isAttVal)) {
		                if (i != start)
		                    out.write(ch, start, i - start);
		                start = i + 1;
		                switch (ch[i]) {
		                    case '&':
		                        out.write("&amp;");
		                        break;
		                    case '<':
		                        out.write("&lt;");
		                        break;
		                    case '>':
		                        out.write("&gt;");
		                        break;
		                    case '\"':
		                        out.write("&quot;");
		                        break;
		                    case '\n':
		                    case '\r':
		                        out.write("&#xD");
		                        out.write(Integer.toHexString(c));
		                        out.write(';');
		                        break;
		                    default:
		                        throw new IllegalArgumentException("Cannot escape: '" + c + "'");
		                }
		            }
		        }
		        
		        if( start!=limit )
		            out.write(ch,start,limit-start);
		    }

		};
		return my;
	}
	
	@Bean
	public Jaxb2Marshaller jaxb() {
		Jaxb2Marshaller jaxb = new Jaxb2Marshaller();
		jaxb.setPackagesToScan("com.example.schema");
		return jaxb;
	}
	
}