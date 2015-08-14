package com.zuehlke.carrera.simulator.model;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.zuehlke.carrera.simulator.model.akka.clock.ClockActor;
import com.zuehlke.carrera.simulator.model.akka.communication.NewsInterface;
import com.zuehlke.carrera.simulator.model.akka.communication.TickEventDispatcherActor;
import com.zuehlke.carrera.simulator.model.akka.messages.ActorRegistration;
import org.apache.commons.math3.distribution.RealDistribution;

class ClockActorCreator {
    private final ActorSystem actorSystem;

    public ClockActorCreator(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    public ActorRef create(RealDistribution tickDistribution, NewsInterface newsChannel) {
        ActorRef clockActor = createClockActor(tickDistribution);
        ActorRef tickDispatchActor = createTickDispatchActor(newsChannel);
        registerTickDispatcher(clockActor, tickDispatchActor);
        return clockActor;
    }

    private void registerTickDispatcher(ActorRef clockActor, ActorRef tickDispatcher) {
        clockActor.tell(new ActorRegistration(tickDispatcher), ActorRef.noSender());
    }

    private ActorRef createClockActor(RealDistribution tickDistribution) {
        return actorSystem.actorOf(ClockActor.props(tickDistribution));
    }

    private ActorRef createTickDispatchActor(NewsInterface newsChannel) {
        return actorSystem.actorOf(TickEventDispatcherActor.props(newsChannel));
    }
}
