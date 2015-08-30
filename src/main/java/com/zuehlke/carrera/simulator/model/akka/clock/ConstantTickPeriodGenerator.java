package com.zuehlke.carrera.simulator.model.akka.clock;

class ConstantTickPeriodGenerator implements TickPeriodGenerator {
    private final int tickPeriod;

    public ConstantTickPeriodGenerator(int tickPeriod) {
        this.tickPeriod = tickPeriod;
    }

    @Override
    public int nextTick() {
        return tickPeriod;
    }
}
