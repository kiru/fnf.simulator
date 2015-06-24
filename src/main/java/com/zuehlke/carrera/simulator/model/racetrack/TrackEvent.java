package com.zuehlke.carrera.simulator.model.racetrack;

import com.zuehlke.carrera.relayapi.messages.SensorEvent;

public class TrackEvent {

    private SensorEvent sensorEvent;
    private float ri;
    private float v;
    private float pos;

    public TrackEvent(SensorEvent sensorEvent, float ri, float v, float pos) {
        this.sensorEvent = sensorEvent;
        this.ri = ri;
        this.v = v;
        this.pos = pos;
    }

    public SensorEvent getSensorEvent() {
        return sensorEvent;
    }

    public float getRi() {
        return ri;
    }

    public float getV() {
        return v;
    }

    public float getPos() {
        return pos;
    }
}
