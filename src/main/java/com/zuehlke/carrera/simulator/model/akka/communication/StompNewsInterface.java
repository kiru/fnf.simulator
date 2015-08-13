package com.zuehlke.carrera.simulator.model.akka.communication;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class StompNewsInterface implements NewsInterface {
    private final SimpMessagingTemplate template;

    public StompNewsInterface(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void send(String channel, Object message) {
        template.convertAndSend(channel, message);
    }
}
