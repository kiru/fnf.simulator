package com.zuehlke.carrera.simulator.services.adapter;

import com.zuehlke.carrera.api.SimulatorApi;
import com.zuehlke.carrera.relayapi.messages.PenaltyMessage;
import com.zuehlke.carrera.relayapi.messages.RoundPassedMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.carrera.simulator.model.PilotInterface;

/**
 * TODO: currently, this is named rabbit and only rabbit is supported. But the API will support STOMP in the future too
 */
public class RabbitSimulibApiAdapter implements PilotInterface {
    private final SimulatorApi api;

    public RabbitSimulibApiAdapter(SimulatorApi api) {
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
    public void send(RoundPassedMessage roundPassedMessage) {
        api.roundPassed(roundPassedMessage);
    }
}
