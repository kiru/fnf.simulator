package com.zuehlke.carrera.simulator.model;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.zuehlke.carrera.akkautils.AkkaUtils;
import com.zuehlke.carrera.racetrack.client.RaceTrackToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;
import com.zuehlke.carrera.simulator.model.akka.RaceTrackSimulationActor;
import com.zuehlke.carrera.simulator.model.akka.clock.GaussianClock;
import com.zuehlke.carrera.simulator.model.akka.clock.StartClock;
import com.zuehlke.carrera.simulator.model.akka.clock.StopClock;
import com.zuehlke.carrera.simulator.model.akka.messages.ActorRegistration;
import com.zuehlke.carrera.simulator.model.communication.StompSimulatorNewsDispatcher;
import com.zuehlke.carrera.simulator.model.communication.StompTickDispatcher;
import com.zuehlke.carrera.simulator.model.communication.StompToPilotDispatcher;
import com.zuehlke.carrera.simulator.model.racetrack.TrackDesign;
import com.zuehlke.carrera.simulator.model.racetrack.TrackPhysicsModel;
import org.apache.commons.math3.distribution.RealDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Represents a single simulated race track
 */
public class RaceTrackSimulatorSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaceTrackSimulatorSystem.class);

    private final String raceTrackId;

    private ActorSystem simulator;

    private ActorRef raceTrackActor;
    private ActorRef clock;

    private final SimpMessagingTemplate template;
    private final PilotInterface connection;

    public RaceTrackSimulatorSystem(String raceTrackId, PilotInterface connection,
                                    SimpMessagingTemplate template, RealDistribution tickDistribution,
                                    SimulatorProperties properties) {

        if(raceTrackId == null) throw new IllegalArgumentException("raceTrackId must not be null");
        if(template == null) throw new IllegalArgumentException("template must not be null");
        if(connection == null) throw new IllegalArgumentException("relayConnectorService must not be null");

        this.raceTrackId = raceTrackId;
        this.template = template;
        this.connection = connection;

        init(tickDistribution, properties);
    }

    private void init( RealDistribution tickDistribution, SimulatorProperties properties ) {

        LOGGER.info("Starting racetrack simulator with mean " + (int)tickDistribution.getNumericalMean() + " ms" );

        simulator = ActorSystem.create ("Simulator");
        clock = simulator.actorOf(GaussianClock.props( tickDistribution));

        // dispatch the tick to stomp clients
        ActorRef tickDispatcher = simulator.actorOf(StompTickDispatcher.props(template));
        clock.tell(new ActorRegistration(tickDispatcher), ActorRef.noSender());

        // dispatch the sensor data to the bot interface
        ActorRef sensorEventDispatcher = simulator.actorOf(StompToPilotDispatcher.props(raceTrackId, connection));

        // dispatch the news from the race track actor to any stomp client
        ActorRef newsDispatcher = simulator.actorOf(StompSimulatorNewsDispatcher.props(template));

        // The central race track actor feeding a track, connected to sensor data dispatcher, news dispatcher
        raceTrackActor = simulator.actorOf(RaceTrackSimulationActor.props(
                raceTrackId, sensorEventDispatcher, newsDispatcher,
                new TrackPhysicsModel(), properties));

        clock.tell(new ActorRegistration(raceTrackActor), ActorRef.noSender());
    }



    /**
     *  Starts the clock that ticks the simulator
     */
    public void startClock(){
        // Start the clock which drives the whole simulator
        clock.tell(new StartClock(), ActorRef.noSender());
    }

    /**
     *   Stops the clock that ticks the simulator
     */
    public void stopClock() {
        clock.tell(new StopClock(), ActorRef.noSender());
    }

    public void startRace ( RaceStartMessage message ) {
        startClock();
        raceTrackActor.tell(message, ActorRef.noSender());
    }

    public void stopRace ( RaceStopMessage message ) {
        stopClock();
        raceTrackActor.tell(message, ActorRef.noSender());
    }

    public void shutdown () {
        stopClock();
        simulator.shutdown();
    }

    /**
     * ask and the raceTrackActor and...
     * @return the current track design
     */
    public TrackDesign getTrackDesign() {

        return AkkaUtils.askActor(getClass(), TrackDesign.class, raceTrackActor, new QueryTrackDesign());
    }

    public void setPower(PowerControl power) {
        raceTrackActor.tell(power, ActorRef.noSender());
    }

    public void powerup(int delta) {
        raceTrackActor.tell(new PowerChange(delta), ActorRef.noSender());
    }

    public void powerdown(int delta) {
        raceTrackActor.tell(new PowerChange(-delta), ActorRef.noSender());
    }

    public void reset () {
        raceTrackActor.tell(new Reset(), ActorRef.noSender());
    }

    // select design and make sure we don't return before it's done.
    public TrackDesign selectDesign(String trackDesign) {
        return AkkaUtils.askActor(getClass(), TrackDesign.class, raceTrackActor, new QuerySelectDesign(trackDesign));

    }
}
