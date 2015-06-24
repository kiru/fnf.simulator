package com.zuehlke.carrera.simulator.model;


import com.zuehlke.carrera.relayapi.messages.SensorEvent;

/**
 * Created by wgiersche on 06/09/14.
 */
public interface RecordProcessor {
    RecordProcessor add(double lateral);

    SensorEvent currentValue();
}
