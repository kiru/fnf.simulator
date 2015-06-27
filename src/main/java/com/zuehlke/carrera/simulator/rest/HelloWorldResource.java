package com.zuehlke.carrera.simulator.rest;


import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class HelloWorldResource {

    private static final String template = "Welcome to the REST API, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(method= RequestMethod.GET)
    public String sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
        return counter.incrementAndGet() + String.format(template, name);
    }

}