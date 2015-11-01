 package com.zuehlke.carrera.simulator.model.racetrack;

 import com.zuehlke.carrera.relayapi.messages.SensorEvent;
 import org.junit.Test;

 import java.util.ArrayList;
 import java.util.List;

 import static java.util.Arrays.asList;
 import static org.hamcrest.MatcherAssert.assertThat;
 import static org.hamcrest.Matchers.is;
 import static org.hamcrest.Matchers.nullValue;

 /**
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class PlaybackHandlerTest  {

    @Test
    public void testName() throws Exception {

        int startTime = 1_000;
        SensorEvent event1 = new SensorEvent("rackTrack", startTime);
        event1.offSetTime(startTime); // t = 0

        SensorEvent event2 = new SensorEvent("rackTrack", startTime + 100);
        event2.offSetTime(startTime);

        SensorEvent event3 = new SensorEvent("rackTrack", startTime + 200);
        event3.offSetTime(startTime);

        SensorEvent event4 = new SensorEvent("rackTrack", startTime + 300);
        event4.offSetTime(startTime);

        List<SensorEvent> sensorEvents = new ArrayList<>(asList(event1, event2, event3, event4));
        sensorEvents.sort(((o1, o2) -> o2.getT() - o1.getT()));

        sensorEvents.forEach((x) -> System.out.println(x + " " + x.getT()));

        // getNextEvent(delta);
        assertThat(getNextEventOrNull(sensorEvents, 10), is(event1));
        assertThat(getNextEventOrNull(sensorEvents, 20), is(nullValue()));
        assertThat(getNextEventOrNull(sensorEvents, 30), is(nullValue()));

        assertThat(getNextEventOrNull(sensorEvents, 120), is(event2));
        assertThat(getNextEventOrNull(sensorEvents, 150), is(nullValue()));
        assertThat(getNextEventOrNull(sensorEvents, 199), is(nullValue()));

        assertThat(getNextEventOrNull(sensorEvents, 201), is(event3));
        assertThat(getNextEventOrNull(sensorEvents, 211), is(nullValue()));

        assertThat(getNextEventOrNull(sensorEvents, 300), is(event4));
        assertThat(getNextEventOrNull(sensorEvents, 300), is(nullValue()));
    }

     private SensorEvent getNextEventOrNull(List<SensorEvent> sensorEvents, int milliesDelta) {
         for(int i = sensorEvents.size() - 1; i >= 0; i--) {
             SensorEvent sensorEvent = sensorEvents.get(i);
             int t = sensorEvent.getT();
             if (t <= milliesDelta) {
                 return sensorEvents.remove(i);
             }else{
                 return null;
             }
         }
         return null;
     }
 }