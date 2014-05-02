/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zuehlke.sbdfx.dataaccess.api;

import com.zuehlke.sbdfx.domain.City;
import com.zuehlke.sbdfx.domain.Country;
import com.zuehlke.sbdfx.domain.FeatureClass;
import com.zuehlke.sbdfx.domain.FeatureCode;

import javax.ejb.Local;

/**
 *
 * @author cbu
 */
@Local
public interface CitiesDao {
    
    City findZuerich();

    FeatureClass getOrCreateFeatureClass(String id);

    FeatureCode getOrCreateFeatureCode(String id);

    Country getOrCreateCountry(String isoCode);
}