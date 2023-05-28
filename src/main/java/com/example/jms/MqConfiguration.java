//package com.example.jms;
//
//import com.ibm.mq.jms.*;
//import com.ibm.msg.client.wmq.WMQConstants;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jms.annotation.EnableJms;
//import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
//import org.springframework.jms.config.JmsListenerContainerFactory;
//import org.springframework.jms.config.JmsListenerEndpointRegistry;
//import org.springframework.jms.config.SimpleJmsListenerEndpoint;
//import org.springframework.jms.connection.CachingConnectionFactory;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.support.QosSettings;
//import org.springframework.jms.support.converter.SimpleMessageConverter;
//import org.springframework.jms.support.destination.JndiDestinationResolver;
//
//import javax.jms.ConnectionFactory;
//import javax.jms.JMSException;
//import javax.jms.Session;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static javax.jms.DeliveryMode.NON_PERSISTENT;
//import static javax.jms.Session.CLIENT_ACKNOWLEDGE;
//
//
////@Configuration
//@EnableJms
//public class MqConfiguration {
//
//    @Autowired
//    JMSConfigLoader mqConfig;
//
//    @Autowired
//    private JmsListenerEndpointRegistry registry;
//
//    //connectionFactories
//    @Bean
//    public List<JmsListenerContainerFactory> myFactories(@Qualifier("jmsConnectionFactory")
//            								List<CachingConnectionFactory> connectionFactories) {
//        List<JmsListenerContainerFactory> factories = new ArrayList<>();
//        connectionFactories.forEach(connectionFactory -> {
//            DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//            factory.setConnectionFactory(connectionFactory);
//            factory.setSessionAcknowledgeMode(CLIENT_ACKNOWLEDGE);
//
//            QosSettings qosSettings = new QosSettings();
//            qosSettings.setDeliveryMode(NON_PERSISTENT);
//            factory.setReplyQosSettings(qosSettings);
//
//            SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
//            endpoint.setId("myJmsEndpoint-"+ UUID.randomUUID());
//            endpoint.setDestination("Reply Queue destination");
//
//            factories.add(factory);
//        });
//        return factories;
//    }
//    //     ,
//    @Bean
//    public List<JmsTemplate> jmsTemplate( @Qualifier("jmsConnectionFactory")
//    							List<CachingConnectionFactory> connectionFactories) {
//        return getJmsTemplates(new ArrayList<ConnectionFactory>(connectionFactories));
//    }
//
//    public List<JmsTemplate> getJmsTemplates(List<ConnectionFactory> connectionFactories) {
//        List<JmsTemplate> jmsTemplates = new ArrayList<>();
//        for (ConnectionFactory connectionFactory : connectionFactories) {
//            JmsTemplate jmsTemplate = new JmsTemplate();
//            jmsTemplate.setConnectionFactory(connectionFactory);
//            jmsTemplate.setMessageConverter(new SimpleMessageConverter());
//            jmsTemplate.setDefaultDestinationName("Response Queue Destination name");
//            jmsTemplate.setDeliveryMode(NON_PERSISTENT);
//            jmsTemplate.setDeliveryPersistent(false);
//            jmsTemplate.setExplicitQosEnabled(true);
//            jmsTemplate.setPubSubDomain(false);
//            jmsTemplate.setDestinationResolver(new JndiDestinationResolver());
//
//            jmsTemplates.add(jmsTemplate);
//        }
//        return jmsTemplates;
//    }
//
//    @Bean("jmsConnectionFactory")
//    public List<CachingConnectionFactory> connectionFactories() throws JMSException {
//        List<CachingConnectionFactory> factories = new ArrayList<>();
//
//        for (ConnectionConfiguration server : mqConfig.getServers()) {
//            CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
//            MQConnectionFactory cf = new MQConnectionFactory();
//            cachingConnectionFactory.setTargetConnectionFactory(cf);
//            cf.setQueueManager(server.getQueueManager());
//            cf.setChannel(server.getChannel());
//            cf.setStringProperty(WMQConstants.USERID, server.getUser());
//            cf.setStringProperty(WMQConstants.PASSWORD, "");
//            cf.setStringProperty("XMSC_WMQ_CONNECTION_MODE", "1");
//
//            factories.add(cachingConnectionFactory);
//        }
//        return factories;
//    }
//
//    @Bean
//    public MQDestination replyQ(){
//        MQQueueConnectionFactory cf = null;
//        MQQueueConnection connection = null;
//        MQQueueSession session = null;
//        MQQueue queue = null;
//        MQQueueSender sender = null;
//
//        try {
//            cf = new MQQueueConnectionFactory();
//            cf.setHostName(mqConfig.getServers().get(0).getHostName());// host
//            cf.setPort(Integer.parseInt(mqConfig.getServers().get(0).getPort()));// port
//            cf.setTransportType(1);// JMSC.MQJMS_TP_CLIENT_MQ_TCPIP
//            cf.setQueueManager(mqConfig.getServers().get(0).getQueueManager());// queue
//            cf.setChannel(mqConfig.getServers().get(0).getChannel());// channel
//
//            connection = (MQQueueConnection) cf.createQueueConnection();
//            session = (MQQueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//            queue = (MQQueue) session.createQueue(mqConfig.getReplyQ());// queue
//            sender = (MQQueueSender) session.createSender(queue);
//
//            // Start the connection
//            connection.start();
//        } catch (JMSException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                sender.close();
//            } catch (Exception e) {
//            }
//            try {
//                session.close();
//            } catch (Exception e) {
//            }
//            if(connection != null){
//                try {
//                    connection.close();
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return queue;
//    }
//
//    @Bean
//    public MQDestination reqQ(){
//        MQQueueConnectionFactory cf = null;
//        MQQueueConnection connection = null;
//        MQQueueSession session = null;
//        MQQueue queue = null;
//        MQQueueSender sender = null;
//
//        try {
//            cf = new MQQueueConnectionFactory();
//            cf.setHostName(mqConfig.getServers().get(0).getHostName());// host
//            cf.setPort(Integer.parseInt(mqConfig.getServers().get(0).getPort()));// port
//            cf.setTransportType(1);// JMSC.MQJMS_TP_CLIENT_MQ_TCPIP
//            cf.setQueueManager(mqConfig.getServers().get(0).getQueueManager());// queue
//            cf.setChannel(mqConfig.getServers().get(0).getChannel());// channel
//
//            connection = (MQQueueConnection) cf.createQueueConnection();
//            session = (MQQueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//            queue = (MQQueue) session.createQueue(mqConfig.getReqQ());// queue
//            sender = (MQQueueSender) session.createSender(queue);
//
//            // Start the connection
//            connection.start();
//        } catch (JMSException e){
//            e.printStackTrace();
//        } finally {
//            try {
//                sender.close();
//            } catch (Exception e) {
//            }
//            try {
//                session.close();
//            } catch (Exception e) {
//            }
//            if(connection != null){
//                try {
//                    connection.close();
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return queue;
//    }
//
//}