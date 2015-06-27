package com.zuehlke.carrera.simulator.model.communication;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.relayapi.messages.PenaltyMessage;
import com.zuehlke.carrera.relayapi.messages.RoundPassedMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.carrera.simulator.model.PilotInterface;

/**
 * This actor dispatches relay-track event data to the pilot or proxy (relay).
 */
public class StompToPilotDispatcher extends UntypedActor {

    private final PilotInterface connection;


    /**
     *
     * @param connection the interface to some pilot counterpart
     * Create Props for an actor of this type.
     * @return a Props for creating this actor, which can then be further configured
     * (e.g. calling ‘.withDispatcher()‘ on it)
     */
    public static Props props(final String raceTrackId, final PilotInterface connection) {
        if(raceTrackId == null) throw new IllegalArgumentException("raceTrackId must not be NULL!");
        if(connection == null) throw new IllegalArgumentException("relayConnectorService must not be NULL!");

        return Props.create(StompToPilotDispatcher.class, () -> new StompToPilotDispatcher(connection));
    }


    private StompToPilotDispatcher(PilotInterface connection){
        this.connection = connection;
    }


    @Override
    public void onReceive(Object message) throws Exception {
        if ( message instanceof SensorEvent) {
            connection.send((SensorEvent) message);

        } else if ( message instanceof VelocityMessage ){
            connection.send((VelocityMessage) message);

        } else if ( message instanceof PenaltyMessage) {
            connection.send((PenaltyMessage) message);

        } else if ( message instanceof RoundPassedMessage ) {
            connection.send((RoundPassedMessage) message);

        } else {
            unhandled(message);
        }
    }

}
