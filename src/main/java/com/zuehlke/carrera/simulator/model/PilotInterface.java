package com.zuehlke.carrera.simulator.model;

import com.zuehlke.carrera.relayapi.messages.*;

/**
 * The interface to the pilot. Describes what messages or event a pilot can deal with
 * A physical connection client that supposed to connect a simulator to a relay station
 * must implement this.
 */
public interface PilotInterface {

    void send(SensorEvent message);

    void send(VelocityMessage message);

    void send(PenaltyMessage message);

    void send(RoundTimeMessage message);

    void ensureConnection( String url );
}
