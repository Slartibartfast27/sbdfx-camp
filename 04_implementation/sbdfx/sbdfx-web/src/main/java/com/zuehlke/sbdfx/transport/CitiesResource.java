/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.sbdfx.transport;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.zuehlke.sbdfx.biz.api.CitiesService;
import com.zuehlke.sbdfx.dataaccess.api.CitiesDao;
import com.zuehlke.sbdfx.domain.City;
import com.zuehlke.sbdfx.domain.TestScalaDomainObject;

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
        City result = dao.findZuerich();
        return ok(result);
    }

    @GET
    @Path("ScalaTest")
    public Response getScalaTestObject() {
        TestScalaDomainObject result = new TestScalaDomainObject(42, "foo");
        return ok(result);
    }

}
