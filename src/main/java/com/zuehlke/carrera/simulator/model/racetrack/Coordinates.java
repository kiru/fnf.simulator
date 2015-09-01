package com.zuehlke.carrera.simulator.model.racetrack;

/**
 * physical coordinates position and velocity
 */
public class Coordinates {

    private float position;
    private float velocity;

    public Coordinates(float position, float velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public float getPosition() {
        return position;
    }

    public float getVelocity() {
        return velocity;
    }
}
