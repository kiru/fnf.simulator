package com.zuehlke.carrera.simulator.services.adapter;

import com.zuehlke.carrera.api.SimulibNewsApi;
import com.zuehlke.carrera.simulator.model.akka.communication.NewsInterface;

public class RabbitSimulibNewsAdapter implements NewsInterface {
    private final SimulibNewsApi newsApi;

    public RabbitSimulibNewsAdapter(SimulibNewsApi newsApi) {
        this.newsApi = newsApi;
    }

    @Override
    public void data(Object message) {
        newsApi.data(message);
    }

    @Override
    public void tick(Object message) {
        newsApi.tick(message);
    }
}
