package com.example.config;

import javax.jms.JMSException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.spring.boot.MQConfigurationProperties;
import com.ibm.mq.spring.boot.MQConnectionFactoryFactory;
import com.ibm.msg.client.jms.JmsConstants;
import com.ibm.msg.client.wmq.common.CommonConstants;

import lombok.Data;

@Data
@ConfigurationProperties("ibm.mq")
public class IBMQueueConfiguration {

	private String queueManager;
	private String channel;
	private String connName;
	private String hostName;
	private int port;
	private String sendToQ;
	private String replyToQ;
	private long timeout;
	
	@Bean
	public MQConnectionFactory mqConnectionFactory() throws JMSException {
		MQConfigurationProperties properties = new MQConfigurationProperties();
		MQConnectionFactoryFactory conff = new MQConnectionFactoryFactory(properties, null);
		MQConnectionFactory conf = conff.createConnectionFactory(MQConnectionFactory.class);
		conf.setStringProperty(CommonConstants.WMQ_HOST_NAME, hostName);
		conf.setIntProperty(CommonConstants.WMQ_PORT, port);
		conf.setStringProperty(CommonConstants.WMQ_CHANNEL, channel);
		conf.setIntProperty(CommonConstants.WMQ_CONNECTION_MODE, CommonConstants.WMQ_CM_CLIENT);
		conf.setStringProperty(CommonConstants.WMQ_QUEUE_MANAGER, queueManager);
		conf.setBooleanProperty(JmsConstants.USER_AUTHENTICATION_MQCSP, false);
		conf.setTransportType(CommonConstants.WMQ_CM_CLIENT);
		conf.setIntProperty(JmsConstants.JMS_IBM_CHARACTER_SET, 819);
		return conf;
	}
}
