package com.zuehlke.carrera.simulator.domain.simulib;

import com.zuehlke.carrera.simulator.config.SimulatorProperties;
import com.zuehlke.carrera.simulator.model.PilotInterface;
import com.zuehlke.carrera.simulator.model.RaceTrackSimulatorSystem;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class SimulatorSystemFactory {
    private final PilotInterface pilotInterface;
    private final SimulatorProperties settings;
    private final SimpMessagingTemplate stompDispatcher;

    public SimulatorSystemFactory(PilotInterface pilotInterface,
                                  SimulatorProperties settings,
                                  SimpMessagingTemplate stompDispatcher) {
        this.pilotInterface = pilotInterface;
        this.settings = settings;
        this.stompDispatcher = stompDispatcher;
    }

    public RaceTrackSimulatorSystem create() {
        return new RaceTrackSimulatorSystem(
                settings.getName(),
                pilotInterface,
                stompDispatcher,
                createDistribution(settings),
                settings);
    }

    private NormalDistribution createDistribution(SimulatorProperties settings) {
        return new NormalDistribution(settings.getTickPeriod(), settings.getSigma());
    }
}
