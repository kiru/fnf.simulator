package com.zuehlke.carrera.simulator.model.akka.communication;

public interface NewsInterface {

    void send(String channel, Object message);
}
