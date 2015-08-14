package com.zuehlke.carrera.simulator.config;

import com.zuehlke.carrera.api.SimulatorApi;
import com.zuehlke.carrera.api.SimulatorApiImpl;
import com.zuehlke.carrera.api.SimulibNewsApi;
import com.zuehlke.carrera.api.SimulibNewsApiImpl;
import com.zuehlke.carrera.api.channel.NewsChannelNames;
import com.zuehlke.carrera.api.channel.PilotToSimulatorChannelNames;
import com.zuehlke.carrera.api.channel.RaceChannelNames;
import com.zuehlke.carrera.api.client.Client;
import com.zuehlke.carrera.api.client.rabbit.RabbitClient;
import com.zuehlke.carrera.api.seralize.JacksonSerializer;
import com.zuehlke.carrera.api.seralize.Serializer;
import com.zuehlke.carrera.simulator.model.PilotInterface;
import com.zuehlke.carrera.simulator.model.akka.communication.NewsInterface;
import com.zuehlke.carrera.simulator.services.adapter.RabbitSimulatorApiAdapter;
import com.zuehlke.carrera.simulator.services.adapter.RabbitSimulibNewsAdapter;
import com.zuehlke.carrera.simulator.services.adapter.RabbitSimulibPilotAdapter;
import com.zuehlke.carrera.simulator.services.adapter.SimulatorApiAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"rabbit", "dev"})
public class RabbitCommunicationProtocolConfig {

    @Bean
    public SimulatorApiAdapter simulatorApiAdapter(SimulatorApi api, SimulatorProperties settings) {
        return new RabbitSimulatorApiAdapter(api, settings);
    }

    @Bean
    public NewsInterface newsInterface(SimulibNewsApi newsApi) {
        return new RabbitSimulibNewsAdapter(newsApi);
    }

    @Bean
    public PilotInterface pilotInterface(SimulatorApi simulatorApi) {
        return new RabbitSimulibPilotAdapter(simulatorApi);
    }

    @Bean
    public SimulibNewsApi simulibNewsApi(NewsChannelNames names, Client client, Serializer serializer) {
        return new SimulibNewsApiImpl(names, client, serializer);
    }

    @Bean
    public SimulatorApi simulatorApi(RaceChannelNames names, Client client, Serializer serializer) {
        return new SimulatorApiImpl(names, client, serializer);
    }

    @Bean
    public NewsChannelNames newsChannelNames() {
        return new NewsChannelNames();
    }

    @Bean
    public RaceChannelNames raceChannelNames(SimulatorProperties settings) {
        return new PilotToSimulatorChannelNames(settings.getName());
    }

    @Bean
    public Client client() {
        return new RabbitClient();
    }

    @Bean
    public Serializer serializer() {
        return new JacksonSerializer();
    }
}
