package com.zuehlke.carrera.simulator.model.akka.messages;

import akka.actor.ActorRef;

/**
 * Reverse Engineering note:
 * This class seems to be a marker-wrapper to keep another actor reference.
 * This "message" is sent, with the intent to add the given actorRef as a subscriber.
 *
 * Created by wgiersche on 06/09/14.
 */
public class ActorRegistration {

    private final ActorRef actorRef;


    public ActorRegistration(ActorRef actorRef) {
        this.actorRef = actorRef;
    }

    public ActorRef getActor() {
        return actorRef;
    }
}
