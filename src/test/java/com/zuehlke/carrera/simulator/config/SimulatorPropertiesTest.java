package com.zuehlke.carrera.simulator.config;

import com.zuehlke.carrera.simulator.model.racetrack.RazorModel2;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *
 * @author Wolfgang Giersche
 *
 * This may seem strange: We need to start a full Spring App to actually see the
 * Properties being correctly picked up. This class serves as
 * a) unit test,
 * b) spring application,
 * c) and application context
 * all in one. Somewhat cool but still quite a hack. Any cleaner approach highly welcome.
 * <p/>
 * Another thing is that this is a statistical test, so it may fail once in 100'000 times.
 * Since this starts the application, it's going to bind to port 8090. Make sure that's
 * available when you in-ignore this
 *
 * I recommend to make this an integration test, where the port availability is part of the setup
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties({SimulatorProperties.class})  // loaded from classpath:/application.yml
public class SimulatorPropertiesTest {

    SimulatorProperties properties;

    @Test
    @Ignore
    public void testApplicationStart() {
        SpringApplication.run(SimulatorPropertiesTest.class);
    }

    /**
     * The SpringApplication will create a bean that requires the properties.
     * That's where the actual test sets in.
     */
    @Bean
    public RazorModel2 testRazorModel ( SimulatorProperties properties ) {

        this.properties = properties;

        Assert.assertNotNull ( properties );

        RazorModel2 model = new RazorModel2(properties.getRazor());

        testStationaryGyroz ( model );
        testGyrozInMotion ( model );
        testHitTheCurve( model );

        return null;
    }

    private void testStationaryGyroz(RazorModel2 model) {
        Gyro_z_Properties props = properties.getRazor().getGyroz();
        int sampleSize = 200;
        double [] gyroz_array = new double[sampleSize];
        double ri = 0.0;
        double v = 0.0;
        for ( int i = 0; i < sampleSize; i ++ ) {
            double gyroz = model.gyro_z(ri, v, 20 * i);
            gyroz_array[i] = gyroz;
        }
        double mean = StatUtils.mean( gyroz_array );
        double var = StatUtils.variance( gyroz_array);
        Assert.assertEquals ( props.getOffset(), mean, 1.0 );
        Assert.assertEquals ( 1.0, var, 0.3 );
    }

    private void testGyrozInMotion(RazorModel2 model) {
        Gyro_z_Properties props = properties.getRazor().getGyroz();
        int sampleSize = 200;
        double [] gyroz_array = new double[sampleSize];
        double ri = 0.0;
        double v = 200.0;
        for ( int i = 0; i < sampleSize; i ++ ) {
            double gyroz = model.gyro_z(ri, v, 20 * i);
            gyroz_array[i] = gyroz;
        }
        double mean = StatUtils.mean( gyroz_array );
        double stddev = Math.sqrt(StatUtils.variance(gyroz_array));
        Assert.assertEquals ( props.getOffset(), mean, 20 );
        Assert.assertEquals ( 150, stddev, 50 );
    }

    private void testHitTheCurve ( RazorModel2 model ) {

        int sampleSize = 200;
        double [] gyroz_array = new double[sampleSize];
        double ri = 1 / 30.0;
        double v = 300.0;
        model.gyro_z(0, v, 0);
        int curveLength = 25; // in number of samples
        for ( int i = 0; i < curveLength; i ++ ) {
            double gyroz = model.gyro_z(ri, v, 20 * (i + 1));
            gyroz_array[i] = gyroz;
        }
        for ( int i = curveLength; i < sampleSize; i ++ ) {
            double gyroz = model.gyro_z(0, v, 20 * (i + 1));
            gyroz_array[i] = gyroz;
        }
        double stddev = Math.sqrt(StatUtils.variance(gyroz_array));
        Assert.assertTrue ( stddev > 400 );


    }
}
