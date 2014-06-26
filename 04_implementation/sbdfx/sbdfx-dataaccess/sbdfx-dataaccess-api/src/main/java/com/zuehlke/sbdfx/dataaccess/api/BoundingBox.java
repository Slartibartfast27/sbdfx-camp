package com.zuehlke.sbdfx.dataaccess.api;

public class BoundingBox {

    private double longitudeMin;
    private double longitudeMax;
    private double latitudeMin;
    private double latitudeMax;

    public double getLongitudeMin() {
        return longitudeMin;
    }

    public void setLongitudeMin(final double longitudeMin) {
        this.longitudeMin = longitudeMin;
    }

    public double getLongitudeMax() {
        return longitudeMax;
    }

    public void setLongitudeMax(final double longitudeMax) {
        this.longitudeMax = longitudeMax;
    }

    public double getLatitudeMin() {
        return latitudeMin;
    }

    public void setLatitudeMin(final double latitudeMin) {
        this.latitudeMin = latitudeMin;
    }

    public double getLatitudeMax() {
        return latitudeMax;
    }

    public void setLatitudeMax(final double latitudeMax) {
        this.latitudeMax = latitudeMax;
    }

}
