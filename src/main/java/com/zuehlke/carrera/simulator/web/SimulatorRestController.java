package com.zuehlke.carrera.simulator.web;

import com.zuehlke.carrera.simulator.model.racetrack.TrackInfo;
import com.zuehlke.carrera.simulator.domain.SimulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/simulator")
public class SimulatorRestController {
    private final SimulatorService simulatorService;

    @Autowired
    public SimulatorRestController(SimulatorService simulatorService) {
        this.simulatorService = simulatorService;
    }

    @RequestMapping(value = "/track", method = RequestMethod.GET, produces = "application/json")
    public TrackInfo getTrack() {
        return simulatorService.getTrackInfo();
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public void startSimulator() {
        simulatorService.startClock();
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public void stopSimulator() {
        simulatorService.stopClock();
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public void resetSimulator() {
        simulatorService.reset();
    }

    @RequestMapping(value = "/powerup/{delta}", method = RequestMethod.POST)
    public void powerup(@PathVariable int delta) {
        simulatorService.powerup(delta);
    }

    @RequestMapping(value = "/powerdown/{delta}", method = RequestMethod.POST)
    public void powerdown(@PathVariable int delta) {
        simulatorService.powerdown(delta);
    }

    @RequestMapping(value = "/selectDesign", method = RequestMethod.POST, produces = "application/json")
    public TrackInfo selectDesign(@RequestBody String trackDesign) {
        return simulatorService.selectDesign(trackDesign);
    }
}