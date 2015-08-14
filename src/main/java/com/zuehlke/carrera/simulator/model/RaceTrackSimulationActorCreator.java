package com.zuehlke.carrera.simulator.model;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;
import com.zuehlke.carrera.simulator.model.akka.RaceTrackSimulationActor;
import com.zuehlke.carrera.simulator.model.akka.communication.ClientMessageDispatcherActor;
import com.zuehlke.carrera.simulator.model.akka.communication.NewsInterface;
import com.zuehlke.carrera.simulator.model.akka.communication.SimulatorNewsDispatcherActor;
import com.zuehlke.carrera.simulator.model.racetrack.TrackPhysicsModel;

class RaceTrackSimulationActorCreator {
    private final String raceTrackId;
    private final ActorSystem actorSystem;
    private final SimulatorProperties properties;

    public RaceTrackSimulationActorCreator(String raceTrackId, ActorSystem actorSystem, SimulatorProperties
            properties) {
        this.raceTrackId = raceTrackId;
        this.actorSystem = actorSystem;
        this.properties = properties;
    }

    public ActorRef create(PilotInterface pilotChannel, NewsInterface newsChannel) {
        ActorRef clientMessageDispatchActor = createClientMessageDispatcherActor(pilotChannel);
        ActorRef simulatorNewsDispatchActor = createSimulatorNewsDispatcherActor(newsChannel);
        return createRaceTrackSimulationActor(clientMessageDispatchActor, simulatorNewsDispatchActor);
    }

    private ActorRef createRaceTrackSimulationActor(ActorRef clientMessageDispatchActor, ActorRef
            simulatorNewsDispatchActor) {
        // The central race track actor feeding a track, connected to sensor data dispatcher, news dispatcher
        return actorSystem.actorOf(RaceTrackSimulationActor.props(raceTrackId, clientMessageDispatchActor,
                simulatorNewsDispatchActor, new TrackPhysicsModel(), properties));
    }

    private ActorRef createSimulatorNewsDispatcherActor(NewsInterface newsChannel) {
        // dispatch the news from the race track actor to any stomp client
        return actorSystem.actorOf(SimulatorNewsDispatcherActor.props(newsChannel));
    }

    private ActorRef createClientMessageDispatcherActor(PilotInterface pilotChannel) {
        // dispatch the sensor data to the bot interface
        return actorSystem.actorOf(ClientMessageDispatcherActor.props(raceTrackId, pilotChannel));
    }
}
