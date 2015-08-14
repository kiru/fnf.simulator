package com.zuehlke.carrera.simulator.model.akka.communication;

public interface NewsInterface {

    void data(Object message);

    void tick(Object message);
}
