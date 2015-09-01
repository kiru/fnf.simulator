package com.zuehlke.carrera.simulator.model;

/**
 * used only by the simulator itself
 */
public class PowerChange {
    private final int delta;

    public PowerChange(int delta) {
        this.delta = delta;
    }

    public int getDelta() {
        return delta;
    }
}
