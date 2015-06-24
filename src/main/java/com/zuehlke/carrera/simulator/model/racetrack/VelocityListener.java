package com.zuehlke.carrera.simulator.model.racetrack;

import com.zuehlke.carrera.relayapi.messages.VelocityMessage;

public interface VelocityListener {
    void onVelocityMessage(VelocityMessage message);
}
