package com.zuehlke.carrera.simulator.communication.announce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class WhereAmIHosted implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(WhereAmIHosted.class);
    private String httpEndpointUrl;

    public String getHttpEndpoint() {
        return httpEndpointUrl;
    }

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent initializedEvent) {
        String host = getHostName();
        int port = getServicePort(initializedEvent);
        setHttpEndpointUrl(host, port);
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            LOG.error("Hostname lookup failed", e);
            throw new HostNameLookupException("Could not lookup the hostname of the system", e);
        }
    }

    private int getServicePort(EmbeddedServletContainerInitializedEvent embeddedServletContainerInitializedEvent) {
        return embeddedServletContainerInitializedEvent.getEmbeddedServletContainer().getPort();
    }

    private void setHttpEndpointUrl(String host, int port) {
        httpEndpointUrl = "http://" + host + ":" + port;
        LOG.info("Server is running on " + httpEndpointUrl);
    }
}
