/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.sbdfx.transport;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.enterprise.context.RequestScoped;

/**
 * REST Web Service
 *
 * @author cbu
 */
@Path("cities")
@RequestScoped
public class CitiesResource extends BaseResource {

    @GET
    public String findCities() {
        return "Cities-Result";
    }

}
