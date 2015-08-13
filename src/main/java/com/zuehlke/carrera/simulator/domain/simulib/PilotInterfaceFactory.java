package com.zuehlke.carrera.simulator.domain.simulib;

import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.simulator.model.PilotInterface;

import java.util.function.Consumer;

public interface PilotInterfaceFactory {

    PilotInterface create(Consumer<PowerControl> onPowerControl,
                          Consumer<RaceStartMessage> onRaceStart,
                          Consumer<RaceStopMessage> onRaceStop);
}
