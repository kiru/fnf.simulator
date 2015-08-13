package com.zuehlke.carrera.simulator.domain.api;

import com.zuehlke.carrera.racetrack.client.RaceTrackToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;

public class StompSimulatorApiAdapter implements SimulatorApiAdapter {
    private final RaceTrackToRelayConnection connection;

    public StompSimulatorApiAdapter(RaceTrackToRelayConnection connection) {
        this.connection = connection;
    }

    @Override
    public void announce(String whereAmIHosted) {
        connection.announce(whereAmIHosted);
    }

    @Override
    public void ensureConnection() {
        connection.ensureConnection();
    }

    @Override
    public void send(SensorEvent message) {
        connection.send(message);
    }
}
