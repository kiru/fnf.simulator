package com.zuehlke.carrera.simulator.services.adapter;

import com.zuehlke.carrera.api.channel.NewsChannelNames;
import com.zuehlke.carrera.simulator.model.akka.communication.NewsInterface;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class StompSimulibNewsAdapter implements NewsInterface {
    private final NewsChannelNames channelNames = new NewsChannelNames();
    private final SimpMessagingTemplate messagingTemplate;

    public StompSimulibNewsAdapter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void data(Object message) {
        messagingTemplate.convertAndSend(channelNames.data(), message);
    }

    @Override
    public void tick(Object message) {
        messagingTemplate.convertAndSend(channelNames.tick(), message);
    }
}
