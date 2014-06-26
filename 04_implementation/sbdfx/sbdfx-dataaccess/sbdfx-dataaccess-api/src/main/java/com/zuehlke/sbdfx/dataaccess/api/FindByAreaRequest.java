package com.zuehlke.sbdfx.dataaccess.api;

public class FindByAreaRequest extends BaseRequest {

    private Double longitudeMin;
    private Double longitudeMax;
    private Double latitudeMin;
    private Double latitudeMax;

    public Double getLongitudeMin() {
        return longitudeMin;
    }

    public void setLongitudeMin(final Double longitudeMin) {
        this.longitudeMin = longitudeMin;
    }

    public Double getLongitudeMax() {
        return longitudeMax;
    }

    public void setLongitudeMax(final Double longitudeMax) {
        this.longitudeMax = longitudeMax;
    }

    public Double getLatitudeMin() {
        return latitudeMin;
    }

    public void setLatitudeMin(final Double latitudeMin) {
        this.latitudeMin = latitudeMin;
    }

    public Double getLatitudeMax() {
        return latitudeMax;
    }

    public void setLatitudeMax(final Double latitudeMax) {
        this.latitudeMax = latitudeMax;
    }

}
