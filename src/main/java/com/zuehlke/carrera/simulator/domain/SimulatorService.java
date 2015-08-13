package com.zuehlke.carrera.simulator.domain;

import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;
import com.zuehlke.carrera.simulator.domain.api.SimulatorApiAdapter;
import com.zuehlke.carrera.simulator.announce.WhereAmIHostedService;
import com.zuehlke.carrera.simulator.domain.simulib.SimulatorSystemFactory;
import com.zuehlke.carrera.simulator.model.RaceTrackSimulatorSystem;
import com.zuehlke.carrera.simulator.model.racetrack.TrackDesign;
import com.zuehlke.carrera.simulator.model.racetrack.TrackInfo;
import com.zuehlke.carrera.simulator.model.racetrack.TrackSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Service
public class SimulatorService {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorService.class);
    private final SimulatorApiAdapter apiAdapter;
    private final SimulatorSystemFactory simulatorSystemFactory;
    private final WhereAmIHostedService endpointService;
    private final SimulatorProperties settings;
    private RaceTrackSimulatorSystem raceTrackSimulatorSystem;

    @Autowired
    public SimulatorService(SimulatorApiAdapter apiAdapter,
                            SimulatorSystemFactory simulatorSystemFactory,
                            WhereAmIHostedService endpointService,
                            SimulatorProperties settings) {
        this.apiAdapter = apiAdapter;
        this.simulatorSystemFactory = simulatorSystemFactory;
        this.endpointService = endpointService;
        this.settings = settings;
    }

    @PostConstruct
    public void init() {
        raceTrackSimulatorSystem = simulatorSystemFactory.create(
                this::firePowerControl,
                this::fireRaceStartEvent,
                this::fireRaceStopEvent);
    }

    @PreDestroy
    public void shutDownActorSystem() {
        raceTrackSimulatorSystem.shutdown();
    }

    public void startClock() {
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
        List<TrackSection> sections = design.getTrackData();
        String trackId = settings.getName();
        return new TrackInfo(sections, trackId, design.getBoundarywidth(),
                design.getBoudaryHeight(), design.getInitialAnchor());
    }

    public void send(SensorEvent sensorEvent) {
        apiAdapter.send(sensorEvent);
    }

    private void firePowerControl(PowerControl control) {
        if (raceTrackSimulatorSystem != null) {
            raceTrackSimulatorSystem.setPower(control);
        }
    }

    private void fireRaceStartEvent(RaceStartMessage message) {
        LOG.info("received race start message");
        if (message.getTrackId().equals(settings.getName())) {
            if (raceTrackSimulatorSystem != null) {
                raceTrackSimulatorSystem.startRace(message);
            }
        } else {
            LOG.warn("received start message for another track. I am " + settings.getName() + ". Message is for " + message.getTrackId() + ". Ignoring message.");
        }
    }

    private void fireRaceStopEvent(RaceStopMessage message) {
        if (message.getTrackId().equals(settings.getName())) {
            LOG.info("received race stop message");
            if (raceTrackSimulatorSystem != null) {
                raceTrackSimulatorSystem.stopRace(message);
            }
        } else {
            LOG.warn("received stop message for another track. I am " + settings.getName() + ". Message is for " + message.getTrackId() + ". Ignoring message.");
        }
    }

    public void powerup(int delta) {
        raceTrackSimulatorSystem.powerup(delta);
    }

    public void powerdown(int delta) {
        raceTrackSimulatorSystem.powerdown(delta);
    }

    public void reset() {
        raceTrackSimulatorSystem.reset();
    }

    public TrackInfo selectDesign(String trackDesign) {
        // will return the trackdesign and discard it, since we need the complete info.
        // ???
        TrackDesign newDesign = raceTrackSimulatorSystem.selectDesign(trackDesign);
        return getTrackInfo();
    }
}
