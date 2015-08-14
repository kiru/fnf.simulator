package com.zuehlke.carrera.simulator.config;

import com.zuehlke.carrera.simulator.model.PilotInterface;
import com.zuehlke.carrera.simulator.model.RaceTrackSimulatorSystem;
import com.zuehlke.carrera.simulator.model.akka.communication.NewsInterface;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class DefaultBeansConfig {

    @Bean
    public RaceTrackSimulatorSystem raceTrackSimulatorSystem(PilotInterface pilotInterface,
                                                             NewsInterface newsInterface,
                                                             SimulatorProperties settings) {
        return new RaceTrackSimulatorSystem(settings.getName(), pilotInterface, newsInterface,
                new NormalDistribution(settings.getTickPeriod(), settings.getSigma()), settings);
    }
}
