package com.zuehlke.carrera.simulator.racetrack;

import com.zuehlke.carrera.simulator.model.racetrack.FloatingAverage;
import org.junit.Assert;
import org.junit.Test;

public class FloatingAverageTest {

    private static final double delta = 0.00001;

    @Test
    public void testIt () {

        FloatingAverage f = new FloatingAverage(4);

        // 3333
        Assert.assertEquals(3, f.nextAverage(3), delta);
        // 7333
        Assert.assertEquals(4, f.nextAverage(7), delta);
        // 3733
        Assert.assertEquals(4, f.nextAverage(3), delta);
        // 7373
        Assert.assertEquals(5, f.nextAverage(7), delta);
        // 0737
        Assert.assertEquals(4.25, f.nextAverage(0), delta);
        // 0073
        Assert.assertEquals(2.5, f.nextAverage(0), delta);
        // 1007
        Assert.assertEquals(2, f.nextAverage(1), delta);

    }
}
