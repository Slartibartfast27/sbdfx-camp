/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.sbdfx.transport;

import com.zuehlke.sbdfx.biz.api.CitiesService;
import com.zuehlke.sbdfx.dataaccess.api.CitiesDao;
import com.zuehlke.sbdfx.domain.City;
import com.zuehlke.sbdfx.domain.IsoCode;
import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author cbu
 */
@Path("cities")
@RequestScoped
public class CitiesResource extends BaseResource {

    @EJB
    private CitiesDao dao;
    
    @EJB
    private CitiesService service;
    
    @GET
    @Path("Zuerich")
    public Response findZuerich() {
        City result = dao.findCity(IsoCode.SWITZERLAND, "8000");
        return ok(result);
    }

}
