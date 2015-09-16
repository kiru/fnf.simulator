package com.zuehlke.carrera.simulator.model.racetrack;

import com.zuehlke.carrera.relayapi.messages.RoundTimeMessage;

public interface RoundTimeListener {
    void onRoundPassed(RoundTimeMessage message);
}
