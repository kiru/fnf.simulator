package com.zuehlke.carrera.simulator.model.racetrack;

import com.zuehlke.carrera.relayapi.messages.RaceEventData;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.carrera.simulator.recording.ParseRecordedData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

/**
 * TODO Kiru: write decent comment
 *
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class PlaybackHandler {

    private final List<SensorEvent> sensorEvents;
    private final List<VelocityMessage> velocityEvents;
    private int milliesSinceStart;
    private long startTimeAsMillisecond = System.currentTimeMillis();

    public PlaybackHandler() {
        RaceEventData kuwaitRaceEventData = ParseRecordedData.readKuwaitData();

        sensorEvents = new ArrayList<>(kuwaitRaceEventData.getSensorEvents());
        sensorEvents.sort((o1, o2) -> o2.getT() - o1.getT());

        velocityEvents = new ArrayList<>(kuwaitRaceEventData.getVelocityMessages());
        velocityEvents.sort((o1, o2) -> o2.getT() - o1.getT());
    }

    public void updateTime(int milliesDelta) {
        milliesSinceStart += milliesDelta;
    }

    public Optional<SensorEvent> getNextSensorEvents() {
        SensorEvent nextEventOrNull = getNextEventOrNull(sensorEvents, milliesSinceStart, SensorEvent::getT);
        if (nextEventOrNull == null) {
            return Optional.empty();
        } else {
            /**
             * The recorded events don't contain the timestamp. It only contains the value t ( diff to start time )
             * We create a new event where the time stamp is equal to t
             */
            SensorEvent value = new SensorEvent(nextEventOrNull.getRaceTrackId(),
                                                nextEventOrNull.getA(),
                                                nextEventOrNull.getG(),
                                                nextEventOrNull.getM(),
                                                nextEventOrNull.getT() + startTimeAsMillisecond);
            return Optional.of(value);
        }

    }


    public Optional<VelocityMessage> getNextVelocityMessage() {
        VelocityMessage nextEventOrNull =
            getNextEventOrNull(velocityEvents, milliesSinceStart, VelocityMessage::getT);
        if (nextEventOrNull != null) {
            nextEventOrNull.setTimeStamp(startTimeAsMillisecond + nextEventOrNull.getT());
        }
        return Optional.ofNullable(nextEventOrNull);
    }

    /**
     * TODO Kiru: comment
     */
    private <T> T getNextEventOrNull(List<T> sensorEvents, int milliesDelta, ToIntFunction<T> extractT) {
        for(int i = sensorEvents.size() - 1; i >= 0; i--) {
            T sensorEvent = sensorEvents.get(i);
            int t = extractT.applyAsInt(sensorEvent);
            boolean latestMillies = t <= milliesDelta;
            if (latestMillies) {
                return sensorEvents.remove(i);
            } else {
                return null;
            }
        }
        return null;
    }
}
