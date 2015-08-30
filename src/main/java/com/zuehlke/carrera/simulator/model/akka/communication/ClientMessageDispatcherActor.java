package com.zuehlke.carrera.simulator.model.akka.communication;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.relayapi.messages.PenaltyMessage;
import com.zuehlke.carrera.relayapi.messages.RoundPassedMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.carrera.simulator.model.PilotInterface;

public class ClientMessageDispatcherActor extends UntypedActor {
    private final PilotInterface connection;

    public static Props props(final String raceTrackId, final PilotInterface connection) {
        if (raceTrackId == null) {
            throw new IllegalArgumentException("raceTrackId must not be NULL!");
        }
        if (connection == null) {
            throw new IllegalArgumentException("relayConnectorService must not be NULL!");
        }
        return Props.create(ClientMessageDispatcherActor.class, () -> {
            return new ClientMessageDispatcherActor(connection);
        });
    }

    private ClientMessageDispatcherActor(PilotInterface connection) {
        this.connection = connection;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SensorEvent) {
            handleSensor((SensorEvent) message);
        } else if (message instanceof VelocityMessage) {
            handleVelocity((VelocityMessage) message);
        } else if (message instanceof PenaltyMessage) {
            handlePenalty((PenaltyMessage) message);
        } else if (message instanceof RoundPassedMessage) {
            handleRoundPassed((RoundPassedMessage) message);
        } else {
            unhandled(message);
        }
    }

    private void handleSensor(SensorEvent message) {
        connection.send(message);
    }

    private void handleVelocity(VelocityMessage message) {
        connection.send(message);
    }

    private void handlePenalty(PenaltyMessage message) {
        connection.send(message);
    }

    private void handleRoundPassed(RoundPassedMessage message) {
        connection.send(message);
    }
}
