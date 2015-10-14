package com.zuehlke.carrera.simulator;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class SimulatorApp {

    public static void main(String[] args) {
        SpringApplication.run(SimulatorApp.class, args);
    }
}