package com.zuehlke.carrera.simulator.domain.simulib;

import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;
import com.zuehlke.carrera.simulator.model.PilotInterface;
import com.zuehlke.carrera.simulator.model.RaceTrackSimulatorSystem;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.function.Consumer;

public class SimulatorSystemFactory {
    private final PilotInterfaceFactory pilotInterfaceFactory;
    private final SimulatorProperties settings;
    private final SimpMessagingTemplate stompDispatcher;

    public SimulatorSystemFactory(PilotInterfaceFactory pilotInterfaceFactory,
                                  SimulatorProperties settings,
                                  SimpMessagingTemplate stompDispatcher) {
        this.pilotInterfaceFactory = pilotInterfaceFactory;
        this.settings = settings;
        this.stompDispatcher = stompDispatcher;
    }

    public RaceTrackSimulatorSystem create(Consumer<PowerControl> onPowerControl,
                                           Consumer<RaceStartMessage> onRaceStart,
                                           Consumer<RaceStopMessage> onRaceStop) {
        return new RaceTrackSimulatorSystem(
                settings.getName(),
                createPilotInterface(onPowerControl, onRaceStart, onRaceStop),
                stompDispatcher,
                createDistribution(settings),
                settings);
    }

    private PilotInterface createPilotInterface(Consumer<PowerControl> onPowerControl,
                                                Consumer<RaceStartMessage> onRaceStart,
                                                Consumer<RaceStopMessage> onRaceStop) {
        return pilotInterfaceFactory.create(onPowerControl, onRaceStart, onRaceStop);
    }

    private NormalDistribution createDistribution(SimulatorProperties settings) {
        return new NormalDistribution(settings.getTickPeriod(), settings.getSigma());
    }
}
