package com.zuehlke.carrera.simulator.model.racetrack;

/**
 *  provides a floating average keeping a history of a certain size
 *  the oldest entry is evicted and the new one taken into the history.
 *  Note that the very first value always initiates the history as if
 *  all former values also have been like that.
 */
public class FloatingAverage {

    private final double[] values;
    private int currentIndex = 0;
    private double sum;
    private double size;
    private boolean first = true;

    public FloatingAverage(int size) {
        this.size = size;
        values = new double[size];
    }

    public double nextAverage ( double nextValue ) {

        if ( first ) {
            for ( int i = 0; i < size; i++ ) {
                values[i] = nextValue;
                sum = size * nextValue;
            }
        }
        first = false;
        double former = values[currentIndex];
        values[currentIndex] = nextValue;
        sum -= former;
        sum += nextValue;
        currentIndex ++;
        if ( currentIndex == size ) {
            currentIndex = 0;
        }
        return sum / size;
    }

    public double currentAverage () {
        return sum / size;
    }

}
