/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.sbdfx.domain;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class City {

    /**
     * integer id of record in geonames database
     */
    private int geonameid;
    /**
     * name of geographical point (utf8) varchar(200)
     */
    private String name;
    /**
     * name of geographical point in plain ascii characters, varchar(200)
     */
    private String asciiName;
    /**
     * comma separated, ascii names automatically transliterated, convenience
     * attribute from alternatename table, varchar(8000)
     */
    private List<String> alternatenames;
    /**
     * latitude in decimal degrees (wgs84)
     */
    private double latitude;
    /**
     * longitude in decimal degrees (wgs84)
     */
    private double longitude;
    /**
     * see http://www.geonames.org/export/codes.html, char(1)
     */
    private FeatureClass featureClass;
    /**
     * see http://www.geonames.org/export/codes.html, varchar(10)
     */
    private FeatureCode featureCode;
    /**
     * ISO-3166 2-letter country code, 2 characters
     */
    private Country country;
    /**
     * alternate country codes, comma separated, ISO-3166 2-letter country code,
     * 60 characters
     */
    private List<Country> alternateCountryCodes;
    /**
     * fipscode (final subject to change to iso code), see exceptions below, see
     * file admin1Codes.txt for display names of this code; varchar(20)
     */
    private String admin1Code;
    /**
     * code for the second administrative division, a county in the US, see file
     * admin2Codes.txt; varchar(80)
     */
    private String admin2Code;
    /**
     * code for third level administrative division, varchar(20)
     */
    private String admin3Code;
    /**
     * code for fourth level administrative division, varchar(20)
     */
    private String admin4Code;
    /**
     * bigint (8 byte int)
     */
    private long population;
    /**
     * in meters, integer
     */
    private Integer elevation;
    /**
     * digital elevation model, srtm3 or gtopo30, average elevation of 3''x3''
     * (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm
     * processed by cgiar/ciat.
     */
    private String dem;
    /**
     * the timezone id (final see file timeZone.txt) varchar(40)
     */
    private String timezone;
    /**
     * date of last modification in yyyy-MM-dd format
     */
    private Calendar modificationDate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public int getGeonameid() {
        return geonameid;
    }

    public void setGeonameid(int geonameid) {
        this.geonameid = geonameid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsciiName() {
        return asciiName;
    }

    public void setAsciiName(String asciiName) {
        this.asciiName = asciiName;
    }

    public List<String> getAlternatenames() {
        return alternatenames;
    }

    public void setAlternatenames(List<String> alternatenames) {
        this.alternatenames = alternatenames;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public FeatureClass getFeatureClass() {
        return featureClass;
    }

    public void setFeatureClass(FeatureClass featureClass) {
        this.featureClass = featureClass;
    }

    public FeatureCode getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(FeatureCode featureCode) {
        this.featureCode = featureCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Country> getAlternateCountryCodes() {
        return alternateCountryCodes;
    }

    public void setAlternateCountryCodes(List<Country> alternateCountryCodes) {
        this.alternateCountryCodes = alternateCountryCodes;
    }

    public String getAdmin1Code() {
        return admin1Code;
    }

    public void setAdmin1Code(String admin1Code) {
        this.admin1Code = admin1Code;
    }

    public String getAdmin2Code() {
        return admin2Code;
    }

    public void setAdmin2Code(String admin2Code) {
        this.admin2Code = admin2Code;
    }

    public String getAdmin3Code() {
        return admin3Code;
    }

    public void setAdmin3Code(String admin3Code) {
        this.admin3Code = admin3Code;
    }

    public String getAdmin4Code() {
        return admin4Code;
    }

    public void setAdmin4Code(String admin4Code) {
        this.admin4Code = admin4Code;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public Integer getElevation() {
        return elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    public String getDem() {
        return dem;
    }

    public void setDem(String dem) {
        this.dem = dem;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Calendar getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Calendar modificationDate) {
        this.modificationDate = modificationDate;
    }


    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this,  obj);
    }
}
