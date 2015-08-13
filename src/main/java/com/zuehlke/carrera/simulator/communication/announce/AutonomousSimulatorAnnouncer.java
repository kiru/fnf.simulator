package com.zuehlke.carrera.simulator.communication.announce;

import com.zuehlke.carrera.simulator.domain.api.SimulatorApiAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class AutonomousSimulatorAnnouncer {
    private final SimulatorApiAdapter apiAdapter;
    private final WhereAmIHostedService whereAmIHostedService;

    @Autowired
    public AutonomousSimulatorAnnouncer(SimulatorApiAdapter apiAdapter, WhereAmIHostedService whereAmIHosted) {
        this.apiAdapter = apiAdapter;
        this.whereAmIHostedService = whereAmIHosted;
    }

    @Scheduled(fixedRate = 2000)
    public void announce() {
        apiAdapter.announce(whereAmIHostedService.getHttpEndpoint());
    }

    @Scheduled(fixedRate = 10000)
    public void ensureConnection() {
        apiAdapter.ensureConnection();
    }
}
