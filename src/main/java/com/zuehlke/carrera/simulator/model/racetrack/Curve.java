package com.zuehlke.carrera.simulator.model.racetrack;

/**
 * Created by wgiersche on 06/09/14.
 */
public class Curve extends TrackSection {
    private final double radius;
    private final ORIENTATION orientation;
    private final double angle;
    private final Anchor finalAnchor;

    public Curve(double radius, int eighth, Anchor initialAnchor) {
        super(Math.abs(2.0 * Math.PI * 45 * eighth / 360 * radius), initialAnchor);
        double deg360 = 45 * eighth;
        assert (radius > 0);
        this.angle = Math.abs(deg360);
        this.radius = radius;
        this.orientation = deg360 > 0 ? ORIENTATION.LEFT : ORIENTATION.RIGHT;
        this.finalAnchor = calculateFinalAnchor();
    }

    private Anchor calculateFinalAnchor() {
        return calculateFinalAnchor( this.angle );
    }

    public Anchor calculateFinalAnchor ( double someAngle ) {

        Anchor i = super.initialAnchor;
        double angleR0 = Math.PI * i.getAngle360() / 180;

        int sgn = orientation == ORIENTATION.LEFT ? 1 : -1;

        double xCenter = i.getPosX() - sgn * radius * Math.sin(angleR0);
        double yCenter = i.getPosY() - sgn * radius * Math.cos(angleR0);

        // arc angels
        double ang0 = sgn * Math.PI/2 - angleR0;
        double ang1 = ang0 - sgn * someAngle * Math.PI / 180;

        double newAngle = i.getAngle360() + sgn * someAngle;

        return new Anchor (
                newAngle,
                xCenter + radius * Math.cos(ang1),
                yCenter + radius * Math.sin(ang1));
    }

    @Override
    public double invRadius() {
        int sign = orientation == ORIENTATION.LEFT? -1 : 1;
        return sign / radius;
    }

    @Override
    public Anchor getFinalAnchor() {
        return finalAnchor;
    }

    @Override
    public Anchor findAnchorAt(double position) {

        Anchor ia = initialAnchor;
        double distance = length - ( lengthUntilEnd - position );

        double deltaAngle = angle * distance / length;

        return calculateFinalAnchor(deltaAngle);
    }

    public enum ORIENTATION {
        LEFT,
        RIGHT
    }

    public double getRadius() {
        return radius;
    }

    public ORIENTATION getOrientation() {
        return orientation;
    }

    public double getAngle() {
        return angle;
    }
}
