/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zuehlke.sbdfx.biz.api;

import com.zuehlke.sbdfx.domain.City;
import javax.ejb.Local;

/**
 *
 * @author cbu
 */
@Local
public interface CitiesService {
    
    void doSomethingWithACity( City city);
}
