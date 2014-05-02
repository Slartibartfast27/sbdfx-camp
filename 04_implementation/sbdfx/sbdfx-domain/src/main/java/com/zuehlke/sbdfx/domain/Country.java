/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.sbdfx.domain;


/**
 * 
 * @author cbu
 */
public class Country {

    public final static Country SWITZERLAND = new Country(IsoCode.SWITZERLAND, "Switzerland");

    private String isoCode;
    private String name;

    public Country(final String isoCode, final String name) {
        this.isoCode = isoCode;
        this.name = name;
    }

    public Country(final String isoCode) {
        this.isoCode = isoCode;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(final String isoCode) {
        this.isoCode = isoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return isoCode;
    }

}
