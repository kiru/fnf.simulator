package com.zuehlke.carrera.simulator.services.adapter;

import com.zuehlke.carrera.racetrack.client.RaceTrackToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.*;
import com.zuehlke.carrera.simulator.model.PilotInterface;

public class StompSimulibPilotAdapter implements PilotInterface {

    private final RaceTrackToRelayConnection adaptee;

    public StompSimulibPilotAdapter(RaceTrackToRelayConnection connection) {
        this.adaptee = connection;
    }

    @Override
    public void send(SensorEvent message) {
        adaptee.send(message);
    }

    @Override
    public void send(VelocityMessage message) {
        adaptee.send(message);
    }

    @Override
    public void send(PenaltyMessage message) {
        adaptee.send(message);
    }

    @Override
    public void send(RoundTimeMessage message) {
        /* We chose to implement the pilot interface. But we can't send RoundTimeMessages
         * to the relay, because it's the relay to calculate and send them to the pilot.
         */
        throw new UnsupportedOperationException("NOT SUPPORTED");
    }

    @Override
    public void ensureConnection(String url) {
        adaptee.ensureConnection();
    }
}
