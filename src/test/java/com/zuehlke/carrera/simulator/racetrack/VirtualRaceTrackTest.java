package com.zuehlke.carrera.simulator.racetrack;

import com.zuehlke.carrera.relayapi.messages.RoundPassedMessage;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.carrera.simulator.config.Gyro_z_Properties;
import com.zuehlke.carrera.simulator.config.RazorProperties;
import com.zuehlke.carrera.simulator.config.SimulatorProperties;
import com.zuehlke.carrera.simulator.model.racetrack.TrackEvent;
import com.zuehlke.carrera.simulator.model.racetrack.TrackEventListener;
import com.zuehlke.carrera.simulator.model.racetrack.TrackPhysicsModel;
import com.zuehlke.carrera.simulator.model.racetrack.VirtualRaceTrack;
import org.junit.Test;

public class VirtualRaceTrackTest implements TrackEventListener {

    private long start;

    @Test
    public void testAll() {

        TrackPhysicsModel trackModel = new TrackPhysicsModel();

        SimulatorProperties simulatorProperties = new SimulatorProperties();
        RazorProperties razorProperties = new RazorProperties();
        simulatorProperties.setRazor(razorProperties);
        Gyro_z_Properties gyro_z_properties = new Gyro_z_Properties();
        gyro_z_properties.setOffset(5);
        gyro_z_properties.setSigmaStationary(1);
                gyro_z_properties.setSigma0(130);
                gyro_z_properties.setFloatingAverageSize(5);
                gyro_z_properties.setFactorDuration1(40000);
                gyro_z_properties.setAmplitudeSigmaContribution(0.083333);
                gyro_z_properties.setAmplitudeFactorIntoCurve(600);
                gyro_z_properties.setAmplitudeFactorWithinCurve(400);

        razorProperties.setGyroz(gyro_z_properties);

        VirtualRaceTrack track = new VirtualRaceTrack("test-virtual-track", trackModel, simulatorProperties);

        track.design()
                .straight(220)
                .lightBarrier("eins", 20, 300).asRoundStart()
                .curve(30, -4)
                .lightBarrier("zwei", 20, 300)
                .straight(220)
                .lightBarrier("drei", 20, 300)
                .curve(30, -4)
                .lightBarrier("vier", 20, 300)
                .create();

        track.addListener(this);
        track.addListener((VelocityMessage message) -> {
            System.out.println();
            System.out.println(" Velocity: " + message.getVelocity());
            System.out.println();
        });

        track.addListener((RoundPassedMessage message) -> {
            System.out.println();
            System.out.println(" Round passed at: " + message.getTimeStamp());
            System.out.println();
        });

        track.setPower(150);
        track.setPeriod(20); // 50 Hertz, like in real life
        track.setPosition(0);
        start = System.currentTimeMillis();
        track.doSomeRounds(1);

    }

    @Override
    public void onTrackEvent(TrackEvent event) {
        printSensorValues(event, event.getSensorEvent().getG()[2]);
    }

    private void printSensorValues(TrackEvent event, double value){
        String buffer = String.format ("%6d:       v: %4.0f", System.currentTimeMillis() - start, event.getV());
        long k = (long)(20 + 60.0 * value / 8000);
        for ( int i = 0; i < 100; i ++ ) {
            String symbol = " ";
            if ( i == k ) {
                symbol = "*";
            }
            buffer = buffer + symbol;
        }
        System.out.println ( buffer );
    }
}
