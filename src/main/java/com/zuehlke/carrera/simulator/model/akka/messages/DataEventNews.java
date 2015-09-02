package com.zuehlke.carrera.simulator.model.akka.messages;


import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.simulator.model.racetrack.Anchor;

/**
 * Holds together information of the (simulated) racetrack
 * to allow a client to draw / update the current relay situation.
 * <p>
 * Created by wgiersche on 06/09/14.
 */
public class DataEventNews {

    private final SensorEvent event;
    private final double velocity;
    private final int currentPower;
    private final Anchor position;
    private final String teamId;
    private final int roundNumber;

    public DataEventNews(String teamId, SensorEvent event, double velocity, int currentPower, Anchor position, int
            roundNumber) {
        this.teamId = teamId;
        this.event = event;
        this.velocity = velocity;
        this.currentPower = currentPower;
        this.position = position;
        this.roundNumber = roundNumber;
    }

    public SensorEvent getEvent() {
        return event;
    }

    public double getVelocity() {
        return velocity;
    }

    public Anchor getPosition() {
        return position;
    }

    public String getRaceTrackId() {
        return event == null ? null : event.getRaceTrackId();
    }

    public String getTeamId() {
        return teamId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getCurrentPower() {
        return currentPower;
    }
}
