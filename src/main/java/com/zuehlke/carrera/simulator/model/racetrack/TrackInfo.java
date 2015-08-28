package com.zuehlke.carrera.simulator.model.racetrack;

import java.util.List;

/**
 * This class brings the structure of the track to the web client
 */
public class TrackInfo {

    String trackId;
    private List<TrackSection> sections;
    private double width;
    private double height;
    private Anchor initialAnchor;

    public TrackInfo(List<TrackSection> sections, String trackId,
                     double witdh, double height, Anchor initialAncor) {
        this.sections = sections;
        this.trackId = trackId;
        this.width = witdh;
        this.height = height;
        this.initialAnchor = initialAncor;
    }

    public List<TrackSection> getSections() {
        return sections;
    }

    public void setSections(List<TrackSection> sections) {
        this.sections = sections;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Anchor getInitialAnchor() {
        return initialAnchor;
    }

    public void setInitialAnchor(Anchor initialAnchor) {
        this.initialAnchor = initialAnchor;
    }
}
