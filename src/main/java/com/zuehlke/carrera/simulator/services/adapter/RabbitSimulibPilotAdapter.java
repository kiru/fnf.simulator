package com.zuehlke.carrera.simulator.services.adapter;

import com.zuehlke.carrera.api.SimulatorApi;
import com.zuehlke.carrera.relayapi.messages.*;
import com.zuehlke.carrera.simulator.model.PilotInterface;

/**
 * TODO: currently, this is named rabbit and only rabbit is supported. But the API will support STOMP in the future too
 */
public class RabbitSimulibPilotAdapter implements PilotInterface {
    private final SimulatorApi api;

    public RabbitSimulibPilotAdapter(SimulatorApi api) {
        this.api = api;
    }

    @Override
    public void send(SensorEvent sensorEvent) {
        api.sensor(sensorEvent);
    }

    @Override
    public void send(VelocityMessage velocityMessage) {
        api.velocity(velocityMessage);
    }

    @Override
    public void send(PenaltyMessage penaltyMessage) {
        api.penalty(penaltyMessage);
    }

    @Override
    public void send(RoundTimeMessage roundTimeMessage) {
        api.roundPassed(roundTimeMessage);
    }

    @Override
    public void ensureConnection(String url) {
        // currently not implemented
    }
}
