package com.zuehlke.carrera.simulator.recording;

import com.google.gson.Gson;
import com.zuehlke.carrera.relayapi.messages.RaceEventData;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import org.apache.commons.lang.StringUtils;
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
    private static final String OUR_RECORDING = "/recorded-data/perferct 2_1446813379464.json";

    public static RaceEventData readKuwaitData() {
        LOGGER.info("Reading data from {}", OUR_RECORDING);
        InputStream resourceAsStream = ParseRecordedData.class.getResourceAsStream(OUR_RECORDING);
        Objects.requireNonNull(resourceAsStream, "No file found");

        Gson gson = new Gson();
        return gson.fromJson(new InputStreamReader(resourceAsStream), RaceEventData.class);
    }

    public static void main(String[] args) {
        RaceEventData raceEvent = readKuwaitData();

        print(raceEvent.getSensorEvents());
        print(raceEvent.getVelocityMessages());
        print(raceEvent.getPowerControls());

        printVelocityMessageFormatted(raceEvent);
    }

    private static void printVelocityMessageFormatted(RaceEventData raceEvent) {
        List<VelocityMessage> velocityMessages = raceEvent.getVelocityMessages();

        VelocityMessage previous = new VelocityMessage();

        for (VelocityMessage velocityMessage : velocityMessages) {
            String time = StringUtils.leftPad(String.valueOf(velocityMessage.getT()), 6);
            String velocity = StringUtils.leftPad(String.valueOf(velocityMessage.getVelocity()), 6);
            String diffToPrevious = String.valueOf(Math.abs(previous.getT() - velocityMessage.getT()));

            System.out.printf("%sms %s %sms %n", time, velocity, diffToPrevious);
            previous = velocityMessage;
        }
    }

    private static <T> List<T> print(List<T> sensorEvents) {
        for (T sensorEvent : sensorEvents) {
            String toString = ReflectionToStringBuilder.toString(sensorEvent);
            System.out.println(toString);
        }
        return sensorEvents;
    }
}
