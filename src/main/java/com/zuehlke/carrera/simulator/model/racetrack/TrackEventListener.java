package com.zuehlke.carrera.simulator.model.racetrack;


import java.util.EventListener;

/**
 * A event listener which will receive subsequent sensor events
 * which happen on a relay-track
 */
public interface TrackEventListener extends EventListener {

    /**
     * Occurs when a sensor event has been received
     *
     * @param event: the event to listen to
     */
    void onTrackEvent(TrackEvent event);
}
