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
    private final NewsInterface newsInterface;

    public static Props props(SimpMessagingTemplate template) {
        return Props.create(TickEventDispatcherActor.class, () -> {
            return new TickEventDispatcherActor(new StompNewsInterface(template));
        });
    }

    public static Props props(NewsInterface newsInterface) {
        return Props.create(TickEventDispatcherActor.class, () -> {
            return new TickEventDispatcherActor(newsInterface);
        });
    }

    private TickEventDispatcherActor(NewsInterface newsInterface) {
        this.newsInterface = newsInterface;
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
        newsInterface.send(TOPIC_CLOCK, message);
    }
}
