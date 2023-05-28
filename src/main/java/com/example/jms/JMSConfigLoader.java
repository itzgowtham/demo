package com.example.jms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//@Configuration
//@ConfigurationProperties(prefix = "mq")
public class JMSConfigLoader {

    List<ConnectionConfiguration> servers;
    private String replyQ;

    private String reqQ;

    public List<ConnectionConfiguration> getServers() {
        return servers;
    }

    public void setServers(List<ConnectionConfiguration> servers) {
        this.servers = servers;
    }

    public String getReplyQ() {
        return replyQ;
    }

    public void setReplyQ(String replyQ) {
        this.replyQ = replyQ;
    }

    public String getReqQ() {
        return reqQ;
    }

    public void setReqQ(String reqQ) {
        this.reqQ = reqQ;
    }
}
