package com.zuehlke.carrera.simulator.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class WhereAmIHostedService implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(WhereAmIHostedService.class);
    private String address;
    private int port;

    public WhereAmIHostedService(){
        setAddress();
    }

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent embeddedServletContainerInitializedEvent) {
        port = embeddedServletContainerInitializedEvent.getEmbeddedServletContainer().getPort();
        LOG.info("Server is running on port " + port);
    }

    public String getHttpEndpoint(){
        return "http://"+ address + ":" + port;
    }

    private void setAddress(){
        try {
            address = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            LOG.error("", e);
        }
    }
}
