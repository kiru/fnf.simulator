package com.zuehlke.carrera.simulator.model.racetrack;

import com.zuehlke.carrera.relayapi.messages.RaceEventData;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.carrera.simulator.recording.ParseRecordedData;

/**
 * TODO Kiru: write decent comment
 *
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
        SensorEvent s = kuwaitRaceEventData.getSensorEvents().get(sensorEventIndex++);
        /**
         * The recorded events don't contain the timestamp. It only contains the value t ( diff to start time )
         * We create a new event where the time stamp is equal to t
         */
        return new SensorEvent(s.getRaceTrackId(), s.getA(), s.getG(), s.getM(), s.getT());
    }

    public VelocityMessage getNextVelocityMessage() {
        return kuwaitRaceEventData.getVelocityMessages().get(velocityMessageIndex++);
    }
}
