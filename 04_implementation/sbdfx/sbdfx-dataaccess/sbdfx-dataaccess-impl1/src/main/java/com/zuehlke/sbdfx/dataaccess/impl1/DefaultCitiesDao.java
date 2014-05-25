/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zuehlke.sbdfx.dataaccess.impl1;

import java.util.Map;

import javax.ejb.Stateless;

import com.google.common.collect.Maps;
import com.zuehlke.sbdfx.dataaccess.api.CitiesDao;
import com.zuehlke.sbdfx.domain.City;
import com.zuehlke.sbdfx.domain.Country;
import com.zuehlke.sbdfx.domain.FeatureClass;
import com.zuehlke.sbdfx.domain.FeatureCode;

/**
 * 
 * @author cbu
 */
@Stateless
public class DefaultCitiesDao implements CitiesDao {

    private Map<String, FeatureClass> featureClassCache = Maps.newLinkedHashMap(); 
    private Map<String, FeatureCode> featureCodeCache = Maps.newLinkedHashMap(); 
    private Map<String, Country> countryCache = Maps.newLinkedHashMap(); 
    
    @Override
    public City findZuerich() {
        final City zuerich = new City();
        zuerich.setCountry(Country.SWITZERLAND);
        zuerich.setName("Zuerich");
        zuerich.setPopulation(383_707);
        return zuerich;
    }

    @Override
    public FeatureClass getOrCreateFeatureClass(String id) {
        FeatureClass result = featureClassCache.get(id);
        if (result == null) {
            result = new FeatureClass(id);
            featureClassCache.put(id, result);
        }
        return result;
    }

    @Override
    public FeatureCode getOrCreateFeatureCode(String id) {
        FeatureCode result = featureCodeCache.get(id);
        if (result == null) {
            result = new FeatureCode(id);
            featureCodeCache.put(id, result);
        }
        return result;
    }

    @Override
    public Country getOrCreateCountry(String isoCode) {
        Country result = countryCache.get(isoCode);
        if (result == null) {
            result = new Country(isoCode);
            countryCache.put(isoCode, result);
        }
        return result;
    }

}
