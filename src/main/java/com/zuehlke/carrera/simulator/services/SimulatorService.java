package com.zuehlke.carrera.simulator.services;

import com.zuehlke.carrera.racetrack.client.RaceTrackToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.*;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;
import com.zuehlke.carrera.simulator.model.RaceTrackSimulatorSystem;
import com.zuehlke.carrera.simulator.model.racetrack.TrackDesign;
import com.zuehlke.carrera.simulator.model.racetrack.TrackInfo;
import com.zuehlke.carrera.simulator.model.racetrack.TrackSection;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * Manages the racetrack simulator instance.
 */
@Service
@EnableScheduling
public class SimulatorService {

    private static final Logger LOG = LoggerFactory.getLogger(SimulatorService.class);

    private RaceTrackToRelayConnection raceTrackToRelayConnection;
    private RaceTrackSimulatorSystem raceTrackSimulatorSystem;

    private final EndpointService endpointService;
    private final SimulatorProperties settings;
    private final SimpMessagingTemplate stompDispatcher;

    @Autowired
    public SimulatorService(SimulatorProperties settings, SimpMessagingTemplate stompDispatcher, EndpointService endpointService  ){
        this.settings = settings;
        this.stompDispatcher = stompDispatcher;
        this.endpointService = endpointService;
    }

    @PostConstruct
    public void init(){
        raceTrackToRelayConnection =  new RaceTrackToRelayConnection(
                settings.getRelayUrl(),
                settings.getName(),
                RaceTrackType.SIMULATOR,
                "admin",
                "admin",
                this::firePowerControl,
                this::fireRaceStartEvent,
                this::fireRaceStopEvent);

        raceTrackSimulatorSystem = new RaceTrackSimulatorSystem(
                settings.getName(),
                new RelayToPilotAdapter(raceTrackToRelayConnection),
                stompDispatcher,
                new NormalDistribution(settings.getTickPeriod(), settings.getSigma()),
                settings);
    }

    @PreDestroy
    public void shutDownActorSystem () {
        raceTrackSimulatorSystem.shutdown();
    }

    public void startClock(){
        raceTrackSimulatorSystem.startClock();
    }

    public void stopClock() {
        raceTrackSimulatorSystem.stopClock();
    }

    /**
     * @return the race-track model, which can be used to draw the virtual race-track.
     */
    public TrackInfo getTrackInfo() {
        TrackDesign design = raceTrackSimulatorSystem.getTrackDesign();
        List < TrackSection> sections = design.getTrackData();
        String trackId = settings.getName();
        return new TrackInfo( sections, trackId, design.getBoundarywidth(),
                design.getBoudaryHeight(), design.getInitialAnchor() );
    }

    /**
    /**
     * @return the racetrack simulator system.
     * If there is currently no simulator system running, a new one is being created.
     */
    private synchronized RaceTrackSimulatorSystem getSimulatorSystem(){
        if(raceTrackSimulatorSystem == null) {

            LOG.info("Creating a new racetrack simulator system...");

            //raceTrackSimulatorSystem.start();
        }
        return raceTrackSimulatorSystem;
    }

    @Scheduled(fixedRate = 10000)
    public void ensureConnection() {
        raceTrackToRelayConnection.ensureConnection();
    }

    @Scheduled(fixedRate = 2000)
    public void announce() {
        raceTrackToRelayConnection.announce(endpointService.getHttpEndpoint());
    }

    /**
     * Send the given SensorEvent to the backend.
     */
    public boolean send(SensorEvent sensorEvent) {
        return raceTrackToRelayConnection.send(sensorEvent);
    }

    private void firePowerControl(PowerControl control){
        if(raceTrackSimulatorSystem != null) {
            raceTrackSimulatorSystem.setPower(control);
        }
    }

    private void fireRaceStartEvent ( RaceStartMessage message) {
        LOG.info("received race start message");
        if ( message.getTrackId().equals(settings.getName())) {
            if (raceTrackSimulatorSystem != null) {
                raceTrackSimulatorSystem.startRace(message);
            }
        } else {
            LOG.warn("received start message for another track. I am " + settings.getName() + ". Message is for " + message.getTrackId() + ". Ignoring message.");
        }
    }

    private void fireRaceStopEvent ( RaceStopMessage message) {
        if (message.getTrackId().equals(settings.getName())) {
            LOG.info("received race stop message");
            if (raceTrackSimulatorSystem != null) {
                raceTrackSimulatorSystem.stopRace(message);
            }
        } else {
            LOG.warn("received stop message for another track. I am " + settings.getName() + ". Message is for " + message.getTrackId() + ". Ignoring message.");
        }
    }

    public void powerup( int delta ) {
        raceTrackSimulatorSystem.powerup(delta);
    }
    public void powerdown( int delta ) {
        raceTrackSimulatorSystem.powerdown(delta);
    }

    public void reset() {
        raceTrackSimulatorSystem.reset();
    }

    public TrackInfo selectDesign(String trackDesign) {

        // will return the trackdesign and discard it, since we need the complete info.
        TrackDesign newDesign = raceTrackSimulatorSystem.selectDesign(trackDesign);

        TrackInfo trackInfo = getTrackInfo();

        return trackInfo;
    }
}
