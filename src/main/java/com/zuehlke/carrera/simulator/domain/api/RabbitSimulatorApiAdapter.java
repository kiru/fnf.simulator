package com.zuehlke.carrera.simulator.domain.api;

import com.zuehlke.carrera.api.SimulatorApi;
import com.zuehlke.carrera.relayapi.messages.RaceTrack;
import com.zuehlke.carrera.relayapi.messages.RaceTrackType;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;

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
        // TODO: move rabbit queue location to config
        api.connect("localhost");
    }

    @Override
    public void send(SensorEvent message) {
        api.sensor(message);
    }
}
