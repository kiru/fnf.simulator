package com.zuehlke.carrera.simulator.services.adapter;

import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;

import java.util.function.Consumer;

public interface SimulatorApiAdapter {

    void announce(String whereAmIHosted);

    void ensureConnection();

    void send(SensorEvent message);

    void onPowerControl(Consumer<PowerControl> onPowerControl);

    void onRaceStart(Consumer<RaceStartMessage> onRaceStart);

    void onRaceStop(Consumer<RaceStopMessage> onRaceStop);
}
