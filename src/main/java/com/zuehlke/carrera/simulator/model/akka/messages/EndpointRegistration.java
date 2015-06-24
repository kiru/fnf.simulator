package com.zuehlke.carrera.simulator.model.akka.messages;

/**
 *
 * Created by paba on 9/23/14.
 */
public class EndpointRegistration {
    private final String id;
    private final String url;

    public EndpointRegistration(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }
}
