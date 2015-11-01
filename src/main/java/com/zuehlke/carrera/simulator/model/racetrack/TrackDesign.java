package com.zuehlke.carrera.simulator.model.racetrack;

import java.util.*;

/**
 * Represents the simulated race track design
 * <p>
 * Created by wgiersche on 06/09/14.
 */
public class TrackDesign {

    private String trackId = UUID.randomUUID().toString();
    private double length;
    private List<TrackSection> sections = new ArrayList<>();
    private Anchor initialAnchor = new Anchor(0, 0, 0);
    private double boundarywidth = 0;
    private double boudaryHeight = 0;

    // Map of section radius mapped by the position of the section end
    private TreeMap<Double, TrackSection> radialIndex = new TreeMap<>();


    public TrackDesign() {
    }

    public TrackDesign(boolean initWithDefault) {
        if (initWithDefault) {
            init();
        }
    }

    private Anchor getLastAnchor() {
        if (sections == null || sections.size() == 0) {
            return initialAnchor;
        } else {
            return sections.get(sections.size() - 1).getFinalAnchor();
        }
    }

    /**
     * convenience API to add a straight section to the design^
     *
     * @param length: the length of the section
     * @return the resulting track design
     */
    public TrackDesign straight(double length) {

        Anchor anchor = getLastAnchor();
        TrackSection section = new Straight(length, anchor);
        sections.add(section);
        return this;
    }

    /**
     * convenience API to add a curved section to the design
     *
     * @param radius:  the bending radius of the section
     * @param eighths: the angle in multiples of 45 deg
     * @return the resulting track design
     */
    public TrackDesign curve(double radius, int eighths) {

        // reasoning: with quarters only, boxing the track becomes a matter of boxing the anchors
        for (int i = 0; i < Math.abs(eighths); i++) {
            Anchor previous = getLastAnchor();
            TrackSection section = new Curve(radius, (int) (Math.signum(eighths)), previous);
            sections.add(section);
        }
        return this;
    }

    public TrackDesign lightBarrier(String id, double length, double speedLimit) {

        Anchor anchor = getLastAnchor();
        TrackSection section = new LightBarrier(length, anchor, id, speedLimit);
        sections.add(section);
        return this;
    }


    public TrackDesign create() {

        length = 0.0;
        for (TrackSection section : sections) {
            length += section.getLength();
            section.setLengthUntilEnd(length);
            radialIndex.put(length, section);
        }
        calculateBoundaryBox();
        return this;
    }

    private void calculateBoundaryBox() {

        double x = 0, y = 0;
        double xl = 0, xr = 0;
        double yu = 0, yl = 0;

        for (TrackSection section : sections) {
            x = section.getInitialAnchor().getPosX();
            y = section.getInitialAnchor().getPosY();

            xl = Math.min(x, xl);
            yu = Math.min(y, yu);
            xr = Math.max(x, xr);
            yl = Math.max(y, yl);
        }
        initialAnchor = new Anchor(0, -xl, -yu);

        boundarywidth = xr - xl;
        boudaryHeight = yl - yu;
    }

    /**
     * determine the inverse radius at the position. The value is negative for left curves
     *
     * @param pos the position as trajectorial distance from the start point
     * @return the inverse radius at the position
     */
    public double invRad(double pos) {
        return findSectionAt(pos).invRadius();
    }

    public TrackSection findSectionAt(double pos) {
        assert (pos <= length);
        for (Map.Entry<Double, TrackSection> entry : radialIndex.entrySet()) {
            if (entry.getKey() > pos) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException("Position " + pos + " is not within current design. Length is " + length);
    }

    public double getLength() {
        return length;
    }

    public void create(List<TrackSection> track) {
        sections = new Vector<>();
        sections.addAll(track);
        create();
    }

    public List<TrackSection> getTrackData() {

        return sections;
    }

    private void init() {
        this
                .straight(180)
                .lightBarrier("BA01", 40, 300).asRoundStart()
                .curve(30, -1)
                .curve(30, 1)
                .curve(50, -4)
                .curve(50, 2)
                .straight(60)
                .curve(30, 2)
                .curve(30, -4)
                .straight(340)
                .lightBarrier("BA02", 40, 300)
                .curve(30, -4)
                .lightBarrier("BA03", 40, 300)
                .straight(160)
                .lightBarrier("BA04", 40, 300)
                .curve(50, 2)
                .straight(60)
                .curve(50, 2)
                .lightBarrier("BA05", 40, 300)
                .straight(100)
                .lightBarrier("BA06", 40, 300)
                .curve(30, -1)
                .curve(30, 1)
                .straight(40)
                .curve(30, -2)
                .curve(50, -2)
                .straight(140)
                .create();
    }

    public Anchor findAnchorAt(double position) {
        TrackSection section = findSectionAt(position);

        return section.findAnchorAt(position);
    }

    public double getBoundarywidth() {
        return boundarywidth;
    }

    public double getBoudaryHeight() {
        return boudaryHeight;
    }

    public Anchor getInitialAnchor() {
        return initialAnchor;
    }

    public void setInitialAnchor(Anchor initialAnchor) {
        this.initialAnchor = initialAnchor;
    }

    public TrackDesign asRoundStart() {

        TrackSection section = sections.get(sections.size() - 1);
        if (!(section instanceof LightBarrier)) {
            throw new IllegalStateException("Can only take light barriers as round start");
        }
        ((LightBarrier) section).setRoundStart(true);
        return this;
    }

}
