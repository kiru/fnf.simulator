package com.zuehlke.carrera.simulator.domain.simulib;

import com.zuehlke.carrera.racetrack.client.RaceTrackToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.simulator.model.PilotInterface;

import java.util.function.Consumer;

public class StompPilotInterfaceFactory implements PilotInterfaceFactory {
    private final RaceTrackToRelayConnection connection;

    public StompPilotInterfaceFactory(RaceTrackToRelayConnection connection) {
        this.connection = connection;
    }

    @Override
    public PilotInterface create(Consumer<PowerControl> onPowerControl,
                                 Consumer<RaceStartMessage> onRaceStart,
                                 Consumer<RaceStopMessage> onRaceStop) {
        connection.onSpeedControl(onPowerControl);
        connection.onRaceStart(onRaceStart);
        connection.onRaceStop(onRaceStop);
        return new RelayToPilotAdapter(connection);
    }
}
