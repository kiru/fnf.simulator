package com.zuehlke.carrera.simulator.model.akka.messages;

import akka.actor.ActorRef;

/**
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
