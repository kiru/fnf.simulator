package com.zuehlke.carrera.simulator.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix="razor")
public class RazorProperties {

    @NestedConfigurationProperty
    private Gyro_z_Properties gyroz;

    public Gyro_z_Properties getGyroz() {
        return gyroz;
    }

    public void setGyroz(Gyro_z_Properties gyroz) {
        this.gyroz = gyroz;
    }
}
