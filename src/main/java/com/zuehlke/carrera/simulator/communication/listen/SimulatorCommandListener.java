package com.zuehlke.carrera.simulator.communication.listen;

import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;
import com.zuehlke.carrera.simulator.model.RaceTrackSimulatorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;

public class SimulatorCommandListener implements Lifecycle {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorCommandListener.class);
    private final RaceTrackSimulatorSystem simulatorSystem;
    private final SimulatorProperties settings;

    public SimulatorCommandListener(RaceTrackSimulatorSystem simulatorSystem, SimulatorProperties settings) {
        this.simulatorSystem = simulatorSystem;
        this.settings = settings;
    }

    public void onPowerControl(PowerControl control) {
        simulatorSystem.setPower(control);
    }

    public void onRaceStart(RaceStartMessage message) {
        LOG.info("Received race start message");
        if (message.getTrackId().equals(settings.getName())) {
            simulatorSystem.startRace(message);
        } else {
            LOG.warn("received start message for another track. I am " + settings.getName() + ". Message is for " +
                    message.getTrackId() + ". Ignoring message.");
        }
    }

    public void onRaceStop(RaceStopMessage message) {
        if (message.getTrackId().equals(settings.getName())) {
            LOG.info("received race stop message");
            simulatorSystem.stopRace(message);
        } else {
            LOG.warn("received stop message for another track. I am " + settings.getName() + ". " +
                    "Message is for " + message.getTrackId() + ". Ignoring message.");
        }
    }

    @Override
    public void start() {
        LOG.info("Command listener started, waiting for messages");
    }

    @Override
    public void stop() {
        LOG.info("Command listener stopped, messages will no longer be dispatched");
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}
