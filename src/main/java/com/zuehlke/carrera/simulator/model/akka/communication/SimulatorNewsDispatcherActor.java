package com.zuehlke.carrera.simulator.model.akka.communication;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.simulator.model.akka.messages.DataEventNews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulatorNewsDispatcherActor extends UntypedActor {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorNewsDispatcherActor.class);
    private final NewsInterface newsInterface;

    public static Props props(NewsInterface newsInterface) {
        return Props.create(SimulatorNewsDispatcherActor.class, () -> {
            return new SimulatorNewsDispatcherActor(newsInterface);
        });
    }

    private SimulatorNewsDispatcherActor(NewsInterface newsInterface) {
        if (newsInterface == null) {
            throw new IllegalArgumentException("newsInterface must not be null!");
        }
        this.newsInterface = newsInterface;
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
        newsInterface.data(message);
    }
}