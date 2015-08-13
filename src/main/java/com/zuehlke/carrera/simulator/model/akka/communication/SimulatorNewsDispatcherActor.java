package com.zuehlke.carrera.simulator.model.akka.communication;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.simulator.model.akka.messages.DataEventNews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class SimulatorNewsDispatcherActor extends UntypedActor {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorNewsDispatcherActor.class);
    private static final String TOPIC_NEWS = "/topic/simulator/news";
    private final SimpMessagingTemplate template;

    public static Props props(SimpMessagingTemplate template) {
        return Props.create(SimulatorNewsDispatcherActor.class, () -> {
            return new SimulatorNewsDispatcherActor(template);
        });
    }

    private SimulatorNewsDispatcherActor(SimpMessagingTemplate template) {
        if (template == null) {
            throw new IllegalArgumentException("template must not be null!");
        }
        this.template = template;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DataEventNews) {
            handleDataEventNews(message);
        } else {
            unhandled(message);
        }
    }

    private void handleDataEventNews(Object message) {
        try {
            tryHandleDataEventNews(message);
        } catch (Exception e) {
            LOG.warn("Sending data event news failed. Sender was " + sender(), e);
        }
    }

    private void tryHandleDataEventNews(Object message) {
        template.convertAndSend(TOPIC_NEWS, message);
    }
}