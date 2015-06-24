package com.zuehlke.carrera.simulator.model.racetrack;

import com.zuehlke.carrera.relayapi.messages.PenaltyMessage;

public interface PenaltyListener {
    void onPenaltyMessage(PenaltyMessage message);
}
