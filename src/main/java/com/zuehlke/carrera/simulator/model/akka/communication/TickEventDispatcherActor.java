package com.zuehlke.carrera.simulator.model.akka.communication;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.simulator.model.akka.clock.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class TickEventDispatcherActor extends UntypedActor {
    private static final Logger LOG = LoggerFactory.getLogger(TickEventDispatcherActor.class);
    private static final String TOPIC_CLOCK = "/topic/simulator/clock";
    private final SimpMessagingTemplate template;

    public static Props props(SimpMessagingTemplate template) {
        return Props.create(TickEventDispatcherActor.class, () -> {
            return new TickEventDispatcherActor(template);
        });
    }

    private TickEventDispatcherActor(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Tick) {
            handleTick((Tick) message);
        } else {
            unhandled(message);
        }
    }

    private void handleTick(Tick message) {
        try {
            tryHandleTick(message);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
        }
    }

    private void tryHandleTick(Tick message) {
        template.convertAndSend(TOPIC_CLOCK, message);
    }
}
