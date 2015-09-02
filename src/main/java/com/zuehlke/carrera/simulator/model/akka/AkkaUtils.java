package com.zuehlke.carrera.simulator.model.akka;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class AkkaUtils {

    public static final long ASK_TIMEOUT = 5000;

    /**
     * @param callingClass the calling class
     * @param expectedType the type expected for the return value
     * @param actor        the actor to ask
     * @param message      the message containing the "question"
     * @param <T>          Using the Akka Ask-Pattern, send a query message
     *                     to the any of the actors referenced from here, and expect a answer message
     *                     in reasonable time.
     * @return the object that the receiving actor hopefully sends back right away.
     */
    public static <T> T askActor(Class<?> callingClass, Class<T> expectedType, ActorRef actor, Object message) {
        Duration durationAskTimeout = Duration.create(ASK_TIMEOUT, TimeUnit.MILLISECONDS);
        try {
            return expectedType.cast(Await.result(
                    Patterns.ask(actor, message, ASK_TIMEOUT),
                    durationAskTimeout));
        } catch (Exception e) {
            Logger LOGGER = LoggerFactory.getLogger(callingClass);
            LOGGER.error(String.format("Asking actor %s failed!", actor.toString()), e);
            throw new RuntimeException("Ask timed out");
        }
    }


}
