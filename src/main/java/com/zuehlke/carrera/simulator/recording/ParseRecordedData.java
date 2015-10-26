package com.zuehlke.carrera.simulator.recording;

import com.google.gson.Gson;
import com.zuehlke.carrera.relayapi.messages.RaceEventData;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

/**
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class ParseRecordedData {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseRecordedData.class);
    private static final String FILE_NAME = "/recorded-data/Kobayashi-Kuwait-2015-10-6.json";

    public static RaceEventData readKuwaitData() {
        LOGGER.info("Reading data from {}", FILE_NAME);
        InputStream resourceAsStream = ParseRecordedData.class.getResourceAsStream(FILE_NAME);
        Objects.requireNonNull(resourceAsStream, "No file found");

        Gson gson = new Gson();
        return gson.fromJson(new InputStreamReader(resourceAsStream), RaceEventData.class);
    }

    public static void main(String[] args) {
        RaceEventData raceEvent = readKuwaitData();

        print(raceEvent.getSensorEvents());
        print(raceEvent.getVelocityMessages());
        print(raceEvent.getPowerControls());
    }

    private static <T> List<T> print(List<T> sensorEvents) {
        for (T sensorEvent : sensorEvents) {
            String x = ReflectionToStringBuilder.toString(sensorEvent);
            System.out.println(x);
        }
        return sensorEvents;
    }
}
