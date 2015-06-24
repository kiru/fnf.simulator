package com.zuehlke.carrera.simulator.model.communication;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.simulator.model.akka.messages.DataEventNews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Dispatches simulator specific data
 * Created by wgi on 13.09.2014.
 */
public class StompSimulatorNewsDispatcher extends UntypedActor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StompSimulatorNewsDispatcher.class);

    private static final String TOPIC_NEWS = "/topic/simulator/news";

    public static Props props( final SimpMessagingTemplate template) {
        return Props.create(StompSimulatorNewsDispatcher.class, () -> new StompSimulatorNewsDispatcher(template));
    }

    private final SimpMessagingTemplate template;


    /**
     * Creates a new StompNewsDispatcher
     * @param template the messaging template to use
     */
    protected StompSimulatorNewsDispatcher(SimpMessagingTemplate template) {
        if(template == null) throw new IllegalArgumentException("template must not be null!");
        this.template = template;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        try{
            if(message instanceof DataEventNews){
                template.convertAndSend( TOPIC_NEWS, message );
            } else {
                unhandled( message );
            }
        }catch (Exception e){
            LOGGER.warn("Sending STOMP over web socket failed. Sender was " + sender(), e);
        }
    }



}
