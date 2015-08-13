package com.zuehlke.carrera.simulator.domain;

import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;
import com.zuehlke.carrera.simulator.domain.api.SimulatorApiAdapter;
import com.zuehlke.carrera.simulator.model.RaceTrackSimulatorSystem;
import com.zuehlke.carrera.simulator.model.racetrack.TrackDesign;
import com.zuehlke.carrera.simulator.model.racetrack.TrackInfo;
import com.zuehlke.carrera.simulator.model.racetrack.TrackSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;

@Service
public class SimulatorService {
    private final SimulatorApiAdapter apiAdapter;
    private final SimulatorProperties settings;
    private final RaceTrackSimulatorSystem simulatorSystem;

    @Autowired
    public SimulatorService(SimulatorApiAdapter apiAdapter,
                            SimulatorProperties settings,
                            RaceTrackSimulatorSystem simulatorSystem) {
        this.apiAdapter = apiAdapter;
        this.settings = settings;
        this.simulatorSystem = simulatorSystem;
    }

    @PreDestroy
    public void shutDownActorSystem() {
        simulatorSystem.shutdown();
    }

    public void startClock() {
        simulatorSystem.startClock();
    }

    public void stopClock() {
        simulatorSystem.stopClock();
    }

    /**
     * @return the race-track model, which can be used to draw the virtual race-track.
     */
    public TrackInfo getTrackInfo() {
        TrackDesign design = simulatorSystem.getTrackDesign();
        List<TrackSection> sections = design.getTrackData();
        String trackId = settings.getName();
        return new TrackInfo(sections, trackId, design.getBoundarywidth(),
                design.getBoudaryHeight(), design.getInitialAnchor());
    }

    public void send(SensorEvent sensorEvent) {
        apiAdapter.send(sensorEvent);
    }

    public void powerup(int delta) {
        simulatorSystem.powerup(delta);
    }

    public void powerdown(int delta) {
        simulatorSystem.powerdown(delta);
    }

    public void reset() {
        simulatorSystem.reset();
    }

    public TrackInfo selectDesign(String trackDesign) {
        // will return the trackdesign and discard it, since we need the complete info.
        // ???
        TrackDesign newDesign = simulatorSystem.selectDesign(trackDesign);
        return getTrackInfo();
    }
}
