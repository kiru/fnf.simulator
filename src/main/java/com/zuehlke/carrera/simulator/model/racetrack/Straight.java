package com.zuehlke.carrera.simulator.model.racetrack;

public class Straight extends TrackSection {

    protected final Anchor finalAnchor;
    public Straight(double length, Anchor initialAnchor ) {
        super(length, initialAnchor);
        finalAnchor = calculateFinalAnchor();
    }

    private Anchor calculateFinalAnchor() {

        Anchor i = super.initialAnchor;
        double angle = Math.PI * i.getAngle360() / 180;

        return new Anchor (
                i.getAngle360(),
                i.getPosX() + length * Math.cos(angle),
                i.getPosY() - length * Math.sin(angle));
    }

    @Override
    public double invRadius() {
        return 0;
    }

    public Anchor getFinalAnchor() {
        return finalAnchor;
    }

    @Override
    public Anchor findAnchorAt(double position) {
        Anchor ia = initialAnchor;
        double distanceFromStart = length - ( lengthUntilEnd - position );

        Anchor result = new Anchor(
                ia.getAngle360(),
                ia.getPosX() + distanceFromStart * Math.cos(ia.getAngle360() * Math.PI / 180 ),
                ia.getPosY() - distanceFromStart * Math.sin(ia.getAngle360() * Math.PI / 180 )
        );
        return result;
    }

    /**
     * calculate the distance between the two point represented by two anchors
     * @param s the anchor representing the start of the line
     * @param e the anchor representing the end of the line
     * @return the length of the straight line that connects s and e
     */
    double distance ( Anchor s, Anchor e ) {
        double dx = s.getPosX() - e.getPosX();
        double dy = s.getPosY() - e.getPosY();
        return Math.sqrt(dx * dx + dy * dy );
    }


}
