package com.zuehlke.carrera.simulator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Simulator Settings
 */
@ConfigurationProperties(prefix="simulator") // see: /resources/application.yml
public class SimulatorProperties {

    private String relayUrl;
    private String name;
    private int penalty;
    private int tickPeriod;
    private double sigma;

    @NestedConfigurationProperty
    private RazorProperties razor;



    public String getRelayUrl() {
        return relayUrl;
    }

    public int getTickPeriod() {
        return tickPeriod;
    }

    public String getName() {
        return name;
    }

    public void setRelayUrl(String relayUrl) {
        this.relayUrl = relayUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTickPeriod(int tickPeriod) {
        this.tickPeriod = tickPeriod;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public RazorProperties getRazor() {
        return razor;
    }

    public void setRazor(RazorProperties razor) {
        this.razor = razor;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }
}