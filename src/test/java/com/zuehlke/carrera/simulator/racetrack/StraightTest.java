package com.zuehlke.carrera.simulator.racetrack;

import com.zuehlke.carrera.simulator.model.racetrack.Anchor;
import com.zuehlke.carrera.simulator.model.racetrack.Straight;
import com.zuehlke.carrera.simulator.model.racetrack.TrackDesign;
import org.junit.Assert;
import org.junit.Test;


public class StraightTest {

    @Test
    public  void testAnchorAtPos () {

        TrackDesign design = new TrackDesign();
        Anchor start = new Anchor( 45, 0, 0 );
        design.setInitialAnchor(start);
        design.straight(10).create();
        Straight straight = (Straight) design.getTrackData().get(0);
        Assert.assertEquals(10, straight.getLengthUntilEnd(), 0.000001);

        Assert.assertEquals(5 * Math.sqrt(.5), straight.findAnchorAt(5).getPosX(), 0.000001);
        Assert.assertEquals(-5 * Math.sqrt(.5), straight.findAnchorAt(5).getPosY(), 0.000001);
    }
}
