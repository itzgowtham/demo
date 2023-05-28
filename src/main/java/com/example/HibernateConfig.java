//package com.example;
//
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//
//import com.zaxxer.hikari.HikariDataSource;
//
//@Configuration
//public class HibernateConfig {
//
//	@Bean("primaryDatabase")
//	@Primary
//	@ConfigurationProperties("spring.datasource")
//	public DataSourceProperties datasourceProperties() {
//		return new DataSourceProperties();
//	}
//	
//	@Bean("primaryDatasource")
//	@Primary
//	@ConfigurationProperties("spring.datasource.hikari")
//	public DataSource dataSource(@Qualifier("primaryDatabase") 
//									DataSourceProperties dataSourceProperties) {
//		DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder()
//				.type(HikariDataSource.class).build();
//		((HikariDataSource)dataSource).setConnectionTestQuery("SELECT 1");
//		return dataSource;
//	}
//	
//	@Bean("secondaryDatabase")
//	@ConfigurationProperties("spring.secondary.datasource")
//	public DataSourceProperties datasourceProperties2() {
//		return new DataSourceProperties();
//	}
//	
//	@Bean("secondaryDatasource")
//	@ConfigurationProperties("spring.datasource.hikari")
//	public DataSource dataSource2(@Qualifier("secondaryDatabase") 
//									DataSourceProperties dataSourceProperties) {
//		DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder()
//				.type(HikariDataSource.class).build();
//		((HikariDataSource)dataSource).setConnectionTestQuery("SELECT 1");
//		return dataSource;
//	}
//	
//	@Bean("primary.sessionFactory")
//	@Primary
//	public LocalSessionFactoryBean sessionFactory() {
//		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//		sessionFactory.setDataSource(dataSource(datasourceProperties()));
//		sessionFactory.setPackagesToScan("com.example.repository");//provide @repository classes
//		sessionFactory.setHibernateProperties(hibernateProperties());
//		return sessionFactory;
//	}
//	
//	@Bean("secondary.sessionFactory")
//	public LocalSessionFactoryBean sessionFactory2() {
//		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//		sessionFactory.setDataSource(dataSource2(datasourceProperties2()));
//		sessionFactory.setPackagesToScan("com.example.repository");//provide @repository classes
//		sessionFactory.setHibernateProperties(hibernateProperties());
//		return sessionFactory;
//	}
//
//	private Properties hibernateProperties() {
//		Properties prop = new Properties();
//		prop.put("hibernate.dialect", "org.hibernate.dialect.Oracle9Dialect");
//		prop.put("hibernate.show_sql", true);
//		prop.put("hibernate.format_sql", true);
//		return prop;
//	}
//}
