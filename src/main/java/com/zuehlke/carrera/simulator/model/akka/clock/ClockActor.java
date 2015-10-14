package com.zuehlke.carrera.simulator.model.akka.clock;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.simulator.model.akka.messages.ActorRegistration;
import org.apache.commons.math3.distribution.RealDistribution;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The sole purpose of this actor is to manage a clock and send ticks periodically
 * ( which are generated from given TickPeriodGenerator ) to all registered subscribers
 */
public class ClockActor extends UntypedActor {
    private final List<ActorRef> tickSubscribers = new ArrayList<>();
    private final TickPeriodGenerator tickPeriodGenerator;
    private boolean running = false;

    private ClockActor(TickPeriodGenerator tickPeriodGenerator) {
        this.tickPeriodGenerator = tickPeriodGenerator;
    }

    public static Props props(int tickPeriodMillis) {
        return Props.create(ClockActor.class, () -> {
            return new ClockActor(new ConstantTickPeriodGenerator(tickPeriodMillis));
        });
    }

    public static Props props(RealDistribution clockTickDistribution) {
        return Props.create(ClockActor.class, () -> {
            return new ClockActor(new RealDistributionTickPeriodGenerator(clockTickDistribution));
        });
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof StartClock) {
            handleStartClock();
        } else if (message instanceof StopClock) {
            handleStopClock();
        } else if (message instanceof Tick) {
            handleTick((Tick) message);
        } else if (message instanceof ActorRegistration) {
            handleActorRegistration((ActorRegistration) message);
        } else {
            unhandled(message);
        }
    }

    private void handleStartClock() {
        if (!running) {
            running = true;
            sendTickNotification();
        }
    }

    private void handleStopClock() {
        running = false;
    }

    private void handleActorRegistration(ActorRegistration registration) {
        tickSubscribers.add(registration.getActor());
    }

    private void handleTick(Tick tick) {
        for (ActorRef client : tickSubscribers) {
            client.tell(tick, getSelf());
        }
        if (running) {
            sendTickNotification();
        }
    }

    private void sendTickNotification() {
        int period = tickPeriodGenerator.nextTick();

        FiniteDuration timeToNextTickInMs = Duration.create(period, TimeUnit.MILLISECONDS);
        Tick nextTick = new Tick(period);
        ActorRef receiver = getSelf();

        getContext().system().scheduler().scheduleOnce(timeToNextTickInMs, receiver, nextTick, getContext().dispatcher(), null);
    }
}