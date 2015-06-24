package com.zuehlke.carrera.simulator.model.akka.messages;


public class CarPosition {

    private final double newPosition;

    public CarPosition(double newPosition) {
        this.newPosition = newPosition;
    }

    public double getNewPosition() {
        return newPosition;
    }
}
