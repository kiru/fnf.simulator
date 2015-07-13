package com.zuehlke.carrera.simulator.model.racetrack;

import com.zuehlke.carrera.simulator.config.RazorProperties;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.HashMap;
import java.util.Map;

/**
 *  a better model of the gyro z sensor. Replaces RazorModel
 */
public class RazorModel2 {

    private RazorProperties properties;

    private double lastRi;

    private FloatingAverage gyroz_floatingAverage;

    private double v_lastCurveEntry;
    private long t_lastCurveEntry;

    private enum Phase {
        STRAIGHT,
        INTO_CURVE,
        WITHIN_CURVE
    }

    private Map<Phase, Double> ampltitudes = new HashMap<>();

    private Phase phase = Phase.STRAIGHT;

    public RazorModel2 ( RazorProperties properties ) {
        this.properties = properties;
        gyroz_floatingAverage = new FloatingAverage(properties.getGyroz().getFloatingAverageSize());
        ampltitudes.put(Phase.STRAIGHT, properties.getGyroz().getOffset());
    }



    public double gyro_z ( double ri, double v, long timestamp ) {

        // Phase 1: sharp rise at the curve entry
        if ( ri != 0 && lastRi == 0 ) {
            enteredCurve(ri, v, timestamp);
        }

        if ( ri == 0 && lastRi != 0 ) {
            phase = Phase.STRAIGHT;
            lastRi = 0;
        }


        // Switched from left to right curve or vice verca
        if (ri != 0 && lastRi != 0 && (ri - lastRi < 0.1)) {
            enteredCurve(ri, v, timestamp);
        }

        // Phase 2: plateau during the rest of the curve
        if ( phase == Phase.INTO_CURVE ) {
            if (timestamp - t_lastCurveEntry > duration_phase1(v_lastCurveEntry)) {
                phase = Phase.WITHIN_CURVE;
            }
        }

        double amplitude = amplitute( phase );

        amplitude = gyroz_floatingAverage.nextAverage(amplitude);

        double sigma = sigma ( amplitude, v );
        RealDistribution distribution = new NormalDistribution( amplitude, sigma);

        return distribution.sample();
    }

    private void enteredCurve(final double ri, final double v, final long timestamp) {
        phase = Phase.INTO_CURVE;
        calculateNewAmplitudes ( ri, v );
        v_lastCurveEntry = v;
        t_lastCurveEntry = timestamp;
        lastRi = ri;
    }

    private double duration_phase1 ( double v0 ) {
        return properties.getGyroz().getFactorDuration1() / v0;
    }

    private double sigma(double amplitude, double v) {
        if ( v == 0 ) {
            return properties.getGyroz().getSigmaStationary();
        } else {
            return properties.getGyroz().getSigma0() + Math.abs(amplitude) * properties.getGyroz().getAmplitudeSigmaContribution();
        }
    }

    private void calculateNewAmplitudes(double ri, double v0 ) {

        ampltitudes.put(Phase.INTO_CURVE, properties.getGyroz().getAmplitudeFactorIntoCurve() * ri * v0);
        ampltitudes.put(Phase.WITHIN_CURVE, properties.getGyroz().getAmplitudeFactorWithinCurve() * ri * v0);
    }

    private double amplitute ( Phase phase ) {
        return ampltitudes.get(phase);
    }

}
