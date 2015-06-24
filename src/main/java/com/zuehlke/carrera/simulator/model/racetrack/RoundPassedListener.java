package com.zuehlke.carrera.simulator.model.racetrack;

import com.zuehlke.carrera.relayapi.messages.RoundPassedMessage;

public interface RoundPassedListener {
    void onRoundPassed(RoundPassedMessage message);
}
