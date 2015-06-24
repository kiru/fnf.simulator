package com.zuehlke.carrera.simulator.model.racetrack;

/**
 */
public class LightBarrier extends Straight {

    private String id;
    private double speedLimit;
    private boolean roundStart;

    public LightBarrier(double length, Anchor initialAnchor, String id, double speedLimit) {
        super(length, initialAnchor);
        this.id = id;
        this.speedLimit = speedLimit;
    }

    public String getId() {
        return id;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setRoundStart(boolean roundStart) {
        this.roundStart = roundStart;
    }

    public boolean isRoundStart() {
        return roundStart;
    }


}
