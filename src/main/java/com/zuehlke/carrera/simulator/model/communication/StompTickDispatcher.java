package com.zuehlke.carrera.simulator.model.communication;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.simulator.model.akka.clock.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 */
public class StompTickDispatcher extends UntypedActor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StompTickDispatcher.class);


    public static final String TOPIC_CLOCK = "/topic/simulator/clock";

    public static final Props props ( final SimpMessagingTemplate template ) {
        return Props.create(StompTickDispatcher.class, () -> new StompTickDispatcher( template ));
    }

    private final SimpMessagingTemplate template;

    public StompTickDispatcher ( SimpMessagingTemplate template ) {
        this.template = template;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        try{
            if ( message instanceof Tick ) {
                Tick tick = (Tick) message;
                template.convertAndSend( TOPIC_CLOCK, tick);
            } else {
                unhandled(message);
            }
        }catch (Exception e){
            LOGGER.warn(e.getMessage());
        }

    }
}
