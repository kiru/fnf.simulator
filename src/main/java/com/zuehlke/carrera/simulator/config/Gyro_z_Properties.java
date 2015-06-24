package com.zuehlke.carrera.simulator.config;

public class Gyro_z_Properties {

    private double sigma0;
    private int floatingAverageSize;
    private double amplitudeFactorIntoCurve;
    private double amplitudeFactorWithinCurve;
    private double amplitudeSigmaContribution;
    private double factorDuration1;
    private double sigmaStationary;
    private double offset;

    public double getSigma0() {
        return sigma0;
    }

    public void setSigma0(double sigma0) {
        this.sigma0 = sigma0;
    }

    public int getFloatingAverageSize() {
        return floatingAverageSize;
    }

    public void setFloatingAverageSize(int floatingAverageSize) {
        this.floatingAverageSize = floatingAverageSize;
    }

    public double getAmplitudeFactorIntoCurve() {
        return amplitudeFactorIntoCurve;
    }

    public void setAmplitudeFactorIntoCurve(double amplitudeFactorIntoCurve) {
        this.amplitudeFactorIntoCurve = amplitudeFactorIntoCurve;
    }

    public double getAmplitudeFactorWithinCurve() {
        return amplitudeFactorWithinCurve;
    }

    public void setAmplitudeFactorWithinCurve(double amplitudeFactorWithinCurve) {
        this.amplitudeFactorWithinCurve = amplitudeFactorWithinCurve;
    }

    public double getAmplitudeSigmaContribution() {
        return amplitudeSigmaContribution;
    }

    public void setAmplitudeSigmaContribution(double amplitudeSigmaContribution) {
        this.amplitudeSigmaContribution = amplitudeSigmaContribution;
    }

    public double getFactorDuration1() {
        return factorDuration1;
    }

    public void setFactorDuration1(double factorDuration1) {
        this.factorDuration1 = factorDuration1;
    }

    public double getSigmaStationary() {
        return sigmaStationary;
    }

    public void setSigmaStationary(double sigmaStationary) {
        this.sigmaStationary = sigmaStationary;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }
}
