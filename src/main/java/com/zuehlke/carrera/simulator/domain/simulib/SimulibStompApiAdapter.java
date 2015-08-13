package com.zuehlke.carrera.simulator.domain.simulib;

import com.zuehlke.carrera.racetrack.client.RaceTrackToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.PenaltyMessage;
import com.zuehlke.carrera.relayapi.messages.RoundPassedMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.carrera.simulator.model.PilotInterface;

public class SimulibStompApiAdapter implements PilotInterface {

    private final RaceTrackToRelayConnection adaptee;

    public SimulibStompApiAdapter(RaceTrackToRelayConnection connection) {
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
    public void send(RoundPassedMessage message) {
        adaptee.send(message);
    }
}
