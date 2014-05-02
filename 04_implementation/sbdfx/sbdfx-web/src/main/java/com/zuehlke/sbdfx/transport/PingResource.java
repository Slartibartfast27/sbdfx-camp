/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.sbdfx.transport;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author cbu
 */
@Path("ping")
@RequestScoped
public class PingResource extends BaseResource {

    @GET
    @Path("ok")
    public Response pingOK() {
        return ping();
    }
    
    @GET
    @Path("failing")
    public Response pingWithException() {
        throw new RuntimeException("Ping failed intentionally");
    }

}
