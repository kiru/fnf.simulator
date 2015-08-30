package com.zuehlke.carrera.simulator.services;

import com.zuehlke.carrera.simulator.config.SimulatorProperties;
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
    private final RaceTrackSimulatorSystem simulatorSystem;
    private final SimulatorProperties settings;

    @Autowired
    public SimulatorService(RaceTrackSimulatorSystem simulatorSystem, SimulatorProperties settings) {
        this.simulatorSystem = simulatorSystem;
        this.settings = settings;
    }

    public void startClock() {
        simulatorSystem.startClock();
    }

    public void stopClock() {
        simulatorSystem.stopClock();
    }

    public void powerUp(int delta) {
        simulatorSystem.powerup(delta);
    }

    public void powerDown(int delta) {
        simulatorSystem.powerdown(delta);
    }

    public void reset() {
        simulatorSystem.reset();
    }

    public TrackInfo getTrackInfo() {
        TrackDesign design = simulatorSystem.getTrackDesign();
        List<TrackSection> sections = design.getTrackData();
        String trackId = settings.getName();
        return new TrackInfo(sections, trackId, design.getBoundarywidth(),
                design.getBoudaryHeight(), design.getInitialAnchor());
    }

    public TrackInfo selectDesign(String trackDesign) {
        // will return the trackdesign and discard it, since we need the complete info.
        simulatorSystem.selectDesign(trackDesign);
        return getTrackInfo();
    }

    @PreDestroy
    public void preDestroy() {
        simulatorSystem.shutdown();
    }
}
