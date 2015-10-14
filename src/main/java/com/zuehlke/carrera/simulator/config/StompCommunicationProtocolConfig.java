package com.zuehlke.carrera.simulator.config;

import com.zuehlke.carrera.racetrack.client.RaceTrackToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.RaceTrackType;
import com.zuehlke.carrera.simulator.model.PilotInterface;
import com.zuehlke.carrera.simulator.services.adapter.SimulatorApiAdapter;
import com.zuehlke.carrera.simulator.services.adapter.StompSimulatorApiAdapter;
import com.zuehlke.carrera.simulator.services.adapter.StompSimulibPilotAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("websocket")
public class StompCommunicationProtocolConfig {

    @Bean
    public SimulatorApiAdapter simulatorApiAdapter(RaceTrackToRelayConnection connection) {
        return new StompSimulatorApiAdapter(connection);
    }

    @Bean
    public PilotInterface pilotInterface(RaceTrackToRelayConnection raceTrackToRelayConnection) {
        return new StompSimulibPilotAdapter(raceTrackToRelayConnection);
    }

    @Bean
    public RaceTrackToRelayConnection raceTrackToRelayConnection(SimulatorProperties settings) {
        return new RaceTrackToRelayConnection(settings.getRelayUrl(), settings.getName(),
                RaceTrackType.SIMULATOR, "admin", "admin");
    }
}
