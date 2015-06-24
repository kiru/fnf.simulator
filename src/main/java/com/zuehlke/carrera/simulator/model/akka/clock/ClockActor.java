package com.zuehlke.carrera.simulator.model.akka.clock;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.simulator.model.akka.messages.ActorRegistration;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Accepts a Start message to start the clock at requested interval
 *
 * Created by wgiersche on 06/09/14.
 */
public class ClockActor extends UntypedActor {

    /**
     * Creates a new props for creating a ClockActor
     * @param tickPeriodMillis The interval of this clock in milliseconds
     * @return a Props
     */
    public static Props props ( final int tickPeriodMillis ) {
        return Props.create(ClockActor.class, () -> new ClockActor( tickPeriodMillis ));
    }

    private final int period;
    private List<ActorRef> actors = new ArrayList<>();
    private boolean on = false;

    public ClockActor(int tickPeriodMillis) {
        this.period = tickPeriodMillis;
    }


    @Override
    public void onReceive(Object message) throws Exception {

        if ( message instanceof StartClock) {
            handleStart( (StartClock)message );

        } else if ( message instanceof StopClock) {
            handleStop((StopClock) message);

        } else if ( message instanceof Tick) {
            handleTick( (Tick) message );

        } else if ( message instanceof ActorRegistration) {
            handleActorRegistration( (ActorRegistration) message );

        } else {
            unhandled(message);
        }
    }

    private void handleStart(StartClock start){
        if ( !on ) {
            on = true;
            scheduleNext();
        }
    }

    private void handleStop(StopClock stop) {
        on = false;
    }

    private void handleActorRegistration(ActorRegistration registration){
        actors.add ( registration.getActor());
    }

    private void handleTick(Tick tick) {

        for (ActorRef client : actors) {
            client.tell(new Tick(period), getSelf());
        }
        if ( on ) {
            scheduleNext();
        }
    }

    private void scheduleNext () {
        getContext().system().scheduler().scheduleOnce(
                Duration.create(period, TimeUnit.MILLISECONDS),
                getSelf(), new Tick(period), getContext().dispatcher(), null);
    }

}
