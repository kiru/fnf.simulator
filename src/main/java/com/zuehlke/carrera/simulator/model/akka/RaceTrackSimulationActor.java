package com.zuehlke.carrera.simulator.model.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.relayapi.messages.*;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;
import com.zuehlke.carrera.simulator.model.*;
import com.zuehlke.carrera.simulator.model.akka.clock.Tick;
import com.zuehlke.carrera.simulator.model.akka.messages.ActorRegistration;
import com.zuehlke.carrera.simulator.model.akka.messages.CarPosition;
import com.zuehlke.carrera.simulator.model.akka.messages.DataEventNews;
import com.zuehlke.carrera.simulator.model.racetrack.TrackEvent;
import com.zuehlke.carrera.simulator.model.racetrack.TrackPhysicsModel;
import com.zuehlke.carrera.simulator.model.racetrack.VirtualRaceTrack;

/**
 * Race track Actor for the simulation.
 * <p>
 * Created by wgiersche on 06/09/14.
 */
public class RaceTrackSimulationActor extends UntypedActor {

    private final VirtualRaceTrack track;
    private final ActorRef newsDispatcher;
    private String currentTeam;
    private ActorRef pilot;
    private long penaltyStart;
    private boolean penaltyPhase;
    private SimulatorProperties properties;
    private RoundTimeMessage previousRoundTimeMessage;
    private boolean raceOngoing = false;

    public RaceTrackSimulationActor(String trackId, ActorRef pilot, ActorRef newsDispatcher,
                                    TrackPhysicsModel trackPhysicsModel, SimulatorProperties properties) {
        this.newsDispatcher = newsDispatcher;
        this.pilot = pilot;
        this.properties = properties;

        track = new VirtualRaceTrack(trackId, trackPhysicsModel, properties);
        track.defaultDesign();
        track.addListener((TrackEvent event) -> {
            getSelf().tell(event.getSensorEvent(), ActorRef.noSender());
        });
        track.addListener((VelocityMessage velo) -> {
            getSelf().tell(velo, ActorRef.noSender());
        });
        track.addListener((PenaltyMessage penalty) -> {
            getSelf().tell(penalty, ActorRef.noSender());
        });
        track.addListener((RoundTimeMessage roundTime) -> {
            getSelf().tell(roundTime, ActorRef.noSender());
        });
    }

    public static Props props(final String trackId, final ActorRef toPlayerDispatcher, final ActorRef newsDispatcher,
                              TrackPhysicsModel trackPhysicsModel, SimulatorProperties properties) {

        return Props.create(RaceTrackSimulationActor.class, () ->
            new RaceTrackSimulationActor(trackId, toPlayerDispatcher, newsDispatcher,
                trackPhysicsModel, properties));
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof PowerControl) {
            handlePowerControl((PowerControl) message);
        } else if (message instanceof PowerChange) {
            handlePowerChange((PowerChange) message);
        } else if (message instanceof Reset) {
            handleReset();
        } else if (message instanceof RaceStartMessage) {
            handleRaceStart((RaceStartMessage) message);
        } else if (message instanceof RaceStopMessage) {
            handleRaceStop((RaceStopMessage) message);
        } else if (message instanceof Tick) {
            handleClockTick((Tick) message);
        } else if (message instanceof CarPosition) {
            handleCarPosition((CarPosition) message);
        } else if (message instanceof ActorRegistration) {
            handlePlayerRegistration((ActorRegistration) message);
        } else if (message instanceof SensorEvent) {
            handleTrackEvent((SensorEvent) message);
        } else if (message instanceof VelocityMessage) {
            handleVelocityMessage((VelocityMessage) message);
        } else if (message instanceof PenaltyMessage) {
            handlePenaltyMessage((PenaltyMessage) message);
        } else if (message instanceof RoundTimeMessage) {
            handleRoundPassedMessage((RoundTimeMessage) message);
        } else if (message instanceof QueryTrackDesign) {
            handleQueryTrackDesign();
        } else if (message instanceof QuerySelectDesign) {
            handleQuerySelectDesign((QuerySelectDesign) message);
        } else if ( message instanceof PilotInterface ) {
            pilot.forward(message, getContext());
        } else if ( "ENSURE_CONNECTION".equals ( message )) {
            pilot.forward(message, getContext());
        } else {
            unhandled(message);
        }
    }

    private void handlePenaltyMessage(PenaltyMessage message) {
        if (pilot != null) {
            pilot.tell(message, getSelf());
            penaltyPhase = true;
            penaltyStart = System.currentTimeMillis();

            // on the real track this would be a screaching halt. Here we simply put the power down for a while
            handlePowerControl(new PowerControl(0, "penalty", "", penaltyStart));
        }
    }

    private void handleQuerySelectDesign(QuerySelectDesign message) {
        track.selectDesign(message.getTrackDesign());
        handleQueryTrackDesign();
    }

    private void handleQueryTrackDesign() {
        getSender().tell(track.getDesign(), getSelf());
    }

    private void handleReset() {
        track.reset();
        dispatchNews(new SensorEvent(currentTeam, System.currentTimeMillis()));
    }

    private void handleVelocityMessage(VelocityMessage message) {
        if (pilot != null) {
            pilot.tell(message, getSelf());
        }
    }

    private void handleRoundPassedMessage(RoundTimeMessage message) {
        if ( previousRoundTimeMessage != null ) {
            long duration = message.getTimestamp() - previousRoundTimeMessage.getTimestamp();
            message = new RoundTimeMessage(message.getTrack(), currentTeam, message.getTimestamp(), duration );
        }
        previousRoundTimeMessage = message;
        if (pilot != null) {
            pilot.tell(message, getSelf());
        }
    }

    private void handleTrackEvent(SensorEvent event) {
        if (pilot != null) {
            pilot.tell(event, getSelf());
        }
        dispatchNews(event);
    }

    /**
     * register internal player with track and vice versa
     *
     * @param registration the registration message
     */
    private void handlePlayerRegistration(ActorRegistration registration) {
        this.pilot = registration.getActor();
        pilot.tell(getSelf(), ActorRef.noSender());
    }

    private void handleCarPosition(CarPosition position) {
        track.setPosition(position.getNewPosition());
    }

    private void handleClockTick(Tick tick) {
        track.forward(tick.getMillies());
    }

    private void dispatchNews(SensorEvent event) {
        if (newsDispatcher != null) {

            newsDispatcher.tell(new DataEventNews(
                    currentTeam,
                    event,
                    track.getLastMeasuredVelocity(),
                    track.getPower(),
                    track.calculateCurrentAnchor(),
                    track.getRoundNumber()),
                getSelf());
        }
    }

    private void handlePowerControl(PowerControl control) {

        if ( ! raceOngoing ) {
            track.setPower(0);
            return;
        }
        // TODO: this shouldn't be here
        currentTeam = control.getTeamId();

        int newPower = control.getP();
        if (penaltyPhase && (System.currentTimeMillis() < penaltyStart + properties.getPenalty())) {
            newPower = 0;
        }
        if (newPower >= 0) {
            track.setPower(newPower);
        }
    }

    private void handlePowerChange(PowerChange message) {
        track.changePower(message.getDelta());
    }

    private void handleRaceStart(RaceStartMessage message) {
        raceOngoing = true;
        track.reset();
    }

    private void handleRaceStop(RaceStopMessage message) {
        track.setPower(0);
        raceOngoing = false;
    }

}