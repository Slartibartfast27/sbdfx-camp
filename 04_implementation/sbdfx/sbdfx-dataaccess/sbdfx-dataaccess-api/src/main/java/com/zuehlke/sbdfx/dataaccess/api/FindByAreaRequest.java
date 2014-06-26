package com.zuehlke.sbdfx.dataaccess.api;

public class FindByAreaRequest extends BaseRequest {

    private Integer longitudeMin;
    private Integer longitudeMax;
    private Integer latitudeMin;
    private Integer latitudeMax;

    public Integer getLongitudeMin() {
        return longitudeMin;
    }

    public void setLongitudeMin(final Integer longitudeMin) {
        this.longitudeMin = longitudeMin;
    }

    public Integer getLongitudeMax() {
        return longitudeMax;
    }

    public void setLongitudeMax(final Integer longitudeMax) {
        this.longitudeMax = longitudeMax;
    }

    public Integer getLatitudeMin() {
        return latitudeMin;
    }

    public void setLatitudeMin(final Integer latitudeMin) {
        this.latitudeMin = latitudeMin;
    }

    public Integer getLatitudeMax() {
        return latitudeMax;
    }

    public void setLatitudeMax(final Integer latitudeMax) {
        this.latitudeMax = latitudeMax;
    }

}
