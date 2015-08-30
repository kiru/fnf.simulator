package com.zuehlke.carrera.simulator.config;

import com.zuehlke.carrera.simulator.communication.listen.SimulatorCommandListener;
import com.zuehlke.carrera.simulator.services.adapter.SimulatorApiAdapter;
import com.zuehlke.carrera.simulator.model.RaceTrackSimulatorSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultCommunicationProtocolConfig {

    @Bean
    public SimulatorCommandListener simulatorCommandListener(SimulatorApiAdapter apiAdapter,
                                                             RaceTrackSimulatorSystem simulatorSystem,
                                                             SimulatorProperties settings) {
        SimulatorCommandListener listener = new SimulatorCommandListener(simulatorSystem, settings);
        apiAdapter.onPowerControl(message -> listener.onPowerControl(message));
        apiAdapter.onRaceStart(message -> listener.onRaceStart(message));
        apiAdapter.onRaceStop(message -> listener.onRaceStop(message));
        return listener;
    }
}
