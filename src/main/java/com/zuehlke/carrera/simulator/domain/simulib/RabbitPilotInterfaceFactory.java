package com.zuehlke.carrera.simulator.domain.simulib;

import com.zuehlke.carrera.api.SimulatorApi;
import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.simulator.model.PilotInterface;

import java.util.function.Consumer;

public class RabbitPilotInterfaceFactory implements PilotInterfaceFactory {
    private final SimulatorApi api;

    public RabbitPilotInterfaceFactory(SimulatorApi api) {
        this.api = api;
    }

    @Override
    public PilotInterface create(Consumer<PowerControl> onPowerControl,
                                 Consumer<RaceStartMessage> onRaceStart,
                                 Consumer<RaceStopMessage> onRaceStop) {
        api.onPowerControl(onPowerControl);
        api.onRaceStart(onRaceStart);
        api.onRaceStop(onRaceStop);
        return new SimulibRabbitApiAdapter(api);
    }
}
