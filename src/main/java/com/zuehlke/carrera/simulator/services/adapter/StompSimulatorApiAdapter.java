package com.zuehlke.carrera.simulator.services.adapter;

import com.zuehlke.carrera.racetrack.client.RaceTrackToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;

import java.util.function.Consumer;

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

    @Override
    public void onPowerControl(Consumer<PowerControl> onPowerControl) {
        connection.onSpeedControl(onPowerControl);
    }

    @Override
    public void onRaceStart(Consumer<RaceStartMessage> onRaceStart) {
        connection.onRaceStart(onRaceStart);
    }

    @Override
    public void onRaceStop(Consumer<RaceStopMessage> onRaceStop) {
        connection.onRaceStop(onRaceStop);
    }
}
