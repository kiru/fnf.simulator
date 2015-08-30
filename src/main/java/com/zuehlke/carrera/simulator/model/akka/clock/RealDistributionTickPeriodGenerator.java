package com.zuehlke.carrera.simulator.model.akka.clock;

import org.apache.commons.math3.distribution.RealDistribution;

class RealDistributionTickPeriodGenerator implements TickPeriodGenerator {
    private final RealDistribution clockTickDistribution;

    public RealDistributionTickPeriodGenerator(RealDistribution clockTickDistribution) {
        this.clockTickDistribution = clockTickDistribution;
    }

    @Override
    public int nextTick() {
        return (int) clockTickDistribution.sample();
    }
}
