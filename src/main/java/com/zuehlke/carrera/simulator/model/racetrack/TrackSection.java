package com.zuehlke.carrera.simulator.model.racetrack;

/**
 * Base class for all track sections.
 * A relay-track consists of many connected TrackSections.
 * The final Anchor is the point where the next section will start.
 * <p>
 * Created by wgiersche on 06/09/14.
 */
public abstract class TrackSection {

    protected final double length;
    protected final Anchor initialAnchor;
    protected double lengthUntilEnd;

    protected TrackSection(double length, Anchor initialAnchor) {
        this.length = length;
        this.initialAnchor = initialAnchor;
    }

    public abstract double invRadius();

    public abstract Anchor getFinalAnchor();

    public abstract Anchor findAnchorAt(double position);

    public double getLength() {
        return length;
    }

    public double getLengthUntilEnd() {
        return lengthUntilEnd;
    }

    public void setLengthUntilEnd(double lengthUntilEnd) {
        this.lengthUntilEnd = lengthUntilEnd;
    }

    public Anchor getInitialAnchor() {
        return initialAnchor;
    }


}
