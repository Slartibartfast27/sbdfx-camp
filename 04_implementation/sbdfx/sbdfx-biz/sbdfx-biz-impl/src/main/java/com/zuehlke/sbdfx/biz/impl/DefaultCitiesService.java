/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zuehlke.sbdfx.biz.impl;

import com.zuehlke.sbdfx.biz.api.CitiesService;
import javax.ejb.Stateless;
import com.zuehlke.sbdfx.domain.City;

/**
 *
 * @author cbu
 */
@Stateless
public class DefaultCitiesService implements CitiesService {

    @Override
    public void doSomethingWithACity(City city) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
