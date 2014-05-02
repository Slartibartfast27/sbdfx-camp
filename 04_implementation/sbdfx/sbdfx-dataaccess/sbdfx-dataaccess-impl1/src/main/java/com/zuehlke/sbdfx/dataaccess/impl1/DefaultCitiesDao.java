/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zuehlke.sbdfx.dataaccess.impl1;

import com.zuehlke.sbdfx.dataaccess.api.CitiesDao;
import com.zuehlke.sbdfx.domain.City;
import com.zuehlke.sbdfx.domain.Country;
import com.zuehlke.sbdfx.domain.IsoCode;
import javax.ejb.Stateless;

/**
 *
 * @author cbu
 */
@Stateless
public class DefaultCitiesDao implements CitiesDao {

    @Override
    public City findCity(String countryIsoCode, String zip) {
        final String SWITZERLAND = IsoCode.SWITZERLAND;
        if (SWITZERLAND.equalsIgnoreCase(countryIsoCode) && "8000".equals(zip)) {
            City zuerich = new City();
            zuerich.setCountry(Country.SWITZERLAND);
            zuerich.setName("Zuerich");
            zuerich.setZip(zip);
            return zuerich;
        }
        throw new UnsupportedOperationException("Not supported yet: " + countryIsoCode + " / " +  zip);
    }

}
