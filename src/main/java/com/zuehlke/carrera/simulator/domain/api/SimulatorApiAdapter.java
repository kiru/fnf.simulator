package com.zuehlke.carrera.simulator.domain.api;

import com.zuehlke.carrera.relayapi.messages.SensorEvent;

public interface SimulatorApiAdapter {

    void announce(String whereAmIHosted);

    void ensureConnection();

    void send(SensorEvent message);
}
