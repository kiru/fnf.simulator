package com.zuehlke.carrera.simulator.model.racetrack;

import com.zuehlke.carrera.relayapi.messages.RaceEventData;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.carrera.simulator.recording.ParseRecordedData;

/**
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class PlaybackHandler {
    private RaceEventData kuwaitRaceEventData;

    private int sensorEventIndex = 0;
    private int velocityMessageIndex = 0;

    public PlaybackHandler() {
        kuwaitRaceEventData = ParseRecordedData.readKuwaitData();
    }

    public SensorEvent getNextSensorEvent() {
        return kuwaitRaceEventData.getSensorEvents().get(sensorEventIndex++);
    }

    public VelocityMessage getNextVelocityMessage() {
        return kuwaitRaceEventData.getVelocityMessages().get(velocityMessageIndex++);
    }
}
