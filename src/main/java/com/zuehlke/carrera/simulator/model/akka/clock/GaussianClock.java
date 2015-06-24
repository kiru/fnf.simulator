package com.zuehlke.carrera.simulator.model.akka.clock;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.simulator.model.akka.messages.ActorRegistration;
import org.apache.commons.math3.distribution.RealDistribution;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Accepts a Start message to start the clock at requested interval
 *
 * Created by wgiersche on 06/09/14.
 */
public class GaussianClock extends UntypedActor {

    /**
     * Creates a new props for creating a ClockActor
     * @param distribution the Distribution for the periods between two ticks
     * @return a Props
     */
    public static Props props ( RealDistribution distribution ) {
        return Props.create(GaussianClock.class, () -> new GaussianClock( distribution ));
    }

    private final RealDistribution distribution;
    private List<ActorRef> actors = new ArrayList<>();
    private boolean on = false;

    public GaussianClock(RealDistribution distribution) {
        this.distribution = distribution;
    }


    @Override
    public void onReceive(Object message) throws Exception {

        if ( message instanceof StartClock) {
            handleStart();

        } else if ( message instanceof StopClock) {
            handleStop();

        } else if ( message instanceof Tick) {
            handleTick( (Tick) message );

        } else if ( message instanceof ActorRegistration) {
            handleActorRegistration( (ActorRegistration) message );

        } else {
            unhandled(message);
        }
    }

    private void handleStart(){
        if ( !on ) {
            on = true;
            scheduleNext();
        }
    }

    private void handleStop() {
        on = false;
    }

    private void handleActorRegistration(ActorRegistration registration){
        actors.add ( registration.getActor());
    }

    private void handleTick(Tick tick) {

        for (ActorRef client : actors) {
            client.tell(tick, getSelf());
        }
        if ( on ) {
            scheduleNext();
        }
    }

    private void scheduleNext () {
        int period = (int) distribution.sample();
        getContext().system().scheduler().scheduleOnce(
                Duration.create(period, TimeUnit.MILLISECONDS),
                getSelf(), new Tick(period), getContext().dispatcher(), null);
    }

}
