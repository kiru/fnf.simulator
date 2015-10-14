package com.zuehlke.carrera.simulator.services.adapter;

import com.zuehlke.carrera.api.SimulatorApi;
import com.zuehlke.carrera.relayapi.messages.*;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;

import java.util.function.Consumer;

public class RabbitSimulatorApiAdapter implements SimulatorApiAdapter {
    private final SimulatorApi api;
    private final SimulatorProperties settings;

    public RabbitSimulatorApiAdapter(SimulatorApi api, SimulatorProperties settings) {
        this.api = api;
        this.settings = settings;
    }

    @Override
    public void announce(String whereAmIHosted) {
        RaceTrack message = new RaceTrack(RaceTrackType.SIMULATOR, settings.getName());
        message.setLink(whereAmIHosted);
        api.announce(message);
    }

    @Override
    public void ensureConnection() {
        api.connect(settings.getRabbitUrl());
    }

    @Override
    public void send(SensorEvent message) {
        api.sensor(message);
    }

    @Override
    public void onPowerControl(Consumer<PowerControl> onPowerControl) {
        api.onPowerControl(onPowerControl);
    }

    @Override
    public void onRaceStart(Consumer<RaceStartMessage> onRaceStart) {
        api.onRaceStart(onRaceStart);
    }

    @Override
    public void onRaceStop(Consumer<RaceStopMessage> onRaceStop) {
        api.onRaceStop(onRaceStop);
    }
}
