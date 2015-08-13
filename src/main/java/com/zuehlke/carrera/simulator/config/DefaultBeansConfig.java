package com.zuehlke.carrera.simulator.config;

import com.zuehlke.carrera.simulator.domain.simulib.PilotInterfaceFactory;
import com.zuehlke.carrera.simulator.domain.simulib.SimulatorSystemFactory;
import com.zuehlke.carrera.simulator.model.PilotInterface;
import com.zuehlke.carrera.simulator.model.RaceTrackSimulatorSystem;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class DefaultBeansConfig {

    @Bean
    public SimulatorSystemFactory simulatorSystemFactory(PilotInterfaceFactory pilotInterfaceFactory,
                                                         SimulatorProperties settings,
                                                         SimpMessagingTemplate stompDispatcher) {
        return new SimulatorSystemFactory(pilotInterfaceFactory, settings, stompDispatcher);
    }
}
