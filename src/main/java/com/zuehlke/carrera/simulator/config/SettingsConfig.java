package com.zuehlke.carrera.simulator.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({SimulatorProperties.class})  // loaded from /resources/application.yml
public class SettingsConfig {

}
