package com.zuehlke.carrera.simulator.config;

import com.zuehlke.carrera.api.SimulatorApi;
import com.zuehlke.carrera.api.SimulatorApiImpl;
import com.zuehlke.carrera.api.channel.ChannelNames;
import com.zuehlke.carrera.api.channel.PilotToSimulatorChannelNames;
import com.zuehlke.carrera.api.client.Client;
import com.zuehlke.carrera.api.client.rabbit.RabbitClient;
import com.zuehlke.carrera.api.seralize.JacksonSerializer;
import com.zuehlke.carrera.api.seralize.Serializer;
import com.zuehlke.carrera.simulator.services.adapter.RabbitSimulatorApiAdapter;
import com.zuehlke.carrera.simulator.services.adapter.SimulatorApiAdapter;
import com.zuehlke.carrera.simulator.services.adapter.RabbitSimulibApiAdapter;
import com.zuehlke.carrera.simulator.model.PilotInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = { "rabbit", "dev" })
public class RabbitCommunicationProtocolConfig {

    @Bean
    public SimulatorApiAdapter simulatorApiAdapter(SimulatorApi api, SimulatorProperties settings) {
        return new RabbitSimulatorApiAdapter(api, settings);
    }

    @Bean
    public PilotInterface pilotInterface(SimulatorApi simulatorApi) {
        return new RabbitSimulibApiAdapter(simulatorApi);
    }

    @Bean
    public SimulatorApi simulatorApi(ChannelNames names, Client client, Serializer serializer) {
        return new SimulatorApiImpl(names, client, serializer);
    }

    @Bean
    public ChannelNames channelNames(SimulatorProperties settings) {
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
