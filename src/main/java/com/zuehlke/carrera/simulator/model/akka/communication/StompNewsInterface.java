package com.zuehlke.carrera.simulator.model.akka.communication;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class StompNewsInterface implements NewsInterface {
    private static final String TOPIC_NEWS = "/topic/simulator/news";
    private static final String TOPIC_CLOCK = "/topic/simulator/clock";
    private final SimpMessagingTemplate template;

    public StompNewsInterface(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void data(Object message) {
        template.convertAndSend(TOPIC_NEWS, message);
    }

    @Override
    public void tick(Object message) {
        template.convertAndSend(TOPIC_CLOCK, message);
    }
}
