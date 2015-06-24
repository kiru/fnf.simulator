package com.zuehlke.carrera.simulator.model.akka;

import akka.actor.UntypedActor;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.simulator.model.akka.messages.DataEventNews;

/**
 * Created by wgiersche on 06/09/14.
 */
public class Journalist extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {

        if ( message instanceof DataEventNews) {
            handleNews((DataEventNews) message);
        } else {
            unhandled(message);
        }
    }


    private void handleNews(DataEventNews message) {
        SensorEvent event = message.getEvent();

        float accelerationY =  event.getA()[1];

        String speed = printSpeed(message.getVelocity());
        String buffer = String.format( "%8.4f %40s - %8.4f", message.getVelocity(), speed, accelerationY);
        long k = (long)(70 + 8 * accelerationY);
        for ( int i = 0; i < 140; i ++ ) {
            String symbol = " ";
            if ( i == k ) {
                symbol = ".";
            } else if (i == 70) {
                symbol = "|";
            }
            buffer = buffer + symbol;
        }
        System.out.println ( buffer );

    }

    private String printSpeed ( double speed ) {
        String result = "";
         for ( int i = 0; i < 40; i++ ) {
             if ( speed * 5.0 > i ) {
                 result = result + "#";
             } else {
                 result = result + " ";
             }
        }
        return result;
    }
}
