package com.zuehlke.carrera.simulator.model.racetrack;

public class Anchor {

    private final double angle360;
    private final double posX;
    private final double posY;

    public Anchor(double angle360, double posX, double posY) {
        this.angle360 = angle360;
        this.posX = posX;
        this.posY = posY;
    }

    public double getAngle360() {
        return angle360;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public String toString() {
        return String.format("Anchor from ( x = %.2f, y = %.2f ) at %.0f degrees", posX, posY, angle360 );
    }
}
