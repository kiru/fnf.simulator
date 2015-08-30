package com.zuehlke.carrera.simulator.model.akka.clock;


import com.zuehlke.carrera.relayapi.messages.StandardMessage;

public class Tick extends StandardMessage {
    private int millies;

    public Tick(int millies) {
        this.millies = millies;
    }

    public int getMillies() {
        return millies;
    }
}
