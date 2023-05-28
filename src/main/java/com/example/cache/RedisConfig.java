package com.example.cache;

import javax.annotation.PostConstruct;

import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

//@Configuration
public class RedisConfig {

    @Autowired
    private Environment env;

    @PostConstruct
    public void redisConfiguration() {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.setThreads(Integer.parseInt(env.getRequiredProperty("threads")));
        config.setNettyThreads(Integer.parseInt(env.getRequiredProperty("nettyThreads")));
        config.useSingleServer().setAddress(env.getRequiredProperty("singleServerConfig.address"))
                .setClientName(env.getRequiredProperty("singleServerConfig.clientName"))
                .setPassword(env.getRequiredProperty("singleServerConfig.password"))
                .setIdleConnectionTimeout(Integer.parseInt(env.getRequiredProperty("singleServerConfig.idleConnectionTimeout")))
                .setConnectTimeout(Integer.parseInt(env.getRequiredProperty("singleServerConfig.connectTimeout")))
                .setRetryAttempts(Integer.parseInt(env.getRequiredProperty("singleServerConfig.retryAttempts")))
                .setRetryInterval(Integer.parseInt(env.getRequiredProperty("singleServerConfig.retryInterval")))
                .setSubscriptionsPerConnection(Integer.parseInt(env.getRequiredProperty("singleServerConfig.subscriptionsPerConnection")))
                .setSubscriptionConnectionMinimumIdleSize(Integer.parseInt(env.getRequiredProperty("singleServerConfig.subscriptionConnectionMinimumIdleSize")))
                .setConnectionPoolSize(Integer.parseInt(env.getRequiredProperty("singleServerConfig.connectionPoolSize")))
                .setConnectionMinimumIdleSize(Integer.parseInt(env.getRequiredProperty("singleServerConfig.connectionMinimumIdleSize")))
                .setDatabase(Integer.parseInt(env.getRequiredProperty("singleServerConfig.database")))
                .setDnsMonitoringInterval(Integer.parseInt(env.getRequiredProperty("singleServerConfig.dnsMonitoringInterval")));
    }
}
